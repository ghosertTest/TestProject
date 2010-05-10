//$Id: DefaultDeleteEventListener.java,v 1.18 2005/08/08 23:24:43 oneovthafew Exp $
package org.hibernate.event.def;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CacheMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.TransientObjectException;
import org.hibernate.action.EntityDeleteAction;
import org.hibernate.classic.Lifecycle;
import org.hibernate.engine.Cascade;
import org.hibernate.engine.CascadingAction;
import org.hibernate.engine.EntityEntry;
import org.hibernate.engine.EntityKey;
import org.hibernate.engine.ForeignKeys;
import org.hibernate.engine.Nullability;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.engine.Status;
import org.hibernate.event.DeleteEvent;
import org.hibernate.event.DeleteEventListener;
import org.hibernate.event.EventSource;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.pretty.MessageHelper;
import org.hibernate.type.Type;
import org.hibernate.type.TypeFactory;


/**
 * Defines the default delete event listener used by hibernate for deleting entities
 * from the datastore in response to generated delete events.
 *
 * @author Steve Ebersole
 */
public class DefaultDeleteEventListener implements DeleteEventListener {

	private static final Log log = LogFactory.getLog(DefaultDeleteEventListener.class);

    /** Handle the given delete event.
     *
     * @param event The delete event to be handled.
     * @throws HibernateException
     */
	public void onDelete(DeleteEvent event) throws HibernateException {
		final EventSource source = event.getSession();
		
		final PersistenceContext persistenceContext = source.getPersistenceContext();
		Object entity = persistenceContext.unproxyAndReassociate( event.getObject() );
		EntityEntry entityEntry = persistenceContext.getEntry(entity);

		final EntityPersister persister;
		final Serializable id;
		final Object version;
		if ( entityEntry == null ) {
			log.trace( "deleting a detached instance" );

			persister = source.getEntityPersister( event.getEntityName(), entity );
			id = persister.getIdentifier( entity, source.getEntityMode() );

			if ( id == null ) {
				throw new TransientObjectException(
					"the detached instance passed to delete() had a null identifier"
				);
			}
			
			EntityKey key = new EntityKey( id, persister, source.getEntityMode() );

			persistenceContext.checkUniqueness(key, entity);

			new OnUpdateVisitor( source, id ).process( entity, persister );
			
			version = persister.getVersion( entity, source.getEntityMode() );

			entityEntry = persistenceContext.addEntity(
					entity,
					Status.MANAGED,
					persister.getPropertyValues( entity, source.getEntityMode() ),
					key,
					version,
					LockMode.NONE,
					true,
					persister,
					false,
					false
				);
		}
		else {
			log.trace( "deleting a persistent instance" );

			if ( entityEntry.getStatus() == Status.DELETED || entityEntry.getStatus() == Status.GONE ) {
				log.trace( "object was already deleted" );
				return;
			}
			persister = entityEntry.getPersister();
			id = entityEntry.getId();
			version = entityEntry.getVersion();
		}

		/*if ( !persister.isMutable() ) {
			throw new HibernateException(
					"attempted to delete an object of immutable class: " +
					MessageHelper.infoString(persister)
				);
		}*/

		if ( invokeDeleteLifecycle( source, entity, persister ) ) return;

		deleteEntity( source, entity, entityEntry, event.isCascadeDeleteEnabled(), persister );

		if ( source.getFactory().getSettings().isIdentifierRollbackEnabled() ) {
			persister.resetIdentifier( entity, id, version, source.getEntityMode() );
		}
		
	}

	protected final void deleteEntity(
		final EventSource session,
		final Object entity,
		final EntityEntry entityEntry,
		final boolean isCascadeDeleteEnabled,
		final EntityPersister persister)
	throws HibernateException {

		if ( log.isTraceEnabled() ) {
			log.trace(
					"deleting " + 
					MessageHelper.infoString( persister, entityEntry.getId(), session.getFactory() )
				);
		}

		final PersistenceContext persistenceContext = session.getPersistenceContext();

		Type[] propTypes = persister.getPropertyTypes();

		final Object version = entityEntry.getVersion();

		final Object[] currentState;
		if ( entityEntry.getLoadedState() == null ) { //ie. the entity came in from update()
			currentState = persister.getPropertyValues( entity, session.getEntityMode() );
		}
		else {
			currentState = entityEntry.getLoadedState();
		}
		
		final Object[] deletedState = new Object[propTypes.length];
		TypeFactory.deepCopy( 
				currentState, 
				propTypes, 
				persister.getPropertyUpdateability(), 
				deletedState, 
				session
			);
		entityEntry.setDeletedState(deletedState);

		session.getInterceptor().onDelete(
				entity,
				entityEntry.getId(),
				deletedState,
				persister.getPropertyNames(),
				propTypes
			);

		// before any callbacks, etc, so subdeletions see that this deletion happened first
		persistenceContext.setEntryStatus(entityEntry, Status.DELETED);
		EntityKey key = new EntityKey( entityEntry.getId(), persister, session.getEntityMode()  );

		cascadeBeforeDelete(session, persister, entity, entityEntry);

		new ForeignKeys.Nullifier(entity, true, false, session)
			.nullifyTransientReferences( entityEntry.getDeletedState(), propTypes );
		new Nullability(session).checkNullability( entityEntry.getDeletedState(), persister, true );
		persistenceContext.getNullifiableEntityKeys().add(key);

		// Ensures that containing deletions happen before sub-deletions
		session.getActionQueue().addAction(
				new EntityDeleteAction( 
						entityEntry.getId(), 
						deletedState, 
						version, 
						entity, 
						persister, 
						isCascadeDeleteEnabled, 
						session 
					)
			);
		
		cascadeAfterDelete(session, persister, entity);
		
		// the entry will be removed after the flush, and will no longer
		// override the stale snapshot
		// This is now handled by removeEntity() in EntityDeleteAction
		//persistenceContext.removeDatabaseSnapshot(key);

	}

	protected boolean invokeDeleteLifecycle(EventSource session, Object entity, EntityPersister persister) {
		if ( persister.implementsLifecycle( session.getEntityMode() ) ) {
			log.debug( "calling onDelete()" );
			if ( ( (Lifecycle) entity ).onDelete(session) ) {
				log.debug("deletion vetoed by onDelete()");
				return true;
			}
		}
		return false;
	}
	
	protected void cascadeBeforeDelete(
			EventSource session,
	        EntityPersister persister,
	        Object entity,
	        EntityEntry entityEntry) throws HibernateException {

		CacheMode cacheMode = session.getCacheMode();
		session.setCacheMode(CacheMode.GET);
		session.getPersistenceContext().incrementCascadeLevel();
		try {
			// cascade-delete to collections BEFORE the collection owner is deleted
			new Cascade(CascadingAction.DELETE, Cascade.AFTER_INSERT_BEFORE_DELETE, session)
					.cascade(persister, entity);
		}
		finally {
			session.getPersistenceContext().decrementCascadeLevel();
			session.setCacheMode(cacheMode);
		}
	}

	protected void cascadeAfterDelete(
			EventSource session,
	        EntityPersister persister,
	        Object entity) throws HibernateException {

		CacheMode cacheMode = session.getCacheMode();
		session.setCacheMode(CacheMode.GET);
		session.getPersistenceContext().incrementCascadeLevel();
		try {
			// cascade-delete to many-to-one AFTER the parent was deleted
			new Cascade(CascadingAction.DELETE, Cascade.BEFORE_INSERT_AFTER_DELETE, session)
					.cascade(persister, entity);
		}
		finally {
			session.getPersistenceContext().decrementCascadeLevel();
			session.setCacheMode(cacheMode);
		}
	}

}

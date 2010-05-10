//$Id: EntityDeleteAction.java,v 1.25 2005/08/12 21:19:50 oneovthafew Exp $
package org.hibernate.action;

import java.io.Serializable;

import org.hibernate.AssertionFailure;
import org.hibernate.HibernateException;
import org.hibernate.cache.CacheKey;
import org.hibernate.cache.CacheConcurrencyStrategy.SoftLock;
import org.hibernate.engine.EntityEntry;
import org.hibernate.engine.EntityKey;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PreDeleteEvent;
import org.hibernate.event.PreDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;

public final class EntityDeleteAction extends EntityAction {

	private final Object version;
	private SoftLock lock;
	private final boolean isCascadeDeleteEnabled;
	private final Object[] state;

	public EntityDeleteAction(final Serializable id,
							  final Object[] state,
							  final Object version,
							  final Object instance,
							  final EntityPersister persister,
							  final boolean isCascadeDeleteEnabled,
							  final SessionImplementor session) {

		super( session, id, instance, persister );
		this.version = version;
		this.isCascadeDeleteEnabled = isCascadeDeleteEnabled;
		this.state = state;
	}

	public void execute() throws HibernateException {
		Serializable id = getId();
		EntityPersister persister = getPersister();
		SessionImplementor session = getSession();
		Object instance = getInstance();

		boolean veto = preDelete();
		
		final CacheKey ck;
		if ( persister.hasCache() ) {
			ck = new CacheKey( 
					id, 
					persister.getIdentifierType(), 
					persister.getRootEntityName(), 
					session.getEntityMode(), 
					session.getFactory() 
				);
			lock = persister.getCache().lock(ck, version);
		}
		else {
			ck = null;
		}

		if ( !isCascadeDeleteEnabled && !veto ) {
			persister.delete( id, version, instance, session );
		}
		
		//postDelete:
		// After actually deleting a row, record the fact that the instance no longer 
		// exists on the database (needed for identity-column key generation), and
		// remove it from the session cache
		final PersistenceContext persistenceContext = session.getPersistenceContext();
		EntityEntry entry = persistenceContext.removeEntry( instance );
		if ( entry == null ) {
			throw new AssertionFailure( "possible nonthreadsafe access to session" );
		}
		entry.postDelete();

		EntityKey key = new EntityKey( entry.getId(), entry.getPersister(), session.getEntityMode() );
		persistenceContext.removeEntity(key);
		persistenceContext.removeProxy(key);
		
		if ( persister.hasCache() ) persister.getCache().evict(ck);

		postDelete();

		if ( getSession().getFactory().getStatistics().isStatisticsEnabled() && !veto ) {
			getSession().getFactory().getStatisticsImplementor()
					.deleteEntity( getPersister().getEntityName() );
		}
	}

	private boolean preDelete() {
		PreDeleteEventListener[] preListeners = getSession().getListeners()
				.getPreDeleteEventListeners();
		boolean veto = false;
		if (preListeners.length>0) {
			PreDeleteEvent preEvent = new PreDeleteEvent( getInstance(), getId(), state, getPersister() );
			for ( int i = 0; i < preListeners.length; i++ ) {
				veto = preListeners[i].onPreDelete(preEvent) || veto;
			}
		}
		return veto;
	}

	private void postDelete() {
		PostDeleteEventListener[] postListeners = getSession().getListeners()
				.getPostDeleteEventListeners();
		if (postListeners.length>0) {
			PostDeleteEvent postEvent = new PostDeleteEvent( getInstance(), getId(), state, getPersister() );
			for ( int i = 0; i < postListeners.length; i++ ) {
				postListeners[i].onPostDelete(postEvent);
			}
		}
	}

	private void postCommitDelete() {
		PostDeleteEventListener[] postListeners = getSession().getListeners()
				.getPostCommitDeleteEventListeners();
		if (postListeners.length>0) {
			PostDeleteEvent postEvent = new PostDeleteEvent( getInstance(), getId(), state, getPersister() );
			for ( int i = 0; i < postListeners.length; i++ ) {
				postListeners[i].onPostDelete(postEvent);
			}
		}
	}

	public void afterTransactionCompletion(boolean success) throws HibernateException {
		if ( getPersister().hasCache() ) {
			final CacheKey ck = new CacheKey( 
					getId(), 
					getPersister().getIdentifierType(), 
					getPersister().getRootEntityName(),
					getSession().getEntityMode(), 
					getSession().getFactory()
				);
			getPersister().getCache().release(ck, lock);
		}
		postCommitDelete();
	}

	protected boolean hasPostCommitEventListeners() {
		return getSession().getListeners().getPostCommitDeleteEventListeners().length>0;
	}

}








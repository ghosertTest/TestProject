//$Id: Cascade.java,v 1.6 2005/07/26 05:51:45 oneovthafew Exp $
package org.hibernate.engine;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.event.EventSource;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.AbstractComponentType;
import org.hibernate.type.AssociationType;
import org.hibernate.type.CollectionType;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;
import org.hibernate.util.CollectionHelper;

/**
 * Implements cascaded save / delete / update / lock / evict / replicate / persist / merge
 *
 * @see org.hibernate.type.AssociationType
 * @author Gavin King
 */
public final class Cascade {
	
	private int cascadeTo;
	private EventSource eventSource;
	private CascadingAction action;

	public Cascade(
			final CascadingAction action, 
			final int cascadeTo, 
			final EventSource eventSource
	) {
		this.cascadeTo = cascadeTo;
		this.eventSource = eventSource;
		this.action = action;
	}

	private static final Log log = LogFactory.getLog(Cascade.class);

	// The available cascade actions:

	/**
	 * A cascade point that occurs just after the insertion of the parent entity and
	 * just before deletion
	 */
	public static final int AFTER_INSERT_BEFORE_DELETE = 1;
	/**
	 * A cascade point that occurs just before the insertion of the parent entity and
	 * just after deletion
	 */
	public static final int BEFORE_INSERT_AFTER_DELETE = 2;
	/**
	 * A cascade point that occurs just after the insertion of the parent entity and
	 * just before deletion, inside a collection
	 */
	public static final int AFTER_INSERT_BEFORE_DELETE_VIA_COLLECTION = 3;
	/**
	 * A cascade point that occurs just after update of the parent entity
	 */
	public static final int AFTER_UPDATE = 0;
	/**
	 * A cascade point that occurs just before the session is flushed
	 */
	public static final int BEFORE_FLUSH = 0;
	/**
	 * A cascade point that occurs just after eviction of the parent entity from the
	 * session cache
	 */
	public static final int AFTER_EVICT = 0;
	/**
	 * A cascade point that occurs just after locking a transient parent entity into the
	 * session cache
	 */
	public static final int BEFORE_REFRESH = 0;
	/**
	 * A cascade point that occurs just after refreshing a parent entity
	 */
	public static final int AFTER_LOCK = 0;
	/**
	 * A cascade point that occurs just before merging from a transient parent entity into
	 * the object in the session cache
	 */
	public static final int BEFORE_MERGE = 0;

	// The allowable cascade styles for a property:

	/**
	 * Cascade an action to the child or children
	 */
	private void cascadeProperty(
		final Object child,
		final Type type,
		final CascadeStyle style,
		final Object anything,
		final boolean isCascadeDeleteEnabled) 
	throws HibernateException {

		if (child!=null) {
			if ( type.isAssociationType() ) {
				AssociationType associationType = (AssociationType) type;
				if ( cascadeAssociationNow( associationType ) ) {
					cascadeAssociation( 
							child, 
							type, 
							style,  
							anything, 
							isCascadeDeleteEnabled 
						);
				}
			}
			else if ( type.isComponentType() ) {
				cascadeComponent( child, (AbstractComponentType) type, anything );
			}
		}
	}

	private boolean cascadeAssociationNow(AssociationType associationType) {
		return associationType.getForeignKeyDirection().cascadeNow(cascadeTo) &&
			( eventSource.getEntityMode()!=EntityMode.DOM4J || associationType.isEmbeddedInXML() );
	}

	private void cascadeComponent(
			final Object child, 
			final AbstractComponentType componentType, 
			final Object anything
	) {
		Object[] children = componentType.getPropertyValues(child, eventSource);
		Type[] types = componentType.getSubtypes();
		for ( int i=0; i<types.length; i++ ) {
			CascadeStyle componentPropertyStyle = componentType.getCascadeStyle(i);
			if ( componentPropertyStyle.doCascade(action) ) {
				cascadeProperty( 
						children[i], 
						types[i], 
						componentPropertyStyle, 
						anything, 
						false
					);
			}
		}
	}

	private void cascadeAssociation(
			final Object child, 
			final Type type, 
			final CascadeStyle style,
			final Object anything, 
			final boolean isCascadeDeleteEnabled
	) {
		if ( type.isEntityType() || type.isAnyType() ) {
			cascadeToOne( child, type, style, anything, isCascadeDeleteEnabled );
		}
		else if ( type.isCollectionType() ) {
			cascadeCollection( child, style, anything, (CollectionType) type );
		}
	}
	
	/**
	 * Cascade an action to a collection
	 */
	private void cascadeCollection(
			final Object child, 
			final CascadeStyle style, 
			final Object anything, 
			final CollectionType type
	) {
		
		CollectionPersister persister = eventSource.getFactory()
				.getCollectionPersister( type.getRole() );
		Type elemType = persister.getElementType();

		final int oldCascadeTo = cascadeTo;
		if ( cascadeTo==AFTER_INSERT_BEFORE_DELETE) {
			cascadeTo = AFTER_INSERT_BEFORE_DELETE_VIA_COLLECTION;
		}
		
		//cascade to current collection elements
		if ( elemType.isEntityType() || elemType.isAnyType() || elemType.isComponentType() ) {
			cascadeCollectionElements(
				child, 
				type, 
				style, 
				elemType,
				anything, 
				persister.isCascadeDeleteEnabled() 
			);
		}
		
		cascadeTo = oldCascadeTo;
	}
	
	/**
	 * Cascade an action to a to-one association or any type
	 */
	private void cascadeToOne(
			final Object child, 
			final Type type, 
			final CascadeStyle style, 
			final Object anything, 
			final boolean isCascadeDeleteEnabled
	) {
		
		final String entityName = type.isEntityType() ?
				( (EntityType) type ).getAssociatedEntityName() : null;
				
		if ( style.reallyDoCascade(action) ) { //not really necessary, but good for consistency...
			action.cascade(eventSource, child, entityName, anything, isCascadeDeleteEnabled);
		}
		
	}

	/**
	 * Cascade an action from the parent entity instance to all its children
	 */
	public void cascade(final EntityPersister persister, final Object parent) 
	throws HibernateException {
		cascade(persister, parent, null);
	}

	/**
	 * Cascade an action from the parent entity instance to all its children
	 */
	public void cascade(
		final EntityPersister persister,
		final Object parent,
		final Object anything) 
	throws HibernateException {

		if ( persister.hasCascades() ) { // performance opt
			if ( log.isTraceEnabled() ) {
				log.trace( "processing cascade " + action + " for: " + persister.getEntityName() );
			}
			
			Type[] types = persister.getPropertyTypes();
			CascadeStyle[] cascadeStyles = persister.getPropertyCascadeStyles();
			for ( int i=0; i<types.length; i++) {
				CascadeStyle style = cascadeStyles[i];
				if ( style.doCascade(action) ) { 
					// associations cannot be field-level lazy="true", so don't 
					// need to check that the field is fetched (laziness for
					// associations is always done by proxying currently)
					cascadeProperty(
					        persister.getPropertyValue( parent, i, eventSource.getEntityMode() ),
					        types[i],
					        style,
					        anything,
					        false
					);
				}
			}
			
			if ( log.isTraceEnabled() ) {
				log.trace( "done processing cascade " + action + " for: " + persister.getEntityName() );
			}
		}
	}

	/**
	 * Cascade to the collection elements
	 */
	private void cascadeCollectionElements(
		final Object child,
		final CollectionType collectionType,
		final CascadeStyle style,
		final Type elemType,
		final Object anything,
		final boolean isCascadeDeleteEnabled) 
	throws HibernateException {
		
		// we can't cascade to non-embedded elements
		boolean embeddedElements = eventSource.getEntityMode()!=EntityMode.DOM4J ||
				( (EntityType) collectionType.getElementType( eventSource.getFactory() ) ).isEmbeddedInXML();
		
		boolean reallyDoCascade = style.reallyDoCascade(action) && 
			embeddedElements && child!=CollectionType.UNFETCHED_COLLECTION;
		
		if ( reallyDoCascade ) {
			if ( log.isTraceEnabled() ) {
				log.trace( "cascade " + action + " for collection: " + collectionType.getRole() );
			}
			
			Iterator iter = action.getCascadableChildrenIterator(eventSource, collectionType, child);
			while ( iter.hasNext() ) {
				cascadeProperty(
						iter.next(), 
						elemType,
						style, 
						anything, 
						isCascadeDeleteEnabled 
					);
			}
			
			if ( log.isTraceEnabled() ) {
				log.trace( "done cascade " + action + " for collection: " + collectionType.getRole() );
			}
		}
		
		final boolean deleteOrphans = style.hasOrphanDelete() && 
				action.deleteOrphans() && 
				elemType.isEntityType() && 
				child instanceof PersistentCollection; //a newly instantiated collection can't have orphans
		
		if ( deleteOrphans ) { // handle orphaned entities!!
			if ( log.isTraceEnabled() ) {
				log.trace( "deleting orphans for collection: " + collectionType.getRole() );
			}
			
			// we can do the cast since orphan-delete does not apply to:
			// 1. newly instantiated collections
			// 2. arrays (we can't track orphans for detached arrays)
			final String entityName = collectionType.getAssociatedEntityName( eventSource.getFactory() );
			deleteOrphans( entityName, (PersistentCollection) child );
			
			if ( log.isTraceEnabled() ) {
				log.trace( "done deleting orphans for collection: " + collectionType.getRole() );
			}
		}
	}

	/**
	 * Delete any entities that were removed from the collection
	 */
	private void deleteOrphans(String entityName, PersistentCollection pc) 
			throws HibernateException {
		
		final Collection orphans;
		//TODO: suck this logic into the collection!
		if ( pc.wasInitialized() ) {
			CollectionEntry ce = eventSource.getPersistenceContext().getCollectionEntry(pc);
			orphans = ce==null ? 
					CollectionHelper.EMPTY_COLLECTION :
					ce.getOrphans(entityName, pc);
		}
		else {
			orphans = pc.getQueuedOrphans(entityName);
		}
		
		final Iterator orphanIter = orphans.iterator();
		while ( orphanIter.hasNext() ) {
			Object orphan = orphanIter.next();
			if (orphan!=null) {
				if ( log.isTraceEnabled() ) {
					log.trace("deleting orphaned entity instance: " + entityName);
				}
				eventSource.delete(entityName, orphan, false);
			}
		}
		
	}

}

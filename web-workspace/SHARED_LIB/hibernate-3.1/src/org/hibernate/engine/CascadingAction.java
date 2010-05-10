//$Id: CascadingAction.java,v 1.7 2005/10/16 13:27:54 epbernard Exp $
package org.hibernate.engine;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.ReplicationMode;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.event.EventSource;
import org.hibernate.type.CollectionType;

/**
 * A session action that may be cascaded from parent entity to its children
 * 
 * @author Gavin King
 */
public abstract class CascadingAction {
	
	private static final Log log = LogFactory.getLog(CascadingAction.class);

	/**
	 * cascade the action to the child object
	 */
	public abstract void cascade(EventSource session, Object child, String entityName, Object anything, boolean isCascadeDeleteEnabled) 
	throws HibernateException;
	/**
	 * Should this action be cascaded to the given (possibly uninitialized) collection?
	 */
	public abstract Iterator getCascadableChildrenIterator(EventSource session, CollectionType collectionType, Object collection);
	/**
	 * Do we need to handle orphan delete for this action?
	 */
	public abstract boolean deleteOrphans();

	/**
	 * @see org.hibernate.Session#delete(Object)
	 */
	public static final CascadingAction DELETE = new CascadingAction() {
		public void cascade(EventSource session, Object child, String entityName, Object anything, boolean isCascadeDeleteEnabled) 
		throws HibernateException {
			if ( log.isTraceEnabled() ) log.trace("cascading to delete: " + entityName);
			if ( ForeignKeys.isNotTransient(entityName, child, null, session) ) {
				session.delete(entityName, child, isCascadeDeleteEnabled);
			}
		}
		public Iterator getCascadableChildrenIterator(EventSource session, CollectionType collectionType, Object collection) {
			// delete does cascade to uninitialized collections
			return CascadingAction.getAllElementsIterator(session, collectionType, collection);
		}
		public boolean deleteOrphans() {
			// orphans should be deleted during delete
			return true;
		}
		public String toString() {
			return "ACTION_DELETE";
		}
	};
	
	/**
	 * @see org.hibernate.Session#lock(Object, LockMode)
	 */
	public static final CascadingAction LOCK = new CascadingAction() {
		public void cascade(EventSource session, Object child, String entityName, Object anything, boolean isCascadeDeleteEnabled) 
		throws HibernateException {
			if ( log.isTraceEnabled() ) log.trace("cascading to lock: " + entityName);
			session.lock( entityName, child, LockMode.NONE/*(LockMode) anything*/ );
		}
		public Iterator getCascadableChildrenIterator(EventSource session, CollectionType collectionType, Object collection) {
			// lock doesn't cascade to uninitialized collections
			return getLoadedElementsIterator(session, collectionType, collection);
		}
		public boolean deleteOrphans() {
			//TODO: should orphans really be deleted during lock???
			return false;
		}
		public String toString() {
			return "ACTION_LOCK";
		}
	};
	
	/**
	 * @see org.hibernate.Session#refresh(Object)
	 */
	public static final CascadingAction REFRESH = new CascadingAction() {
		public void cascade(EventSource session, Object child, String entityName, Object anything, boolean isCascadeDeleteEnabled) 
		throws HibernateException {
			if ( log.isTraceEnabled() ) log.trace("cascading to refresh: " + entityName);
			session.refresh( child, (Map) anything );
		}
		public Iterator getCascadableChildrenIterator(EventSource session, CollectionType collectionType, Object collection) {
			// refresh doesn't cascade to uninitialized collections
			return getLoadedElementsIterator(session, collectionType, collection);
		}
		public boolean deleteOrphans() {
			return false;
		}
		public String toString() {
			return "ACTION_REFRESH";
		}
	};
	
	/**
	 * @see org.hibernate.Session#evict(Object)
	 */
	public static final CascadingAction EVICT = new CascadingAction() {
		public void cascade(EventSource session, Object child, String entityName, Object anything, boolean isCascadeDeleteEnabled) 
		throws HibernateException {
			if ( log.isTraceEnabled() ) log.trace("cascading to evict: " + entityName);
			session.evict(child);
		}
		public Iterator getCascadableChildrenIterator(EventSource session, CollectionType collectionType, Object collection) {
			// evicts don't cascade to uninitialized collections
			return getLoadedElementsIterator(session, collectionType, collection);
		}
		public boolean deleteOrphans() {
			return false;
		}
		public String toString() {
			return "ACTION_EVICT";
		}
	};
	
	/**
	 * @see org.hibernate.Session#saveOrUpdate(Object)
	 */
	public static final CascadingAction SAVE_UPDATE = new CascadingAction() {
		public void cascade(EventSource session, Object child, String entityName, Object anything, boolean isCascadeDeleteEnabled) 
		throws HibernateException {
			if ( log.isTraceEnabled() ) log.trace("cascading to saveOrUpdate: " + entityName);
			session.saveOrUpdate(entityName, child);
		}
		public Iterator getCascadableChildrenIterator(EventSource session, CollectionType collectionType, Object collection) {
			// saves / updates don't cascade to uninitialized collections
			return getLoadedElementsIterator(session, collectionType, collection);
		}
		public boolean deleteOrphans() {
			// orphans should be deleted during save/update
			return true;
		}
		public String toString() {
			return "ACTION_SAVE_UPDATE";
		}
	};
	
	/**
	 * @see org.hibernate.Session#merge(Object)
	 */
	public static final CascadingAction MERGE = new CascadingAction() {
		public void cascade(EventSource session, Object child, String entityName, Object anything, boolean isCascadeDeleteEnabled) 
		throws HibernateException {
			if ( log.isTraceEnabled() ) log.trace("cascading to merge: " + entityName);
			session.merge( entityName, child, (Map) anything );
		}
		public Iterator getCascadableChildrenIterator(EventSource session, CollectionType collectionType, Object collection) {
			// merges don't cascade to uninitialized collections
//			//TODO: perhaps this does need to cascade after all....
			return getLoadedElementsIterator(session, collectionType, collection);
		}
		public boolean deleteOrphans() {
			// orphans should not be deleted during merge??
			return false;
		}
		public String toString() {
			return "ACTION_MERGE";
		}
	};
	
	/**
	 * @see org.hibernate.classic.Session#saveOrUpdateCopy(Object)
	 */
	public static final CascadingAction SAVE_UPDATE_COPY = new CascadingAction() {
		// for deprecated saveOrUpdateCopy()
		public void cascade(EventSource session, Object child, String entityName, Object anything, boolean isCascadeDeleteEnabled) 
		throws HibernateException {
			if ( log.isTraceEnabled() ) log.trace("cascading to saveOrUpdateCopy: " + entityName);
			session.saveOrUpdateCopy( entityName, child, (Map) anything );
		}
		public Iterator getCascadableChildrenIterator(EventSource session, CollectionType collectionType, Object collection) {
			// saves / updates don't cascade to uninitialized collections
			return getLoadedElementsIterator(session, collectionType, collection);
		}
		public boolean deleteOrphans() {
			// orphans should not be deleted during copy??
			return false;
		}
		public String toString() {
			return "ACTION_SAVE_UPDATE_COPY";
		}
	};
	
	/**
	 * @see org.hibernate.Session#persist(Object)
	 */
	public static final CascadingAction PERSIST = new CascadingAction() {
		public void cascade(EventSource session, Object child, String entityName, Object anything, boolean isCascadeDeleteEnabled) 
		throws HibernateException {
			if ( log.isTraceEnabled() ) log.trace("cascading to persist: " + entityName);
			session.persist( entityName, child, (Map) anything );
		}
		public Iterator getCascadableChildrenIterator(EventSource session, CollectionType collectionType, Object collection) {
			// persists don't cascade to uninitialized collections
			return CascadingAction.getAllElementsIterator(session, collectionType, collection);
		}
		public boolean deleteOrphans() {
			return false;
		}
		public String toString() {
			return "ACTION_PERSIST";
		}
	};

	/**
	 * Execute persist during flush time
	 *
	 * @see org.hibernate.Session#persist(Object)
	 */
	public static final CascadingAction PERSIST_ON_FLUSH = new CascadingAction() {
		public void cascade(EventSource session, Object child, String entityName, Object anything, boolean isCascadeDeleteEnabled)
		throws HibernateException {
			if ( log.isTraceEnabled() ) log.trace("cascading to persistOnFlush: " + entityName);
			session.persistOnFlush( entityName, child, (Map) anything );
		}
		public Iterator getCascadableChildrenIterator(EventSource session, CollectionType collectionType, Object collection) {
			// persists don't cascade to uninitialized collections
			return CascadingAction.getLoadedElementsIterator(session, collectionType, collection);
		}
		public boolean deleteOrphans() {
			return true;
		}
		public String toString() {
			return "ACTION_PERSIST_ON_FLUSH";
		}
	};
	
	/**
	 * @see org.hibernate.Session#replicate(Object, org.hibernate.ReplicationMode)
	 */
	public static final CascadingAction REPLICATE = new CascadingAction() {
		public void cascade(EventSource session, Object child, String entityName, Object anything, boolean isCascadeDeleteEnabled) 
		throws HibernateException {
			if ( log.isTraceEnabled() ) log.trace("cascading to replicate: " + entityName);
			session.replicate( entityName, child, (ReplicationMode) anything );
		}
		public Iterator getCascadableChildrenIterator(EventSource session, CollectionType collectionType, Object collection) {
			// replicate does cascade to uninitialized collections
			return getLoadedElementsIterator(session, collectionType, collection);
		}
		public boolean deleteOrphans() {
			return false; //I suppose?
		}
		public String toString() {
			return "ACTION_REPLICATE";
		}
	};
	
	CascadingAction() {}

	/**
	 * Iterate all the collection elements, loading them from the database if necessary.
	 */
	private static Iterator getAllElementsIterator(EventSource session, CollectionType collectionType, Object collection) {
		return collectionType.getElementsIterator(collection, session);
	}
	/**
	 * Iterate just the elements of the collection that are already there. Don't load
	 * any new elements from the database.
	 */
	public static Iterator getLoadedElementsIterator(SessionImplementor session, CollectionType collectionType, Object collection) {
		if ( collectionIsInitialized(collection) ) {
			// handles arrays and newly instantiated collections
			return collectionType.getElementsIterator(collection, session);
		}
		else {
			// does not handle arrays (thats ok, cos they can't be lazy)
			// or newly instantiated collections, so we can do the cast
			return ( (PersistentCollection) collection ).queuedAdditionIterator();
		}
	}
	
	private static boolean collectionIsInitialized(Object collection) {
		return !(collection instanceof PersistentCollection) || ( (PersistentCollection) collection ).wasInitialized();
	}

}
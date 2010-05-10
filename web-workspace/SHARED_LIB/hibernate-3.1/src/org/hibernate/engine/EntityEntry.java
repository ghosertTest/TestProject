//$Id: EntityEntry.java,v 1.17 2005/08/10 23:58:48 oneovthafew Exp $
package org.hibernate.engine;

import java.io.Serializable;


import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.intercept.FieldInterceptor;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.UniqueKeyLoadable;
import org.hibernate.pretty.MessageHelper;

/**
 * We need an entry to tell us all about the current state
 * of an object with respect to its persistent state
 * 
 * @author Gavin King
 */
public final class EntityEntry implements Serializable {

	private LockMode lockMode;
	private Status status;
	private final Serializable id;
	private Object[] loadedState;
	private Object[] deletedState;
	private boolean existsInDatabase;
	private Object version;
	private transient EntityPersister persister; // for convenience to save some lookups
	private final EntityMode entityMode;
	private final String entityName;
	private boolean isBeingReplicated;
	private boolean loadedWithLazyPropertiesUnfetched; //NOTE: this is not updated when properties are fetched lazily!
	private final transient Object rowId;

	EntityEntry(
			final Status status,
			final Object[] loadedState,
			final Object rowId,
			final Serializable id,
			final Object version,
			final LockMode lockMode,
			final boolean existsInDatabase,
			final EntityPersister persister,
			final EntityMode entityMode,
			final boolean disableVersionIncrement,
			final boolean lazyPropertiesAreUnfetched) {
		this.status=status;
		this.loadedState=loadedState;
		this.id=id;
		this.rowId=rowId;
		this.existsInDatabase=existsInDatabase;
		this.version=version;
		this.lockMode=lockMode;
		this.isBeingReplicated=disableVersionIncrement;
		this.loadedWithLazyPropertiesUnfetched = lazyPropertiesAreUnfetched;
		this.persister=persister;
		this.entityMode = entityMode;
		this.entityName = persister == null ?
				null : persister.getEntityName();
	}

	public LockMode getLockMode() {
		return lockMode;
	}

	public void setLockMode(LockMode lockMode) {
		this.lockMode = lockMode;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		if (status==Status.READ_ONLY) {
			loadedState = null; //memory optimization
		}
		this.status = status;
	}

	public Serializable getId() {
		return id;
	}

	public Object[] getLoadedState() {
		return loadedState;
	}

	public Object[] getDeletedState() {
		return deletedState;
	}

	public void setDeletedState(Object[] deletedState) {
		this.deletedState = deletedState;
	}

	public boolean isExistsInDatabase() {
		return existsInDatabase;
	}

	public Object getVersion() {
		return version;
	}

	public EntityPersister getPersister() {
		return persister;
	}

	void afterDeserialize(SessionFactoryImplementor factory) {
		persister = factory.getEntityPersister( entityName );
	}

	public String getEntityName() {
		return entityName;
	}

	public boolean isBeingReplicated() {
		return isBeingReplicated;
	}
	
	public Object getRowId() {
		return rowId;
	}
	
	/**
	 * After actually updating the database, update the snapshot information,
	 * and escalate the lock mode
	 */
	public void postUpdate(Object entity, Object[] updatedState, Object nextVersion) {
		this.loadedState = updatedState;
		
		setLockMode(LockMode.WRITE);
		
		if ( getPersister().isVersioned() ) {
			this.version = nextVersion;
			getPersister().setPropertyValue( 
					entity, 
					getPersister().getVersionProperty(), 
					nextVersion, 
					entityMode 
				);
		}
		
		FieldInterceptor.clearDirty( entity );
	}

	/**
	 * After actually deleting a row, record the fact that the instance no longer
	 * exists in the database
	 */
	public void postDelete() {
		status = Status.GONE;
		existsInDatabase = false;
	}
	
	/**
	 * After actually inserting a row, record the fact that the instance exists on the 
	 * database (needed for identity-column key generation)
	 */
	public void postInsert() {
		existsInDatabase = true;
	}
	
	public boolean isNullifiable(boolean earlyInsert, SessionImplementor session) {
		return getStatus() == Status.SAVING || (
				earlyInsert ?
						!isExistsInDatabase() :
						session.getPersistenceContext().getNullifiableEntityKeys()
							.contains( new EntityKey( getId(), getPersister(), entityMode ) )
				);
	}
	
	public Object getLoadedValue(String propertyName) {
		int propertyIndex = ( (UniqueKeyLoadable) persister ).getPropertyIndex(propertyName);
		return loadedState[propertyIndex];
	}
	
	
	public boolean requiresDirtyCheck(Object entity) {
		
		boolean isMutableInstance = 
				status != Status.READ_ONLY && 
				persister.isMutable();
		
		return isMutableInstance && (
				getPersister().hasMutableProperties() ||
				!FieldInterceptor.hasInterceptor( entity ) ||
				FieldInterceptor.getFieldInterceptor(entity).isDirty()
			);
		
	}
	
	public void setReadOnly(boolean readOnly, Object entity) {
		if (status!=Status.MANAGED && status!=Status.READ_ONLY) {
			throw new HibernateException("instance was not in a valid state");
		}
		if (readOnly) {
			setStatus(Status.READ_ONLY);
			loadedState = null;
		}
		else {
			setStatus(Status.MANAGED);
			loadedState = getPersister().getPropertyValues(entity, entityMode);
		}
	}
	
	public String toString() {
		return "EntityEntry" + 
				MessageHelper.infoString(entityName, id) + 
				'(' + status + ')';
	}

	public boolean isLoadedWithLazyPropertiesUnfetched() {
		return loadedWithLazyPropertiesUnfetched;
	}

}

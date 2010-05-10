//$Id: PostDeleteEvent.java,v 1.14 2005/07/20 22:48:15 oneovthafew Exp $
package org.hibernate.event;

import java.io.Serializable;

import org.hibernate.persister.entity.EntityPersister;

/**
 * Occurs after deleting an item from the datastore
 * 
 * @author Gavin King
 */
public class PostDeleteEvent {
	private Object entity;
	private EntityPersister persister;
	private Serializable id;
	private Object[] deletedState;
	
	public PostDeleteEvent(
			Object entity, 
			Serializable id,
			Object[] deletedState,
			EntityPersister persister
	) {
		this.entity = entity;
		this.id = id;
		this.persister = persister;
		this.deletedState = deletedState;
	}
	
	public Serializable getId() {
		return id;
	}
	public EntityPersister getPersister() {
		return persister;
	}
	public Object getEntity() {
		return entity;
	}
	public Object[] getDeletedState() {
		return deletedState;
	}
}

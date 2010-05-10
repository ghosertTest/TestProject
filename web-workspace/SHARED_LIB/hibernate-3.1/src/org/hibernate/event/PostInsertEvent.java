//$Id: PostInsertEvent.java,v 1.7 2005/07/20 22:48:22 oneovthafew Exp $
package org.hibernate.event;

import java.io.Serializable;

import org.hibernate.persister.entity.EntityPersister;

/**
 * Occurs after inserting an item in the datastore
 * 
 * @author Gavin King
 */
public class PostInsertEvent {
	private Object entity;
	private EntityPersister persister;
	private Object[] state;
	private Serializable id;
	
	public PostInsertEvent(
			Object entity, 
			Serializable id,
			Object[] state,
			EntityPersister persister
	) {
		this.entity = entity;
		this.id = id;
		this.state = state;
		this.persister = persister;
	}
	
	public Object getEntity() {
		return entity;
	}
	public Serializable getId() {
		return id;
	}
	public EntityPersister getPersister() {
		return persister;
	}
	public Object[] getState() {
		return state;
	}
}

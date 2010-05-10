//$Id: PostUpdateEvent.java,v 1.7 2005/07/20 22:48:22 oneovthafew Exp $
package org.hibernate.event;

import java.io.Serializable;

import org.hibernate.persister.entity.EntityPersister;

/**
 * Occurs after the datastore is updated
 * 
 * @author Gavin King
 */
public class PostUpdateEvent {
	private Object entity;
	private EntityPersister persister;
	private Object[] state;
	private Object[] oldState;
	private Serializable id;
	
	public PostUpdateEvent(
			Object entity, 
			Serializable id,
			Object[] state,
			Object[] oldState,
			EntityPersister persister
	) {
		this.entity = entity;
		this.id = id;
		this.state = state;
		this.oldState = oldState;
		this.persister = persister;
	}
	
	public Object getEntity() {
		return entity;
	}
	public Serializable getId() {
		return id;
	}
	public Object[] getOldState() {
		return oldState;
	}
	public EntityPersister getPersister() {
		return persister;
	}
	public Object[] getState() {
		return state;
	}
}

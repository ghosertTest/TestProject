//$Id: MergeEvent.java,v 1.7 2005/08/08 23:24:43 oneovthafew Exp $
package org.hibernate.event;

import java.io.Serializable;

/** 
 * An event class for merge() and saveOrUpdateCopy()
 *
 * @author Gavin King
 */
public class MergeEvent extends AbstractEvent {

	private Object original;
	private Serializable requestedId;
	private String entityName;
	private Object entity;
	private Object result;

	public MergeEvent(String entityName, Object original, EventSource source) {
		this(original, source);
		this.entityName = entityName;
	}

	public MergeEvent(String entityName, Object original, Serializable id, EventSource source) {
		this(entityName, original, source);
		this.requestedId = id;
		if ( requestedId == null ) {
			throw new IllegalArgumentException(
					"attempt to create merge event with null identifier"
				);
		}
	}

	public MergeEvent(Object object, EventSource source) {
		super(source);
		if ( object == null ) {
			throw new IllegalArgumentException(
					"attempt to create merge event with null entity"
				);
		}
		this.original = object;
	}

	public Object getOriginal() {
		return original;
	}

	public void setOriginal(Object object) {
		this.original = object;
	}

	public Serializable getRequestedId() {
		return requestedId;
	}

	public void setRequestedId(Serializable requestedId) {
		this.requestedId = requestedId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Object getEntity() {
		return entity;
	}
	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}

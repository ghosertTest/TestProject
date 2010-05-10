//$Id: EvictEvent.java,v 1.6 2005/05/27 03:53:58 oneovthafew Exp $
package org.hibernate.event;


/**
 *  Defines an event class for the evicting of an entity.
 *
 * @author Steve Ebersole
 */
public class EvictEvent extends AbstractEvent {

	private Object object;

	public EvictEvent(Object object, EventSource source) {
		super(source);
		this.object = object;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}

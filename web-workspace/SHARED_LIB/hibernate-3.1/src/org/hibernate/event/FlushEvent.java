//$Id: FlushEvent.java,v 1.7 2005/05/27 03:53:58 oneovthafew Exp $
package org.hibernate.event;


/** 
 * Defines an event class for the flushing of a session.
 *
 * @author Steve Ebersole
 */
public class FlushEvent extends AbstractEvent {
	
	public FlushEvent(EventSource source) {
		super(source);
	}

}

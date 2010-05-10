//$Id: PreUpdateEventListener.java,v 1.2 2005/07/20 22:48:22 oneovthafew Exp $
package org.hibernate.event;

import java.io.Serializable;

/**
 * Called before updating the datastore
 * 
 * @author Gavin King
 */
public interface PreUpdateEventListener extends Serializable {
	public boolean onPreUpdate(PreUpdateEvent event);
}

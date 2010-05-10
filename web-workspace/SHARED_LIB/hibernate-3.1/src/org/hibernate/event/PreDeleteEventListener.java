//$Id: PreDeleteEventListener.java,v 1.2 2005/07/20 22:48:22 oneovthafew Exp $
package org.hibernate.event;

import java.io.Serializable;

/**
 * Called before deleting an item from the datastore
 * 
 * @author Gavin King
 */
public interface PreDeleteEventListener extends Serializable {
	public boolean onPreDelete(PreDeleteEvent event);
}

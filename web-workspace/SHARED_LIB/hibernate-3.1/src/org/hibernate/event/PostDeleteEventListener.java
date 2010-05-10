//$Id: PostDeleteEventListener.java,v 1.7 2005/07/20 22:48:22 oneovthafew Exp $
package org.hibernate.event;

import java.io.Serializable;

/**
 * Called after deleting an item from the datastore
 * 
 * @author Gavin King
 */
public interface PostDeleteEventListener extends Serializable {
	public void onPostDelete(PostDeleteEvent event);
}

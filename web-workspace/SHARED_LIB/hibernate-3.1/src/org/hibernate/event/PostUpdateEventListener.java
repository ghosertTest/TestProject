//$Id: PostUpdateEventListener.java,v 1.2 2005/07/20 22:48:22 oneovthafew Exp $
package org.hibernate.event;

import java.io.Serializable;

/**
 * Called after updating the datastore
 * 
 * @author Gavin King
 */
public interface PostUpdateEventListener extends Serializable {
	public void onPostUpdate(PostUpdateEvent event);
}

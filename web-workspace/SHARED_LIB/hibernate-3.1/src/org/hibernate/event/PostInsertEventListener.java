//$Id: PostInsertEventListener.java,v 1.2 2005/07/20 22:48:22 oneovthafew Exp $
package org.hibernate.event;

import java.io.Serializable;

/**
 * Called after insterting an item in the datastore
 * 
 * @author Gavin King
 */
public interface PostInsertEventListener extends Serializable {
	public void onPostInsert(PostInsertEvent event);
}

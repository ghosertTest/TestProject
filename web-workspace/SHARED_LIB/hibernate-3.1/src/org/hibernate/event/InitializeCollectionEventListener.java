//$Id: InitializeCollectionEventListener.java,v 1.2 2004/08/16 12:12:12 oneovthafew Exp $
package org.hibernate.event;

import org.hibernate.HibernateException;

import java.io.Serializable;

/**
 * Defines the contract for handling of collection initialization events 
 * generated by a session.
 *
 * @author Gavin King
 */
public interface InitializeCollectionEventListener extends Serializable {

	public void onInitializeCollection(InitializeCollectionEvent event) throws HibernateException;

}

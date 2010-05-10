//$Id: PersistentObjectException.java,v 1.3 2005/05/23 15:00:23 oneovthafew Exp $
package org.hibernate;

/**
 * Thrown when the user passes a persistent instance to a <tt>Session</tt>
 * method that expects a transient instance.
 *
 * @author Gavin King
 */
public class PersistentObjectException extends HibernateException {
	
	public PersistentObjectException(String s) {
		super(s);
	}
}







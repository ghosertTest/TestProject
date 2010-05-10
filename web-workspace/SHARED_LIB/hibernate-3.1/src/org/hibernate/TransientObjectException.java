//$Id: TransientObjectException.java,v 1.2 2005/05/23 15:00:24 oneovthafew Exp $
package org.hibernate;

/**
 * Thrown when the user passes a transient instance to a <tt>Session</tt>
 * method that expects a persistent instance.
 *
 * @author Gavin King
 */

public class TransientObjectException extends HibernateException {

	public TransientObjectException(String s) {
		super(s);
	}

}







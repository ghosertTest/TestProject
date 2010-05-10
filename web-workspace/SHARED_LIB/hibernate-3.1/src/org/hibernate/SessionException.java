//$Id: SessionException.java,v 1.1 2005/05/23 15:00:24 oneovthafew Exp $
package org.hibernate;

/**
 * Thrown when the user calls a method of a <tt>Session</tt>
 * that is in an inappropropriate state (for example, the
 * the session is closed or disconnected).
 *
 * @author Gavin King
 */
public class SessionException extends HibernateException {
	
	public SessionException(String s) {
		super(s);
	}
}







//$Id: ObjectNotFoundException.java,v 1.2 2004/08/29 09:59:15 oneovthafew Exp $
package org.hibernate;

import java.io.Serializable;

/**
 * Thrown when <tt>Session.load()</tt> fails to select a row with
 * the given primary key (identifier value). This exception might not
 * be thrown when <tt>load()</tt> is called, even if there was no
 * row on the database, because <tt>load()</tt> returns a proxy if
 * possible. Applications should use <tt>Session.get()</tt> to test if
 * a row exists in the database.<br>
 * <br> 
 * Like all Hibernate exceptions, this exception is considered 
 * unrecoverable.
 *
 * @author Gavin King
 */
public class ObjectNotFoundException extends UnresolvableObjectException {

	public ObjectNotFoundException(Serializable identifier, String clazz) {
		super(identifier, clazz);
	}

	public static void throwIfNull(Object o, Serializable id, String clazz)
	throws ObjectNotFoundException {
		if (o==null) throw new ObjectNotFoundException(id, clazz);
	}

}








//$Id: CacheException.java,v 1.2 2005/04/15 18:20:53 steveebersole Exp $
package org.hibernate.cache;

import org.hibernate.HibernateException;

/**
 * Something went wrong in the cache
 */
public class CacheException extends HibernateException {
	
	public CacheException(String s) {
		super(s);
	}

	public CacheException(String s, Exception e) {
		super(s, e);
	}
	
	public CacheException(Exception e) {
		super(e);
	}
	
}







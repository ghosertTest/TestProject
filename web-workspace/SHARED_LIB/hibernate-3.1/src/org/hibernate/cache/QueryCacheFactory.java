// $Id: QueryCacheFactory.java,v 1.2 2004/10/26 09:35:35 oneovthafew Exp $
package org.hibernate.cache;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Settings;

import java.util.Properties;

/**
 * Defines a factory for query cache instances.  These factories are responsible for
 * creating individual QueryCache instances.
 *
 * @author Steve Ebersole
 */
public interface QueryCacheFactory {

	public QueryCache getQueryCache(
	        String regionName,
	        UpdateTimestampsCache updateTimestampsCache,
			Settings settings,
	        Properties props) 
	throws HibernateException;

}

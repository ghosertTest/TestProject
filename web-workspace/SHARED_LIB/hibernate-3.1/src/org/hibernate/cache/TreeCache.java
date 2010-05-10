//$Id: TreeCache.java,v 1.17 2005/08/10 04:54:07 oneovthafew Exp $
package org.hibernate.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.Fqn;
import org.jboss.cache.lock.TimeoutException;

/**
 * Represents a particular region within the given JBossCache TreeCache.
 *
 * @author Gavin King
 */
public class TreeCache implements Cache {
	
	private static final Log log = LogFactory.getLog(TreeCache.class);

	private static final String ITEM = "item";

	private org.jboss.cache.TreeCache cache;
	private final String regionName;
	private final String userRegionName;
	private final TransactionManager transactionManager;

	public TreeCache(org.jboss.cache.TreeCache cache, String regionName, TransactionManager transactionManager) 
	throws CacheException {
		this.cache = cache;
		userRegionName = regionName;
		this.regionName = regionName.replace('.', '/');
		this.transactionManager = transactionManager;
	}

	public Object get(Object key) throws CacheException {
		Transaction tx = suspend();
		try {
			return read(key);
		}
		finally {
			resume( tx );
		}
	}
	
	public Object read(Object key) throws CacheException {
		try {
			return cache.get( new Fqn( new Object[] { regionName, key } ), ITEM );
		}
		catch (Exception e) {
			throw new CacheException(e);
		}
	}

	public void update(Object key, Object value) throws CacheException {
		try {
			cache.put( new Fqn( new Object[] { regionName, key } ), ITEM, value );
		}
		catch (Exception e) {
			throw new CacheException(e);
		}
	}

	public void put(Object key, Object value) throws CacheException {
		Transaction tx = suspend();
		try {
			//do the failfast put outside the scope of the JTA txn
			cache.putFailFast( new Fqn( new Object[] { regionName, key } ), ITEM, value, 0 );
		}
		catch (TimeoutException te) {
			//ignore!
			log.debug("ignoring write lock acquisition failure");
		}
		catch (Exception e) {
			throw new CacheException(e);
		}
		finally {
			resume( tx );
		}
	}

	private void resume(Transaction tx) {
		try {
			if (tx!=null) transactionManager.resume(tx);
		}
		catch (Exception e) {
			throw new CacheException("Could not resume transaction", e);
		}
	}

	private Transaction suspend() {
		Transaction tx = null;
		try {
			if ( transactionManager!=null ) {
				tx = transactionManager.suspend();
			}
		}
		catch (SystemException se) {
			throw new CacheException("Could not suspend transaction", se);
		}
		return tx;
	}

	public void remove(Object key) throws CacheException {
		try {
			cache.remove( new Fqn( new Object[] { regionName, key } ) );
		}
		catch (Exception e) {
			throw new CacheException(e);
		}
	}

	public void clear() throws CacheException {
		try {
			cache.remove( new Fqn(regionName) );
		}
		catch (Exception e) {
			throw new CacheException(e);
		}
	}

	public void destroy() throws CacheException {
		clear();
	}

	public void lock(Object key) throws CacheException {
		throw new UnsupportedOperationException("TreeCache is a fully transactional cache" + regionName);
	}

	public void unlock(Object key) throws CacheException {
		throw new UnsupportedOperationException("TreeCache is a fully transactional cache: " + regionName);
	}

	public long nextTimestamp() {
		return System.currentTimeMillis() / 100;
	}

	public int getTimeout() {
		return 600; //60 seconds
	}

	public String getRegionName() {
		return userRegionName;
	}

	public long getSizeInMemory() {
		return -1;
	}

	public long getElementCountInMemory() {
		try {
			Set children = cache.getChildrenNames( new Fqn(regionName) );
			return children == null ? 0 : children.size();
		}
		catch (Exception e) {
			throw new CacheException(e);
		}
	}

	public long getElementCountOnDisk() {
		return 0;
	}
	
	public Map toMap() {
		try {
			Map result = new HashMap();
			Set childrenNames = cache.getChildrenNames( new Fqn(regionName) );
			if (childrenNames != null) {
				Iterator iter = childrenNames.iterator();
				while ( iter.hasNext() ) {
					Object key = iter.next();
					result.put( 
							key, 
							cache.get( new Fqn( new Object[] { regionName, key } ), ITEM ) 
						);
				}
			}
			return result;
		}
		catch (Exception e) {
			throw new CacheException(e);
		}
	}
	
	public String toString() {
		return "TreeCache(" + userRegionName + ')';
	}
	
}

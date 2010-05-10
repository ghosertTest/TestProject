package org.hibernate.util;

import org.apache.commons.collections.ReferenceMap;

import java.util.Map;
import java.io.Serializable;

/**
 * Emulates constant time LRU/MRU cache algorithm.  Specifically, it holds all
 * entries in a completely soft-reference cache while maintaining
 * strong-references to the most recently utilized (MRU) entries.
 *
 * @author <a href="mailto:steve@hibernate.org">Steve Ebersole </a>
 */
public class SimpleMRUCache implements Serializable {

	private static final int MAX_STRONG_REF_COUNT = 128; //TODO: configurable?
	private final transient Object[] strongRefs = new Object[MAX_STRONG_REF_COUNT]; //strong reference to MRU queries
	private transient int strongRefIndex = 0;
	private final transient Map softQueryCache = new ReferenceMap(ReferenceMap.SOFT, ReferenceMap.SOFT) ;
	// both keys and values may be soft since value keeps a hard ref to the key (and there is a hard ref to MRU values)

	public synchronized Object get(Object key) {
		Object result = softQueryCache.get( key );
		if( result != null ) {
			strongRefs[ ++strongRefIndex % MAX_STRONG_REF_COUNT ] = result;
		}
		return result;
	}

	public void put(Object key, Object value) {
		softQueryCache.put( key, value );
		strongRefs[ ++strongRefIndex % MAX_STRONG_REF_COUNT ] = value;
	}
}

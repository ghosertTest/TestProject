//$Id: HolderInstantiator.java,v 1.1 2005/04/22 18:05:56 oneovthafew Exp $
package org.hibernate.hql;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.QueryException;

/**
 * @author Gavin King
 */
public final class HolderInstantiator {
	
	//TODO: have an interface and three different subclasses!
	
	private final Constructor constructor;
	private final boolean returnMaps;
	private final boolean returnLists; 
	private final String[] queryReturnAliases;
	
	public HolderInstantiator( 
			Constructor constructor, 
			boolean returnMaps, 
			boolean returnLists, 
			String[] queryReturnAliases
	) {
		this.constructor = constructor;
		this.returnLists = returnLists;
		this.returnMaps = returnMaps;
		this.queryReturnAliases = queryReturnAliases;
	}
	
	public boolean isRequired() {
		return constructor!=null || returnLists || returnMaps;
	}
	
	public Object instantiate(Object[] row) {
		if ( constructor != null ) {
				try {
					return constructor.newInstance( row );
				}
				catch ( Exception e ) {
					throw new QueryException( 
						"could not instantiate: " + 
						constructor.getDeclaringClass().getName(), 
						e );
				}
		}
		else if ( returnMaps ) {
				Map map = new HashMap();
				for ( int j = 0; j < row.length; j++ ) {
					map.put( queryReturnAliases[j], row[j] );
				}
				return map;
		}
		else if ( returnLists ) {
				return Arrays.asList(row);
		}
		else {
			return row;
		}
	}
	
}

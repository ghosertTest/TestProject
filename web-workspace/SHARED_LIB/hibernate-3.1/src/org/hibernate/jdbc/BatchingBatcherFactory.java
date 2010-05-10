//$Id: BatchingBatcherFactory.java,v 1.6 2005/07/29 19:10:18 maxcsaucdk Exp $
package org.hibernate.jdbc;

import org.hibernate.Interceptor;


/**
 * A BatcherFactory implementation which constructs Batcher instances
 * capable of actually performing batch operations.
 * 
 * @author Gavin King
 */
public class BatchingBatcherFactory implements BatcherFactory {

	public Batcher createBatcher(ConnectionManager connectionManager, Interceptor interceptor) {
		return new BatchingBatcher( connectionManager, interceptor );
	}

}

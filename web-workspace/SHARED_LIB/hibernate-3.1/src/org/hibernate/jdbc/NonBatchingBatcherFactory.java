//$Id: NonBatchingBatcherFactory.java,v 1.5 2005/07/29 19:10:18 maxcsaucdk Exp $
package org.hibernate.jdbc;

import org.hibernate.Interceptor;


/**
 * A BatcherFactory implementation which constructs Batcher instances
 * that do not perform batch operations.
 *
 * @author Gavin King
 */
public class NonBatchingBatcherFactory implements BatcherFactory {

	public Batcher createBatcher(ConnectionManager connectionManager, Interceptor interceptor) {
		return new NonBatchingBatcher( connectionManager, interceptor );
	}

}

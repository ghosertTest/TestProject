//$Id: BatcherFactory.java,v 1.5 2005/07/29 19:10:18 maxcsaucdk Exp $
package org.hibernate.jdbc;

import org.hibernate.Interceptor;


/**
 * Factory for <tt>Batcher</tt> instances.
 * @author Gavin King
 */
public interface BatcherFactory {
	public Batcher createBatcher(ConnectionManager connectionManager, Interceptor interceptor);
}

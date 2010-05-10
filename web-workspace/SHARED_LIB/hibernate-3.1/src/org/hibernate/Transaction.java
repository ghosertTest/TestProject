//$Id: Transaction.java,v 1.7 2005/12/10 17:25:32 steveebersole Exp $
package org.hibernate;

import javax.transaction.Synchronization;

/**
 * Allows the application to define units of work, while
 * maintaining abstraction from the underlying transaction
 * implementation (eg. JTA, JDBC).<br>
 * <br>
 * A transaction is associated with a <tt>Session</tt> and is
 * usually instantiated by a call to <tt>Session.beginTransaction()</tt>.
 * A single session might span multiple transactions since
 * the notion of a session (a conversation between the application
 * and the datastore) is of coarser granularity than the notion of
 * a transaction. However, it is intended that there be at most one
 * uncommitted <tt>Transaction</tt> associated with a particular
 * <tt>Session</tt> at any time.<br>
 * <br>
 * Implementors are not intended to be threadsafe.
 *
 * @see Session#beginTransaction()
 * @see org.hibernate.transaction.TransactionFactory
 * @author Anton van Straaten
 */
public interface Transaction {
	
	/**
	 * Begin a new transaction.
	 */
	public void begin() throws HibernateException;

	/**
	 * Flush the associated <tt>Session</tt> and end the unit of work.
	 * This method will commit the underlying transaction if and only
	 * if the transaction was initiated by this object.
	 *
	 * @throws HibernateException
	 */
	public void commit() throws HibernateException;

	/**
	 * Force the underlying transaction to roll back.
	 *
	 * @throws HibernateException
	 */
	public void rollback() throws HibernateException;

	/**
	 * Was this transaction rolled back or set to rollback only?
	 *
	 * @return boolean
	 * @throws HibernateException
	 */
	public boolean wasRolledBack() throws HibernateException;

	/**
	 * Check if this transaction was successfully committed. This method
	 * could return <tt>false</tt> even after successful invocation
	 * of <tt>commit()</tt>.
	 *
	 * @return boolean
	 * @throws HibernateException
	 */
	public boolean wasCommitted() throws HibernateException;
	
	/**
	 * Is this transaction still active?
	 * @return boolean 
	 */
	public boolean isActive() throws HibernateException;
	
	/**
	 * Register a user synchronization callback for this transaction
	 */
	public void registerSynchronization(Synchronization synchronization) 
	throws HibernateException;
	
	/**
	 * Set the transaction timeout for any transaction started by
	 * a subsequent call to <tt>begin()</tt> on this instance.
	 */
	public void setTimeout(int seconds);
}

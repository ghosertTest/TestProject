//$Id: JDBCTransactionFactory.java,v 1.8 2005/12/10 17:25:32 steveebersole Exp $
package org.hibernate.transaction;

import java.util.Properties;

import org.hibernate.ConnectionReleaseMode;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.jdbc.JDBCContext;

/**
 * Factory for <tt>JDBCTransaction</tt>.
 * @see JDBCTransaction
 * @author Anton van Straaten
 */
public final class JDBCTransactionFactory implements TransactionFactory {

	public ConnectionReleaseMode getDefaultReleaseMode() {
		return ConnectionReleaseMode.AFTER_TRANSACTION;
	}

	public Transaction createTransaction(JDBCContext jdbcContext, Context transactionContext) 
	throws HibernateException {
		return new JDBCTransaction( jdbcContext, transactionContext );
	}

	public void configure(Properties props) throws HibernateException {}

	public boolean isTransactionManagerRequired() {
		return false;
	}

	public boolean areCallbacksLocalToHibernateTransactions() {
		return true;
	}

}

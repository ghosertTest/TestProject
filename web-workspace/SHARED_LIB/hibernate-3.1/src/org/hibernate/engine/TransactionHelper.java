//$Id: TransactionHelper.java,v 1.7 2005/10/11 13:53:19 epbernard Exp $
package org.hibernate.engine;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.exception.JDBCExceptionHelper;

/**
 * Allows work to be done outside the current transaction, by suspending it,
 * and performing work in a new transaction
 * 
 * @author Emmanuel Bernard
 */
public abstract class TransactionHelper {
	private static final Log log = LogFactory.getLog( TransactionHelper.class );
	
	/**
	 * The work to be done
	 */
	protected abstract Serializable doWorkInCurrentTransaction(Connection conn, String sql) throws SQLException;

	/**
	 * Suspend the current transaction and perform work in a new transaction
	 */
	public Serializable doWorkInNewTransaction(SessionImplementor session) 
	throws HibernateException {
	
		// This has to be done using a different connection to the
		// containing transaction because the new hi value must
		// remain valid even if the containing transaction rolls
		// back
		TransactionManager tm = session.getFactory().getTransactionManager();
		Transaction surroundingTransaction = null;  // for resuming in finally block
		Connection conn = null; // for ressource cleanup
		String sql = null; // for exception
		boolean isJta = tm != null;
		boolean catchedException = false;
		try {
			if ( isJta ) {
				//JTA environment
				// prepare a new transaction context for the generator
				surroundingTransaction = tm.suspend();
				if ( log.isDebugEnabled() ) {
					log.debug( "surrounding tx suspended" );
				}
				tm.begin();
				// get connection from managed environment
				conn = session.getBatcher().openConnection();
			}
			else {
				// get connection from the hibernate defined pool
				conn = session.getBatcher().openConnection();
				if ( conn.getAutoCommit() ) conn.setAutoCommit( false );
			}

			Serializable result = doWorkInCurrentTransaction( conn, sql );

			// commit transaction to ensure updated sequence is not rolled back
			if ( isJta ) {
				session.getBatcher().closeConnection( conn );
				tm.commit();
			}
			else {
				conn.commit();
			}
			return result;
		}
		catch ( SQLException sqle ) {
			catchedException = true;
			if ( isJta ) {
				try {
					try {
						if ( ! conn.isClosed() ) {
							session.getBatcher().closeConnection( conn );
						}
					}
					catch (Throwable t) {
						//clean as much as we can the JTA context
					}
					tm.rollback();
				}
				catch( Throwable t ) {
					//clean as much as we can the JTA context
				}
			}
			throw JDBCExceptionHelper.convert( 
					session.getFactory().getSQLExceptionConverter(),
					sqle,
					"could not get or update next value",
					sql 
				);
		}
		catch ( Exception e ) {
			catchedException = true;
			if ( isJta ) {
				try {
					try {
						if ( ! conn.isClosed() ) {
							session.getBatcher().closeConnection( conn );
						}
					}
					catch (Throwable t) {
						//clean as much as we can the JTA context
					}
					tm.rollback();
					throw new HibernateException( e );
				}
				catch ( SystemException e1 ) {
					//TODO raise the original exception?
					throw new HibernateException( e1 );
				}
			}
			else {
				throw new HibernateException( e );
			}
		}
		finally {
			if ( isJta ) {
				// switch back to surrounding transaction context
				if ( isJta && surroundingTransaction != null ) {
					try {
						tm.resume( surroundingTransaction );
						if ( log.isDebugEnabled() ) {
							log.debug( "surrounding tx resumed" );
						}
					}
					catch ( Exception e ) {
						//do not hide a previous exception
						if ( ! catchedException ) throw new HibernateException( e );
					}
				}
			}
			else {
				session.getBatcher().closeConnection( conn );
			}
		}
	}
}

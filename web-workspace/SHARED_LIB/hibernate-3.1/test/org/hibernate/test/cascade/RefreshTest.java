// $Id: RefreshTest.java,v 1.2 2005/05/04 02:32:31 steveebersole Exp $
package org.hibernate.test.cascade;

import org.hibernate.test.TestCase;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.PreparedStatement;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Implementation of RefreshTest.
 *
 * @author Steve Ebersole
 */
public class RefreshTest extends TestCase {

	public RefreshTest(String name) {
		super( name );
	}

	protected String[] getMappings() {
		return new String[] {
			"cascade/Job.hbm.xml",
			"cascade/JobBatch.hbm.xml"
		};
	}

	public static Test suite() {
		return new TestSuite( RefreshTest.class );
	}

	public void testRefreshCascade() throws Throwable {
		Session session = openSession();
		Transaction txn = session.beginTransaction();

		JobBatch batch = new JobBatch( new Date() );
		batch.createJob().setProcessingInstructions( "Just do it!" );
		batch.createJob().setProcessingInstructions( "I know you can do it!" );

		// write the stuff to the database; at this stage all job.status values are zero
		session.persist( batch );
		session.flush();

		// behind the session's back, let's modify the statuses
		updateStatuses( session.connection() );

		// Now lets refresh the persistent batch, and see if the refresh cascaded to the jobs collection elements
		session.refresh( batch );

		Iterator itr = batch.getJobs().iterator();
		while( itr.hasNext() ) {
			Job job = ( Job ) itr.next();
			assertEquals( "Jobs not refreshed!", 1, job.getStatus() );
		}

		txn.rollback();
		session.close();
	}

	private void updateStatuses(Connection connection) throws Throwable {

		PreparedStatement stmnt = null;
		try {
			stmnt = connection.prepareStatement( "UPDATE t_job SET job_status = 1" );
			stmnt.executeUpdate();
		}
		finally {
			if ( stmnt != null ) {
				try {
					stmnt.close();
				}
				catch( Throwable ignore ) {
				}
			}
		}
	}
}

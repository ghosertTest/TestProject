// $Id: AggressiveReleaseTest.java,v 1.4 2005/06/03 16:15:48 steveebersole Exp $
package org.hibernate.test.connections;

import org.hibernate.Session;
import org.hibernate.ConnectionReleaseMode;
import org.hibernate.ScrollableResults;
import org.hibernate.util.SerializationHelper;
import org.hibernate.test.tm.DummyConnectionProvider;
import org.hibernate.test.tm.DummyTransactionManagerLookup;
import org.hibernate.test.tm.DummyTransactionManager;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import junit.framework.Test;
import junit.framework.TestSuite;

import java.sql.Connection;

/**
 * Implementation of AggressiveReleaseTest.
 *
 * @author Steve Ebersole
 */
public class AggressiveReleaseTest extends ConnectionManagementTestCase {

	public AggressiveReleaseTest(String name) {
		super( name );
	}

	public static Test suite() {
		return new TestSuite( AggressiveReleaseTest.class );
	}

	protected void configure(Configuration cfg) {
		cfg.setProperty( Environment.RELEASE_CONNECTIONS, ConnectionReleaseMode.AFTER_STATEMENT.toString() );
		cfg.setProperty( Environment.CONNECTION_PROVIDER, DummyConnectionProvider.class.getName() );
		cfg.setProperty( Environment.TRANSACTION_MANAGER_STRATEGY, DummyTransactionManagerLookup.class.getName() );
	}

	protected Session getSessionUnderTest() throws Throwable {
		return openSession();
	}

	protected void reconnect(Session session) {
		session.reconnect();
	}

	protected void prepare() throws Throwable {
		DummyTransactionManager.INSTANCE.begin();
	}

	protected void done() throws Throwable {
//		long initialCount = getSessions().getStatistics().getSessionCloseCount();
		DummyTransactionManager.INSTANCE.commit();
//		long currentCount = getSessions().getStatistics().get.getSessionCloseCount();
//		assertEquals( "Transaction commit did not force close of session", initialCount, currentCount - 1 );
	}

	// Some additional tests specifically for the aggressive-release functionality...

	public void testSerializationOnAfterStatementAggressiveRelease() throws Throwable {
		prepare();
		Session s = getSessionUnderTest();
		Silly silly = new Silly( "silly" );
		s.save( silly );

		// this should cause the CM to obtain a connection, and then release it
		s.flush();

		// We should be able to serialize the session at this point...
		SerializationHelper.serialize( s );

		s.delete( silly );
		s.flush();

		release( s );
		done();
	}

	public void testSerializationFailsOnAfterStatementAggressiveReleaseWithOpenResources() throws Throwable {
		prepare();
		Session s = getSessionUnderTest();

		Silly silly = new Silly( "silly" );
		s.save( silly );

		// this should cause the CM to obtain a connection, and then release it
		s.flush();

		// both scroll() and iterate() cause the batcher to hold on
		// to resources, which should make aggresive-release not release
		// the connection (and thus cause serialization to fail)
		ScrollableResults sr = s.createQuery( "from Silly" ).scroll();

		try {
			SerializationHelper.serialize( s );
			fail( "Serialization allowed on connected session; or aggressive release released connection with open resources" );
		}
		catch( IllegalStateException e ) {
			// expected behavior
		}

		// Closing the ScrollableResults does currently force the batcher to
		// aggressively release the connection
		sr.close();
		SerializationHelper.serialize( s );

		s.delete( silly );
		s.flush();

		release( s );
		done();
	}

	public void testSuppliedConnection() throws Throwable {
		prepare();

		Connection originalConnection = DummyTransactionManager.INSTANCE.getCurrent().getConnection();
		Session session = getSessions().openSession( originalConnection );

		Silly silly = new Silly( "silly" );
		session.save( silly );

		// this will cause the connection manager to cycle through the aggressive release logic;
		// it should not release the connection since we explicitly suplied it ourselves.
		session.flush();

		assertTrue( "Different connections", originalConnection == session.connection() );

		session.delete( silly );
		session.flush();

		release( session );
		done();
	}
}

// $Id: CurrentSessionConnectionTest.java,v 1.2 2005/10/05 19:40:14 steveebersole Exp $
package org.hibernate.test.connections;

import org.hibernate.Session;
import org.hibernate.test.tm.DummyConnectionProvider;
import org.hibernate.test.tm.DummyTransactionManagerLookup;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Implementation of CurrentSessionConnectionTest.
 *
 * @author Steve Ebersole
 */
public class CurrentSessionConnectionTest extends AggressiveReleaseTest {

	public CurrentSessionConnectionTest(String name) {
		super( name );
	}

	public static Test suite() {
		return new TestSuite( CurrentSessionConnectionTest.class );
	}

	protected Session getSessionUnderTest() throws Throwable {
		return getSessions().getCurrentSession();
	}

	protected void release(Session session) {
		// do nothing, txn synch should release session as part of current-session definition
	}
}

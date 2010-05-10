// $Id: BasicConnectionProviderTest.java,v 1.2 2005/05/12 18:27:21 steveebersole Exp $
package org.hibernate.test.connections;

import org.hibernate.Session;
import org.hibernate.ConnectionReleaseMode;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Implementation of BasicConnectionProviderTest.
 *
 * @author Steve Ebersole
 */
public class BasicConnectionProviderTest extends ConnectionManagementTestCase {

	public BasicConnectionProviderTest(String name) {
		super( name );
	}

	public static Test suite() {
		return new TestSuite( BasicConnectionProviderTest.class );
	}

	protected Session getSessionUnderTest() {
		return openSession();
	}

	protected void reconnect(Session session) {
		session.reconnect();
	}

	protected void configure(Configuration cfg) {
		cfg.setProperty( Environment.RELEASE_CONNECTIONS, ConnectionReleaseMode.ON_CLOSE.toString() );
	}
}

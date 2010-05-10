// $Id: SuppliedConnectionTest.java,v 1.2 2005/05/12 18:27:21 steveebersole Exp $
package org.hibernate.test.connections;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.ConnectionReleaseMode;
import org.hibernate.Session;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.connection.ConnectionProviderFactory;
import org.hibernate.connection.UserSuppliedConnectionProvider;

import java.sql.Connection;
import java.sql.ResultSet;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Implementation of SuppliedConnectionTest.
 *
 * @author Steve Ebersole
 */
public class SuppliedConnectionTest extends ConnectionManagementTestCase {

	private ConnectionProvider cp = ConnectionProviderFactory.newConnectionProvider();
	private Connection connectionUnderTest;

	public SuppliedConnectionTest(String name) {
		super( name );
	}

	public static Test suite() {
		return new TestSuite( SuppliedConnectionTest.class );
	}

	protected Session getSessionUnderTest() throws Throwable {
		connectionUnderTest = cp.getConnection();
		return getSessions().openSession( connectionUnderTest );
	}

	protected void reconnect(Session session) {
		session.reconnect( connectionUnderTest );
	}

	protected void done() throws Throwable {
		cp.closeConnection( connectionUnderTest );
	}

	protected void configure(Configuration cfg) {
		cfg.setProperty( Environment.RELEASE_CONNECTIONS, ConnectionReleaseMode.ON_CLOSE.toString() );
		cfg.setProperty( Environment.CONNECTION_PROVIDER, UserSuppliedConnectionProvider.class.getName() );
		boolean supportsScroll = true;
		try {
			Connection conn = cp.getConnection();
			supportsScroll = conn.getMetaData().supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
		}
		catch( Throwable ignore ) {
		}
		cfg.setProperty( Environment.USE_SCROLLABLE_RESULTSET, "" + supportsScroll );
	}

	protected boolean dropAfterFailure() {
		return false;
	}

	protected boolean recreateSchema() {
		return false;
	}

	protected void setUp() throws Exception {
		super.setUp();
		Connection conn = cp.getConnection();
		try {
			new SchemaExport( getCfg(), conn ).create( false, true );
		}
		finally {
			if ( conn != null ) {
				try {
					cp.closeConnection( conn );
				}
				catch( Throwable ignore ) {
				}
			}
		}
	}

	protected void tearDown() throws Exception {
		Connection conn = cp.getConnection();
		try {
			new SchemaExport( getCfg(), conn ).drop( false, true );
		}
		finally {
			if ( conn != null ) {
				try {
					cp.closeConnection( conn );
				}
				catch( Throwable ignore ) {
				}
			}
		}
		try {
			cp.close();
		}
		catch( Throwable ignore ) {
		}
		super.tearDown();
	}
}

// $Id: ConnectionManagementTestCase.java,v 1.6 2005/12/10 17:25:56 steveebersole Exp $
package org.hibernate.test.connections;

import org.hibernate.test.TestCase;
import org.hibernate.Session;
import org.hibernate.util.SerializationHelper;

/**
 * Implementation of ConnectionManagementTestCase.
 *
 * @author Steve Ebersole
 */
public abstract class ConnectionManagementTestCase extends TestCase {

	public ConnectionManagementTestCase(String name) {
		super( name );
	}

	protected final String[] getMappings() {
		return new String[] { "connections/Silly.hbm.xml" };
	}

	protected void release(Session session) {
		if ( session != null ) {
			try {
				session.close();
			}
			catch( Throwable ignore ) {
			}
		}
	}

	protected void prepare() throws Throwable {
	}
	protected abstract Session getSessionUnderTest() throws Throwable;
	protected abstract void reconnect(Session session) throws Throwable;
	protected void done() throws Throwable {
	}
	protected void checkSerializedState(Session session) {
	}
	protected void checkDeserializedState(Session session) {
	}

	public final void testConnectedSerialization() throws Throwable {
		prepare();
		Session sessionUnderTest = getSessionUnderTest();

		// force the connection to be retained
		sessionUnderTest.createQuery( "from Silly" ).scroll();

		try {
			SerializationHelper.serialize( sessionUnderTest );

			fail( "Serialization of connected session allowed!" );
		}
		catch( IllegalStateException e ) {
			// expected behaviour
		}
		finally {
			release( sessionUnderTest );
			done();
		}
	}

	public final void testManualDisconnectedSerialization() throws Throwable {
		prepare();
		Session sessionUnderTest = getSessionUnderTest();

		sessionUnderTest.disconnect();

		SerializationHelper.serialize( sessionUnderTest );
		checkSerializedState( sessionUnderTest );

		release( sessionUnderTest );
		done();
	}

	public final void testManualDisconnectChain() throws Throwable {
		prepare();
		Session sessionUnderTest = getSessionUnderTest();

		sessionUnderTest.disconnect();

		byte[] bytes = SerializationHelper.serialize( sessionUnderTest );
		checkSerializedState( sessionUnderTest );
		Session s2 = ( Session ) SerializationHelper.deserialize( bytes );
		checkDeserializedState( s2 );

		reconnect( s2 );

		s2.disconnect();
		reconnect( s2 );

		release( sessionUnderTest );
		release( s2 );
		done();
	}

	public final void testManualDisconnectWithOpenResources() throws Throwable {
		prepare();
		Session sessionUnderTest = getSessionUnderTest();

		Silly silly = new Silly( "tester" );
		sessionUnderTest.save( silly );
		sessionUnderTest.flush();

		sessionUnderTest.createQuery( "from Silly" ).iterate();

		sessionUnderTest.disconnect();
		SerializationHelper.serialize( sessionUnderTest );
		checkSerializedState( sessionUnderTest );

		reconnect( sessionUnderTest );
		sessionUnderTest.createQuery( "from Silly" ).scroll();

		sessionUnderTest.disconnect();
		SerializationHelper.serialize( sessionUnderTest );
		checkSerializedState( sessionUnderTest );

		reconnect( sessionUnderTest );
		sessionUnderTest.delete( silly );
		sessionUnderTest.flush();

		release( sessionUnderTest );
		done();
	}
}

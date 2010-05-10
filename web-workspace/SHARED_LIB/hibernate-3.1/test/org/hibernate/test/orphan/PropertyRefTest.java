// $Id: PropertyRefTest.java,v 1.2 2005/06/22 18:58:16 oneovthafew Exp $
package org.hibernate.test.orphan;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.TestCase;


/**
 * Test for HHH-565
 *
 * @author Steve Ebersole
 */
public class PropertyRefTest extends TestCase {

	public PropertyRefTest(String name) {
		super( name );
	}

	protected String[] getMappings() {
		return new String[] {
			"orphan/User.hbm.xml",
			"orphan/Mail.hbm.xml"
		};
	}

	public void testDeleteParentWithBidirOrphanDeleteCollectionBasedOnPropertyRef() {
		Session session = openSession();
		Transaction txn = session.beginTransaction();
		User user = new User( "test" );
		user.addMail( "test" );
		user.addMail( "test" );
		session.save( user );
		txn.commit();
		session.close();

		session = openSession();
		txn = session.beginTransaction();
		user = ( User ) session.load( User.class, user.getId() );
		session.delete( user );
		txn.commit();
		session.close();

		session = openSession();
		txn = session.beginTransaction();
		session.createQuery( "delete from Mail where alias = :alias" ).setString( "alias", "test" ).executeUpdate();
		session.createQuery( "delete from User where userid = :userid" ).setString( "userid", "test" ).executeUpdate();
		txn.commit();
		session.close();
	}

	public static Test suite() {
		return new TestSuite(PropertyRefTest.class);
	}
	
}

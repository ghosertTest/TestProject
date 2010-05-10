//$Id$
package org.hibernate.test.usercollection;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.TestCase;

/**
 * @author Max Rydahl Andersen
 */
public class UserCollectionTypeTest extends TestCase {
	
	public UserCollectionTypeTest(String str) {
		super(str);
	}

	public void testBasicOperation() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User u = new User("max");
		u.getEmailAddresses().add( new Email("max@hibernate.org") );
		u.getEmailAddresses().add( new Email("max.andersen@jboss.com") );
		s.persist(u);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		User u2 = (User) s.createCriteria(User.class).uniqueResult();
		assertTrue( Hibernate.isInitialized( u2.getEmailAddresses() ) );
		assertEquals( u2.getEmailAddresses().size(), 2 );
		s.delete(u2);
		t.commit();
		s.close();
	}
	
		
	protected String[] getMappings() {
		return new String[] { "usercollection/UserPermissions.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(UserCollectionTypeTest.class);
	}

}


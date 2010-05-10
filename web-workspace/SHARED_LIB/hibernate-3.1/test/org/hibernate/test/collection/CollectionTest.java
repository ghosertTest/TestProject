//$Id: CollectionTest.java,v 1.13 2005/07/24 06:55:01 oneovthafew Exp $
package org.hibernate.test.collection;

import java.sql.SQLException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class CollectionTest extends TestCase {
	
	public CollectionTest(String str) {
		super(str);
	}

	public void testExtraLazy() throws HibernateException, SQLException {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User u = new User("gavin");
		u.getPermissions().add( new Permission("obnoxiousness") );
		u.getPermissions().add( new Permission("pigheadedness") );
		u.getSessionData().put("foo", "foo value");
		s.persist(u);
		t.commit();
		s.close();
		s = openSession();
		t = s.beginTransaction();
		u = (User) s.get(User.class, "gavin");
		
		assertFalse( Hibernate.isInitialized( u.getPermissions() ) );
		assertEquals( u.getPermissions().size(), 2 );
		assertTrue( u.getPermissions().contains( new Permission("obnoxiousness") ) );
		assertFalse( u.getPermissions().contains( new Permission("silliness") ) );
		assertNotNull( u.getPermissions().get(1) );
		assertNull( u.getPermissions().get(3) );
		assertFalse( Hibernate.isInitialized( u.getPermissions() ) );
		
		assertFalse( Hibernate.isInitialized( u.getSessionData() ) );
		assertEquals( u.getSessionData().size(), 1 );
		assertTrue( u.getSessionData().containsKey("foo") );
		assertFalse( u.getSessionData().containsKey("bar") );
		assertTrue( u.getSessionData().containsValue("foo value") );
		assertFalse( u.getSessionData().containsValue("bar") );
		assertEquals( "foo value", u.getSessionData().get("foo") );
		assertNull( u.getSessionData().get("bar") );
		assertFalse( Hibernate.isInitialized( u.getSessionData() ) );
		
		assertFalse( Hibernate.isInitialized( u.getSessionData() ) );
		u.getSessionData().put("bar", "bar value");
		u.getSessionAttributeNames().add("bar");
		assertFalse( Hibernate.isInitialized( u.getSessionAttributeNames() ) );
		assertTrue( Hibernate.isInitialized( u.getSessionData() ) );

		s.delete(u);
		t.commit();
		s.close();
	}
	
	/*public void testFilter() throws HibernateException, SQLException {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User u = new User("gavin");
		u.getPermissions().add( new Permission("obnoxiousness") );
		u.getPermissions().add( new Permission("pigheadedness") );
		s.persist(u);
		t.commit();
		s.close();
		s = openSession();
		t = s.beginTransaction();
		u = (User) s.get(User.class, "gavin");
		assertFalse( Hibernate.isInitialized( u.getPermissions() ) );
		assertEquals( s.createFilter( u.getPermissions(), "select count(*)" ).uniqueResult(), new Integer(2) );
		assertFalse( Hibernate.isInitialized( u.getPermissions() ) );
		assertFalse( Hibernate.isInitialized( u.getSessionData() ) );
		assertEquals( s.createFilter( u.getSessionData(), "select count(*)" ).uniqueResult(), new Integer(0) );
		assertFalse( Hibernate.isInitialized( u.getSessionData() ) );
		s.delete(u);
		t.commit();
		s.close();
	}*/
	
	public void testMerge() throws HibernateException, SQLException {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User u = new User("gavin");
		u.getPermissions().add( new Permission("obnoxiousness") );
		u.getPermissions().add( new Permission("pigheadedness") );
		s.persist(u);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		User u2 = (User) s.createCriteria(User.class).uniqueResult();
		u2.setPermissions(null); //forces one shot delete
		s.merge(u);
		t.commit();
		s.close();
		
		u.getPermissions().add( new Permission("silliness") );
		
		s = openSession();
		t = s.beginTransaction();
		s.merge(u);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		u2 = (User) s.createCriteria(User.class).uniqueResult();
		assertEquals( u2.getPermissions().size(), 3 );
		assertEquals( ( (Permission) u2.getPermissions().get(0) ).getType(), "obnoxiousness" );
		assertEquals( ( (Permission) u2.getPermissions().get(2) ).getType(), "silliness" );
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		s.delete(u2);
		s.flush();
		t.commit();
		s.close();
		
	}
	
	public void testFetch() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User u = new User("gavin");
		u.getPermissions().add( new Permission("obnoxiousness") );
		u.getPermissions().add( new Permission("pigheadedness") );
		u.getEmailAddresses().add( new Email("gavin@hibernate.org") );
		u.getEmailAddresses().add( new Email("gavin.king@jboss.com") );
		s.persist(u);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		User u2 = (User) s.createCriteria(User.class).uniqueResult();
		assertTrue( Hibernate.isInitialized( u2.getEmailAddresses() ) );
		assertFalse( Hibernate.isInitialized( u2.getPermissions() ) );
		assertEquals( u2.getEmailAddresses().size(), 2 );
		s.delete(u2);
		t.commit();
		s.close();
	}
	
	public void testUpdateOrder() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User u = new User("gavin");
		u.getSessionData().put("foo", "foo value");
		u.getSessionData().put("bar", "bar value");
		u.getEmailAddresses().add( new Email("gavin.king@jboss.com") );
		u.getEmailAddresses().add( new Email("gavin@hibernate.org") );
		u.getEmailAddresses().add( new Email("gavin@illflow.com") );
		u.getEmailAddresses().add( new Email("gavin@nospam.com") );
		s.persist(u);
		t.commit();
		s.close();
		
		u.getSessionData().clear();
		u.getSessionData().put("baz", "baz value");
		u.getSessionData().put("bar", "bar value");
		u.getEmailAddresses().remove(0);
		u.getEmailAddresses().remove(2);
		
		s = openSession();
		t = s.beginTransaction();
		s.update(u);
		t.commit();
		s.close();
		
		u.getSessionData().clear();
		u.getEmailAddresses().add(0, new Email("gavin@nospam.com") );
		u.getEmailAddresses().add( new Email("gavin.king@jboss.com") );
		
		s = openSession();
		t = s.beginTransaction();
		s.update(u);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		s.delete(u);
		t.commit();
		s.close();
		
	}
	
	public void testValueMap() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User u = new User("gavin");
		u.getSessionData().put("foo", "foo value");
		u.getSessionData().put("bar", null);
		u.getEmailAddresses().add(null);
		u.getEmailAddresses().add( new Email("gavin.king@jboss.com") );
		u.getEmailAddresses().add(null);
		u.getEmailAddresses().add(null);
		s.persist(u);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		User u2 = (User) s.createCriteria(User.class).uniqueResult();
		assertFalse( Hibernate.isInitialized( u2.getSessionData() ) );
		assertEquals( u2.getSessionData().size(), 1 );
		assertEquals( u2.getEmailAddresses().size(), 2 );
		u2.getSessionData().put("foo", "new foo value");
		u2.getEmailAddresses().set( 1, new Email("gavin@hibernate.org") );
		//u2.getEmailAddresses().remove(3);
		//u2.getEmailAddresses().remove(2);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		u2 = (User) s.createCriteria(User.class).uniqueResult();
		assertFalse( Hibernate.isInitialized( u2.getSessionData() ) );
		assertEquals( u2.getSessionData().size(), 1 );
		assertEquals( u2.getEmailAddresses().size(), 2 );
		assertEquals( u2.getSessionData().get("foo"), "new foo value" );
		assertEquals( ( (Email) u2.getEmailAddresses().get(1) ).getAddress(), "gavin@hibernate.org" );
		s.delete(u2);
		t.commit();
		s.close();
	}
	
	
	
	protected String[] getMappings() {
		return new String[] { "collection/UserPermissions.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(CollectionTest.class);
	}

}


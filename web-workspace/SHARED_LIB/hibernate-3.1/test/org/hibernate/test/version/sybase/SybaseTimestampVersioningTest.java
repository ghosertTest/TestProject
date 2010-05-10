// $Id: SybaseTimestampVersioningTest.java,v 1.3 2005/08/10 17:20:25 steveebersole Exp $
package org.hibernate.test.version.sybase;

import org.hibernate.test.TestCase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.SybaseDialect;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Implementation of VersionTest.
 *
 * @author Steve Ebersole
 */
public class SybaseTimestampVersioningTest extends TestCase {

	public SybaseTimestampVersioningTest(String x) {
		super( x );
	}

	public boolean isRunnable() {
		return Dialect.getDialect() instanceof SybaseDialect;
	}

	protected void setUp() throws Exception {
		// only build SF (and export schema) if this is running on a sybase derivative dialect
		// (which support the TIMESTAMP datatype).
		if ( isRunnable() ) {
			super.setUp();
		}
	}

	protected void runTest() throws Throwable {
		// only run test if this is running on a sybase derivative dialect
		// (which support the TIMESTAMP datatype).
		if ( isRunnable() ) {
			super.runTest();
		}
	}

	protected String[] getMappings() {
		return new String[] { "version/sybase/User.hbm.xml" };
	}


	public void testLocking() throws Throwable {
		// First, create the needed row...
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User steve = new User( "steve" );
		s.persist( steve );
		t.commit();
		s.close();

		// next open two sessions, and try to update from each "simultaneously"...
		Session s1 = null;
		Session s2 = null;
		Transaction t1 = null;
		Transaction t2 = null;
		try {
			s1 = getSessions().openSession();
			t1 = s1.beginTransaction();
			s2 = getSessions().openSession();
			t2 = s2.beginTransaction();

			User user1 = ( User ) s1.get( User.class, steve.getId() );
			User user2 = ( User ) s2.get( User.class, steve.getId() );

			user1.setUsername( "se" );
			t1.commit();
			t1 = null;

			user2.setUsername( "steve-e" );
			try {
				t2.commit();
				fail( "optimistic lock check did not fail" );
			}
			catch( HibernateException e ) {
				// expected...
				try {
					t2.rollback();
				}
				catch( Throwable ignore ) {
				}
			}
		}
		catch( Throwable error ) {
			if ( t1 != null ) {
				try {
					t1.rollback();
				}
				catch( Throwable ignore ) {
				}
			}
			if ( t2 != null ) {
				try {
					t2.rollback();
				}
				catch( Throwable ignore ) {
				}
			}
			throw error;
		}
		finally {
			if ( s1 != null ) {
				try {
					s1.close();
				}
				catch( Throwable ignore ) {
				}
			}
			if ( s2 != null ) {
				try {
					s2.close();
				}
				catch( Throwable ignore ) {
				}
			}
		}

		// lastly, clean up...
		s = openSession();
		t = s.beginTransaction();
		s.delete( s.load( User.class, steve.getId() ) );
		t.commit();
		s.close();
	}

	public void testCollectionVersion() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User steve = new User( "steve" );
		s.persist( steve );
		Group admin = new Group( "admin" );
		s.persist( admin );
		t.commit();
		s.close();

		byte[] steveTimestamp = steve.getTimestamp();

		s = openSession();
		t = s.beginTransaction();
		steve = ( User ) s.get( User.class, steve.getId() );
		admin = ( Group ) s.get( Group.class, admin.getId() );
		steve.getGroups().add( admin );
		admin.getUsers().add( steve );
		t.commit();
		s.close();

		assertFalse( "owner version not incremented", Hibernate.BINARY.isEqual( steveTimestamp, steve.getTimestamp() ) );

		steveTimestamp = steve.getTimestamp();

		s = openSession();
		t = s.beginTransaction();
		steve = ( User ) s.get( User.class, steve.getId() );
		steve.getGroups().clear();
		t.commit();
		s.close();

		assertFalse( "owner version not incremented", Hibernate.BINARY.isEqual( steveTimestamp, steve.getTimestamp() ) );

		s = openSession();
		t = s.beginTransaction();
		s.delete( s.load( User.class, steve.getId() ) );
		s.delete( s.load( Group.class, admin.getId() ) );
		t.commit();
		s.close();
	}


	public void testCollectionNoVersion() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		User steve = new User( "steve" );
		s.persist( steve );
		Permission perm = new Permission( "silly", "user", "rw" );
		s.persist( perm );
		t.commit();
		s.close();

		byte[] steveTimestamp = steve.getTimestamp();

		s = openSession();
		t = s.beginTransaction();
		steve = ( User ) s.get( User.class, steve.getId() );
		perm = ( Permission ) s.get( Permission.class, perm.getId() );
		steve.getPermissions().add( perm );
		t.commit();
		s.close();

		assertTrue( "owner version was incremented", Hibernate.BINARY.isEqual( steveTimestamp, steve.getTimestamp() ) );

		s = openSession();
		t = s.beginTransaction();
		steve = ( User ) s.get( User.class, steve.getId() );
		steve.getPermissions().clear();
		t.commit();
		s.close();

		assertTrue( "owner version was incremented", Hibernate.BINARY.isEqual( steveTimestamp, steve.getTimestamp() ) );

		s = openSession();
		t = s.beginTransaction();
		s.delete( s.load( User.class, steve.getId() ) );
		s.delete( s.load( Permission.class, perm.getId() ) );
		t.commit();
		s.close();
	}

	public static Test suite() {
		return new TestSuite(SybaseTimestampVersioningTest.class);
	}
}
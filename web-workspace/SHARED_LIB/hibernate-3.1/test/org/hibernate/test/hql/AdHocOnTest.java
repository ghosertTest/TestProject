// $Id: AdHocOnTest.java,v 1.3 2005/06/15 23:16:17 oneovthafew Exp $
package org.hibernate.test.hql;

import org.hibernate.test.TestCase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Implementation of AdHocOnTest.
 *
 * @author Steve Ebersole
 */
public class AdHocOnTest extends TestCase {

	public AdHocOnTest(String name) {
		super( name );
	}

	protected String[] getMappings() {
		return new String[] { "hql/Animal.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite( AdHocOnTest.class );
	}

	public void testAdHocOnFailsWithFetch() {
		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction txn = s.beginTransaction();

		try {
			s.createQuery( "from Animal a inner join fetch a.offspring as o with o.bodyWeight = :someLimit" )
			        .setDouble( "someLimit", 1 )
			        .list();
			fail( "ad-hoc on clause allowed with fetched association" );
		}
		catch ( HibernateException e ) {
			System.out.println( "TEST (OK) : " + e.getMessage() );
			// the expected response...
		}

		txn.commit();
		s.close();

		data.cleanup();
	}

	public void testAdHocOn() {
		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction txn = s.beginTransaction();

		List list = s.createQuery( "from Animal a inner join a.offspring as o with o.bodyWeight < :someLimit" )
				.setDouble( "someLimit", 1 )
				.list();
		assertTrue( "ad-hoc on did not take effect", list.isEmpty() );

		list = s.createQuery( "from Animal a inner join a.mother as m with m.bodyWeight < :someLimit" )
				.setDouble( "someLimit", 1 )
				.list();
		assertTrue( "ad-hoc on did not take effect", list.isEmpty() );

		txn.commit();
		s.close();

		data.cleanup();
	}

	private class TestData {
		public void prepare() {
			Session session = openSession();
			Transaction txn = session.beginTransaction();

			Animal mother = new Animal();
			mother.setBodyWeight( 10 );
			mother.setDescription( "mother" );

			Animal father = new Animal();
			father.setBodyWeight( 15 );
			father.setDescription( "father" );

			Animal child1 = new Animal();
			child1.setBodyWeight( 5 );
			child1.setDescription( "child1" );

			Animal child2 = new Animal();
			child2.setBodyWeight( 6 );
			child2.setDescription( "child2" );

			child1.setMother( mother );
			child1.setFather( father );
			mother.addOffspring( child1 );
			father.addOffspring( child1 );

			child2.setMother( mother );
			child2.setFather( father );
			mother.addOffspring( child2 );
			father.addOffspring( child2 );

			session.save( mother );
			session.save( father );
			session.save( child1 );
			session.save( child2 );

			txn.commit();
			session.close();
		}

		public void cleanup() {
			Session session = openSession();
			Transaction txn = session.beginTransaction();
			session.createQuery( "delete Animal where mother is not null" ).executeUpdate();
			session.createQuery( "delete Animal" ).executeUpdate();
			txn.commit();
			session.close();
		}
	}
}

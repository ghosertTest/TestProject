//$Id: OrderByTest.java,v 1.1 2005/08/05 23:03:46 oneovthafew Exp $
package org.hibernate.test.ordered;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class OrderByTest extends TestCase {
	
	public OrderByTest(String str) {
		super(str);
	}
	
	public void testOrderBy() {
		Search s = new Search("Hibernate");
		s.getSearchResults().add("jboss.com");
		s.getSearchResults().add("hibernate.org");
		s.getSearchResults().add("HiA");
		
		Session sess = openSession();
		Transaction tx = sess.beginTransaction();
		sess.persist(s);
		sess.flush();
		
		sess.clear();
		s = (Search) sess.createCriteria(Search.class).uniqueResult();
		assertFalse( Hibernate.isInitialized( s.getSearchResults() ) );
		Iterator iter = s.getSearchResults().iterator();
		assertEquals( iter.next(), "HiA" );
		assertEquals( iter.next(), "hibernate.org" );
		assertEquals( iter.next(), "jboss.com" );
		assertFalse( iter.hasNext() );
		
		sess.clear();
		s = (Search) sess.createCriteria(Search.class)
				.setFetchMode("searchResults", FetchMode.JOIN)
				.uniqueResult();
		assertTrue( Hibernate.isInitialized( s.getSearchResults() ) );
		iter = s.getSearchResults().iterator();
		assertEquals( iter.next(), "HiA" );
		assertEquals( iter.next(), "hibernate.org" );
		assertEquals( iter.next(), "jboss.com" );
		assertFalse( iter.hasNext() );
		
		sess.clear();
		s = (Search) sess.createQuery("from Search s left join fetch s.searchResults")
				.uniqueResult();
		assertTrue( Hibernate.isInitialized( s.getSearchResults() ) );
		iter = s.getSearchResults().iterator();
		assertEquals( iter.next(), "HiA" );
		assertEquals( iter.next(), "hibernate.org" );
		assertEquals( iter.next(), "jboss.com" );
		assertFalse( iter.hasNext() );
		
		/*sess.clear();
		s = (Search) sess.createCriteria(Search.class).uniqueResult();
		assertFalse( Hibernate.isInitialized( s.getSearchResults() ) );
		iter = sess.createFilter( s.getSearchResults(), "").iterate();
		assertEquals( iter.next(), "HiA" );
		assertEquals( iter.next(), "hibernate.org" );
		assertEquals( iter.next(), "jboss.com" );
		assertFalse( iter.hasNext() );*/
		
		sess.delete(s);
		tx.commit();
		sess.close();
	}
	
	protected String[] getMappings() {
		return new String[] { "ordered/Search.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(OrderByTest.class);
	}

}


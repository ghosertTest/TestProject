//$Id: NaturalIdTest.java,v 1.4 2005/08/04 00:13:38 oneovthafew Exp $
package org.hibernate.test.naturalid;

import java.lang.reflect.Field;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.criterion.Restrictions;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class NaturalIdTest extends TestCase {
	
	public NaturalIdTest(String str) {
		super(str);
	}
	
	public void testNaturalIdCheck() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		
		User u = new User("gavin", "hb", "secret");
		s.persist(u);
		Field name = u.getClass().getDeclaredField("name");
		name.setAccessible(true);
		name.set(u, "Gavin");
		try {
			s.flush();
			fail();
		}
		catch (HibernateException he) {}
		name.set(u, "gavin");
		s.delete(u);
		t.commit();
		s.close();
	}
	
	public void testNonexistentNaturalIdCache() {
		getSessions().getStatistics().clear();

		Session s = openSession();
		Transaction t = s.beginTransaction();
		
		Object nullUser = s.createCriteria(User.class)
			.add( Restrictions.naturalId()
				.set("name", "gavin")
				.set("org", "hb") 
			)
			.setCacheable(true)
			.uniqueResult();
		
		assertNull(nullUser);
	
		t.commit();
		s.close();
	
		assertEquals( getSessions().getStatistics().getQueryExecutionCount(), 1 );
		assertEquals( getSessions().getStatistics().getQueryCacheHitCount(), 0 );
		assertEquals( getSessions().getStatistics().getQueryCachePutCount(), 0 );
		
		s = openSession();
		t = s.beginTransaction();
		
		User u = new User("gavin", "hb", "secret");
		s.persist(u);
		
		t.commit();
		s.close();
		
		getSessions().getStatistics().clear();

		s = openSession();
		t = s.beginTransaction();
		
		u = (User) s.createCriteria(User.class)
			.add( Restrictions.naturalId()
				.set("name", "gavin")
				.set("org", "hb") 
			)
			.setCacheable(true)
			.uniqueResult();
		
		assertNotNull(u);
		
		t.commit();
		s.close();

		assertEquals( getSessions().getStatistics().getQueryExecutionCount(), 1 );
		assertEquals( getSessions().getStatistics().getQueryCacheHitCount(), 0 );
		assertEquals( getSessions().getStatistics().getQueryCachePutCount(), 1 );
		
		getSessions().getStatistics().clear();

		s = openSession();
		t = s.beginTransaction();
		
		u = (User) s.createCriteria(User.class)
			.add( Restrictions.naturalId()
				.set("name", "gavin")
				.set("org", "hb") 
			).setCacheable(true)
			.uniqueResult();
		
		s.delete(u);
		
		t.commit();
		s.close();
		
		assertEquals( getSessions().getStatistics().getQueryExecutionCount(), 0 );
		assertEquals( getSessions().getStatistics().getQueryCacheHitCount(), 1 );

		getSessions().getStatistics().clear();

		s = openSession();
		t = s.beginTransaction();
		
		nullUser = s.createCriteria(User.class)
			.add( Restrictions.naturalId()
				.set("name", "gavin")
				.set("org", "hb") 
			)
			.setCacheable(true)
			.uniqueResult();
		
		assertNull(nullUser);
	
		t.commit();
		s.close();
	
		assertEquals( getSessions().getStatistics().getQueryExecutionCount(), 1 );
		assertEquals( getSessions().getStatistics().getQueryCacheHitCount(), 0 );
		assertEquals( getSessions().getStatistics().getQueryCachePutCount(), 0 );
		
	}

	public void testNaturalIdCache() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		
		User u = new User("gavin", "hb", "secret");
		s.persist(u);
		
		t.commit();
		s.close();
		
		getSessions().getStatistics().clear();

		s = openSession();
		t = s.beginTransaction();
		
		u = (User) s.createCriteria(User.class)
			.add( Restrictions.naturalId()
				.set("name", "gavin")
				.set("org", "hb") 
			)
			.setCacheable(true)
			.uniqueResult();
		
		assertNotNull(u);
		
		t.commit();
		s.close();

		assertEquals( getSessions().getStatistics().getQueryExecutionCount(), 1 );
		assertEquals( getSessions().getStatistics().getQueryCacheHitCount(), 0 );
		assertEquals( getSessions().getStatistics().getQueryCachePutCount(), 1 );
		
		s = openSession();
		t = s.beginTransaction();
		
		User v = new User("xam", "hb", "foobar");
		s.persist(v);
		
		t.commit();
		s.close();
		
		getSessions().getStatistics().clear();

		s = openSession();
		t = s.beginTransaction();
		
		u = (User) s.createCriteria(User.class)
			.add( Restrictions.naturalId()
				.set("name", "gavin")
				.set("org", "hb") 
			).setCacheable(true)
			.uniqueResult();
		
		assertNotNull(u);
		
		t.commit();
		s.close();
		
		assertEquals( getSessions().getStatistics().getQueryExecutionCount(), 0 );
		assertEquals( getSessions().getStatistics().getQueryCacheHitCount(), 1 );

		s = openSession();
		t = s.beginTransaction();
		s.createQuery("delete User").executeUpdate();
		t.commit();
		s.close();
	}

	public void testQuerying() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		User u = new User("emmanuel", "hb", "bh");
		s.persist(u);

		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();

		u = (User) s.createQuery( "from User u where u.name = :name" )
			.setParameter( "name", "emmanuel" ).uniqueResult();
		assertEquals( "emmanuel", u.getName() );
		s.delete( u );

		t.commit();
		s.close();
	}


	protected void configure(Configuration cfg) {
		cfg.setProperty(Environment.USE_SECOND_LEVEL_CACHE, "true");
		cfg.setProperty(Environment.USE_QUERY_CACHE, "true");
		cfg.setProperty(Environment.GENERATE_STATISTICS, "true");
	}

	protected String[] getMappings() {
		return new String[] { "naturalid/User.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(NaturalIdTest.class);
	}
	
}


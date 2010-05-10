//$Id: QueryCacheTest.java,v 1.13 2005/08/10 05:13:46 oneovthafew Exp $
package org.hibernate.test.querycache;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class QueryCacheTest extends TestCase {
	
	public QueryCacheTest(String str) {
		super(str);
	}
	
	public void testQueryCacheInvalidation() throws Exception {
		
		getSessions().evictQueries();
		getSessions().getStatistics().clear();
		
		Session s = openSession();
		Transaction t = s.beginTransaction();
		s.createQuery("from Item i where i.name='widget'").setCacheable(true).list();
		Item i = new Item();
		i.setName("widget");
		i.setDescription("A really top-quality, full-featured widget.");
		s.save(i);
		t.commit();
		s.close();
		
		QueryStatistics qs = s.getSessionFactory()
			.getStatistics()
			.getQueryStatistics("from org.hibernate.test.querycache.Item i where i.name='widget'");
		EntityStatistics es = s.getSessionFactory()
			.getStatistics()
			.getEntityStatistics( Item.class.getName() );

		Thread.sleep(200);

		s = openSession();
		t = s.beginTransaction();
		List result = s.createQuery("from Item i where i.name='widget'").setCacheable(true).list();
		assertEquals( result.size(), 1 );
		t.commit();
		s.close();
		
		assertEquals( qs.getCacheHitCount(), 0 );
				
		s = openSession();
		t = s.beginTransaction();
		result = s.createQuery("from Item i where i.name='widget'").setCacheable(true).list();
		assertEquals( result.size(), 1 );
		t.commit();
		s.close();
		
		assertEquals( qs.getCacheHitCount(), 1 );
		assertEquals( s.getSessionFactory().getStatistics().getEntityFetchCount(), 0 );
		
		s = openSession();
		t = s.beginTransaction();
		result = s.createQuery("from Item i where i.name='widget'").setCacheable(true).list();
		assertEquals( result.size(), 1 );
		assertTrue( Hibernate.isInitialized( result.get(0) ) );
		i = (Item) result.get(0);
		i.setName("Widget");
		t.commit();
		s.close();
		
		assertEquals( qs.getCacheHitCount(), 2 );
		assertEquals( qs.getCacheMissCount(), 2 );
		assertEquals( s.getSessionFactory().getStatistics().getEntityFetchCount(), 0 );

		Thread.sleep(200);

		s = openSession();
		t = s.beginTransaction();
		result = s.createQuery("from Item i where i.name='widget'").setCacheable(true).list();
		if ( (!(getDialect() instanceof MySQLDialect)) && (!(getDialect() instanceof SQLServerDialect)) ) assertEquals( result.size(), 0 ); //MySQL and SQLServer is case insensitive on strings
		i = (Item) s.get( Item.class, new Long(i.getId()) );
		assertEquals( i.getName(), "Widget" );
		
		s.delete(i);
		t.commit();
		s.close();

		assertEquals( qs.getCacheHitCount(), 2 );
		assertEquals( qs.getCacheMissCount(), 3 );
		assertEquals( qs.getCachePutCount(), 3 );
		assertEquals( qs.getExecutionCount(), 3 );
		assertEquals( es.getFetchCount(), 0 ); //check that it was being cached
		
	}

	public void testQueryCacheFetch() throws Exception {
		
		getSessions().evictQueries();
		getSessions().getStatistics().clear();
		
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Item i = new Item();
		i.setName("widget");
		i.setDescription("A really top-quality, full-featured widget.");
		Item i2 = new Item();
		i2.setName("other widget");
		i2.setDescription("Another decent widget.");
		s.persist(i);
		s.persist(i2);
		t.commit();
		s.close();
		
		QueryStatistics qs = s.getSessionFactory()
			.getStatistics()
			.getQueryStatistics("from org.hibernate.test.querycache.Item i where i.name like '%widget'");

		Thread.sleep(200);

		s = openSession();
		t = s.beginTransaction();
		List result = s.createQuery("from Item i where i.name like '%widget'").setCacheable(true).list();
		assertEquals( result.size(), 2 );
		t.commit();
		s.close();
		
		assertEquals( qs.getCacheHitCount(), 0 );
		assertEquals( s.getSessionFactory().getStatistics().getEntityFetchCount(), 0 );
		
		getSessions().evict(Item.class);
				
		s = openSession();
		t = s.beginTransaction();
		result = s.createQuery("from Item i where i.name like '%widget'").setCacheable(true).list();
		assertEquals( result.size(), 2 );
		assertTrue( Hibernate.isInitialized( result.get(0) ) );
		assertTrue( Hibernate.isInitialized( result.get(1) ) );
		t.commit();
		s.close();
		
		assertEquals( qs.getCacheHitCount(), 1 );
		assertEquals( s.getSessionFactory().getStatistics().getEntityFetchCount(), 1 );

		s = openSession();
		t = s.beginTransaction();
		s.createQuery("delete Item").executeUpdate();
		t.commit();
		s.close();
		
	}

	public void testProjectionCache() throws Exception {

		getSessions().evictQueries();
        getSessions().getStatistics().clear();
		
		Session s = openSession();
		Transaction t = s.beginTransaction();
		s.createQuery("select i.description from Item i where i.name='widget'").setCacheable(true).list();
		Item i = new Item();
		i.setName("widget");
		i.setDescription("A really top-quality, full-featured widget.");
		s.save(i);
		t.commit();
		s.close();

        QueryStatistics qs = s.getSessionFactory()
			.getStatistics()
			.getQueryStatistics("select i.description from org.hibernate.test.querycache.Item i where i.name='widget'");
		EntityStatistics es = s.getSessionFactory()
			.getStatistics()
			.getEntityStatistics( Item.class.getName() );

		Thread.sleep(200);

		s = openSession();
		t = s.beginTransaction();
		List result = s.createQuery("select i.description from Item i where i.name='widget'").setCacheable(true).list();
		assertEquals( result.size(), 1 );
		t.commit();
		s.close();

		assertEquals( qs.getCacheHitCount(), 0 );

		s = openSession();
		t = s.beginTransaction();
		result = s.createQuery("select i.description from Item i where i.name='widget'").setCacheable(true).list();
		assertEquals( result.size(), 1 );
		t.commit();
		s.close();

		assertEquals( qs.getCacheHitCount(), 1 );

		s = openSession();
		t = s.beginTransaction();
		result = s.createQuery("select i.description from Item i where i.name='widget'").setCacheable(true).list();
		assertEquals( result.size(), 1 );
		assertTrue( Hibernate.isInitialized( result.get(0) ) );
		i = (Item) s.get( Item.class, new Long(i.getId()) );
        i.setName("widget");
		i.setDescription("A middle-quality widget.");
		t.commit();
		s.close();

		assertEquals( qs.getCacheHitCount(), 2 );
		assertEquals( qs.getCacheMissCount(), 2 );

		Thread.sleep(200);

		s = openSession();
		t = s.beginTransaction();
		result = s.createQuery("select i.description from Item i where i.name='widget'").setCacheable(true).list();
		assertEquals( result.size(), 1 );
		i = (Item) s.get( Item.class, new Long(i.getId()) );
		assertEquals( (String) result.get(0), "A middle-quality widget." );
		
		s.delete(i);
		t.commit();
		s.close();

		assertEquals( qs.getCacheHitCount(), 2 );
		assertEquals( qs.getCacheMissCount(), 3 );
		assertEquals( qs.getCachePutCount(), 3 );
		assertEquals( qs.getExecutionCount(), 3 );
		assertEquals( es.getFetchCount(), 0 ); //check that it was being cached

	}


	protected String[] getMappings() {
		return new String[] { "querycache/Item.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(QueryCacheTest.class);
	}

	protected void configure(Configuration cfg) {
		super.configure( cfg );
		cfg.setProperty( Environment.USE_QUERY_CACHE, "true" );
		cfg.setProperty( Environment.CACHE_REGION_PREFIX, "foo" );
		cfg.setProperty( Environment.USE_SECOND_LEVEL_CACHE, "true" );
		cfg.setProperty( Environment.GENERATE_STATISTICS, "true" );
	}
}


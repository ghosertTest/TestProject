//$Id: StatsTest.java,v 1.2 2005/07/29 21:43:18 epbernard Exp $
package org.hibernate.test.stats;

import java.util.HashSet;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.mapping.Collection;
import org.hibernate.stat.Statistics;
import org.hibernate.test.TestCase;

/**
 * Show the difference between fetch and load
 *
 * @author Emmanuel Bernard
 */
public class StatsTest extends TestCase {
	public void testCollectionFetchVsLoad() throws Exception {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Statistics stats = getSessions().getStatistics();
		stats.clear();
		boolean isStats = stats.isStatisticsEnabled();
		stats.setStatisticsEnabled(true);
		Continent europe = fillDb(s);
		tx.commit();
		s.clear();
		tx = s.beginTransaction();
		assertEquals(0, stats.getCollectionLoadCount() );
		assertEquals(0,  stats.getCollectionFetchCount() );
		Continent europe2 = (Continent) s.get( Continent.class, europe.getId() );
		assertEquals("Lazy true: no collection should be loaded", 0, stats.getCollectionLoadCount() );
		assertEquals( 0, stats.getCollectionFetchCount() );
		europe2.getCountries().size();
		assertEquals( 1, stats.getCollectionLoadCount() );
		assertEquals("Explicit fetch of the collection state", 1, stats.getCollectionFetchCount() );
		tx.commit();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		stats.clear();
		europe = fillDb(s);
		tx.commit();
		s.clear();
		tx = s.beginTransaction();
		assertEquals( 0, stats.getCollectionLoadCount() );
		assertEquals( 0, stats.getCollectionFetchCount() );
		europe2 = (Continent) s.createQuery(
				"from " + Continent.class.getName() + " a join fetch a.countries where a.id = " + europe.getId()
			).uniqueResult();
		assertEquals( 1, stats.getCollectionLoadCount() );
		assertEquals( "collection should be loaded in the same query as its parent", 0, stats.getCollectionFetchCount() );
		tx.commit();
		s.close();

		Collection coll = getCfg().getCollectionMapping(Continent.class.getName() + ".countries");
		coll.setFetchMode(FetchMode.JOIN);
		coll.setLazy(false);
		SessionFactory sf = getCfg().buildSessionFactory();
		stats = sf.getStatistics();
		stats.clear();
		stats.setStatisticsEnabled(true);
		s = sf.openSession();
		tx = s.beginTransaction();
		europe = fillDb(s);
		tx.commit();
		s.clear();
		tx = s.beginTransaction();
		assertEquals( 0, stats.getCollectionLoadCount() );
		assertEquals( 0, stats.getCollectionFetchCount() );
		europe2 = (Continent) s.get( Continent.class, europe.getId() );
		assertEquals( 1, stats.getCollectionLoadCount() );
		assertEquals( "Should do direct load, not indirect second load when lazy false and JOIN", 0, stats.getCollectionFetchCount() );
		tx.commit();
		s.close();
		sf.close();

		coll = getCfg().getCollectionMapping(Continent.class.getName() + ".countries");
		coll.setFetchMode(FetchMode.SELECT);
		coll.setLazy(false);
		sf = getCfg().buildSessionFactory();
		stats = sf.getStatistics();
		stats.clear();
		stats.setStatisticsEnabled(true);
		s = sf.openSession();
		tx = s.beginTransaction();
		europe = fillDb(s);
		tx.commit();
		s.clear();
		tx = s.beginTransaction();
		assertEquals( 0, stats.getCollectionLoadCount() );
		assertEquals( 0, stats.getCollectionFetchCount() );
		europe2 = (Continent) s.get( Continent.class, europe.getId() );
		assertEquals( 1, stats.getCollectionLoadCount() );
		assertEquals( "Should do explicit collection load, not part of the first one", 1, stats.getCollectionFetchCount() );
		Iterator countries = europe2.getCountries().iterator();
		while ( countries.hasNext() ) {
			s.delete( countries.next() );
		}
		s.delete(europe2);
		tx.commit();
		s.close();
		sf.close();

		stats.setStatisticsEnabled(isStats);
	}

	private Continent fillDb(Session s) {
		Continent europe = new Continent();
		europe.setName("Europe");
		Country france = new Country();
		france.setName("France");
		europe.setCountries( new HashSet() );
		europe.getCountries().add(france);
		s.persist(france);
		s.persist(europe);
		return europe;
	}

	protected String[] getMappings() {
		return new String[] {
			"stats/Continent.hbm.xml"
		};
	}

	public StatsTest(String x) {
		super(x);
	}

	public static Test suite() {
		return new TestSuite(StatsTest.class);
	}
}

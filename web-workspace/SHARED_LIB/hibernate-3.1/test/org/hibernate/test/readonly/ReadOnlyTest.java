//$Id: ReadOnlyTest.java,v 1.2 2005/06/22 20:30:20 oneovthafew Exp $
package org.hibernate.test.readonly;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.CacheMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.test.TestCase;

/**
 * This is how to do batch processing in Hibernate.
 * Remember to enable JDBC batch updates, or this 
 * test will take a Very Long Time!
 * 
 * @author Gavin King
 */
public class ReadOnlyTest extends TestCase {
	
	public ReadOnlyTest(String str) {
		super(str);
	}
	
	public void testReadOnlyMode() {
		
		Session s = openSession();
		s.setCacheMode(CacheMode.IGNORE);
		Transaction t = s.beginTransaction();		
		for ( int i=0; i<100; i++ ) {
			DataPoint dp = new DataPoint();
			dp.setX( new BigDecimal(i * 0.1d).setScale(19, BigDecimal.ROUND_DOWN) );
			dp.setY( new BigDecimal( Math.cos( dp.getX().doubleValue() ) ).setScale(19, BigDecimal.ROUND_DOWN) );
			s.save(dp);
		}
		t.commit();
		s.close();
		
		s = openSession();
		s.setCacheMode(CacheMode.IGNORE);
		t = s.beginTransaction();
		int i = 0;
		ScrollableResults sr = s.createQuery("from DataPoint dp order by dp.x asc")
				.setReadOnly(true)
				.scroll(ScrollMode.FORWARD_ONLY);
		while ( sr.next() ) {
			DataPoint dp = (DataPoint) sr.get(0);
			if (++i==50) {
				s.setReadOnly(dp, false);
			}
			dp.setDescription("done!");
		}
		t.commit();
		s.clear();
		t = s.beginTransaction();
		List single = s.createQuery("from DataPoint where description='done!'").list();
		assertEquals( single.size(), 1 );
		s.createQuery("delete from DataPoint").executeUpdate();
		t.commit();
		s.close();
		
	}
	
	protected void configure(Configuration cfg) {
		cfg.setProperty(Environment.STATEMENT_BATCH_SIZE, "20");
	}

	protected String[] getMappings() {
		return new String[] { "readonly/DataPoint.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(ReadOnlyTest.class);
	}

	public String getCacheConcurrencyStrategy() {
		return null;
	}

}


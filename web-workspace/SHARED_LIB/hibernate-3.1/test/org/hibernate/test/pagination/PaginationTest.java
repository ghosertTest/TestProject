//$Id: PaginationTest.java,v 1.1 2005/08/11 23:35:33 oneovthafew Exp $
package org.hibernate.test.pagination;

import java.math.BigDecimal;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.criterion.Order;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class PaginationTest extends TestCase {
	
	public PaginationTest(String str) {
		super(str);
	}
	
	public void testPagination() {
		
		Session s = openSession();
		Transaction t = s.beginTransaction();		
		for ( int i=0; i<10; i++ ) {
			DataPoint dp = new DataPoint();
			dp.setX( new BigDecimal(i * 0.1d).setScale(19, BigDecimal.ROUND_DOWN) );
			dp.setY( new BigDecimal( Math.cos( dp.getX().doubleValue() ) ).setScale(19, BigDecimal.ROUND_DOWN) );
			s.persist(dp);
		}
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		int size = s.createSQLQuery("select id, xval, yval, description from DataPoint order by xval, yval")
			.addEntity(DataPoint.class)
			.setMaxResults(5)
			.list().size();
		assertEquals(size, 5);
		size = s.createQuery("from DataPoint order by x, y")
			.setFirstResult(5)
			.setMaxResults(2)
			.list().size();
		assertEquals(size, 2);
		size = s.createCriteria(DataPoint.class)
			.addOrder( Order.asc("x") )
			.addOrder( Order.asc("y") )
			.setFirstResult(8)
			.list().size();
		assertEquals(size, 2);
		t.commit();
		s.close();
		
	}
	
	protected void configure(Configuration cfg) {
		cfg.setProperty(Environment.STATEMENT_BATCH_SIZE, "20");
	}

	protected String[] getMappings() {
		return new String[] { "pagination/DataPoint.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(PaginationTest.class);
	}

	public String getCacheConcurrencyStrategy() {
		return null;
	}

}


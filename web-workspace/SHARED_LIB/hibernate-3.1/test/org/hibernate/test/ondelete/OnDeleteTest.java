//$Id: OnDeleteTest.java,v 1.2 2005/08/22 19:04:10 steveebersole Exp $
package org.hibernate.test.ondelete;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.MySQLInnoDBDialect;
import org.hibernate.stat.Statistics;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class OnDeleteTest extends TestCase {
	
	public OnDeleteTest(String str) {
		super(str);
	}
	
	public void testJoinedSubclass() {
		
		Statistics statistics = getSessions().getStatistics();
		statistics.clear();
		
		Session s = openSession();
		Transaction t = s.beginTransaction();
		
		Salesperson mark = new Salesperson();
		mark.setName("Mark");
		mark.setTitle("internal sales");
		mark.setSex('M');
		mark.setAddress("buckhead");
		mark.setZip("30305");
		mark.setCountry("USA");
		
		Person joe = new Person();
		joe.setName("Joe");
		joe.setAddress("San Francisco");
		joe.setZip("XXXXX");
		joe.setCountry("USA");
		joe.setSex('M');
		joe.setSalesperson(mark);
		mark.getCustomers().add(joe);
				
		s.save(mark);
		
		t.commit();
		
		assertEquals( statistics.getEntityInsertCount(), 2 );
		assertEquals( statistics.getPrepareStatementCount(), 5 );
		
		statistics.clear();
		
		t = s.beginTransaction();
		s.delete(mark);
		t.commit();

		assertEquals( statistics.getEntityDeleteCount(), 2 );
		if ( !(getDialect() instanceof MySQLDialect) || (getDialect() instanceof MySQLInnoDBDialect) ) {
			assertEquals( statistics.getPrepareStatementCount(), 1 );
		}
		
		t = s.beginTransaction();
		List names = s.createQuery("select name from Person").list();
		assertTrue( names.isEmpty() );
		t.commit();

		s.close();
	}

	protected void configure(Configuration cfg) {
		cfg.setProperty(Environment.GENERATE_STATISTICS, "true");
	}

	protected String[] getMappings() {
		return new String[] { "ondelete/Person.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(OnDeleteTest.class);
	}

}


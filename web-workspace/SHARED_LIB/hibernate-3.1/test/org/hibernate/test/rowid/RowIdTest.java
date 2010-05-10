//$Id: RowIdTest.java,v 1.3 2005/11/17 20:39:59 steveebersole Exp $
package org.hibernate.test.rowid;

import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.dialect.Oracle9Dialect;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class RowIdTest extends TestCase {
	
	public RowIdTest(String str) {
		super(str);
	}
	
	protected boolean recreateSchema() {
		return false;
	}
	
	public void setUp() throws Exception {
		super.setUp();
		if ( !( getDialect() instanceof Oracle9Dialect ) ) return;
		Session s = openSession();
		Statement st = s.connection().createStatement();
		try {
			st.execute( "drop table Point");
		}
		catch( Throwable t ) {
			// ignore
		}
		st.execute("create table Point (\"x\" number(19,2) not null, \"y\" number(19,2) not null, description varchar2(255) )");
		s.close();
	}

	public void tearDown() throws Exception {
		/*Session s = openSession();
		Statement st = s.connection().createStatement();
		st.execute("drop table Point");
		s.close();*/
	}

	public void testRowId() {
		if ( !( getDialect() instanceof Oracle9Dialect ) ) return;
		
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Point p = new Point( new BigDecimal(1.0), new BigDecimal(1.0) );
		s.persist(p);
		t.commit();
		s.clear();
		
		t = s.beginTransaction();
		p = (Point) s.createCriteria(Point.class).uniqueResult();
		p.setDescription("new desc");
		t.commit();
		s.clear();
		
		t = s.beginTransaction();
		p = (Point) s.createQuery("from Point").uniqueResult();
		p.setDescription("new new desc");
		t.commit();
		s.clear();
		
		t = s.beginTransaction();
		p = (Point) s.get(Point.class, p);
		p.setDescription("new new new desc");
		t.commit();
		s.close();
		
	}
	
	protected String[] getMappings() {
		return new String[] { "rowid/Point.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(RowIdTest.class);
	}

	public String getCacheConcurrencyStrategy() {
		return null;
	}

}


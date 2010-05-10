//$Id: FormulaJoinTest.java,v 1.3 2005/02/21 14:40:59 oneovthafew Exp $
package org.hibernate.test.formulajoin;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class FormulaJoinTest extends TestCase {
	
	public FormulaJoinTest(String str) {
		super(str);
	}
	
	public void testFormulaJoin() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Master master = new Master();
		master.setName("master 1");
		Detail current = new Detail();
		current.setCurrentVersion(true);
		current.setVersion(2);
		current.setDetails("details of master 1 blah blah");
		current.setMaster(master);
		master.setDetail(current);
		Detail past = new Detail();
		past.setCurrentVersion(false);
		past.setVersion(1);
		past.setDetails("old details of master 1 yada yada");
		past.setMaster(master);
		s.persist(master);
		s.persist(past);
		s.persist(current);
		tx.commit();
		s.close();
		
		if ( getDialect() instanceof PostgreSQLDialect ) return;

		s = openSession();
		tx = s.beginTransaction();
		List l = s.createQuery("from Master m left join m.detail d").list();
		assertEquals( l.size(), 1 );
		tx.commit();
		s.close();
		
		s = openSession();
		tx = s.beginTransaction();
		l = s.createQuery("from Master m left join fetch m.detail").list();
		assertEquals( l.size(), 1 );
		Master m = (Master) l.get(0);
		assertEquals( "master 1", m.getDetail().getMaster().getName() );
		assertTrue( m==m.getDetail().getMaster() );
		tx.commit();
		s.close();
		
		s = openSession();
		tx = s.beginTransaction();
		l = s.createQuery("from Master m join fetch m.detail").list();
		assertEquals( l.size(), 1 );
		tx.commit();
		s.close();
		
		s = openSession();
		tx = s.beginTransaction();
		l = s.createQuery("from Detail d join fetch d.currentMaster.master").list();
		assertEquals( l.size(), 2 );
		tx.commit();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		l = s.createQuery("from Detail d join fetch d.currentMaster.master m join fetch m.detail").list();
		assertEquals( l.size(), 2 );
		tx.commit();
		s.close();

	}

	
	protected String[] getMappings() {
		return new String[] { "formulajoin/Master.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(FormulaJoinTest.class);
	}

}


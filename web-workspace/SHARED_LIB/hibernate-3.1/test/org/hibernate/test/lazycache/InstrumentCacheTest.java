//$Id: InstrumentCacheTest.java,v 1.1 2005/08/05 23:03:46 oneovthafew Exp $
package org.hibernate.test.lazycache;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sf.cglib.transform.impl.InterceptFieldEnabled;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class InstrumentCacheTest extends TestCase {
	
	public InstrumentCacheTest(String str) {
		super(str);
	}
	
	protected boolean overrideCacheStrategy() {
		return false;
	}

	public void testInitFromCache() {
		Session s;
		Transaction tx;
		
		s = getSessions().openSession();
		tx = s.beginTransaction();
		s.persist( new Document("HiA", "Hibernate book", "Hibernate is....") );
		tx.commit();
		s.close();
		
		s = getSessions().openSession();
		tx = s.beginTransaction();
		s.createQuery("from Document fetch all properties").uniqueResult();
		tx.commit();
		s.close();
		
		getSessions().getStatistics().clear();
		
		s = getSessions().openSession();
		tx = s.beginTransaction();
		Document d = (Document) s.createCriteria(Document.class).uniqueResult();
		assertFalse( Hibernate.isPropertyInitialized(d, "text") );
		assertFalse( Hibernate.isPropertyInitialized(d, "summary") );
		assertEquals( "Hibernate is....", d.getText() );
		assertTrue( Hibernate.isPropertyInitialized(d, "text") );
		assertTrue( Hibernate.isPropertyInitialized(d, "summary") );
		tx.commit();
		s.close();
		
		assertEquals( 2, getSessions().getStatistics().getPrepareStatementCount() );

		s = getSessions().openSession();
		tx = s.beginTransaction();
		d = (Document) s.get(Document.class, d.getId());
		assertFalse( Hibernate.isPropertyInitialized(d, "text") );
		assertFalse( Hibernate.isPropertyInitialized(d, "summary") );
		tx.commit();
		s.close();
	}
	
	protected String[] getMappings() {
		return new String[] { "lazycache/Documents.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(InstrumentCacheTest.class);
	}
	
	public static boolean isRunnable() {
		return new Document() instanceof InterceptFieldEnabled;
	}

	protected void configure(Configuration cfg) {
		cfg.setProperty(Environment.GENERATE_STATISTICS, "true");
	}

}


//$Id: ComponentPropertyRefTest.java,v 1.1 2005/07/21 01:22:38 oneovthafew Exp $
package org.hibernate.test.comppropertyref;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class ComponentPropertyRefTest extends TestCase {
	
	public ComponentPropertyRefTest(String str) {
		super(str);
	}
	
	public void testComponentPropertyRef() {
		Person p = new Person();
		p.setIdentity( new Identity() );
		Account a = new Account();
		a.setNumber("123-12345-1236");
		a.setOwner(p);
		p.getIdentity().setName("Gavin");
		p.getIdentity().setSsn("123-12-1234");
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		s.persist(p);
		s.persist(a);
		s.flush();
		s.clear();
		
		a = (Account) s.createQuery("from Account a left join fetch a.owner").uniqueResult();
		assertTrue( Hibernate.isInitialized( a.getOwner() ) );
		assertNotNull( a.getOwner() );
		assertEquals( "Gavin", a.getOwner().getIdentity().getName() );
		s.clear();
		
		a = (Account) s.get(Account.class, "123-12345-1236");
		assertFalse( Hibernate.isInitialized( a.getOwner() ) );
		assertNotNull( a.getOwner() );
		assertEquals( "Gavin", a.getOwner().getIdentity().getName() );
		assertTrue( Hibernate.isInitialized( a.getOwner() ) );
		
		s.clear();
		
		getSessions().evict(Account.class);
		getSessions().evict(Person.class);
		
		a = (Account) s.get(Account.class, "123-12345-1236");
		assertTrue( Hibernate.isInitialized( a.getOwner() ) );
		assertNotNull( a.getOwner() );
		assertEquals( "Gavin", a.getOwner().getIdentity().getName() );
		assertTrue( Hibernate.isInitialized( a.getOwner() ) );
		
		tx.commit();
		s.close();
		
	}

	protected String[] getMappings() {
		return new String[] { "comppropertyref/PersonAccount.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(ComponentPropertyRefTest.class);
	}

	protected void configure(Configuration cfg) {
		//cfg.setProperty(Environment.USE_SECOND_LEVEL_CACHE, "false");
	}

}


//$Id: IdClassTest.java,v 1.3 2005/08/11 21:46:58 epbernard Exp $
package org.hibernate.test.idclass;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.TestCase;
import org.hibernate.type.Type;

/**
 * @author Gavin King
 */
public class IdClassTest extends TestCase {
	
	public IdClassTest(String str) {
		super(str);
	}

	public void testIdClass() {
		Type type = getSessions().getClassMetadata(Customer.class).getIdentifierType();
		Type[] types = getSessions().getClassMetadata(Customer.class).getPropertyTypes();
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Customer cust = new FavoriteCustomer("JBoss", "RouteOne", "Detroit");
		s.persist(cust);
		t.commit();
		s.close();
		
		s = openSession();
		CustomerId custId = new CustomerId("JBoss", "RouteOne");
		t = s.beginTransaction();
		cust = (Customer) s.get(Customer.class, custId);
		assertEquals( "Detroit", cust.getAddress() );
		assertEquals( cust.getCustomerName(), custId.getCustomerName() );
		assertEquals( cust.getOrgName(), custId.getOrgName() );
		t.commit();
		s.close();		

		s = openSession();
		t = s.beginTransaction();
		cust = (Customer) s.createQuery("from Customer where id.customerName = 'RouteOne'").uniqueResult();
		assertEquals( "Detroit", cust.getAddress() );
		assertEquals( cust.getCustomerName(), custId.getCustomerName() );
		assertEquals( cust.getOrgName(), custId.getOrgName() );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		cust = (Customer) s.createQuery("from Customer where customerName = 'RouteOne'").uniqueResult();
		assertEquals( "Detroit", cust.getAddress() );
		assertEquals( cust.getCustomerName(), custId.getCustomerName() );
		assertEquals( cust.getOrgName(), custId.getOrgName() );
		t.commit();
		s.close();
	}
	
	protected String[] getMappings() {
		return new String[] { "idclass/Customer.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(IdClassTest.class);
	}

}


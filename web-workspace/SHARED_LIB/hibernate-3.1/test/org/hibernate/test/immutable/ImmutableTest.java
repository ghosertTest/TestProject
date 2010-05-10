//$Id: ImmutableTest.java,v 1.1 2005/06/19 17:22:01 oneovthafew Exp $
package org.hibernate.test.immutable;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class ImmutableTest extends TestCase {
	
	public void testImmutable() {
		Contract c = new Contract("gavin", "phone");
		ContractVariation cv1 = new ContractVariation(1, c);
		cv1.setText("expensive");
		ContractVariation cv2 = new ContractVariation(2, c);
		cv2.setText("more expensive");
		Session s = openSession();
		Transaction t = s.beginTransaction();
		s.persist(c);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		c = (Contract) s.createCriteria(Contract.class).uniqueResult();
		c.setCustomerName("foo bar");
		c.getVariations().add( new ContractVariation(3, c) );
		cv1 = (ContractVariation) c.getVariations().iterator().next();
		cv1.setText("blah blah");
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		c = (Contract) s.createCriteria(Contract.class).uniqueResult();
		assertEquals( c.getCustomerName(), "gavin" );
		assertEquals( c.getVariations().size(), 2 );
		cv1 = (ContractVariation) c.getVariations().iterator().next();
		assertEquals( cv1.getText(), "expensive" );
		s.delete(c);
		assertEquals( s.createCriteria(Contract.class).setProjection( Projections.rowCount() ).uniqueResult(), new Integer(0) );
		assertEquals( s.createCriteria(ContractVariation.class).setProjection( Projections.rowCount() ).uniqueResult(), new Integer(0) );
		t.commit();
		s.close();
	}
	
	public ImmutableTest(String str) {
		super(str);
	}

	protected String[] getMappings() {
		return new String[] { "immutable/ContractVariation.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(ImmutableTest.class);
	}

}


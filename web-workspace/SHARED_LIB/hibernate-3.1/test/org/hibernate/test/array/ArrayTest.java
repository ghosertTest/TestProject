//$Id: ArrayTest.java,v 1.1 2005/04/26 16:58:52 oneovthafew Exp $
package org.hibernate.test.array;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.TestCase;

/**
 * @author Emmanuel Bernard
 */
public class ArrayTest extends TestCase {
	
	public void testArrayJoinFetch() throws Exception {
		Session s;
		Transaction tx;
		s = openSession();
		tx = s.beginTransaction();
		A a = new A();
		B b = new B();
		a.setBs( new B[] {b} );
		s.persist( a );
		tx.commit();
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		a = (A) s.get( A.class, a.getId() );
		assertNotNull( a );
		assertNotNull( a.getBs() );
		assertEquals( a.getBs().length, 1 );
		assertNotNull( a.getBs()[0] );
		tx.commit();
		s.close();
	}
	
	public ArrayTest(String x) {
		super( x );
	}

	public static Test suite() {
		return new TestSuite( ArrayTest.class );
	}

	protected String[] getMappings() {
		return new String[] {
			"array/A.hbm.xml"
		};
	}
}

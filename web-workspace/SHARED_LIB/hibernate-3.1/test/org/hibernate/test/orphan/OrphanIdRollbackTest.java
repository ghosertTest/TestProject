//$Id: OrphanIdRollbackTest.java,v 1.1 2005/11/21 18:14:52 epbernard Exp $
package org.hibernate.test.orphan;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.test.TestCase;

/**
 * @author Emmanuel Bernard
 */
public class OrphanIdRollbackTest extends TestCase {
	
	public OrphanIdRollbackTest(String str) {
		super(str);
	}

	protected void configure(Configuration cfg) {
		cfg.setProperty( Environment.USE_IDENTIFIER_ROLLBACK, "true");
		super.configure( cfg );
	}

	public void testOrphanDeleteOnDelete() {
		Session session = openSession();
		Transaction t = session.beginTransaction();
		Product prod = new Product();
		session.persist(prod);
		session.flush();
		t.commit();
		session.close();
		session = openSession();
		t = session.beginTransaction();
		prod = (Product) session.get( Product.class, prod.getName() );
		session.delete(prod);
		t.commit();
		session.close();
	}

	protected String[] getMappings() {
		return new String[] { "orphan/ProductAndIdRollback.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite( OrphanIdRollbackTest.class);
	}

}


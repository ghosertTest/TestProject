//$Id: OptimisticLockTest.java,v 1.2 2005/07/19 18:45:59 oneovthafew Exp $
package org.hibernate.test.optlock;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class OptimisticLockTest extends TestCase {
	
	public OptimisticLockTest(String str) {
		super(str);
	}
	
	public void testOptimisticLockDirty() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Document doc = new Document();
		doc.setTitle("Hibernate in Action");
		doc.setAuthor("Bauer et al");
		doc.setSummary("Very boring book about persistence");
		doc.setText("blah blah yada yada yada");
		doc.setPubDate( new PublicationDate(2004) );
		s.save("Dirty", doc);
		s.flush();
		doc.setSummary("A modern classic");
		s.flush();
		doc.getPubDate().setMonth( new Integer(3) );
		s.flush();
		s.delete(doc);
		t.commit();
		s.close();
	}

	public void testOptimisticLockAll() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Document doc = new Document();
		doc.setTitle("Hibernate in Action");
		doc.setAuthor("Bauer et al");
		doc.setSummary("Very boring book about persistence");
		doc.setText("blah blah yada yada yada");
		doc.setPubDate( new PublicationDate(2004) );
		s.save("All", doc);
		s.flush();
		doc.setSummary("A modern classic");
		s.flush();
		doc.getPubDate().setMonth( new Integer(3) );
		s.flush();
		s.delete(doc);
		t.commit();
		s.close();
	}

	
	protected String[] getMappings() {
		return new String[] { "optlock/Document.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(OptimisticLockTest.class);
	}

}


//$Id: OneToOneCacheTest.java,v 1.2 2004/09/26 05:27:23 oneovthafew Exp $
package org.hibernate.test.legacy;

import java.io.Serializable;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.test.TestCase;

/**
 * Simple testcase to illustrate HB-992
 *
 * @author Wolfgang Voelkl, michael
 */
public class OneToOneCacheTest extends TestCase {

	public OneToOneCacheTest(String x) {
		super(x);
	}

	public static Test suite() {
		return new TestSuite(OneToOneCacheTest.class);
	}

	private Serializable generatedId;

	public void testOneToOneCache() throws HibernateException {

			//create a new MainObject
			createMainObject();
			// load the MainObject
			readMainObject();

			//create and add Ojbect2
			addObject2();

			//here the newly created Object2 is written to the database
			//but the MainObject does not know it yet
			MainObject mainObject = readMainObject();

			TestCase.assertNotNull(mainObject.getObj2());

			// after evicting, it works.
			getSessions().evict(MainObject.class);

			mainObject = readMainObject();

			TestCase.assertNotNull(mainObject.getObj2());

	}

	/**
	 * creates a new MainObject
	 *
	 * one hibernate transaction !
	 */
	private void createMainObject() throws HibernateException {
		Session session = openSession();
		Transaction tx = session.beginTransaction();

		MainObject mo = new MainObject();
		mo.setDescription("Main Test");

		generatedId = session.save(mo);

		tx.commit();
		session.close();
	}

	/**
	 * loads the newly created MainObject
	 * and adds a new Object2 to it
	 *
	 * one hibernate transaction
	 */
	private void addObject2() throws HibernateException {
		Session session = openSession();
		Transaction tx = session.beginTransaction();

		MainObject mo =
			(MainObject) session.load(MainObject.class, generatedId);

		Object2 toAdd = new Object2();
		toAdd.setDummy("test");

		//toAdd should now be saved by cascade
		mo.setObj2(toAdd);

		tx.commit();
		session.close();
	}

	/**
	 * reads the newly created MainObject
	 * and its Object2 if it exists
	 *
	 * one hibernate transaction
	 */
	private MainObject readMainObject() throws HibernateException {
		Long returnId = null;
		Session session = openSession();
		Transaction tx = session.beginTransaction();

		Serializable id = generatedId;

		MainObject mo = (MainObject) session.load(MainObject.class, id);

		tx.commit();
		session.close();

		return mo;
	}


	/* (non-Javadoc)
	 * @see org.hibernate.test.TestCase#getMappings()
	 */
	protected String[] getMappings() {
		return new String[] { "legacy/Object2.hbm.xml", "legacy/MainObject.hbm.xml" };
	}
}

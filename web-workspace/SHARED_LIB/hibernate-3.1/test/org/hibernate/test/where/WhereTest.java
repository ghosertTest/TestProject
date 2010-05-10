//$Id: WhereTest.java,v 1.2 2005/08/30 15:22:17 oneovthafew Exp $
package org.hibernate.test.where;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.test.TestCase;

/**
 * @author Max Rydahl Andersen
 */
public class WhereTest extends TestCase {
	
	public void testWhere() {
		Session s = openSession();
		s.getTransaction().begin();
		File parent = new File("parent", null);
		s.persist( parent );
		s.persist( new File("child", parent) );
		File deletedChild = new File("deleted child", parent);
		deletedChild.setDeleted(true);
		s.persist( deletedChild );
		File deletedParent = new File("deleted parent", null);
		deletedParent.setDeleted(true);
		s.persist( deletedParent );
		s.flush();
		s.clear();
		parent = (File) s.createCriteria(File.class)
				.setFetchMode("children", FetchMode.JOIN)
				.add( Restrictions.isNull("parent") )
				.uniqueResult();
		assertEquals( parent.getChildren().size(), 1 );
		s.clear();
		parent = (File) s.createQuery("from File f left join fetch f.children where f.parent is null")
			.uniqueResult();
		assertEquals( parent.getChildren().size(), 1 );
		s.getTransaction().commit();
		s.close();
	}
	
	public WhereTest(String str) {
		super(str);
	}
		
	protected String[] getMappings() {
		return new String[] { "where/File.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(WhereTest.class);
	}

}


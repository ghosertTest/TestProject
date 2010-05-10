//$Id: MergeTest.java,v 1.14 2005/11/25 17:36:29 epbernard Exp $
package org.hibernate.test.ops;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.criterion.Projections;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class MergeTest extends TestCase {
	
	public MergeTest(String str) {
		super(str);
	}
	
	public void testMergeDeepTree() {
		
		clearCounts();
		
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Node root = new Node("root");
		Node child = new Node("child");
		Node grandchild = new Node("grandchild");
		root.addChild(child);
		child.addChild(grandchild);
		s.merge(root);
		tx.commit();
		s.close();
		
		assertInsertCount(3);
		assertUpdateCount(0);
		clearCounts();
		
		grandchild.setDescription("the grand child");
		Node grandchild2 = new Node("grandchild2");
		child.addChild( grandchild2 );

		s = openSession();
		tx = s.beginTransaction();
		s.merge(root);
		tx.commit();
		s.close();
		
		assertInsertCount(1);
		assertUpdateCount(1);
		clearCounts();
		
		Node child2 = new Node("child2");
		Node grandchild3 = new Node("grandchild3");
		child2.addChild( grandchild3 );
		root.addChild(child2);
		
		s = openSession();
		tx = s.beginTransaction();
		s.merge(root);
		tx.commit();
		s.close();
		
		assertInsertCount(2);
		assertUpdateCount(0);
		clearCounts();
		
		s = openSession();
		tx = s.beginTransaction();
		s.delete(grandchild);
		s.delete(grandchild2);
		s.delete(grandchild3);
		s.delete(child);
		s.delete(child2);
		s.delete(root);
		tx.commit();
		s.close();
	
	}
	
	public void testMergeDeepTreeWithGeneratedId() {
		
		clearCounts();
		
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		NumberedNode root = new NumberedNode("root");
		NumberedNode child = new NumberedNode("child");
		NumberedNode grandchild = new NumberedNode("grandchild");
		root.addChild(child);
		child.addChild(grandchild);
		root = (NumberedNode) s.merge(root);
		tx.commit();
		s.close();
		
		assertInsertCount(3);
		assertUpdateCount(0);
		clearCounts();
		
		child = (NumberedNode) root.getChildren().iterator().next();
		grandchild = (NumberedNode) child.getChildren().iterator().next();
		grandchild.setDescription("the grand child");
		NumberedNode grandchild2 = new NumberedNode("grandchild2");
		child.addChild( grandchild2 );

		s = openSession();
		tx = s.beginTransaction();
		root = (NumberedNode) s.merge(root);
		tx.commit();
		s.close();
		
		assertInsertCount(1);
		assertUpdateCount(1);
		clearCounts();
		
		getSessions().evict(NumberedNode.class);
		
		NumberedNode child2 = new NumberedNode("child2");
		NumberedNode grandchild3 = new NumberedNode("grandchild3");
		child2.addChild( grandchild3 );
		root.addChild(child2);
		
		s = openSession();
		tx = s.beginTransaction();
		root = (NumberedNode) s.merge(root);
		tx.commit();
		s.close();
		
		assertInsertCount(2);
		assertUpdateCount(0);
		clearCounts();
		
		s = openSession();
		tx = s.beginTransaction();
		s.createQuery("delete from NumberedNode where name like 'grand%'").executeUpdate();
		s.createQuery("delete from NumberedNode where name like 'child%'").executeUpdate();
		s.createQuery("delete from NumberedNode").executeUpdate();
		tx.commit();
		s.close();
	
	}
	
	public void testMergeTree() {
		
		clearCounts();
		
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Node root = new Node("root");
		Node child = new Node("child");
		root.addChild(child);
		s.persist(root);
		tx.commit();
		s.close();
		
		assertInsertCount(2);
		clearCounts();
		
		root.setDescription("The root node");
		child.setDescription("The child node");
		
		Node secondChild = new Node("second child");
		
		root.addChild(secondChild);
		
		s = openSession();
		tx = s.beginTransaction();
		s.merge(root);
		tx.commit();
		s.close();
		
		assertInsertCount(1);
		assertUpdateCount(2);
		
		s = openSession();
		tx = s.beginTransaction();
		s.createQuery("delete from Node where parent is not null").executeUpdate();
		s.createQuery("delete from Node").executeUpdate();
		tx.commit();
		s.close();
		
	}
		
	public void testMergeTreeWithGeneratedId() {
		
		clearCounts();
		
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		NumberedNode root = new NumberedNode("root");
		NumberedNode child = new NumberedNode("child");
		root.addChild(child);
		s.persist(root);
		tx.commit();
		s.close();
		
		assertInsertCount(2);
		clearCounts();
		
		root.setDescription("The root node");
		child.setDescription("The child node");
		
		NumberedNode secondChild = new NumberedNode("second child");
		
		root.addChild(secondChild);
		
		s = openSession();
		tx = s.beginTransaction();
		s.merge(root);
		tx.commit();
		s.close();
		
		assertInsertCount(1);
		assertUpdateCount(2);
		
		s = openSession();
		tx = s.beginTransaction();
		s.createQuery("delete from NumberedNode where parent is not null").executeUpdate();
		s.createQuery("delete from NumberedNode").executeUpdate();
		tx.commit();
		s.close();

	}
		
	public void testMergeManaged() {
		
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		NumberedNode root = new NumberedNode("root");
		s.persist(root);
		tx.commit();
		
		clearCounts();
		
		tx = s.beginTransaction();
		NumberedNode child = new NumberedNode("child");
		root.addChild(child);
		assertSame( root, s.merge(root) );
		Object mergedChild = root.getChildren().iterator().next();
		assertNotSame( mergedChild, child );
		assertTrue( s.contains(mergedChild) );
		assertFalse( s.contains(child) );
		assertEquals( root.getChildren().size(), 1 );
		assertTrue( root.getChildren().contains(mergedChild) );
		//assertNotSame( mergedChild, s.merge(child) ); //yucky :(
		tx.commit();
		
		assertInsertCount(1);
		assertUpdateCount(0);
		
		assertEquals( root.getChildren().size(), 1 );
		assertTrue( root.getChildren().contains(mergedChild) );
		
		tx = s.beginTransaction();
		assertEquals( 
			s.createCriteria(NumberedNode.class)
				.setProjection( Projections.rowCount() )
				.uniqueResult(), 
			new Integer(2) 
		);
		s.delete(root);
		s.delete(mergedChild);
		tx.commit();
		s.close();
		
	}
	
	public void testRecursiveMergeTransient() {
		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Employer jboss = new Employer();
		Employee gavin = new Employee();
		jboss.setEmployees( new ArrayList() );
		jboss.getEmployees().add(gavin);
		s.merge(jboss);
		s.flush();
		jboss = (Employer) s.createQuery("from Employer e join fetch e.employees").uniqueResult();
		assertTrue( Hibernate.isInitialized( jboss.getEmployees() )  );
		assertEquals( 1, jboss.getEmployees().size() );
		s.clear();
		s.merge( jboss.getEmployees().iterator().next() );
		tx.commit();
		s.close();		
	}

	public void testDeleteAndMerge() throws Exception {
		Session s = openSession();
		s.getTransaction().begin();
		Employer jboss = new Employer();
		s.persist( jboss );
		s.getTransaction().commit();
		s.clear();

		s.getTransaction().begin();
		Employer otherJboss;
		otherJboss = (Employer) s.get( Employer.class, jboss.getId() );
		s.delete( otherJboss );
		s.getTransaction().commit();
		s.clear();
		jboss.setVers( new Integer(1) );
		s.getTransaction().begin();
		s.merge( jboss );
		s.getTransaction().commit();
		s.close();
	}
	
	private void clearCounts() {
		getSessions().getStatistics().clear();
	}
	
	private void assertInsertCount(int count) {
		int inserts = (int) getSessions().getStatistics().getEntityInsertCount();
		assertEquals(count, inserts);
	}
		
	private void assertUpdateCount(int count) {
		int updates = (int) getSessions().getStatistics().getEntityUpdateCount();
		assertEquals(count, updates);
	}
		
	protected void configure(Configuration cfg) {
		cfg.setProperty(Environment.GENERATE_STATISTICS, "true");
		cfg.setProperty(Environment.STATEMENT_BATCH_SIZE, "0");		
	}
	
	protected String[] getMappings() {
		return new String[] { "ops/Node.hbm.xml", "ops/Employer.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(MergeTest.class);
	}

}


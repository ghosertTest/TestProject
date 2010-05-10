//$Id: StatelessSessionTest.java,v 1.6 2005/11/30 13:56:19 steveebersole Exp $
package org.hibernate.test.stateless;

import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class StatelessSessionTest extends TestCase {
	
	public StatelessSessionTest(String str) {
		super(str);
	}

	public void testCreateUpdateReadDelete() {
		StatelessSession ss = getSessions().openStatelessSession();
		Transaction tx = ss.beginTransaction();
		Document doc = new Document("blah blah blah", "Blahs");
		ss.insert(doc);
		assertNotNull( doc.getName() );
		Date initVersion = doc.getLastModified();
		assertNotNull( initVersion );
		tx.commit();
		
		tx = ss.beginTransaction();
		doc.setText("blah blah blah .... blah");
		ss.update(doc);
		assertNotNull( doc.getLastModified() );
		assertNotSame( doc.getLastModified(), initVersion );
		tx.commit();
		
		tx = ss.beginTransaction();
		doc.setText("blah blah blah .... blah blay");
		ss.update(doc);
		tx.commit();
		
		Document doc2 = (Document) ss.get(Document.class.getName(), "Blahs");
		assertEquals("Blahs", doc2.getName());
		assertEquals(doc.getText(), doc2.getText());
				
		doc2 = (Document) ss.createQuery("from Document where text is not null").uniqueResult();
		assertEquals("Blahs", doc2.getName());
		assertEquals(doc.getText(), doc2.getText());
		
		ScrollableResults sr = ss.createQuery("from Document where text is not null")
			.scroll(ScrollMode.FORWARD_ONLY);
		sr.next();
		doc2 = (Document) sr.get(0);
		sr.close();
		assertEquals("Blahs", doc2.getName());
		assertEquals(doc.getText(), doc2.getText());
				
		doc2 = (Document) ss.createSQLQuery("select * from Document")
			.addEntity(Document.class)
			.uniqueResult();
		assertEquals("Blahs", doc2.getName());
		assertEquals(doc.getText(), doc2.getText());
				
		doc2 = (Document) ss.createCriteria(Document.class).uniqueResult();
		assertEquals("Blahs", doc2.getName());
		assertEquals(doc.getText(), doc2.getText());
		
		sr = ss.createCriteria(Document.class).scroll(ScrollMode.FORWARD_ONLY);
		sr.next();
		doc2 = (Document) sr.get(0);
		sr.close();
		assertEquals("Blahs", doc2.getName());
		assertEquals(doc.getText(), doc2.getText());

		tx = ss.beginTransaction();
		ss.delete(doc);
		tx.commit();
		ss.close();

	}

	public void testHqlBulk() {
		StatelessSession ss = getSessions().openStatelessSession();
		Transaction tx = ss.beginTransaction();
		Document doc = new Document("blah blah blah", "Blahs");
		ss.insert(doc);
		Paper paper = new Paper();
		paper.setColor( "White" );
		ss.insert(paper);
		tx.commit();

		tx = ss.beginTransaction();
		int count = ss.createQuery( "update Document set name = :newName where name = :oldName" )
				.setString( "newName", "Foos" )
				.setString( "oldName", "Blahs" )
				.executeUpdate();
		assertEquals( "hql-update on stateless session", 1, count );
		count = ss.createQuery( "update Paper set color = :newColor" )
				.setString( "newColor", "Goldenrod" )
				.executeUpdate();
		assertEquals( "hql-update on stateless session", 1, count );
		tx.commit();

		tx = ss.beginTransaction();
		count = ss.createQuery( "delete Document" ).executeUpdate();
		assertEquals( "hql-delete on stateless session", 1, count );
		count = ss.createQuery( "delete Paper" ).executeUpdate();
		assertEquals( "hql-delete on stateless session", 1, count );
		tx.commit();
		ss.close();
	}

	public void testInitId() {
		StatelessSession ss = getSessions().openStatelessSession();
		Transaction tx = ss.beginTransaction();
		Paper paper = new Paper();
		paper.setColor( "White" );
		ss.insert(paper);
		assertNotNull( paper.getId() );
		tx.commit();

		tx = ss.beginTransaction();
		ss.delete( ss.get( Paper.class, paper.getId() ) );
		tx.commit();
		ss.close();
	}
	
	
	protected String[] getMappings() {
		return new String[] { "stateless/Document.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(StatelessSessionTest.class);
	}

}


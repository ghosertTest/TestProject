//$Id: InstrumentTest.java,v 1.17 2005/07/10 16:51:17 oneovthafew Exp $
package org.hibernate.test.instrument;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import net.sf.cglib.transform.impl.InterceptFieldEnabled;

import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.intercept.FieldInterceptor;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class InstrumentTest extends TestCase {
	
	public InstrumentTest(String str) {
		super(str);
	}
	
	public void testDirtyCheck() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Folder pics = new Folder();
		pics.setName("pics");
		Folder docs = new Folder();
		docs.setName("docs");
		s.persist(docs);
		s.persist(pics);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		List list = s.createCriteria(Folder.class).list();
		for ( Iterator iter = list.iterator(); iter.hasNext(); ) {
			Folder f = (Folder) iter.next();
			assertFalse( f.nameWasread );	
		}
		t.commit();
		s.close();
		
		for ( Iterator iter = list.iterator(); iter.hasNext(); ) {
			Folder f = (Folder) iter.next();
			assertFalse( f.nameWasread );	
		}

		s = openSession();
		t = s.beginTransaction();
		s.createQuery("delete from Folder").executeUpdate();
		t.commit();
		s.close();
	}

	public void testFetchAll() throws Exception {
		Session s = openSession();
		Owner o = new Owner();
		Document doc = new Document();
		Folder fol = new Folder();
		o.setName("gavin");
		doc.setName("Hibernate in Action");
		doc.setSummary("blah");
		doc.updateText("blah blah");
		fol.setName("books");
		doc.setOwner(o);
		doc.setFolder(fol);
		fol.getDocuments().add(doc);
		s.persist(o);
		s.persist(fol);
		s.flush();
		s.clear();
		doc = (Document) s.createQuery("from Document fetch all properties").uniqueResult();
		assertTrue( Hibernate.isPropertyInitialized( doc, "summary" ) );
		assertTrue( Hibernate.isPropertyInitialized( doc, "upperCaseName" ) );
		assertTrue( Hibernate.isPropertyInitialized( doc, "owner" ) );
		assertEquals( doc.getSummary(), "blah" );
		s.delete(doc);
		s.delete( doc.getOwner() );
		s.delete( doc.getFolder() );
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	public void testLazy() throws Exception {
		Session s = openSession();
		Owner o = new Owner();
		Document doc = new Document();
		Folder fol = new Folder();
		o.setName("gavin");
		doc.setName("Hibernate in Action");
		doc.setSummary("blah");
		doc.updateText("blah blah");
		fol.setName("books");
		doc.setOwner(o);
		doc.setFolder(fol);
		fol.getDocuments().add(doc);
		s.save(o);
		s.save(fol);
		s.flush();
		s.connection().commit();
		s.close();
		
		getSessions().evict(Document.class);
		
		s = openSession();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		doc.getName();
		assertEquals( doc.getText(), "blah blah" );
		s.connection().commit();
		s.close();

		s = openSession();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		doc.getName();
		assertFalse(Hibernate.isPropertyInitialized(doc, "text"));
		assertFalse(Hibernate.isPropertyInitialized(doc, "summary"));
		assertEquals( doc.getText(), "blah blah" );
		assertTrue(Hibernate.isPropertyInitialized(doc, "text"));
		assertTrue(Hibernate.isPropertyInitialized(doc, "summary"));
		s.connection().commit();
		s.close();

		s = openSession();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		doc.setName("HiA");
		s.flush();
		s.connection().commit();
		s.close();

		s = openSession();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		assertEquals( doc.getName(), "HiA" );
		assertEquals( doc.getText(), "blah blah" );
		s.connection().commit();
		s.close();

		s = openSession();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		doc.getText();
		doc.setName("HiA second edition");
		s.flush();
		s.connection().commit();
		s.close();

		s = openSession();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		assertTrue(Hibernate.isPropertyInitialized(doc, "weirdProperty"));
		assertTrue(Hibernate.isPropertyInitialized(doc, "name"));
		assertFalse(Hibernate.isPropertyInitialized(doc, "text"));
		assertFalse(Hibernate.isPropertyInitialized(doc, "upperCaseName"));
		//assertFalse(Hibernate.isPropertyInitialized(doc, "owner"));
		assertEquals( doc.getName(), "HiA second edition" );
		assertEquals( doc.getText(), "blah blah" );
		assertEquals( doc.getUpperCaseName(), "HIA SECOND EDITION" );
		assertTrue(Hibernate.isPropertyInitialized(doc, "text"));
		assertTrue(Hibernate.isPropertyInitialized(doc, "weirdProperty"));
		assertTrue(Hibernate.isPropertyInitialized(doc, "upperCaseName"));
		s.connection().commit();
		s.close();

		s = openSession();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		s.connection().commit();
		s.close();
		
		assertFalse(Hibernate.isPropertyInitialized(doc, "text"));

		s = openSession();
		s.lock(doc, LockMode.NONE);
		assertFalse(Hibernate.isPropertyInitialized(doc, "text"));
		assertEquals( doc.getText(), "blah blah" );
		assertTrue(Hibernate.isPropertyInitialized(doc, "text"));
		s.connection().commit();
		s.close();

		s = openSession();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		s.connection().commit();
		s.close();
		
		doc.setName("HiA2");
		
		assertFalse(Hibernate.isPropertyInitialized(doc, "text"));

		s = openSession();
		s.saveOrUpdate(doc);
		s.flush();
		assertFalse(Hibernate.isPropertyInitialized(doc, "text"));
		assertEquals( doc.getText(), "blah blah" );
		assertTrue(Hibernate.isPropertyInitialized(doc, "text"));
		doc.updateText("blah blah blah blah");
		s.flush();
		s.connection().commit();
		s.close();

		s = openSession();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		assertEquals( doc.getName(), "HiA2" );
		assertEquals( doc.getText(), "blah blah blah blah" );
		s.connection().commit();
		s.close();

		s = openSession();
		doc = (Document) s.load( Document.class, doc.getId() );
		doc.getName();
		assertFalse(Hibernate.isPropertyInitialized(doc, "text"));
		assertFalse(Hibernate.isPropertyInitialized(doc, "summary"));
		s.connection().commit();
		s.close();

		s = openSession();
		doc = (Document) s.createQuery("from Document").uniqueResult();
		//s.delete(doc);
		s.delete( doc.getFolder() );
		s.delete( doc.getOwner() );
		s.flush();
		s.connection().commit();
		s.close();

	}
	
	public void testLazyManyToOne() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Owner gavin = new Owner();
		Document hia = new Document();
		Folder fol = new Folder();
		gavin.setName("gavin");
		hia.setName("Hibernate in Action");
		hia.setSummary("blah");
		hia.updateText("blah blah");
		fol.setName("books");
		hia.setOwner(gavin);
		hia.setFolder(fol);
		fol.getDocuments().add(hia);
		s.persist(gavin);
		s.persist(fol);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		hia = (Document) s.createCriteria(Document.class).uniqueResult();
		assertEquals( hia.getFolder().getClass(), Folder.class);
		fol = hia.getFolder();
		assertTrue( Hibernate.isInitialized(fol) );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		hia = (Document) s.createCriteria(Document.class).uniqueResult();
		assertSame( hia.getFolder(), s.load(Folder.class, fol.getId()) );
		assertTrue( Hibernate.isInitialized( hia.getFolder() ) );
		t.commit();
		s.close();
	
		s = openSession();
		t = s.beginTransaction();
		fol = (Folder) s.get(Folder.class, fol.getId());
		hia = (Document) s.createCriteria(Document.class).uniqueResult();
		assertSame( fol, hia.getFolder() );
		fol = hia.getFolder();
		assertTrue( Hibernate.isInitialized(fol) );
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		fol = (Folder) s.load(Folder.class, fol.getId());
		hia = (Document) s.createCriteria(Document.class).uniqueResult();
		assertNotSame( fol, hia.getFolder() );
		fol = hia.getFolder();
		assertTrue( Hibernate.isInitialized(fol) );
		s.delete(hia.getFolder());
		s.delete(hia.getOwner());
		t.commit();
		s.close();
	}
	
	protected String[] getMappings() {
		return new String[] { "instrument/Documents.hbm.xml" };
	}
	
	public void testSetFieldInterceptor() {
		Document doc = new Document();
		FieldInterceptor.initFieldInterceptor(doc, "Document", null, new HashSet());
		doc.getId();
	}

	public static Test suite() {
		return new TestSuite(InstrumentTest.class);
	}
	
	public static boolean isRunnable() {
		return new Document() instanceof InterceptFieldEnabled;
	}

}


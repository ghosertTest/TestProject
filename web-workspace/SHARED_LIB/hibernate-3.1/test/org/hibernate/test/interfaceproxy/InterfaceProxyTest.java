//$Id: InterfaceProxyTest.java,v 1.2 2004/09/29 14:47:55 oneovthafew Exp $
package org.hibernate.test.interfaceproxy;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class InterfaceProxyTest extends TestCase {
	
	public InterfaceProxyTest(String str) {
		super(str);
	}
	
	public void testInterfaceProxies() {
		
		if ( getDialect() instanceof PostgreSQLDialect ) return;
		
		Session s = openSession( new DocumentInterceptor() );
		Transaction t = s.beginTransaction();
		Document d = new DocumentImpl();
		d.setName("Hibernate in Action");
		d.setContent( Hibernate.createBlob( "blah blah blah".getBytes() ) );
		Long did = (Long) s.save(d);
		SecureDocument d2 = new SecureDocumentImpl();
		d2.setName("Secret");
		d2.setContent( Hibernate.createBlob( "wxyz wxyz".getBytes() ) );
		d2.setPermissionBits( (byte) 664 );
		d2.setOwner("gavin");
		Long d2id = (Long) s.save(d2);
		t.commit();
		s.close();

		s = openSession( new DocumentInterceptor() );
		t = s.beginTransaction();
		d = (Document) s.load(ItemImpl.class, did);
		assertEquals( did, d.getId() );
		assertEquals( "Hibernate in Action", d.getName() );
		assertNotNull( d.getContent() );
		
		d2 = (SecureDocument) s.load(ItemImpl.class, d2id);
		assertEquals( d2id, d2.getId() );
		assertEquals( "Secret", d2.getName() );
		assertNotNull( d2.getContent() );
		
		s.clear();
		
		d = (Document) s.load(DocumentImpl.class, did);
		assertEquals( did, d.getId() );
		assertEquals( "Hibernate in Action", d.getName() );
		assertNotNull( d.getContent() );
		
		d2 = (SecureDocument) s.load(SecureDocumentImpl.class, d2id);
		assertEquals( d2id, d2.getId() );
		assertEquals( "Secret", d2.getName() );
		assertNotNull( d2.getContent() );
		assertEquals( "gavin", d2.getOwner() );
		
		//s.clear();
		
		d2 = (SecureDocument) s.load(SecureDocumentImpl.class, did);
		assertEquals( did, d2.getId() );
		assertEquals( "Hibernate in Action", d2.getName() );
		assertNotNull( d2.getContent() );
		
		try {
			d2.getOwner(); //CCE
			assertFalse(true);
		}
		catch (ClassCastException cce) {
			//correct
		}
		
		
		t.commit();
		s.close();
	}

	
	protected String[] getMappings() {
		return new String[] { "interfaceproxy/Item.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(InterfaceProxyTest.class);
	}

	public String getCacheConcurrencyStrategy() {
		return null;
	}
}


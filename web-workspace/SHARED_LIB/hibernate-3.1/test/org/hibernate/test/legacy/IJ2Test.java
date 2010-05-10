//$Id: IJ2Test.java,v 1.4 2005/11/25 14:35:52 turin42 Exp $
package org.hibernate.test.legacy;

import java.io.Serializable;



import junit.framework.Test;

import junit.framework.TestSuite;



import org.hibernate.LockMode;

import org.hibernate.classic.Session;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class IJ2Test extends TestCase {

	public void testUnionSubclass() throws Exception {

		//if ( getDialect() instanceof MySQLDialect ) return;

		Session s = getSessions().openSession();
		I i = new I();
		i.setName( "i" );
		i.setType( 'a' );
		J j = new J();
		j.setName( "j" );
		j.setType( 'x' );
		j.setAmount( 1.0f );
		Serializable iid = s.save(i);
		Serializable jid = s.save(j);
		s.flush();
		s.connection().commit();
		s.close();

		getSessions().evict(I.class);

		s = getSessions().openSession();
		j = (J) s.get(I.class, jid);
		j = (J) s.get(J.class, jid);
		i = (I) s.get(I.class, iid);
		assertTrue( i.getClass()==I.class );
		j.setAmount( 0.5f );
		s.lock(i, LockMode.UPGRADE);
		s.flush();
		s.connection().commit();
		s.close();

		getSessions().evict(I.class);

		s = getSessions().openSession();
		j = (J) s.get(J.class, jid);
		j = (J) s.get(I.class, jid);
		i = (I) s.get(I.class, iid);
		assertTrue( i.getClass()==I.class );
		j.setAmount( 0.5f );
		s.lock(i, LockMode.UPGRADE);
		s.flush();
		s.connection().commit();
		s.close();

		getSessions().evict(I.class);

		s = getSessions().openSession();
		assertTrue( s.find("from I").size()==2 );
		assertTrue( s.find("from J").size()==1 );
		assertTrue( s.find("from J j where j.amount > 0 and j.name is not null").size()==1 );
		assertTrue( s.find("from I i where i.class = org.hibernate.test.legacy.I").size()==1 );
		assertTrue( s.find("from I i where i.class = J").size()==1 );
		s.connection().commit();
		s.close();

		getSessions().evict(I.class);

		s = getSessions().openSession();
		j = (J) s.get(J.class, jid);
		i = (I) s.get(I.class, iid);
		K k = new K();
		Serializable kid = s.save(k);
		i.setParent(k);
		j.setParent(k);
		s.flush();
		s.connection().commit();
		s.close();

		getSessions().evict(I.class);

		s = getSessions().openSession();
		j = (J) s.get(J.class, jid);
		i = (I) s.get(I.class, iid);
		k = (K) s.get(K.class, kid);
		System.out.println(k + "=" + i.getParent());
		assertTrue( i.getParent()==k );
		assertTrue( j.getParent()==k );
		assertTrue( k.getIs().size()==2 );
		s.flush();
		s.connection().commit();
		s.close();

		getSessions().evict(I.class);

		s = getSessions().openSession();
		assertTrue( s.find("from K k inner join k.is i where i.name = 'j'").size()==1 );
		assertTrue( s.find("from K k inner join k.is i where i.name = 'i'").size()==1 );
		assertTrue( s.find("from K k left join fetch k.is").size()==2 );
		s.connection().commit();
		s.close();

		s = getSessions().openSession();
		j = (J) s.get(J.class, jid);
		i = (I) s.get(I.class, iid);
		k = (K) s.get(K.class, kid);
		s.delete(k);
		s.delete(j);
		s.delete(i);
		s.flush();
		s.connection().commit();
		s.close();

	}

	protected String[] getMappings() {
		return new String[] { "legacy/IJ2.hbm.xml" };
	}

	public IJ2Test(String x) {
		super(x);
	}

	public static Test suite() {
		return new TestSuite(IJ2Test.class);
	}
}

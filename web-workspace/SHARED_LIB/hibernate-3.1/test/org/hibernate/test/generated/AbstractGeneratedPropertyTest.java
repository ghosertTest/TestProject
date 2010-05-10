// $Id: AbstractGeneratedPropertyTest.java,v 1.2 2005/08/10 17:20:24 steveebersole Exp $
package org.hibernate.test.generated;

import org.hibernate.test.TestCase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Hibernate;

/**
 * Implementation of AbstractGeneratedPropertyTest.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractGeneratedPropertyTest extends TestCase {
	public AbstractGeneratedPropertyTest(String x) {
		super( x );
	}

	protected final String[] getMappings() {
		return new String[] { "generated/GeneratedPropertyEntity.hbm.xml" };
	}

	protected abstract boolean acceptsCurrentDialect();

	protected void runTest() throws Throwable {
		if ( acceptsCurrentDialect() ) {
			super.runTest();
		}
	}

	public final void testGeneratedProperty() {
		GeneratedPropertyEntity entity = new GeneratedPropertyEntity();
		entity.setName( "entity-1" );
		Session s = openSession();
		Transaction t = s.beginTransaction();
		s.save( entity );
		s.flush();
		assertNotNull( "no timestamp retrieved", entity.getLastModified() );
		t.commit();
		s.close();

		byte[] bytes = entity.getLastModified();

		s = openSession();
		t = s.beginTransaction();
		entity = ( GeneratedPropertyEntity ) s.get( GeneratedPropertyEntity.class, entity.getId() );
		assertTrue( Hibernate.BINARY.isEqual( bytes, entity.getLastModified() ) );
		t.commit();
		s.close();

		assertTrue( Hibernate.BINARY.isEqual( bytes, entity.getLastModified() ) );
	}
}

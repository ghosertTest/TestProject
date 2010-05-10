package org.hibernate.test.generatedkeys.oracle;

import org.hibernate.test.TestCase;
import org.hibernate.Session;
import org.hibernate.dialect.Oracle9Dialect;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author <a href="mailto:steve@hibernate.org">Steve Ebersole </a>
 */
public class OracleGeneratedKeysTest extends TestCase {
	public OracleGeneratedKeysTest(String x) {
		super( x );
	}

	protected String[] getMappings() {
		return new String[] {
				"generatedkeys/oracle/MyEntity.hbm.xml"
		};
	}

	public static Test suite() {
		return new TestSuite( OracleGeneratedKeysTest.class );
	}

	public void testJDBC3GetGeneratedKeysSupportOnOracle() {
		if ( !( getDialect() instanceof Oracle9Dialect ) ) return;

		Session session = openSession();
		session.beginTransaction();

		MyEntity e = new MyEntity( "entity-1" );
		session.save( e );

		// this insert should happen immediately!
		assertEquals( "id not generated through forced insertion", new Long(1), e.getId() );

		session.delete( e );
		session.getTransaction().commit();
		session.close();
	}
}

// $Id: TimestampGeneratedValuesWithCachingTest.java,v 1.4 2005/08/10 17:20:24 steveebersole Exp $
package org.hibernate.test.generated;

import java.sql.SQLWarning;
import java.sql.Statement;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.dialect.SybaseDialect;

import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * Implementation of TimestampGeneratedValuesWithCachingTest.
 *
 * @author Steve Ebersole
 */
public class TimestampGeneratedValuesWithCachingTest extends AbstractGeneratedPropertyTest {

	public TimestampGeneratedValuesWithCachingTest(String x) {
		super( x );
	}

	protected boolean acceptsCurrentDialect() {
		return ( getDialect() instanceof SybaseDialect );
	}

	protected void afterSessionFactoryBuilt() throws Exception {
		if ( acceptsCurrentDialect() ) {
			// alter the table column to be of type TIMESTAMP, instead of the normal
			// BINARY/VARBINARY type-mapping.
			Session s = openSession();
			Statement stmnt = s.connection().createStatement();
			stmnt.execute( "alter table gen_prop drop column lastModified" );
			SQLWarning warning = stmnt.getWarnings();
			if ( warning != null ) {
				stmnt.clearWarnings();
				throw new HibernateException( "could not drop lastModified column : " + warning );
			}
			stmnt.execute( "alter table gen_prop add lastModified TIMESTAMP NOT NULL" );
			warning = stmnt.getWarnings();
			if ( warning != null ) {
				stmnt.clearWarnings();
				throw new HibernateException( "could not re-create lastModified column as TIMESTAMP type : " + warning );
			}
			stmnt.close();
			s.close();
		}
	}

	public static Test suite() {
		return new TestSuite( TimestampGeneratedValuesWithCachingTest.class );
	}
}

// $Id: SQLExceptionConversionTest.java,v 1.5 2005/05/21 15:46:41 oneovthafew Exp $
package org.hibernate.test.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.dialect.MySQLMyISAMDialect;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.SQLExceptionConverter;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.test.TestCase;
import org.hibernate.util.JDBCExceptionReporter;

/**
 * Implementation of SQLExceptionConversionTest.
 *
 * @author Steve Ebersole
 */
public class SQLExceptionConversionTest extends TestCase {

	public SQLExceptionConversionTest(String name) {
		super(name);
	}

	protected String[] getMappings() {
		return new String[] {"exception/User.hbm.xml", "exception/Group.hbm.xml"};
	}

	public void testIntegrityViolation() throws Exception {
		if ( getDialect() instanceof MySQLMyISAMDialect ) return;
		
		SQLExceptionConverter converter = getDialect().buildSQLExceptionConverter();

		Session session = openSession();
		Connection connection = session.connection();

		// Attempt to insert some bad values into the T_MEMBERSHIP table that should
		// result in a constraint violation
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO T_MEMBERSHIP (user_id, group_id) VALUES (?, ?)");
			ps.setLong(1, 52134241);    // Non-existent user_id
			ps.setLong(2, 5342);        // Non-existent group_id
			ps.executeUpdate();
			ps.close();

			fail("INSERT should have failed");
		}
		catch(SQLException sqle) {
			JDBCExceptionReporter.logExceptions(sqle, "Just output!!!!");
			JDBCException jdbcException = converter.convert(sqle, null, null);
			assertEquals( "Bad conversion [" + sqle.getMessage() + "]", ConstraintViolationException.class , jdbcException.getClass() );
			ConstraintViolationException ex = (ConstraintViolationException) jdbcException;
			System.out.println("Violated constraint name: " + ex.getConstraintName());
		}

		session.close();
	}

	public void testBadGrammar() throws Exception {
		SQLExceptionConverter converter = getDialect().buildSQLExceptionConverter();

		Session session = openSession();
		Connection connection = session.connection();

        // prepare a query against a non-existent table
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT user_id, user_name FROM tbl_user");
			ps.executeQuery();
			ps.close();

			fail("SQL compilation should have failed");
		}
		catch( SQLException sqle ) {
			assertEquals( "Bad conversion [" + sqle.getMessage() + "]", SQLGrammarException.class, converter.convert(sqle, null, null).getClass() );
		}

		session.close();
	}

	public static Test suite() {
		return new TestSuite(SQLExceptionConversionTest.class);
	}
}

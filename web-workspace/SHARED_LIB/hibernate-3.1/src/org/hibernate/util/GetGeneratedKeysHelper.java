//$Id: GetGeneratedKeysHelper.java,v 1.2 2005/11/17 18:28:14 steveebersole Exp $
package org.hibernate.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.AssertionFailure;

/**
 * @author Gavin King
 */
public final class GetGeneratedKeysHelper {

	private GetGeneratedKeysHelper() {
	}

	private static final Method PREPARE_STATEMENT_METHOD;
	private static final Method GET_GENERATED_KEYS_METHOD;

	static {
		try {
			PREPARE_STATEMENT_METHOD = Connection.class.getMethod(
					"prepareStatement",
			        new Class[] { String.class, String[].class }
			);
			GET_GENERATED_KEYS_METHOD = Statement.class.getDeclaredMethod(
					"getGeneratedKeys",
			        null
			);
		}
		catch ( Exception e ) {
			throw new AssertionFailure( "could not initialize getGeneratedKeys() support", e );
		}
	}

	public static PreparedStatement prepareStatement(Connection conn, String sql, String[] pkColumnNames)
			throws SQLException {
		Object[] args = new Object[] {sql, pkColumnNames};
		try {
			return ( PreparedStatement ) PREPARE_STATEMENT_METHOD.invoke( conn, args );
		}
		catch ( InvocationTargetException ite ) {
			if ( ite.getTargetException() instanceof SQLException ) {
				throw ( SQLException ) ite.getTargetException();
			}
			else if ( ite.getTargetException() instanceof RuntimeException ) {
				throw ( RuntimeException ) ite.getTargetException();
			}
			else {
				throw new AssertionFailure( "InvocationTargetException preparing statement capable of returning generated keys (JDBC3)", ite );
			}
		}
		catch ( IllegalAccessException iae ) {
			throw new AssertionFailure( "IllegalAccessException preparing statement capable of returning generated keys (JDBC3)", iae );
		}
	}

	public static ResultSet getGeneratedKey(PreparedStatement ps) throws SQLException {
		try {
			return ( ResultSet ) GET_GENERATED_KEYS_METHOD.invoke( ps, null );
		}
		catch ( InvocationTargetException ite ) {
			if ( ite.getTargetException() instanceof SQLException ) {
				throw ( SQLException ) ite.getTargetException();
			}
			else if ( ite.getTargetException() instanceof RuntimeException ) {
				throw ( RuntimeException ) ite.getTargetException();
			}
			else {
				throw new AssertionFailure( "InvocationTargetException extracting generated keys (JDBC3)", ite );
			}
		}
		catch ( IllegalAccessException iae ) {
			throw new AssertionFailure( "IllegalAccessException extracting generated keys (JDBC3)", iae );
		}
	}

}

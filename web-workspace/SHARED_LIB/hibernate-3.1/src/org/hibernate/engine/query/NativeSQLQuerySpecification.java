package org.hibernate.engine.query;

import org.hibernate.loader.custom.SQLQueryReturn;
import org.hibernate.loader.custom.SQLQueryScalarReturn;
import org.hibernate.util.ArrayHelper;
import org.hibernate.engine.NamedSQLQueryDefinition;

import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Collections;

/**
 * Defines the specification or blue-print for a native-sql query.
 * Essentially a simple struct containing the information needed to "translate"
 * a native-sql query and cache that translated representation.
 *
 * @author <a href="mailto:steve@hibernate.org">Steve Ebersole </a>
 */
public class NativeSQLQuerySpecification {
	private final String queryString;
	private final SQLQueryReturn[] sqlQueryReturns;
	private final SQLQueryScalarReturn[] sqlQueryScalarReturns;
	private final Set querySpaces;
	private final int hashCode;

	public NativeSQLQuerySpecification(
			String queryString,
	        SQLQueryReturn[] sqlQueryReturns,
	        SQLQueryScalarReturn[] sqlQueryScalarReturns,
	        Collection querySpaces) {
		this.queryString = queryString;
		this.sqlQueryReturns = sqlQueryReturns;
		this.sqlQueryScalarReturns = sqlQueryScalarReturns;
		if ( querySpaces == null ) {
			this.querySpaces = Collections.EMPTY_SET;
		}
		else {
			Set tmp = new HashSet();
			tmp.addAll( querySpaces );
			this.querySpaces = Collections.unmodifiableSet( tmp );
		}

		// pre-determine and cache the hashcode
		int hashCode = queryString.hashCode();
		hashCode = 29 * hashCode + this.querySpaces.hashCode();
		if ( this.sqlQueryReturns != null ) {
			hashCode = 29 * hashCode + ArrayHelper.toList( this.sqlQueryReturns ).hashCode();
		}
		if ( this.sqlQueryScalarReturns != null ) {
			hashCode = 29 * hashCode + ArrayHelper.toList( this.sqlQueryScalarReturns ).hashCode();
		}
		this.hashCode = hashCode;
	}

	public String getQueryString() {
		return queryString;
	}

	public SQLQueryReturn[] getSqlQueryReturns() {
		return sqlQueryReturns;
	}

	public SQLQueryScalarReturn[] getSqlQueryScalarReturns() {
		return sqlQueryScalarReturns;
	}

	public Set getQuerySpaces() {
		return querySpaces;
	}

	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		final NativeSQLQuerySpecification that = ( NativeSQLQuerySpecification ) o;

		if ( !querySpaces.equals( that.querySpaces ) ) {
			return false;
		}
		if ( !queryString.equals( that.queryString ) ) {
			return false;
		}
		if ( !Arrays.equals( sqlQueryReturns, that.sqlQueryReturns ) ) {
			return false;
		}
		if ( !Arrays.equals( sqlQueryScalarReturns, that.sqlQueryScalarReturns ) ) {
			return false;
		}

		return true;
	}


	public int hashCode() {
		return hashCode;
	}
}

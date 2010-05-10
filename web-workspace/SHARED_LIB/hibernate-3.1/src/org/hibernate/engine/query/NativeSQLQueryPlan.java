package org.hibernate.engine.query;

import org.hibernate.loader.custom.SQLCustomQuery;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.NamedSQLQueryDefinition;

import java.io.Serializable;

/**
 * Defines a query execution plan for a native-SQL query.
 *
 * @author <a href="mailto:steve@hibernate.org">Steve Ebersole </a>
 */
public class NativeSQLQueryPlan implements Serializable {
	private final String sourceQuery;
	private final SQLCustomQuery customQuery;

	public NativeSQLQueryPlan(NativeSQLQuerySpecification specification, SessionFactoryImplementor factory) {
		this.sourceQuery = specification.getQueryString();

		customQuery = new SQLCustomQuery(
				specification.getSqlQueryReturns(),
		        specification.getSqlQueryScalarReturns(),
		        specification.getQueryString(),
		        specification.getQuerySpaces(),
		        factory
		);
	}

	public String getSourceQuery() {
		return sourceQuery;
	}

	public SQLCustomQuery getCustomQuery() {
		return customQuery;
	}
}

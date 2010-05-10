//$Id: SQLQueryImpl.java,v 1.44 2005/12/12 08:57:27 maxcsaucdk Exp $
package org.hibernate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.QueryException;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.MappingException;
import org.hibernate.engine.ResultSetMappingDefinition;
import org.hibernate.engine.NamedSQLQueryDefinition;
import org.hibernate.engine.QueryParameters;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.engine.query.ParameterMetadata;
import org.hibernate.engine.query.NativeSQLQuerySpecification;
import org.hibernate.loader.custom.SQLQueryJoinReturn;
import org.hibernate.loader.custom.SQLQueryReturn;
import org.hibernate.loader.custom.SQLQueryRootReturn;
import org.hibernate.loader.custom.SQLQueryScalarReturn;
import org.hibernate.type.Type;
import org.hibernate.util.CollectionHelper;
import org.hibernate.util.StringHelper;

/**
 * Implements SQL query passthrough.
 *
 * <pre>
 * <sql-query name="mySqlQuery">
 * <return alias="person" class="eg.Person"/>
 *   SELECT {person}.NAME AS {person.name}, {person}.AGE AS {person.age}, {person}.SEX AS {person.sex}
 *   FROM PERSON {person} WHERE {person}.NAME LIKE 'Hiber%'
 * </sql-query>
 * </pre>
 *
 * @author Max Andersen
 */
public class SQLQueryImpl extends AbstractQueryImpl implements SQLQuery {

	private final List queryReturns;
	private final List scalarQueryReturns;
	private final Collection querySpaces;
	private final boolean callable;
	private boolean autodiscovertypes;

	/**
	 * Constructs a SQLQueryImpl given a sql query defined in the mappings.
	 *
	 * @param queryDef The representation of the defined <sql-query/>.
	 * @param session The session to which this SQLQueryImpl belongs.
	 */
	SQLQueryImpl(NamedSQLQueryDefinition queryDef, SessionImplementor session, ParameterMetadata parameterMetadata) {
		super( queryDef.getQueryString(), queryDef.getFlushMode(), session, parameterMetadata );
		if ( queryDef.getResultSetRef() != null ) {
			ResultSetMappingDefinition definition = session.getFactory()
					.getResultSetMapping( queryDef.getResultSetRef() );
			if (definition == null) {
				throw new MappingException(
						"Unable to find resultset-ref definition: " +
						queryDef.getResultSetRef() 
					);
			}
			this.scalarQueryReturns = Arrays.asList( definition.getScalarQueryReturns() );
			this.queryReturns = Arrays.asList( definition.getEntityQueryReturns() );
		}
		else {
			this.scalarQueryReturns = Arrays.asList( queryDef.getScalarQueryReturns() );
			this.queryReturns = Arrays.asList( queryDef.getQueryReturns() );
		}

		this.querySpaces = queryDef.getQuerySpaces();
		this.callable = queryDef.isCallable();
	}

	SQLQueryImpl(
			final String sql,
	        final List queryReturns,
	        final List scalarQueryReturns,
	        final Collection querySpaces,
	        final FlushMode flushMode,
	        boolean callable,
	        final SessionImplementor session,
	        ParameterMetadata parameterMetadata) {
		// TODO : absolutely no usages of this constructor form; can it go away?
		super( sql, flushMode, session, parameterMetadata );
		this.queryReturns = queryReturns;
		this.scalarQueryReturns = scalarQueryReturns;
		this.querySpaces = querySpaces;
		this.callable = callable;
	}

	SQLQueryImpl(
			final String sql,
	        final String returnAliases[],
	        final Class returnClasses[],
	        final LockMode[] lockModes,
	        final SessionImplementor session,
	        final Collection querySpaces,
	        final FlushMode flushMode,
	        ParameterMetadata parameterMetadata) {
		// TODO : this constructor form is *only* used from constructor directly below us; can it go away?
		super( sql, flushMode, session, parameterMetadata );
		scalarQueryReturns=null;
		queryReturns = new ArrayList(returnAliases.length);
		for ( int i=0; i<returnAliases.length; i++ ) {
			SQLQueryRootReturn ret = new SQLQueryRootReturn(
					returnAliases[i],
					returnClasses[i].getName(),
					lockModes==null ? LockMode.NONE : lockModes[i]
				);
			queryReturns.add(ret);
		}
		this.querySpaces = querySpaces;
		this.callable = false;
	}

	SQLQueryImpl(
			final String sql,
	        final String returnAliases[],
	        final Class returnClasses[],
	        final SessionImplementor session,
	        ParameterMetadata parameterMetadata) {
		this( sql, returnAliases, returnClasses, null, session, null, null, parameterMetadata );
	}
	
	SQLQueryImpl(String sql, SessionImplementor session, ParameterMetadata parameterMetadata) {
		super( sql, null, session, parameterMetadata );
		queryReturns = new ArrayList();
		scalarQueryReturns = new ArrayList();
		querySpaces = null;
		callable = false;
	}
	
	private static final SQLQueryReturn[] NO_SQL_RETURNS = new SQLQueryReturn[0];
	private static final SQLQueryScalarReturn[] NO_SQL_SCALAR_RETURNS = new SQLQueryScalarReturn[0];
	
	private SQLQueryReturn[] getQueryReturns() {
		return (SQLQueryReturn[]) queryReturns.toArray(NO_SQL_RETURNS);
	}

	private SQLQueryScalarReturn[] getQueryScalarReturns() {
		return scalarQueryReturns==null ? 
				null : 
				(SQLQueryScalarReturn[]) scalarQueryReturns.toArray(NO_SQL_SCALAR_RETURNS);
	}

	public List list() throws HibernateException {
		verifyParameters();
		Map namedParams = getNamedParams();
		before();

		NativeSQLQuerySpecification spec = new NativeSQLQuerySpecification(
		        expandParameterLists(namedParams),
				getQueryReturns(),
		        getQueryScalarReturns(),
		        querySpaces
		);
		
		try {
			return getSession().list( spec, getQueryParameters( namedParams ) );
		}
		finally {
			after();
		}
	}
	
	public ScrollableResults scroll(ScrollMode scrollMode) throws HibernateException {
		verifyParameters();
		Map namedParams = getNamedParams();
		before();

		NativeSQLQuerySpecification spec = new NativeSQLQuerySpecification(
		        expandParameterLists(namedParams),
				getQueryReturns(),
		        getQueryScalarReturns(),
		        querySpaces
		);
		
		QueryParameters qp = getQueryParameters( namedParams );
		qp.setScrollMode( scrollMode );
		
		try {
			return getSession().scroll( spec, qp );
		}
		finally {
			after();
		}
	}

	public ScrollableResults scroll() throws HibernateException {
		return scroll(ScrollMode.SCROLL_INSENSITIVE);
	}

	public Iterator iterate() throws HibernateException {
		throw new UnsupportedOperationException("SQL queries do not currently support iteration");
	}

	public QueryParameters getQueryParameters(Map namedParams) {
		QueryParameters qp = super.getQueryParameters(namedParams);
		qp.setCallable(callable);
		qp.setAutoDiscoverScalarTypes(autodiscovertypes);
		return qp;
	}

	protected void verifyParameters() {
		verifyParameters(callable);
		boolean noReturns = ( queryReturns==null || queryReturns.isEmpty() ) &&
				( scalarQueryReturns==null || scalarQueryReturns.isEmpty() );
		if (noReturns) {
			this.autodiscovertypes = noReturns;
			/*throw new QueryException(
					"addEntity() or addScalar() must be called on a sql query before executing the query.",
					getQueryString()
				);*/
		}
	}

	public String[] getReturnAliases() throws HibernateException {
		throw new UnsupportedOperationException("SQL queries do not currently support returning aliases");
	}

	public Type[] getReturnTypes() throws HibernateException {
		throw new UnsupportedOperationException("not yet implemented for SQL queries");
	}
	
	public Query setLockMode(String alias, LockMode lockMode) {
		throw new UnsupportedOperationException("cannot set the lock mode for a native SQL query");
	}
	
	protected Map getLockModes() {
		//we never need to apply locks to the SQL
		return CollectionHelper.EMPTY_MAP;
	}

	public SQLQuery addScalar(String columnAlias, Type type) {
		scalarQueryReturns.add( new SQLQueryScalarReturn(columnAlias, type) );
		return this;
	}

	public SQLQuery addScalar(String columnAlias) {
		autodiscovertypes = true;
		scalarQueryReturns.add( new SQLQueryScalarReturn(columnAlias, null) );
		return this;
	}

	public SQLQuery addJoin(String alias, String path) {
		return addJoin(alias, path, LockMode.READ);
	}

	public SQLQuery addEntity(Class entityClass) {
		return addEntity( StringHelper.unqualify( entityClass.getName() ), entityClass );
	}

	public SQLQuery addEntity(String entityName) {
		return addEntity( StringHelper.unqualify( entityName ), entityName );
	}

	public SQLQuery addEntity(String alias, String entityName) {
		return addEntity(alias, entityName, LockMode.READ);
	}

	public SQLQuery addEntity(String alias, Class entityClass) {
		return addEntity( alias, entityClass.getName() );
	}
	
	public SQLQuery addJoin(String alias, String path, LockMode lockMode) {
		int loc = path.indexOf('.');
		if ( loc < 0 ) {
			throw new QueryException( "not a property path: " + path );
		}
		String ownerAlias = path.substring(0, loc);
		String role = path.substring(loc+1);
		queryReturns.add( new SQLQueryJoinReturn(alias, ownerAlias, role, CollectionHelper.EMPTY_MAP, lockMode) );
		return this;
	}

	public SQLQuery addEntity(String alias, String entityName, LockMode lockMode) {
		queryReturns.add( new SQLQueryRootReturn(alias, entityName, lockMode) );
		return this;
	}

	public SQLQuery addEntity(String alias, Class entityClass, LockMode lockMode) {
		return addEntity( alias, entityClass.getName(), lockMode );
	}

	public SQLQuery setResultSetMapping(String name) {
		ResultSetMappingDefinition mapping = session.getFactory().getResultSetMapping(name);
		if (mapping == null) {
			throw new MappingException("Unknown SqlResultSetMapping named:" + name);
		}
		SQLQueryReturn[] returns = mapping.getEntityQueryReturns();
		int length = returns.length;
		for (int index = 0 ; index < length ; index++ ) {
			queryReturns.add( returns[index] );
		}
		SQLQueryScalarReturn[] scalarReturns = mapping.getScalarQueryReturns();
		length = scalarReturns.length;
		for (int index = 0 ; index < length ; index++ ) {
			scalarQueryReturns.add( scalarReturns[index] );
		}
		return this;
	}
}

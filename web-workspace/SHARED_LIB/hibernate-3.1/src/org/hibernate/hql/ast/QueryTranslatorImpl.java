// $Id: QueryTranslatorImpl.java,v 1.67 2005/11/22 13:41:11 steveebersole Exp $
package org.hibernate.hql.ast;

import antlr.ANTLRException;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.QueryException;
import org.hibernate.ScrollableResults;
import org.hibernate.hql.ParameterTranslations;
import org.hibernate.engine.QueryParameters;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.event.EventSource;
import org.hibernate.hql.FilterTranslator;
import org.hibernate.hql.antlr.HqlSqlTokenTypes;
import org.hibernate.hql.antlr.HqlTokenTypes;
import org.hibernate.hql.antlr.SqlTokenTypes;
import org.hibernate.hql.ast.exec.BasicExecutor;
import org.hibernate.hql.ast.exec.MultiTableDeleteExecutor;
import org.hibernate.hql.ast.exec.MultiTableUpdateExecutor;
import org.hibernate.hql.ast.exec.StatementExecutor;
import org.hibernate.hql.ast.tree.FromElement;
import org.hibernate.hql.ast.tree.InsertStatement;
import org.hibernate.hql.ast.tree.QueryNode;
import org.hibernate.hql.ast.tree.Statement;
import org.hibernate.hql.ast.util.ASTPrinter;
import org.hibernate.loader.hql.QueryLoader;
import org.hibernate.persister.entity.Queryable;
import org.hibernate.type.Type;
import org.hibernate.util.StringHelper;
import org.hibernate.util.ArrayHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

/**
 * A QueryTranslator that uses an AST based parser.
 * <br>User: josh
 * <br>Date: Dec 31, 2003
 * <br>Time: 7:50:35 AM
 *
 * @author Joshua Davis (pgmjsd@sourceforge.net)
 */
public class QueryTranslatorImpl implements FilterTranslator {

	private static final Log log = LogFactory.getLog( QueryTranslatorImpl.class );
	private static final Log AST_LOG = LogFactory.getLog( "org.hibernate.hql.ast.AST" );

	private SessionFactoryImplementor factory;

	private String hql;
	private boolean shallowQuery;
	private Map tokenReplacements;

	private Map enabledFilters; //TODO:this is only needed during compilation .. can we eliminate the instvar?

	private boolean compiled;
	private QueryLoader queryLoader;
	private StatementExecutor statementExecutor;

	private Statement sqlAst;
	private String sql;

	private ParameterTranslations paramTranslations;

	/**
	 * Creates a new AST-based query translator.
	 *
	 * @param query          The HQL query string.
	 * @param enabledFilters Any filters currently enabled for the session.
	 * @param factory        The session factory constructing this translator instance.
	 */
	public QueryTranslatorImpl(
	        String query,
	        Map enabledFilters,
	        SessionFactoryImplementor factory) {
		this.hql = query;
		this.compiled = false;
		this.shallowQuery = false;
		this.enabledFilters = enabledFilters;
		this.factory = factory;
	}

	/**
	 * Compile a "normal" query. This method may be called multiple
	 * times. Subsequent invocations are no-ops.
	 *
	 * @param replacements Defined query substitutions.
	 * @param shallow      Does this represent a shallow (scalar or entity-id) select?
	 * @throws QueryException   There was a problem parsing the query string.
	 * @throws MappingException There was a problem querying defined mappings.
	 */
	public void compile(
	        Map replacements,
	        boolean shallow) throws QueryException, MappingException {
		doCompile( replacements, shallow, null );
	}

	/**
	 * Compile a filter. This method may be called multiple
	 * times. Subsequent invocations are no-ops.
	 *
	 * @param collectionRole the role name of the collection used as the basis for the filter.
	 * @param replacements   Defined query substitutions.
	 * @param shallow        Does this represent a shallow (scalar or entity-id) select?
	 * @throws QueryException   There was a problem parsing the query string.
	 * @throws MappingException There was a problem querying defined mappings.
	 */
	public void compile(
	        String collectionRole,
	        Map replacements,
	        boolean shallow) throws QueryException, MappingException {
		doCompile( replacements, shallow, collectionRole );
	}

	public Statement getSqlAST() {
		return sqlAst;
	}

	/**
	 * Performs both filter and non-filter compiling.
	 *
	 * @param replacements   Defined query substitutions.
	 * @param shallow        Does this represent a shallow (scalar or entity-id) select?
	 * @param collectionRole the role name of the collection used as the basis for the filter, NULL if this
	 *                       is not a filter.
	 */
	private synchronized void doCompile(Map replacements, boolean shallow, String collectionRole) {
		// If the query is already compiled, skip the compilation.
		if ( compiled ) {
			if ( log.isDebugEnabled() ) {
				log.debug( "compile() : The query is already compiled, skipping..." );
			}
			return;
		}

		// Remember the parameters for the compilation.
		this.tokenReplacements = replacements;
		if ( tokenReplacements == null ) {
			tokenReplacements = new HashMap();
		}
		this.shallowQuery = shallow;

		try {
			// PHASE 1 : Parse the HQL into an AST.
			HqlParser parser = parse( true );

			// PHASE 2 : Analyze the HQL AST, and produce an SQL AST.
			HqlSqlWalker w = analyze( parser, collectionRole );

			sqlAst = ( Statement ) w.getAST();

			// at some point the generate phase needs to be moved out of here,
			// because a single object-level DML might spawn multiple SQL DML
			// command executions.
			//
			// Possible to just move the sql generation for dml stuff, but for
			// consistency-sake probably best to just move responsiblity for
			// the generation phase completely into the delegates
			// (QueryLoader/StatementExecutor) themselves.  Also, not sure why
			// QueryLoader currently even has a dependency on this at all; does
			// it need it?  Ideally like to see the walker itself given to the delegates directly...

			if ( sqlAst.needsExecutor() ) {
				statementExecutor = buildAppropriateStatementExecutor( w );
			}
			else {
				// PHASE 3 : Generate the SQL.
				generate( ( QueryNode ) sqlAst );
				queryLoader = new QueryLoader( this, factory, w.getSelectClause() );
			}

			compiled = true;
		}
		catch ( QueryException qe ) {
			qe.setQueryString( hql );
			throw qe;
		}
		catch ( RecognitionException e ) {
			throw  new QuerySyntaxException( e, hql );
		}
		catch ( ANTLRException e ) {
			QueryException qe = new QueryException( e.getMessage(), e );
			qe.setQueryString( hql );
			throw qe;
		}

		this.enabledFilters = null; //only needed during compilation phase...
	}

	private void generate(AST sqlAst) throws QueryException, RecognitionException {
		if ( sql == null ) {
			SqlGenerator gen = new SqlGenerator(factory);
			gen.statement( sqlAst );
			sql = gen.getSQL();
			if ( log.isDebugEnabled() ) {
				log.debug( "HQL: " + hql );
				log.debug( "SQL: " + sql );
			}
			gen.getParseErrorHandler().throwQueryException();
		}
	}

	private HqlSqlWalker analyze(HqlParser parser, String collectionRole) throws QueryException, RecognitionException {
		HqlSqlWalker w = new HqlSqlWalker( this, factory, parser, tokenReplacements, collectionRole );
		AST hqlAst = parser.getAST();

		// Transform the tree.
		w.statement( hqlAst );

		if ( AST_LOG.isDebugEnabled() ) {
			ASTPrinter printer = new ASTPrinter( SqlTokenTypes.class );
			AST_LOG.debug( printer.showAsString( w.getAST(), "--- SQL AST ---" ) );
		}

		w.getParseErrorHandler().throwQueryException();

		return w;
	}

	private HqlParser parse(boolean filter) throws TokenStreamException, RecognitionException {
		// Parse the query string into an HQL AST.
		HqlParser parser = HqlParser.getInstance( hql );
		parser.setFilter( filter );

		if ( log.isDebugEnabled() ) {
			log.debug( "parse() - HQL: " + hql );
		}
		parser.statement();

		AST hqlAst = parser.getAST();

		showHqlAst( hqlAst );

		parser.getParseErrorHandler().throwQueryException();
		return parser;
	}

	void showHqlAst(AST hqlAst) {
		if ( AST_LOG.isDebugEnabled() ) {
			ASTPrinter printer = new ASTPrinter( HqlTokenTypes.class );
			printer.setShowClassNames( false ); // The class names aren't interesting in the first tree.
			AST_LOG.debug( printer.showAsString( hqlAst, "--- HQL AST ---" ) );
		}
	}

	private void errorIfDML() throws HibernateException {
		if ( sqlAst.needsExecutor() ) {
			throw new HibernateException( "Not supported for DML operations" );
		}
	}

	private void errorIfSelect() throws HibernateException {
		if ( !sqlAst.needsExecutor() ) {
			throw new HibernateException( "Not supported for select queries" );
		}
	}

	private HqlSqlWalker getWalker() {
		return sqlAst.getWalker();
	}

	/**
	 * Types of the return values of an <tt>iterate()</tt> style query.
	 *
	 * @return an array of <tt>Type</tt>s.
	 */
	public Type[] getReturnTypes() {
		errorIfDML();
		return getWalker().getReturnTypes();
	}

	public String[] getReturnAliases() {
		errorIfDML();
		return getWalker().getReturnAliases();
	}

	public String[][] getColumnNames() {
		errorIfDML();
		return getWalker().getSelectClause().getColumnNames();
	}

	public Set getQuerySpaces() {
		return getWalker().getQuerySpaces();
	}

	public List list(SessionImplementor session, QueryParameters queryParameters)
			throws HibernateException {
		// Delegate to the QueryLoader...
		errorIfDML();
		return queryLoader.list( session, queryParameters );
	}

	/**
	 * Return the query results as an iterator
	 */
	public Iterator iterate(QueryParameters queryParameters, EventSource session)
			throws HibernateException {
		// Delegate to the QueryLoader...
		errorIfDML();
		return queryLoader.iterate( queryParameters, session );
	}

	/**
	 * Return the query results, as an instance of <tt>ScrollableResults</tt>
	 */
	public ScrollableResults scroll(QueryParameters queryParameters, SessionImplementor session)
			throws HibernateException {
		// Delegate to the QueryLoader...
		errorIfDML();
		return queryLoader.scroll( queryParameters, session );
	}

	public int executeUpdate(QueryParameters queryParameters, SessionImplementor session)
			throws HibernateException {
		errorIfSelect();
		return statementExecutor.execute( queryParameters, session );
	}

	/**
	 * The SQL query string to be called; implemented by all subclasses
	 */
	public String getSQLString() {
		return sql;
	}

	public List collectSqlStrings() {
		ArrayList list = new ArrayList();
		if ( isManipulationStatement() ) {
			String[] sqlStatements = statementExecutor.getSqlStatements();
			for ( int i = 0; i < sqlStatements.length; i++ ) {
				list.add( sqlStatements[i] );
			}
		}
		else {
			list.add( sql );
		}
		return list;
	}

	// -- Package local methods for the QueryLoader delegate --

	public boolean isShallowQuery() {
		return shallowQuery;
	}

	public String getQueryString() {
		return hql;
	}

	public Map getEnabledFilters() {
		return enabledFilters;
	}

	public int[] getNamedParameterLocs(String name) {
		return getWalker().getNamedParameterLocations( name );
	}

	public boolean containsCollectionFetches() {
		errorIfDML();
		List collectionFetches = ( ( QueryNode ) sqlAst ).getFromClause().getCollectionFetches();
		return collectionFetches != null && collectionFetches.size() > 0;
	}

	public boolean isManipulationStatement() {
		return sqlAst.needsExecutor();
	}

	public void validateScrollability() throws HibernateException {
		// Impl Note: allows multiple collection fetches as long as the
		// entire fecthed graph still "points back" to a single
		// root entity for return

		errorIfDML();

		QueryNode query = ( QueryNode ) sqlAst;

		// If there are no collection fetches, then no further checks are needed
		List collectionFetches = query.getFromClause().getCollectionFetches();
		if ( collectionFetches.isEmpty() ) {
			return;
		}

		// A shallow query is ok (although technically there should be no fetching here...)
		if ( isShallowQuery() ) {
			return;
		}

		// Otherwise, we have a non-scalar select with defined collection fetch(es).
		// Make sure that there is only a single root entity in the return (no tuples)
		if ( getReturnTypes().length > 1 ) {
			throw new HibernateException( "cannot scroll with collection fetches and returned tuples" );
		}

		FromElement owner = null;
		Iterator itr = query.getSelectClause().getFromElementsForLoad().iterator();
		while ( itr.hasNext() ) {
			// should be the first, but just to be safe...
			final FromElement fromElement = ( FromElement ) itr.next();
			if ( fromElement.getOrigin() == null ) {
				owner = fromElement;
				break;
			}
		}

		if ( owner == null ) {
			throw new HibernateException( "unable to locate collection fetch(es) owner for scrollability checks" );
		}

		// This is not strictly true.  We actually just need to make sure that
		// it is ordered by root-entity PK and that that order-by comes before
		// any non-root-entity ordering...

		AST primaryOrdering = query.getOrderByClause().getFirstChild();
		if ( primaryOrdering != null ) {
			// TODO : this is a bit dodgy, come up with a better way to check this (plus see above comment)
			String [] idColNames = owner.getQueryable().getIdentifierColumnNames();
			String expectedPrimaryOrderSeq = StringHelper.join(
			        ", ",
			        StringHelper.qualify( owner.getTableAlias(), idColNames )
			);
			if (  !primaryOrdering.getText().startsWith( expectedPrimaryOrderSeq ) ) {
				throw new HibernateException( "cannot scroll results with collection fetches which are not ordered primarily by the root entity's PK" );
			}
		}
	}

	private StatementExecutor buildAppropriateStatementExecutor(HqlSqlWalker walker) {
		Statement statement = ( Statement ) walker.getAST();
		if ( walker.getStatementType() == HqlSqlTokenTypes.DELETE ) {
			FromElement fromElement = walker.getFinalFromClause().getFromElement();
			Queryable persister = fromElement.getQueryable();
			if ( persister.isMultiTable() ) {
				return new MultiTableDeleteExecutor( walker );
			}
			else {
				return new BasicExecutor( walker, persister );
			}
		}
		else if ( walker.getStatementType() == HqlSqlTokenTypes.UPDATE ) {
			FromElement fromElement = walker.getFinalFromClause().getFromElement();
			Queryable persister = fromElement.getQueryable();
			if ( persister.isMultiTable() ) {
				// even here, if only properties mapped to the "base table" are referenced
				// in the set and where clauses, this could be handled by the BasicDelegate.
				// TODO : decide if it is better performance-wise to perform that check, or to simply use the MultiTableUpdateDelegate
				return new MultiTableUpdateExecutor( walker );
			}
			else {
				return new BasicExecutor( walker, persister );
			}
		}
		else if ( walker.getStatementType() == HqlSqlTokenTypes.INSERT ) {
			return new BasicExecutor( walker, ( ( InsertStatement ) statement ).getIntoClause().getQueryable() );
		}
		else {
			throw new QueryException( "Unexpected statement type" );
		}
	}

	public ParameterTranslations getParameterTranslations() {
		if ( paramTranslations == null ) {
			paramTranslations = new ParameterTranslationsImpl( getWalker().getParameters() );
		}
		return paramTranslations;
	}

}

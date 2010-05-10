//$Id: AbstractSessionImpl.java,v 1.4 2005/12/09 04:08:43 oneovthafew Exp $
package org.hibernate.impl;

import org.hibernate.MappingException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.engine.NamedQueryDefinition;
import org.hibernate.engine.NamedSQLQueryDefinition;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.engine.QueryParameters;
import org.hibernate.engine.query.HQLQueryPlan;
import org.hibernate.engine.query.NativeSQLQueryPlan;
import org.hibernate.engine.query.NativeSQLQuerySpecification;

import java.util.Set;
import java.util.List;

/**
 * Functionality common to stateless and stateful sessions
 * 
 * @author Gavin King
 */
public abstract class AbstractSessionImpl implements SessionImplementor {
	protected transient SessionFactoryImpl factory;

	protected AbstractSessionImpl(SessionFactoryImpl factory) {
		this.factory = factory;
	}

	public Query getNamedQuery(String queryName) throws MappingException {
		NamedQueryDefinition nqd = factory.getNamedQuery( queryName );
		final Query query;
		if ( nqd != null ) {
			String queryString = nqd.getQueryString();
			query = new QueryImpl(
					queryString,
			        nqd.getFlushMode(),
			        this,
			        getHQLQueryPlan( queryString, false ).getParameterMetadata()
			);
			query.setComment( "named HQL query " + queryName );
		}
		else {
			NamedSQLQueryDefinition nsqlqd = factory.getNamedSQLQuery( queryName );
			if ( nsqlqd==null ) {
				throw new MappingException( "Named query not known: " + queryName );
			}
			query = new SQLQueryImpl(
					nsqlqd,
			        this,
			        factory.getQueryPlanCache().getSQLParameterMetadata( nsqlqd.getQueryString() )
			);
			query.setComment( "named native SQL query " + queryName );
			nqd = nsqlqd;
		}
		initQuery( query, nqd );
		return query;
	}

	public Query getNamedSQLQuery(String queryName) throws MappingException {
		NamedSQLQueryDefinition nsqlqd = factory.getNamedSQLQuery( queryName );
		if ( nsqlqd==null ) {
			throw new MappingException( "Named SQL query not known: " + queryName );
		}
		Query query = new SQLQueryImpl(
				nsqlqd,
		        this,
		        factory.getQueryPlanCache().getSQLParameterMetadata( nsqlqd.getQueryString() )
		);
		query.setComment( "named native SQL query " + queryName );
		initQuery( query, nsqlqd );
		return query;
	}

	private void initQuery(Query query, NamedQueryDefinition nqd) {
		query.setCacheable( nqd.isCacheable() );
		query.setCacheRegion( nqd.getCacheRegion() );
		if ( nqd.getTimeout()!=null ) query.setTimeout( nqd.getTimeout().intValue() );
		if ( nqd.getFetchSize()!=null ) query.setFetchSize( nqd.getFetchSize().intValue() );
		if ( nqd.getCacheMode() != null ) query.setCacheMode( nqd.getCacheMode() );
		query.setReadOnly( nqd.isReadOnly() );
		if ( nqd.getComment() != null ) query.setComment( nqd.getComment() );
	}

	public Query createQuery(String queryString) {
		QueryImpl query = new QueryImpl(
				queryString,
		        this,
		        getHQLQueryPlan( queryString, false ).getParameterMetadata()
			);
		query.setComment( queryString );
		return query;
	}

	public SQLQuery createSQLQuery(String sql) {
		SQLQueryImpl query = new SQLQueryImpl(
				sql,
		        this,
		        factory.getQueryPlanCache().getSQLParameterMetadata( sql )
			);
		query.setComment( "dynamic native SQL query" );
		return query;
	}

	protected HQLQueryPlan getHQLQueryPlan(String query, boolean shallow) throws HibernateException {
		HQLQueryPlan plan = factory.getQueryPlanCache().getHQLQueryPlan( query, shallow, getEnabledFilters() );
		autoFlushIfRequired( plan.getQuerySpaces() );
		return plan;
	}

	protected NativeSQLQueryPlan getNativeSQLQueryPlan(NativeSQLQuerySpecification spec) throws HibernateException {
		NativeSQLQueryPlan plan = factory.getQueryPlanCache().getNativeSQLQueryPlan( spec );
		autoFlushIfRequired( plan.getCustomQuery().getQuerySpaces() );
		return plan;
	}

	public List list(NativeSQLQuerySpecification spec, QueryParameters queryParameters)
			throws HibernateException {
		return listCustomQuery( getNativeSQLQueryPlan( spec ).getCustomQuery(), queryParameters );
	}

	public ScrollableResults scroll(NativeSQLQuerySpecification spec, QueryParameters queryParameters)
			throws HibernateException {
		return scrollCustomQuery( getNativeSQLQueryPlan( spec ).getCustomQuery(), queryParameters );
	}

	protected abstract boolean autoFlushIfRequired(Set querySpaces) throws HibernateException;
}

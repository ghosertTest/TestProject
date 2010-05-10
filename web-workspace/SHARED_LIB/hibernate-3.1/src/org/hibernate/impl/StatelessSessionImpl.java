//$Id: StatelessSessionImpl.java,v 1.18 2005/11/30 13:56:31 steveebersole Exp $
package org.hibernate.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.CacheMode;
import org.hibernate.ConnectionReleaseMode;
import org.hibernate.Criteria;
import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.LockMode;
import org.hibernate.MappingException;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionException;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.engine.EntityKey;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.engine.QueryParameters;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.StatefulPersistenceContext;
import org.hibernate.engine.Versioning;
import org.hibernate.engine.query.HQLQueryPlan;
import org.hibernate.event.EventListeners;
import org.hibernate.id.IdentifierGeneratorFactory;
import org.hibernate.jdbc.Batcher;
import org.hibernate.jdbc.JDBCContext;
import org.hibernate.loader.criteria.CriteriaLoader;
import org.hibernate.loader.custom.CustomLoader;
import org.hibernate.loader.custom.CustomQuery;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.Type;
import org.hibernate.util.CollectionHelper;

/**
 * @author Gavin King
 */
public class StatelessSessionImpl extends AbstractSessionImpl
		implements JDBCContext.Context, StatelessSession {
	
	private JDBCContext jdbcContext;
	private boolean closed;
	
	StatelessSessionImpl(
			Connection connection, 
			SessionFactoryImpl factory
	) {
		super(factory);
		this.jdbcContext = new JDBCContext( this, connection, EmptyInterceptor.INSTANCE );
	}
	
	public void delete(Object entity) {
		delete(null, entity);
	}

	public Serializable insert(Object entity) {
		return insert(null, entity);
	}

	public void update(Object entity) {
		update(null, entity);
	}

	public Serializable insert(String entityName, Object entity) {
		EntityPersister persister = getEntityPersister(entityName, entity);
		Serializable id = persister.getIdentifierGenerator().generate(this, entity);
		Object[] state = persister.getPropertyValues(entity, EntityMode.POJO);
		if ( persister.isVersioned() ) {
			boolean substitute = Versioning.seedVersion(state, persister.getVersionProperty(), persister.getVersionType(), this);
			if (substitute) persister.setPropertyValues(entity, state, EntityMode.POJO);
		}
		if ( id == IdentifierGeneratorFactory.POST_INSERT_INDICATOR ) {
			id = persister.insert(state, entity, this);
		}
		else {
			persister.insert(id, state, entity, this);			
		}
		persister.setIdentifier(entity, id, EntityMode.POJO);
		return id;
	}
	
	public void update(String entityName, Object entity) {
		EntityPersister persister = getEntityPersister(entityName, entity);
		Serializable id = persister.getIdentifier(entity, EntityMode.POJO);
		Object[] state = persister.getPropertyValues(entity, EntityMode.POJO);
		Object oldVersion;
		if ( persister.isVersioned() ) {
			oldVersion = persister.getVersion(entity, EntityMode.POJO);
			Object newVersion = Versioning.increment( oldVersion, persister.getVersionType(), this );
			Versioning.setVersion(state, newVersion, persister);
			persister.setPropertyValues(entity, state, EntityMode.POJO);
		}
		else {
			oldVersion = null;
		}
		persister.update(id, state, null, false, null, oldVersion, entity, null, this);
	}
	
	public void delete(String entityName, Object entity) {
		EntityPersister persister = getEntityPersister(entityName, entity);
		Serializable id = persister.getIdentifier(entity, EntityMode.POJO);
		Object version = persister.getVersion(entity, EntityMode.POJO);
		persister.delete(id, version, entity, this);
	}
	
	public void close() {
		managedClose();
	}

	public ConnectionReleaseMode getConnectionReleaseMode() {
		return factory.getSettings().getConnectionReleaseMode();
	}

	public boolean isAutoCloseSessionEnabled() {
		return factory.getSettings().isAutoCloseSessionEnabled();
	}

	public boolean isFlushBeforeCompletionEnabled() {
		return true;
	}

	public boolean isFlushModeNever() {
		return false;
	}

	public void managedClose() {
		jdbcContext.getConnectionManager().close();
		closed = true;
	}

	public void managedFlush() {
		getBatcher().executeBatch();
	}

	public boolean shouldAutoClose() {
		return isAutoCloseSessionEnabled() && isOpen();
	}

	public void afterTransactionCompletion(boolean successful, Transaction tx) {}

	public void beforeTransactionCompletion(Transaction tx) {}

	public String bestGuessEntityName(Object object) {
		if (object instanceof HibernateProxy) {
			object = ( (HibernateProxy) object ).getHibernateLazyInitializer().getImplementation();
		}
		return guessEntityName(object);
	}

	public Connection connection() {
		return jdbcContext.userConnection();
	}

	public int executeUpdate(String query, QueryParameters queryParameters) 
	throws HibernateException {
		queryParameters.validateParameters();
		HQLQueryPlan plan = getHQLQueryPlan( query, false );
		boolean success = false;
		int result = 0;
		try {
			result = plan.performExecuteUpdate( queryParameters, this );
			success = true;
		}
		finally {
			afterOperation(success);
		}
		temporaryPersistenceContext.clear();
		return result;
	}

	public Batcher getBatcher() {
		return jdbcContext.getConnectionManager()
				.getBatcher();
	}

	public CacheMode getCacheMode() {
		return CacheMode.IGNORE;
	}

	public int getDontFlushFromFind() {
		return 0;
	}

	public Map getEnabledFilters() {
		return CollectionHelper.EMPTY_MAP;
	}

	public Serializable getContextEntityIdentifier(Object object) {
		return null;
	}

	public EntityMode getEntityMode() {
		return EntityMode.POJO;
	}

	public EntityPersister getEntityPersister(String entityName, Object object) 
	throws HibernateException {
		if (entityName==null) {
			return factory.getEntityPersister( guessEntityName(object) );
		}
		else {
			return factory.getEntityPersister( entityName )
					.getSubclassEntityPersister( object, getFactory(), EntityMode.POJO );
		}
	}

	public Object getEntityUsingInterceptor(EntityKey key) throws HibernateException {
		return null;
	}

	public SessionFactoryImplementor getFactory() {
		return factory;
	}

	public Type getFilterParameterType(String filterParameterName) {
		throw new UnsupportedOperationException();
	}

	public Object getFilterParameterValue(String filterParameterName) {
		throw new UnsupportedOperationException();
	}

	public FlushMode getFlushMode() {
		return FlushMode.COMMIT;
	}

	public Interceptor getInterceptor() {
		return EmptyInterceptor.INSTANCE;
	}

	public EventListeners getListeners() {
		throw new UnsupportedOperationException();
	}

	public PersistenceContext getPersistenceContext() {
		return temporaryPersistenceContext;
	}

	public long getTimestamp() {
		throw new UnsupportedOperationException();
	}
	
	private PersistenceContext temporaryPersistenceContext = new StatefulPersistenceContext(this);
	
	public Object get(Class entityClass, Serializable id) {
		return get( entityClass.getName(), id );
	}
	
	public Object get(Class entityClass, Serializable id, LockMode lockMode) {
		return get( entityClass.getName(), id, lockMode );
	}
	
	public Object get(String entityName, Serializable id) {
		return get(entityName, id, LockMode.NONE);
	}

	public Object get(String entityName, Serializable id, LockMode lockMode) {
		Object result = getFactory().getEntityPersister(entityName)
				.load(id, null, lockMode, this);
		temporaryPersistenceContext.clear();
		return result;
	}

	public String guessEntityName(Object entity) throws HibernateException {
		return entity.getClass().getName();
	}

	public Object immediateLoad(String entityName, Serializable id) throws HibernateException {
		throw new SessionException("proxies cannot be fetched by a stateless session");
	}

	public void initializeCollection(PersistentCollection collection, boolean writing) 
	throws HibernateException {
		throw new SessionException("collections cannot be fetched by a stateless session");
	}

	public Object instantiate(String entityName, Serializable id) throws HibernateException {
		return getFactory().getEntityPersister(entityName).instantiate(id, EntityMode.POJO);
	}

	public Object internalLoad(String entityName, Serializable id, boolean eager, boolean nullable) 
	throws HibernateException {
		EntityPersister persister = getFactory().getEntityPersister(entityName);
		if ( !eager && persister.hasProxy() ) {
			return persister.createProxy(id, this);
		}
		Object loaded = temporaryPersistenceContext.getEntity( new EntityKey(id, persister, EntityMode.POJO) );
		//TODO: if not loaded, throw an exception
		return loaded==null ? get(entityName, id) : loaded;
	}

	public boolean isConnected() {
		return jdbcContext.getConnectionManager().isCurrentlyConnected();
	}

	public boolean isOpen() {
		return !closed;
	}

	public boolean isTransactionInProgress() {
		return jdbcContext.isTransactionInProgress();
	}

	public Iterator iterate(String query, QueryParameters queryParameters) throws HibernateException {
		throw new UnsupportedOperationException();
	}

	public Iterator iterateFilter(Object collection, String filter, QueryParameters queryParameters) 
	throws HibernateException {
		throw new UnsupportedOperationException();
	}

	public List listFilter(Object collection, String filter, QueryParameters queryParameters) 
	throws HibernateException {
		throw new UnsupportedOperationException();
	}

	public void setAutoClear(boolean enabled) {
		throw new UnsupportedOperationException();
	}

	public void setCacheMode(CacheMode cm) {
		throw new UnsupportedOperationException();
	}

	public void setFlushMode(FlushMode fm) {
		throw new UnsupportedOperationException();
	}
	
	public Transaction getTransaction() throws HibernateException {
		return jdbcContext.getTransaction();
	}
	
	public Transaction beginTransaction() throws HibernateException {
		Transaction result = getTransaction();
		result.begin();
		return result;
	}
	
	public boolean isEventSource() {
		return false;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//TODO: COPY/PASTE FROM SessionImpl, pull up!

	public List list(String query, QueryParameters queryParameters) throws HibernateException {
		queryParameters.validateParameters();
		HQLQueryPlan plan = getHQLQueryPlan( query, false );
		boolean success = false;
		List results = CollectionHelper.EMPTY_LIST;
		try {
			results = plan.performList( queryParameters, this );
			success = true;
		}
		finally {
			afterOperation(success);
		}
		temporaryPersistenceContext.clear();
		return results;
	}

	public void afterOperation(boolean success) {
		if ( !jdbcContext.isTransactionInProgress() ) {
			jdbcContext.afterNontransactionalQuery(success);
		}
	}

	public Criteria createCriteria(Class persistentClass, String alias) {
		return new CriteriaImpl( persistentClass.getName(), alias, this );
	}

	public Criteria createCriteria(String entityName, String alias) {
		return new CriteriaImpl(entityName, alias, this);
	}

	public Criteria createCriteria(Class persistentClass) {
		return new CriteriaImpl( persistentClass.getName(), this );
	}

	public Criteria createCriteria(String entityName) {
		return new CriteriaImpl(entityName, this);
	}

	public ScrollableResults scroll(CriteriaImpl criteria, ScrollMode scrollMode) {
		String entityName = criteria.getEntityOrClassName();
		CriteriaLoader loader = new CriteriaLoader(
				getOuterJoinLoadable(entityName),
				factory,
				criteria,
				entityName,
		        getEnabledFilters()
			);
		return loader.scroll(this, scrollMode);
	}

	public List list(CriteriaImpl criteria) throws HibernateException {

		String[] implementors = factory.getImplementors( criteria.getEntityOrClassName() );
		int size = implementors.length;

		CriteriaLoader[] loaders = new CriteriaLoader[size];
		Set spaces = new HashSet();
		for( int i=0; i <size; i++ ) {

			loaders[i] = new CriteriaLoader(
					getOuterJoinLoadable( implementors[i] ),
					factory,
					criteria,
					implementors[i],
			        getEnabledFilters()
			);

			spaces.addAll( loaders[i].getQuerySpaces() );

		}


		List results = Collections.EMPTY_LIST;
		boolean success = false;
		try {
			for( int i=0; i<size; i++ ) {
				final List currentResults = loaders[i].list(this);
				currentResults.addAll(results);
				results = currentResults;
			}
			success = true;
		}
		finally {
			afterOperation(success);
		}
		temporaryPersistenceContext.clear();
		return results;
	}

	private OuterJoinLoadable getOuterJoinLoadable(String entityName) throws MappingException {
		EntityPersister persister = factory.getEntityPersister(entityName);
		if ( !(persister instanceof OuterJoinLoadable) ) {
			throw new MappingException( "class persister is not OuterJoinLoadable: " + entityName );
		}
		return ( OuterJoinLoadable ) persister;
	}

	public List listCustomQuery(CustomQuery customQuery, QueryParameters queryParameters) 
	throws HibernateException {

		CustomLoader loader = new CustomLoader( customQuery, getFactory() );

		boolean success = false;
		List results;
		try {
			results = loader.list(this, queryParameters);
			success = true;
		}
		finally {
			afterOperation(success);
		}
		temporaryPersistenceContext.clear();
		return results;
	}

	public ScrollableResults scrollCustomQuery(CustomQuery customQuery, QueryParameters queryParameters) 
	throws HibernateException {
		CustomLoader loader = new CustomLoader( customQuery, getFactory() );
		return loader.scroll(queryParameters, this);
	}

	public ScrollableResults scroll(String query, QueryParameters queryParameters) throws HibernateException {
		HQLQueryPlan plan = getHQLQueryPlan( query, false );
		return plan.performScroll( queryParameters, this );
	}

	public void afterScrollOperation() {
		temporaryPersistenceContext.clear();
	}

	public void flush() {}

	public String getFetchProfile() {
		return null;
	}

	public void setFetchProfile(String name) {}

	public void afterTransactionBegin(Transaction tx) {}

	protected boolean autoFlushIfRequired(Set querySpaces) throws HibernateException {
		// no auto-flushing to support in stateless session
		return false;
	}
}

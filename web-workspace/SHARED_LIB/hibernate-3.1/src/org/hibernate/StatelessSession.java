//$Id: StatelessSession.java,v 1.8 2005/08/19 21:13:20 oneovthafew Exp $
package org.hibernate;

import java.io.Serializable;
import java.sql.Connection;

/**
 * A command-oriented API for performing bulk operations
 * against a database.<br>
 * <br>
 * A stateless session does not implement a first-level cache nor
 * interact with any second-level cache, nor does it implement 
 * transactional write-behind or automatic dirty checking, nor do 
 * operations cascade to associated instances. Collections are 
 * ignored by a stateless session. Operations performed via a
 * stateless session bypass Hibernate's event model and
 * interceptors. Stateless sessions are vulnerable to data
 * aliasing effects, due to the lack of a first-level cache.<br>
 * <br>
 * For certain kinds of transactions, a stateless session may
 * perform slightly faster than a stateful session.
 * 
 * @author Gavin King
 */
public interface StatelessSession extends Serializable {
	/**
	 * Close the stateless session and release the JDBC connection.
	 */
	public void close();
	/**
	 * Insert a row.
	 * @param entity a new transient instance
	 */
	public Serializable insert(Object entity);
	/**
	 * Update a row.
	 * @param entity a detached entity instance
	 */
	public void update(Object entity);
	/**
	 * Delete a row.
	 * @param entity a detached entity instance
	 */
	public void delete(Object entity);
	/**
	 * Insert a row.
	 * @param entity a new transient instance
	 * @return the identifier of the instance
	 */
	public Serializable insert(String entityName, Object entity);
	/**
	 * Update a row.
	 * @param entity a detached entity instance
	 */
	public void update(String entityName, Object entity);
	/**
	 * Delete a row.
	 * @param entity a detached entity instance
	 */
	public void delete(String entityName, Object entity);
	/**
	 * Retrieve a row.
	 * @return a detached entity instance
	 */
	public Object get(String entityName, Serializable id);
	/**
	 * Retrieve a row.
	 * @return a detached entity instance
	 */
	public Object get(Class entityClass, Serializable id);
	/**
	 * Retrieve a row, obtaining the specified lock mode.
	 * @return a detached entity instance
	 */
	public Object get(String entityName, Serializable id, LockMode lockMode);
	/**
	 * Retrieve a row, obtaining the specified lock mode.
	 * @return a detached entity instance
	 */
	public Object get(Class entityClass, Serializable id, LockMode lockMode);
	/**
	 * Create a new instance of <tt>Query</tt> for the given HQL query string.
	 * Entities returned by the query are detached.
	 */
	public Query createQuery(String queryString);
	/**
	 * Obtain an instance of <tt>Query</tt> for a named query string defined in 
	 * the mapping file. Entities returned by the query are detached.
	 */
	public Query getNamedQuery(String queryName);
	/**
	 * Create a new <tt>Criteria</tt> instance, for the given entity class,
	 * or a superclass of an entity class. Entities returned by the query are 
	 * detached.
	 *
	 * @param persistentClass a class, which is persistent, or has persistent subclasses
	 * @return Criteria
	 */
	public Criteria createCriteria(Class persistentClass);
	/**
	 * Create a new <tt>Criteria</tt> instance, for the given entity class,
	 * or a superclass of an entity class, with the given alias.
	 * Entities returned by the query are detached.
	 *
	 * @param persistentClass a class, which is persistent, or has persistent subclasses
	 * @return Criteria
	 */
	public Criteria createCriteria(Class persistentClass, String alias);
	/**
	 * Create a new <tt>Criteria</tt> instance, for the given entity name.
	 * Entities returned by the query are detached.
	 *
	 * @param entityName
	 * @return Criteria
	 */
	public Criteria createCriteria(String entityName);
	/**
	 * Create a new <tt>Criteria</tt> instance, for the given entity name,
	 * with the given alias. Entities returned by the query are detached.
	 *
	 * @param entityName
	 * @return Criteria
	 */
	public Criteria createCriteria(String entityName, String alias);
	/**
	 * Create a new instance of <tt>SQLQuery</tt> for the given SQL query string.
	 * Entities returned by the query are detached.
	 *
	 * @param queryString a SQL query
	 * @return SQLQuery
	 * @throws HibernateException
	 */
	public SQLQuery createSQLQuery(String queryString) throws HibernateException;
	/**
	 * Begin a Hibernate transaction.
	 */
	public Transaction beginTransaction();
	/**
	 * Get the current Hibernate transaction.
	 */
	public Transaction getTransaction();
	/**
	 * Returns the current JDBC connection associated with this
	 * instance.<br>
	 * <br> 
	 * If the session is using aggressive collection release (as in a
	 * CMT environment), it is the application's responsibility to
	 * close the connection returned by this call. Otherwise, the
	 * application should not close the connection.
	 */
	public Connection connection();
}

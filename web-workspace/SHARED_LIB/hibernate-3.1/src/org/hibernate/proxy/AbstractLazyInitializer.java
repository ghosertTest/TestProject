//$Id: AbstractLazyInitializer.java,v 1.13 2005/08/31 20:09:09 oneovthafew Exp $
package org.hibernate.proxy;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.LazyInitializationException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.engine.EntityKey;
import org.hibernate.engine.SessionImplementor;

/**
 * @author Gavin King
 */
public abstract class AbstractLazyInitializer implements LazyInitializer {
	
	private Object target;
	private boolean initialized;
	private String entityName;
	private Serializable id;
	private transient SessionImplementor session;
	private boolean unwrap;
	
	protected AbstractLazyInitializer(String entityName, Serializable id, SessionImplementor session) {
		this.id = id;
		this.session = session;
		this.entityName = entityName;
	}

	public final Serializable getIdentifier() {
		return id;
	}

	public final void setIdentifier(Serializable id) {
		this.id = id;
	}

	public final String getEntityName() {
		return entityName;
	}

	public final boolean isUninitialized() {
		return !initialized;
	}

	public final SessionImplementor getSession() {
		return session;
	}

	public final void initialize() throws HibernateException {
		if (!initialized) {
			if ( session==null ) {
				throw new LazyInitializationException("could not initialize proxy - no Session");
			}
			else if ( !session.isOpen() ) {
				throw new LazyInitializationException("could not initialize proxy - the owning Session was closed");
			}
			else if ( !session.isConnected() ) {
				throw new LazyInitializationException("could not initialize proxy - the owning Session is disconnected");
			}
			else {
				target = session.immediateLoad(entityName, id);
				initialized = true;
				if (!unwrap) {
					ObjectNotFoundException.throwIfNull(target, id, entityName); //should it be UnresolvableObject?
				}
			}
		}
	}

	public final void setSession(SessionImplementor s) throws HibernateException {
		if (s!=session) {
			if ( isConnectedToSession() ) {
				//TODO: perhaps this should be some other RuntimeException...
				throw new HibernateException("illegally attempted to associate a proxy with two open Sessions");
			}
			else {
				session = s;
			}
		}
	}

	private final boolean isConnectedToSession() {
		return session!=null && 
				session.isOpen() && 
				session.getPersistenceContext().containsProxy(this);
	}
	
	public final void setImplementation(Object target) {
		this.target = target;
		initialized = true;
	}

	/**
	 * Return the underlying persistent object, initializing if necessary
	 */
	public final Object getImplementation() {
		initialize();
		return target;
	}

	/**
	 * Return the underlying persistent object in the given <tt>Session</tt>, or null,
	 * do not initialize the proxy
	 */
	public final Object getImplementation(SessionImplementor s) throws HibernateException {
		final EntityKey entityKey = new EntityKey(
				getIdentifier(),
				s.getFactory().getEntityPersister( getEntityName() ),
				s.getEntityMode()
			);
		return s.getPersistenceContext().getEntity( entityKey );
	}

	protected final Object getTarget() {
		return target;
	}

	public boolean isUnwrap() {
		return unwrap;
	}

	public void setUnwrap(boolean unwrap) {
		this.unwrap = unwrap;
	}

}

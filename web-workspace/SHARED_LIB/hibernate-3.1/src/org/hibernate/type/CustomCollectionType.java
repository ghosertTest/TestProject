//$Id: CustomCollectionType.java,v 1.13 2005/08/01 16:29:32 oneovthafew Exp $
package org.hibernate.type;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.usertype.UserCollectionType;

/**
 * A custom type for mapping user-written classes that implement <tt>PersistentCollection</tt>
 * 
 * @see org.hibernate.collection.PersistentCollection
 * @see org.hibernate.usertype.UserCollectionType
 * @author Gavin King
 */
public class CustomCollectionType extends CollectionType {

	private final UserCollectionType userType;

	public CustomCollectionType(Class userTypeClass, String role, String foreignKeyPropertyName, boolean isEmbeddedInXML) {
		super(role, foreignKeyPropertyName, isEmbeddedInXML);
		
		if ( !UserCollectionType.class.isAssignableFrom(userTypeClass) ) {
			throw new MappingException( "Custom type does not implement UserCollectionType: " + userTypeClass.getName() );
		}
		
		try {
			userType = (UserCollectionType) userTypeClass.newInstance();
		}
		catch (InstantiationException ie) {
			throw new MappingException( "Cannot instantiate custom type: " + userTypeClass.getName() );
		}
		catch (IllegalAccessException iae) {
			throw new MappingException( "IllegalAccessException trying to instantiate custom type: " + userTypeClass.getName() );
		}

	}
	
	public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister, Serializable key) 
	throws HibernateException {
		return userType.instantiate(session, persister);
	}

	public PersistentCollection wrap(SessionImplementor session, Object collection) {
		return userType.wrap(session, collection);
	}

	public Class getReturnedClass() {
		return userType.instantiate().getClass();
	}

	public Object instantiate() {
		return userType.instantiate();
	}

	public Iterator getElementsIterator(Object collection) {
		return userType.getElementsIterator(collection);
	}
	public boolean contains(Object collection, Object entity, SessionImplementor session) {
		return userType.contains(collection, entity);
	}
	public Object indexOf(Object collection, Object entity) {
		return userType.indexOf(collection, entity);
	}
	
	public Object replaceElements(Object original, Object target, Object owner, Map copyCache, SessionImplementor session)
	throws HibernateException {
		CollectionPersister cp = session.getFactory().getCollectionPersister( getRole() );
		return userType.replaceElements(original, target, cp, owner, copyCache, session);
	}
}

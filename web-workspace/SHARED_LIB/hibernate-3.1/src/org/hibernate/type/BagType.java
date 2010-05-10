//$Id: BagType.java,v 1.14 2005/08/01 16:29:32 oneovthafew Exp $
package org.hibernate.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.dom4j.Element;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.collection.PersistentBag;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.collection.PersistentElementHolder;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;

public class BagType extends CollectionType {

	public BagType(String role, String propertyRef, boolean isEmbeddedInXML) {
		super(role, propertyRef, isEmbeddedInXML);
	}

	public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister, Serializable key)
	throws HibernateException {
		if ( session.getEntityMode()==EntityMode.DOM4J ) {
			return new PersistentElementHolder(session, persister, key);
		}
		else {
			return new PersistentBag(session);
		}
	}

	public Class getReturnedClass() {
		return java.util.Collection.class;
	}

	public PersistentCollection wrap(SessionImplementor session, Object collection) {
		if ( session.getEntityMode()==EntityMode.DOM4J ) {
			return new PersistentElementHolder( session, (Element) collection );
		}
		else {
			return new PersistentBag( session, (Collection) collection );
		}
	}

	public Object instantiate() {
		//TODO: does not work for EntityMode.DOM4J yet
		return new ArrayList();
	}

}

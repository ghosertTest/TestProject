//$Id: SetType.java,v 1.15 2005/08/01 16:29:32 oneovthafew Exp $
package org.hibernate.type;

import java.io.Serializable;
import java.util.HashSet;

import org.dom4j.Element;
import org.hibernate.EntityMode;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.collection.PersistentElementHolder;
import org.hibernate.collection.PersistentSet;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;

public class SetType extends CollectionType {

	public SetType(String role, String propertyRef, boolean isEmbeddedInXML) {
		super(role, propertyRef, isEmbeddedInXML);
	}

	public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister, Serializable key) {
		if ( session.getEntityMode()==EntityMode.DOM4J ) {
			return new PersistentElementHolder(session, persister, key);
		}
		else {
			return new PersistentSet(session);
		}
	}

	public Class getReturnedClass() {
		return java.util.Set.class;
	}

	public PersistentCollection wrap(SessionImplementor session, Object collection) {
		if ( session.getEntityMode()==EntityMode.DOM4J ) {
			return new PersistentElementHolder( session, (Element) collection );
		}
		else {
			return new PersistentSet( session, (java.util.Set) collection );
		}
	}

	public Object instantiate() {
		//TODO: Might need to be a LinkedHashSet!!!!!!
		return new HashSet();
	}
	
}

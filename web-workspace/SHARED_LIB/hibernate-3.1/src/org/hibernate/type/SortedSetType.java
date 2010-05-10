//$Id: SortedSetType.java,v 1.11 2005/08/01 16:29:32 oneovthafew Exp $
package org.hibernate.type;

import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeSet;

import org.dom4j.Element;
import org.hibernate.EntityMode;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.collection.PersistentElementHolder;
import org.hibernate.collection.PersistentSortedSet;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;

public class SortedSetType extends SetType {

	private final Comparator comparator;

	public SortedSetType(String role, String propertyRef, Comparator comparator, boolean isEmbeddedInXML) {
		super(role, propertyRef, isEmbeddedInXML);
		this.comparator = comparator;
	}

	public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister, Serializable key) {
		if ( session.getEntityMode()==EntityMode.DOM4J ) {
			return new PersistentElementHolder(session, persister, key);
		}
		else {
			PersistentSortedSet set = new PersistentSortedSet(session);
			set.setComparator(comparator);
			return set;
		}
	}

	public Class getReturnedClass() {
		return java.util.SortedSet.class;
	}

	public Object instantiate() {
		return new TreeSet(comparator);
	}
	
	public PersistentCollection wrap(SessionImplementor session, Object collection) {
		if ( session.getEntityMode()==EntityMode.DOM4J ) {
			return new PersistentElementHolder( session, (Element) collection );
		}
		else {
			return new PersistentSortedSet( session, (java.util.SortedSet) collection );
		}
	}
}







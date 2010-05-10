//$Id: CollectionSecondPass.java,v 1.1 2005/09/01 23:29:25 epbernard Exp $
package org.hibernate.cfg;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.MappingException;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.IndexedCollection;
import org.hibernate.mapping.OneToMany;
import org.hibernate.mapping.Selectable;
import org.hibernate.mapping.Value;

/**
 * Collection second pass
 *
 * @author Emmanuel Bernard
 */
public abstract class CollectionSecondPass implements SecondPass {
	private static Log log = LogFactory.getLog( CollectionSecondPass.class );
	Mappings mappings;
	Collection collection;

	public CollectionSecondPass(Mappings mappings, Collection collection) {
		this.collection = collection;
		this.mappings = mappings;
	}

	public void doSecondPass(java.util.Map persistentClasses, java.util.Map inheritedMetas)
			throws MappingException {
		if ( log.isDebugEnabled() )
			log.debug( "Second pass for collection: " + collection.getRole() );

		secondPass( persistentClasses, inheritedMetas );
		collection.createAllKeys();

		if ( log.isDebugEnabled() ) {
			String msg = "Mapped collection key: " + columns( collection.getKey() );
			if ( collection.isIndexed() )
				msg += ", index: " + columns( ( (IndexedCollection) collection ).getIndex() );
			if ( collection.isOneToMany() ) {
				msg += ", one-to-many: "
					+ ( (OneToMany) collection.getElement() ).getReferencedEntityName();
			}
			else {
				msg += ", element: " + columns( collection.getElement() );
			}
			log.debug( msg );
		}
	}

	abstract public void secondPass(java.util.Map persistentClasses, java.util.Map inheritedMetas)
			throws MappingException;

	private static String columns(Value val) {
		StringBuffer columns = new StringBuffer();
		Iterator iter = val.getColumnIterator();
		while ( iter.hasNext() ) {
			columns.append( ( (Selectable) iter.next() ).getText() );
			if ( iter.hasNext() ) columns.append( ", " );
		}
		return columns.toString();
	}
}

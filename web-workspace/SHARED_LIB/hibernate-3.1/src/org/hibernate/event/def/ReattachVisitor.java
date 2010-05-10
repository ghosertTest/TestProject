//$Id: ReattachVisitor.java,v 1.5 2005/07/19 18:17:12 oneovthafew Exp $
package org.hibernate.event.def;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.action.CollectionRemoveAction;
import org.hibernate.event.EventSource;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.pretty.MessageHelper;
import org.hibernate.type.AbstractComponentType;
import org.hibernate.type.Type;

/**
 * Abstract superclass of visitors that reattach collections
 * @author Gavin King
 */
public abstract class ReattachVisitor extends ProxyVisitor {
	
	private static final Log log = LogFactory.getLog(ReattachVisitor.class);

	private final Serializable key;

	final Serializable getKey() {
		return key;
	}

	public ReattachVisitor(EventSource session, Serializable key) {
		super(session);
		this.key=key;
	}

	Object processComponent(Object component, AbstractComponentType componentType)
	throws HibernateException {

		Type[] types = componentType.getSubtypes();
		if (component==null) {
			processValues( new Object[types.length], types );
		}
		else {
			super.processComponent(component, componentType);
			//processValues( componentType.getPropertyValues( component, getSession() ), types );
		}

		return null;
	}

	/**
	 * Schedules a collection for deletion.
	 *
	 * @param role The persister representing the collection to be removed.
	 * @param id The id of the entity containing the collection to be removed.
	 * @throws HibernateException
	 */
	public void removeCollection(CollectionPersister role, Serializable id, EventSource source) 
	throws HibernateException {
		if ( log.isTraceEnabled() )
			log.trace(
					"collection dereferenced while transient " +
					MessageHelper.collectionInfoString( role, id, source.getFactory() )
			);
		/*if ( role.hasOrphanDelete() ) {
			throw new HibernateException(
				"You may not dereference a collection with cascade=\"all-delete-orphan\": " +
				MessageHelper.infoString(role, id)
			);
		}*/
		source.getActionQueue().addAction( new CollectionRemoveAction( null, role, id, false, source ) );
	}

}

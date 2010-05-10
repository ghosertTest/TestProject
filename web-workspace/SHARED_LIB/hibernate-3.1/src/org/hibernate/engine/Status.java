//$Id: Status.java,v 1.3 2005/06/15 04:04:18 oneovthafew Exp $
package org.hibernate.engine;

import java.io.Serializable;
import java.io.ObjectStreamException;
import java.io.InvalidObjectException;

/**
 * Represents the status of an entity with respect to
 * this session. These statuses are for internal
 * book-keeping only and are not intended to represent
 * any notion that is visible to the _application_.
 */
public final class Status implements Serializable {

	public static final Status MANAGED = new Status( "MANAGED" );
	public static final Status READ_ONLY = new Status( "READ_ONLY" );
	public static final Status DELETED = new Status( "DELETED" );
	public static final Status GONE = new Status( "GONE" );
	public static final Status LOADING = new Status( "LOADING" );
	public static final Status SAVING = new Status( "SAVING" );

	private String name;

	private Status(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	private Object readResolve() throws ObjectStreamException {
		if ( name.equals(MANAGED.name) ) return MANAGED;
		if ( name.equals(READ_ONLY.name) ) return READ_ONLY;
		if ( name.equals(DELETED.name) ) return DELETED;
		if ( name.equals(GONE.name) ) return GONE;
		if ( name.equals(LOADING.name) ) return LOADING;
		if ( name.equals(SAVING.name) ) return SAVING;
		throw new InvalidObjectException( "invalid Status" );
	}

}

//$Id: Versioning.java,v 1.5 2005/08/03 20:00:18 steveebersole Exp $
package org.hibernate.engine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.HibernateException;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.VersionType;

/**
 * Utility methods for managing versions and timestamps
 * @author Gavin King
 */
public final class Versioning {

	private Versioning() {}

	private static final Log log = LogFactory.getLog(Versioning.class);

	/**
	 * Increment the given version number
	 */
	public static Object increment(Object version, VersionType versionType, SessionImplementor session) {
		Object next = versionType.next( version, session );
		if ( log.isTraceEnabled() ) log.trace("Incrementing: " + version + " to " + next);
		return next;
	}

	/**
	 * Create an initial version number
	 */
	private static Object seed(VersionType versionType, SessionImplementor session) {
		Object seed = versionType.seed( session );
		if ( log.isTraceEnabled() ) log.trace("Seeding: " + seed);
		return seed;
	}

	/**
	 * Seed the given instance state snapshot with an initial version number
	 */
	public static boolean seedVersion(
	        Object[] fields,
	        int versionProperty,
	        VersionType versionType,
	        SessionImplementor session) {
		Object initialVersion = fields[versionProperty];
		if (
			initialVersion==null ||
			// This next bit is to allow for both unsaved-value="negative"
			// and for "older" behavior where version number did not get
			// seeded if it was already set in the object
			// TODO: shift it into unsaved-value strategy
			( (initialVersion instanceof Number) && ( (Number) initialVersion ).longValue()<0 )
		) {
			fields[versionProperty] = seed( versionType, session );
			return true;
		}
		else {
			if ( log.isTraceEnabled() ) log.trace( "using initial version: " + initialVersion );
			return false;
		}
	}

	private static Object getVersion(Object[] fields, int versionProperty) {
		return fields[versionProperty];
	}

	private static void setVersion(Object[] fields, Object version, int versionProperty) {
		fields[versionProperty] = version;
	}

	/**
	 * Set the version number of the given instance state snapshot
	 */
	public static void setVersion(Object[] fields, Object version, EntityPersister persister) {
		setVersion( fields, version, persister.getVersionProperty()  );
	}

	/**
	 * Get the version number of the given instance state snapshot
	 */
	public static Object getVersion(Object[] fields, EntityPersister persister) throws HibernateException {
		return persister.isVersioned() ? getVersion( fields, persister.getVersionProperty() ) : null;
	}

	/**
	 * Do we need to increment the version number, given the dirty properties?
	 */
	public static boolean isVersionIncrementRequired(
		final int[] dirtyProperties, 
		final boolean hasDirtyCollections, 
		final boolean[] propertyVersionability
	) {
		if (hasDirtyCollections) return true;
		for ( int i=0; i<dirtyProperties.length; i++) {
			if ( propertyVersionability[ dirtyProperties[i] ] ) return true;
		}
	    return false;
	}

	public static final int OPTIMISTIC_LOCK_NONE = -1;
	public static final int OPTIMISTIC_LOCK_ALL = 2;
	public static final int OPTIMISTIC_LOCK_DIRTY = 1;
	public static final int OPTIMISTIC_LOCK_VERSION = 0;

}







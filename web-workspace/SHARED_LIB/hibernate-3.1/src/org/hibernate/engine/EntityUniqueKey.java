//$Id: EntityUniqueKey.java,v 1.9 2005/11/14 13:52:57 steveebersole Exp $
package org.hibernate.engine;

import org.hibernate.EntityMode;
import org.hibernate.pretty.MessageHelper;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * Used to uniquely key an entity instance in relation to a particular session
 * by some unique property reference, as opposed to identifier.
 * <p/>
 * Uniqueing information consists of the entity-name, the referenced
 * property name, and the referenced property value.
 *
 * @see EntityKey
 * @author Gavin King
 */
public class EntityUniqueKey implements Serializable {
	private final String uniqueKeyName;
	private final String entityName;
	private final Object key;
	private final Type keyType;
	private final EntityMode entityMode;
	private final int hashCode;

	public EntityUniqueKey(
			final String entityName,
	        final String uniqueKeyName,
	        final Object semiResolvedKey,
	        final Type keyType,
	        final EntityMode entityMode,
	        final SessionFactoryImplementor factory
	) {
		this.uniqueKeyName = uniqueKeyName;
		this.entityName = entityName;
		this.key = semiResolvedKey;
		this.keyType = keyType.getSemiResolvedType(factory);
		this.entityMode = entityMode;
		this.hashCode = getHashCode(factory);
	}

	public String getEntityName() {
		return entityName;
	}

	public Object getKey() {
		return key;
	}

	public String getUniqueKeyName() {
		return uniqueKeyName;
	}

	public int getHashCode(SessionFactoryImplementor factory) {
		int result = 17;
		result = 37 * result + entityName.hashCode();
		result = 37 * result + uniqueKeyName.hashCode();
		result = 37 * result + keyType.getHashCode(key, entityMode, factory);
		return result;
	}

	public int hashCode() {
		return hashCode;
	}

	public boolean equals(Object other) {
		EntityUniqueKey that = (EntityUniqueKey) other;
		return that.entityName.equals(entityName) &&
		       that.uniqueKeyName.equals(uniqueKeyName) &&
		       keyType.isEqual(that.key, key, entityMode);
	}

	public String toString() {
		return "EntityUniqueKey" + MessageHelper.infoString(entityName, uniqueKeyName, key);
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		// The unique property value represented here may or may not be
		// serializable, so we do an explicit check here in order to generate
		// a better error message
		if ( key != null && ! Serializable.class.isAssignableFrom( key.getClass() ) ) {
			throw new IllegalStateException(
					"Cannot serialize an EntityUniqueKey which represents a non " +
					"serializable property value [" + entityName + "." + uniqueKeyName + "]"
			);
		}
		oos.defaultWriteObject();
	}
}

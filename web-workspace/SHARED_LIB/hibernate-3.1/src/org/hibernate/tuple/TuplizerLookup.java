//$Id: TuplizerLookup.java,v 1.9 2005/07/11 21:47:03 steveebersole Exp $
package org.hibernate.tuple;

import java.io.Serializable;

import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.util.ReflectHelper;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.PersistentClass;

/**
 * Stores references to the tuplizers available for a
 * "tuplizable thing" (i.e., an entity or component).
 *
 * @author Gavin King
 */
public class TuplizerLookup implements Serializable {

	private static final Class[] ENTITY_TUP_CTOR_SIG = new Class[] { EntityMetamodel.class, PersistentClass.class };
	private static final Class[] COMPONENT_TUP_CTOR_SIG = new Class[] { Component.class };

	private final Tuplizer pojoTuplizer;
	private final Tuplizer dynamicMapTuplizer;
	private final Tuplizer dom4jTuplizer;

	/**
	 * TuplizerLookup constructor.
	 *
	 * @param pojoTuplizer The POJO-based tuplizer.
	 * @param dynamicMapTuplizer The java.util.Map-based tuplizer.
	 * @param dom4jTuplizer The org.dom4j.Element-based tuplizer.
	 */
	private TuplizerLookup(Tuplizer pojoTuplizer, Tuplizer dynamicMapTuplizer, Tuplizer dom4jTuplizer) {
		this.pojoTuplizer = pojoTuplizer;
		this.dynamicMapTuplizer = dynamicMapTuplizer;
		this.dom4jTuplizer = dom4jTuplizer;
	}

	/**
	 * Generate a TuplizerLookup based on the given entity mapping and metamodel
	 * definitions.
	 *
	 * @param mappedEntity The entity mapping definition.
	 * @param em The entity metamodel definition.
	 * @return A TuplizerLookup containing the appropriate Tuplizers.
	 */
	public static TuplizerLookup create(PersistentClass mappedEntity, EntityMetamodel em) {
		// Build the dynamic-map tuplizer...
		Tuplizer dynamicMapTuplizer = null;
		String tuplizerImpl = mappedEntity.getTuplizerImplClassName( EntityMode.MAP );
		if ( tuplizerImpl == null ) {
			dynamicMapTuplizer = new DynamicMapEntityTuplizer( em, mappedEntity );
		}
		else {
			dynamicMapTuplizer = buildEntityTuplizer( tuplizerImpl, mappedEntity, em );
		}

		// then the pojo tuplizer, using the dynamic-map tuplizer if no pojo representation is available
		Tuplizer pojoTuplizer = null;
		if ( mappedEntity.hasPojoRepresentation() ) {
			tuplizerImpl = mappedEntity.getTuplizerImplClassName( EntityMode.POJO );
			if ( tuplizerImpl == null ) {
				pojoTuplizer = new PojoEntityTuplizer( em, mappedEntity );
			}
			else {
				pojoTuplizer = buildEntityTuplizer( tuplizerImpl, mappedEntity, em );
			}
		}
		else {
			pojoTuplizer = dynamicMapTuplizer;
		}

		// then dom4j tuplizer, if dom4j representation is available
		Tuplizer dom4jTuplizer = null;
		if ( mappedEntity.hasDom4jRepresentation() ) {
			tuplizerImpl = mappedEntity.getTuplizerImplClassName( EntityMode.DOM4J );
			if ( tuplizerImpl == null ) {
				dom4jTuplizer = new Dom4jEntityTuplizer( em, mappedEntity );
			}
			else {
				dom4jTuplizer = buildEntityTuplizer( tuplizerImpl, mappedEntity, em );
			}
		}
		else {
			dom4jTuplizer = null;
		}

		return new TuplizerLookup( pojoTuplizer, dynamicMapTuplizer, dom4jTuplizer );
	}

	private static EntityTuplizer buildEntityTuplizer(String className, PersistentClass pc, EntityMetamodel em) {
		try {
			Class implClass = ReflectHelper.classForName( className );
			return ( EntityTuplizer ) implClass.getConstructor( ENTITY_TUP_CTOR_SIG ).newInstance( new Object[] { em, pc } );
		}
		catch( Throwable t ) {
			throw new HibernateException( "Could not build tuplizer [" + className + "]", t );
		}
	}

	/**
	 * Generate a TuplizerLookup based on the given component mapping definition.
	 *
	 * @param component The component mapping definition.
	 * @return A TuplizerLookup containing the appropriate Tuplizers.
	 */
	public static TuplizerLookup create(Component component) {
		PersistentClass owner = component.getOwner();

		// Build the dynamic-map tuplizer...
		Tuplizer dynamicMapTuplizer = null;
		String tuplizerImpl = component.getTuplizerImplClassName( EntityMode.MAP );
		if ( tuplizerImpl == null ) {
			dynamicMapTuplizer = new DynamicMapComponentTuplizer( component );
		}
		else {
			dynamicMapTuplizer = buildComponentTuplizer( tuplizerImpl, component );
		}

		// then the pojo tuplizer, using the dynamic-map tuplizer if no pojo representation is available
		Tuplizer pojoTuplizer = null;
		if ( owner.hasPojoRepresentation() && component.hasPojoRepresentation() ) {
			tuplizerImpl = component.getTuplizerImplClassName( EntityMode.POJO );
			if ( tuplizerImpl == null ) {
				pojoTuplizer = new PojoComponentTuplizer( component );
			}
			else {
				pojoTuplizer = buildComponentTuplizer( tuplizerImpl, component );
			}
		}
		else {
			pojoTuplizer = dynamicMapTuplizer;
		}

		// then dom4j tuplizer, if dom4j representation is available
		Tuplizer dom4jTuplizer = null;
		if ( owner.hasDom4jRepresentation() ) {
			tuplizerImpl = component.getTuplizerImplClassName( EntityMode.DOM4J );
			if ( tuplizerImpl == null ) {
				dom4jTuplizer = new Dom4jComponentTuplizer( component );
			}
			else {
				dom4jTuplizer = buildComponentTuplizer( tuplizerImpl, component );
			}
		}
		else {
			dom4jTuplizer = null;
		}

		return new TuplizerLookup( pojoTuplizer, dynamicMapTuplizer, dom4jTuplizer );
	}

	private static ComponentTuplizer buildComponentTuplizer(String tuplizerImpl, Component component) {
		try {
			Class implClass = ReflectHelper.classForName( tuplizerImpl );
			return ( ComponentTuplizer ) implClass.getConstructor( COMPONENT_TUP_CTOR_SIG ).newInstance( new Object[] { component } );
		}
		catch( Throwable t ) {
			throw new HibernateException( "Could not build tuplizer [" + tuplizerImpl + "]", t );
		}

	}

	/**
	 * Given a supposed instance of an entity/component, guess its entity mode.
	 *
	 * @param object The supposed instance of the entity/component.
	 * @return The guessed entity mode.
	 */
	public EntityMode guessEntityMode(Object object) {
		if ( pojoTuplizer != null && pojoTuplizer.isInstance(object) ) {
			return EntityMode.POJO;
		}

		if ( dom4jTuplizer != null && dom4jTuplizer.isInstance(object) ) {
			return EntityMode.DOM4J;
		}

		if ( dynamicMapTuplizer != null && dynamicMapTuplizer.isInstance(object) ) {
			return EntityMode.MAP;
		}

		return null;   // or should we throw an exception?
	}

	/**
	 * Locate the contained tuplizer responsible for the given entity-mode.  If
	 * no such tuplizer is defined on this lookup, then return null.
	 *
	 * @param entityMode The entity-mode for which the client wants a tuplizer.
	 * @return The tuplizer, or null if not found.
	 */
	public Tuplizer getTuplizerOrNull(EntityMode entityMode) {
		Tuplizer rtn = null;
		if ( EntityMode.POJO == entityMode ) {
			rtn = pojoTuplizer;
		}
		else if ( EntityMode.DOM4J == entityMode ) {
			rtn = dom4jTuplizer;
		}
		else if ( EntityMode.MAP == entityMode ) {
			rtn = dynamicMapTuplizer;
		}

		return rtn;
	}

	/**
	 * Locate the contained tuplizer responsible for the given entity-mode.  If
	 * no such tuplizer is defined on this lookup, then an exception is thrown.
	 *
	 * @param entityMode The entity-mode for which the client wants a tuplizer.
	 * @return The tuplizer.
	 * @throws HibernateException Unable to locate the requested tuplizer.
	 */
	public Tuplizer getTuplizer(EntityMode entityMode) {
		Tuplizer rtn = getTuplizerOrNull( entityMode );

		if ( rtn == null ) {
			throw new HibernateException( "No tuplizer found for entity-mode [" + entityMode + "]");
		}

		return rtn;
	}

}

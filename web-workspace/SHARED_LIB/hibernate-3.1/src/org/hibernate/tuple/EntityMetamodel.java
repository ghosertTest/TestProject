// $Id: EntityMetamodel.java,v 1.21 2005/11/29 14:34:11 steveebersole Exp $
package org.hibernate.tuple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.transform.impl.InterceptFieldEnabled;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.CascadeStyle;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.Versioning;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.type.AbstractComponentType;
import org.hibernate.type.AssociationType;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;
import org.hibernate.type.TypeFactory;
import org.hibernate.util.ArrayHelper;
import org.hibernate.util.ReflectHelper;

/**
 * Centralizes metamodel information about an entity.
 *
 * @author Steve Ebersole
 */
public class EntityMetamodel implements Serializable {
	
	private static final Log log = LogFactory.getLog(EntityMetamodel.class);

	private static final int NO_VERSION_INDX = -66;

	private final SessionFactoryImplementor sessionFactory;

	private final String name;
	private final String rootName;
	private final EntityType entityType;

	private final IdentifierProperty identifierProperty;
	private final boolean versioned;

	private final int propertySpan;
	private final int versionPropertyIndex;
	private final StandardProperty[] properties;
	// temporary ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private final String[] propertyNames;
	private final Type[] propertyTypes;
	private final boolean[] propertyLaziness;
	private final boolean[] propertyUpdateability;
	private final boolean[] nonlazyPropertyUpdateability;
	private final boolean[] propertyCheckability;
	private final boolean[] propertyInsertability;
	private final boolean[] propertyInsertGeneration;
	private final boolean[] propertyUpdateGeneration;
	private final boolean[] propertyNullability;
	private final boolean[] propertyVersionability;
	private final CascadeStyle[] cascadeStyles;
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private final Map propertyIndexes = new HashMap();
	private final boolean hasCollections;
	private final boolean hasMutableProperties; 
	private final boolean hasLazyProperties;

	private final int[] naturalIdPropertyNumbers;

	private final boolean lazy;
	private final boolean hasCascades;
	private final boolean mutable;
	private final boolean isAbstract;
	private final boolean selectBeforeUpdate;
	private final boolean dynamicUpdate;
	private final boolean dynamicInsert;
	private final int optimisticLockMode;

	private final boolean polymorphic;
	private final String superclass;  // superclass entity-name
	private final boolean explicitPolymorphism;
	private final boolean inherited;
	private final boolean hasSubclasses;
	private final Set subclassEntityNames = new HashSet();
	
	private final TuplizerLookup tuplizers;
	
	public EntityTuplizer getTuplizer(EntityMode entityMode) {
		return (EntityTuplizer) tuplizers.getTuplizer(entityMode);
	}

	public EntityTuplizer getTuplizerOrNull(EntityMode entityMode) {
		return (EntityTuplizer) tuplizers.getTuplizerOrNull(entityMode);
	}
	
	public EntityMode guessEntityMode(Object object) {
		return tuplizers.guessEntityMode(object);
	}

	public EntityMetamodel(PersistentClass persistentClass, SessionFactoryImplementor sessionFactory) {
		this.sessionFactory = sessionFactory;

		name = persistentClass.getEntityName();
		rootName = persistentClass.getRootClass().getEntityName();
		entityType = TypeFactory.manyToOne( name );

		identifierProperty = PropertyFactory.buildIdentifierProperty(
		        persistentClass,
		        sessionFactory.getIdentifierGenerator( rootName )
			);

		versioned = persistentClass.isVersioned();

		boolean lazyAvailable = persistentClass.hasPojoRepresentation() &&
		        InterceptFieldEnabled.class.isAssignableFrom( persistentClass.getMappedClass() );
		boolean hasLazy = false;

		propertySpan = persistentClass.getPropertyClosureSpan();
		properties = new StandardProperty[propertySpan];
		List naturalIdNumbers = new ArrayList();
		// temporary ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		propertyNames = new String[propertySpan];
		propertyTypes = new Type[propertySpan];
		propertyUpdateability = new boolean[propertySpan];
		propertyInsertability = new boolean[propertySpan];
		propertyInsertGeneration = new boolean[propertySpan];
		propertyUpdateGeneration = new boolean[propertySpan];
		nonlazyPropertyUpdateability = new boolean[propertySpan];
		propertyCheckability = new boolean[propertySpan];
		propertyNullability = new boolean[propertySpan];
		propertyVersionability = new boolean[propertySpan];
		propertyLaziness = new boolean[propertySpan];
		cascadeStyles = new CascadeStyle[propertySpan];
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


		Iterator iter = persistentClass.getPropertyClosureIterator();
		int i = 0;
		int tempVersionProperty = NO_VERSION_INDX;
		boolean foundCascade = false;
		boolean foundCollection = false;
		boolean foundMutable = false;
		
		while ( iter.hasNext() ) {
			Property prop = ( Property ) iter.next();

			if ( prop == persistentClass.getVersion() ) {
				tempVersionProperty = i;
				properties[i] = PropertyFactory.buildVersionProperty( prop, lazyAvailable );
			}
			else {
				properties[i] = PropertyFactory.buildStandardProperty( prop, lazyAvailable );
			}
			
			if ( prop.isNaturalIdentifier() ) {
				naturalIdNumbers.add( new Integer(i) );
			}

			// temporary ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			boolean lazy = prop.isLazy() && lazyAvailable;
			if ( lazy ) hasLazy = true;
			propertyLaziness[i] = lazy;

			propertyNames[i] = properties[i].getName();
			propertyTypes[i] = properties[i].getType();
			propertyNullability[i] = properties[i].isNullable();
			propertyUpdateability[i] = properties[i].isUpdateable();
			propertyInsertability[i] = properties[i].isInsertable();
			propertyInsertGeneration[i] = properties[i].isInsertGenerated();
			propertyUpdateGeneration[i] = properties[i].isUpdateGenerated();
			propertyVersionability[i] = properties[i].isVersionable();
			nonlazyPropertyUpdateability[i] = properties[i].isUpdateable() && !lazy;
			propertyCheckability[i] = propertyUpdateability[i] || 
					( propertyTypes[i].isAssociationType() && ( (AssociationType) propertyTypes[i] ).isAlwaysDirtyChecked() );

			cascadeStyles[i] = properties[i].getCascadeStyle();
			// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

			if ( properties[i].isLazy() ) {
				hasLazy = true;
			}

			if ( properties[i].getCascadeStyle() != CascadeStyle.NONE ) {
				foundCascade = true;
			}

			if ( indicatesCollection( properties[i].getType() ) ) {
				foundCollection = true;
			}
			
			if ( propertyTypes[i].isMutable() && propertyCheckability[i] ) {
				foundMutable = true;
			}

			mapPropertyToIndex(prop, i);
			i++;
		}
		
		if (naturalIdNumbers.size()==0) {
			naturalIdPropertyNumbers = null;
		}
		else {
			naturalIdPropertyNumbers = ArrayHelper.toIntArray(naturalIdNumbers);
		}

		hasCascades = foundCascade;
		versionPropertyIndex = tempVersionProperty;
		hasLazyProperties = hasLazy;
		if (hasLazyProperties) log.info("lazy property fetching available for: " + name);

		lazy = persistentClass.isLazy() && (
				// TODO: this disables laziness even in non-pojo entity modes:
				!persistentClass.hasPojoRepresentation() ||
				!ReflectHelper.isFinalClass( persistentClass.getProxyInterface() )
			);
		mutable = persistentClass.isMutable();
		if ( persistentClass.isAbstract() == null ) {
			// legacy behavior (with no abstract attribute specified)
			isAbstract = persistentClass.hasPojoRepresentation() &&
			             ReflectHelper.isAbstractClass( persistentClass.getMappedClass() );
		}
		else {
			isAbstract = persistentClass.isAbstract().booleanValue();
			if ( !isAbstract && persistentClass.hasPojoRepresentation() &&
			     ReflectHelper.isAbstractClass( persistentClass.getMappedClass() ) ) {
				log.warn( "entity [" + name + "] is abstract-class/interface explicitly mapped as non-abstract; be sure to supply entity-names" );
			}
		}
		selectBeforeUpdate = persistentClass.hasSelectBeforeUpdate();
		dynamicUpdate = persistentClass.useDynamicUpdate();
		dynamicInsert = persistentClass.useDynamicInsert();

		polymorphic = persistentClass.isPolymorphic();
		explicitPolymorphism = persistentClass.isExplicitPolymorphism();
		inherited = persistentClass.isInherited();
		superclass = inherited ?
				persistentClass.getSuperclass().getEntityName() :
				null;
		hasSubclasses = persistentClass.hasSubclasses();

		optimisticLockMode = persistentClass.getOptimisticLockMode();
		if ( optimisticLockMode > Versioning.OPTIMISTIC_LOCK_VERSION && !dynamicUpdate ) {
			throw new MappingException( "optimistic-lock setting requires dynamic-update=\"true\": " + name );
		}

		hasCollections = foundCollection;
		hasMutableProperties = foundMutable;

		tuplizers = TuplizerLookup.create(persistentClass, this);
		
		iter = persistentClass.getSubclassIterator();
		while ( iter.hasNext() ) {
			subclassEntityNames.add( ( (PersistentClass) iter.next() ).getEntityName() );
		}
		subclassEntityNames.add( name );

	}

	private void mapPropertyToIndex(Property prop, int i) {
		propertyIndexes.put( prop.getName(), new Integer(i) );
		if ( prop.getValue() instanceof Component ) {
			Iterator iter = ( (Component) prop.getValue() ).getPropertyIterator();
			while ( iter.hasNext() ) {
				Property subprop = (Property) iter.next();
				propertyIndexes.put( 
						prop.getName() + '.' + subprop.getName(), 
						new Integer(i) 
					);
			}
		}
	}
	
	public int[] getNaturalIdentifierProperties() {
		return naturalIdPropertyNumbers;
	}
	
	public boolean hasNaturalIdentifier() {
		return naturalIdPropertyNumbers!=null;
	}
	
	public Set getSubclassEntityNames() {
		return subclassEntityNames;
	}

	private boolean indicatesCollection(Type type) {
		if ( type.isCollectionType() ) {
			return true;
		}
		else if ( type.isComponentType() ) {
			Type[] subtypes = ( ( AbstractComponentType ) type ).getSubtypes();
			for ( int i = 0; i < subtypes.length; i++ ) {
				if ( indicatesCollection( subtypes[i] ) ) {
					return true;
				}
			}
		}
		return false;
	}

	public SessionFactoryImplementor getSessionFactory() {
		return sessionFactory;
	}

	public String getName() {
		return name;
	}

	public String getRootName() {
		return rootName;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public IdentifierProperty getIdentifierProperty() {
		return identifierProperty;
	}

	public int getPropertySpan() {
		return propertySpan;
	}

	public int getVersionPropertyIndex() {
		return versionPropertyIndex;
	}

	public VersionProperty getVersionProperty() {
		if ( NO_VERSION_INDX == versionPropertyIndex ) {
			return null;
		}
		else {
			return ( VersionProperty ) properties[ versionPropertyIndex ];
		}
	}

	public StandardProperty[] getProperties() {
		return properties;
	}

	public int getPropertyIndex(String propertyName) {
		Integer index = getPropertyIndexOrNull(propertyName);
		if ( index == null ) {
			throw new HibernateException("Unable to resolve property: " + propertyName);
		}
		return index.intValue();
	}
	
	public Integer getPropertyIndexOrNull(String propertyName) {
		return (Integer) propertyIndexes.get( propertyName );
	}

	public boolean hasCollections() {
		return hasCollections;
	}
	
	public boolean hasMutableProperties() {
		return hasMutableProperties;
	}

	public boolean hasLazyProperties() {
		return hasLazyProperties;
	}

	public boolean hasCascades() {
		return hasCascades;
	}

	public boolean isMutable() {
		return mutable;
	}

	public boolean isSelectBeforeUpdate() {
		return selectBeforeUpdate;
	}

	public boolean isDynamicUpdate() {
		return dynamicUpdate;
	}

	public boolean isDynamicInsert() {
		return dynamicInsert;
	}

	public int getOptimisticLockMode() {
		return optimisticLockMode;
	}

	public boolean isPolymorphic() {
		return polymorphic;
	}

	public String getSuperclass() {
		return superclass;
	}

	public boolean isExplicitPolymorphism() {
		return explicitPolymorphism;
	}

	public boolean isInherited() {
		return inherited;
	}

	public boolean hasSubclasses() {
		return hasSubclasses;
	}

	public boolean isLazy() {
		return lazy;
	}

	public boolean isVersioned() {
		return versioned;
	}

	public boolean isAbstract() {
		return isAbstract;
	}
	
	public String toString() {
		return "EntityMetamodel(" + name + ':' + ArrayHelper.toString(properties) + ')';
	}
	
	// temporary ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String[] getPropertyNames() {
		return propertyNames;
	}

	public Type[] getPropertyTypes() {
		return propertyTypes;
	}

	public boolean[] getPropertyLaziness() {
		return propertyLaziness;
	}

	public boolean[] getPropertyUpdateability() {
		return propertyUpdateability;
	}

	public boolean[] getPropertyCheckability() {
		return propertyCheckability;
	}

	public boolean[] getNonlazyPropertyUpdateability() {
		return nonlazyPropertyUpdateability;
	}

	public boolean[] getPropertyInsertability() {
		return propertyInsertability;
	}

	public boolean[] getPropertyInsertGeneration() {
		return propertyInsertGeneration;
	}

	public boolean[] getPropertyUpdateGeneration() {
		return propertyUpdateGeneration;
	}

	public boolean[] getPropertyNullability() {
		return propertyNullability;
	}

	public boolean[] getPropertyVersionability() {
		return propertyVersionability;
	}

	public CascadeStyle[] getCascadeStyles() {
		return cascadeStyles;
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
}

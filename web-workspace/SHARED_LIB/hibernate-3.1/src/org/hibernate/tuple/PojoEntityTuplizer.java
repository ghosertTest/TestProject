// $Id: PojoEntityTuplizer.java,v 1.3 2005/09/09 15:19:45 oneovthafew Exp $
package org.hibernate.tuple;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.beans.BulkBean;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.transform.impl.InterceptFieldEnabled;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.PropertyAccessException;
import org.hibernate.cfg.Environment;
import org.hibernate.classic.Lifecycle;
import org.hibernate.classic.Validatable;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.intercept.FieldInterceptor;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Subclass;
import org.hibernate.property.Getter;
import org.hibernate.property.Setter;
import org.hibernate.proxy.CGLIBProxyFactory;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.ProxyFactory;
import org.hibernate.type.AbstractComponentType;
import org.hibernate.util.ReflectHelper;

/**
 * POJO-based implementation of an EntityTuplizer.
 *
 * @author Steve Ebersole
 */
public class PojoEntityTuplizer extends AbstractEntityTuplizer {

	static final Log log = LogFactory.getLog( PojoEntityTuplizer.class );

	private final Class mappedClass;
	private final Class proxyInterface;
	private final boolean lifecycleImplementor;
	private final boolean validatableImplementor;
	private final Set lazyPropertyNames = new HashSet();
	private BulkBean optimizer;
	private FastClass fastClass;

	public PojoEntityTuplizer(EntityMetamodel entityMetamodel, PersistentClass mappedEntity) {
		super( entityMetamodel, mappedEntity );
		this.mappedClass = mappedEntity.getMappedClass();
		this.proxyInterface = mappedEntity.getProxyInterface();
		this.lifecycleImplementor = Lifecycle.class.isAssignableFrom( mappedClass );
		this.validatableImplementor = Validatable.class.isAssignableFrom( mappedClass );

		Iterator iter = mappedEntity.getPropertyClosureIterator();
		while ( iter.hasNext() ) {
			Property property = (Property) iter.next();
			if ( property.isLazy() ) {
				lazyPropertyNames.add( property.getName() );
			}
		}

		String[] getterNames = new String[propertySpan];
		String[] setterNames = new String[propertySpan];
		Class[] propTypes = new Class[propertySpan];
		for ( int i = 0; i < propertySpan; i++ ) {
			getterNames[i] = getters[i].getMethodName();
			setterNames[i] = setters[i].getMethodName();
			propTypes[i] = getters[i].getReturnType();
		}

		if ( hasCustomAccessors || !Environment.useReflectionOptimizer() ) {
			fastClass = null;
			optimizer = null;
		}
		else {
			fastClass = ReflectHelper.getFastClass( mappedClass );
			optimizer = ReflectHelper.getBulkBean( mappedClass, getterNames, setterNames, propTypes, fastClass );
			if (optimizer==null) fastClass = null;
		}
	
	}

	protected ProxyFactory buildProxyFactory(PersistentClass persistentClass, Getter idGetter, Setter idSetter) {
		// determine the id getter and setter methods from the proxy interface (if any)
        // determine all interfaces needed by the resulting proxy
		HashSet proxyInterfaces = new HashSet();
		proxyInterfaces.add( HibernateProxy.class );
		
		Class mappedClass = persistentClass.getMappedClass();
		Class proxyInterface = persistentClass.getProxyInterface();

		if ( proxyInterface!=null && !mappedClass.equals( proxyInterface ) ) {
			if ( !proxyInterface.isInterface() ) {
				throw new MappingException(
				        "proxy must be either an interface, or the class itself: " + 
				        getEntityName()
					);
			}
			proxyInterfaces.add( proxyInterface );
		}

		if ( mappedClass.isInterface() ) {
			proxyInterfaces.add( mappedClass );
		}

		Iterator iter = persistentClass.getSubclassIterator();
		while ( iter.hasNext() ) {
			Subclass subclass = ( Subclass ) iter.next();
			Class subclassProxy = subclass.getProxyInterface();
			Class subclassClass = subclass.getMappedClass();
			if ( subclassProxy!=null && !subclassClass.equals( subclassProxy ) ) {
				if ( !proxyInterface.isInterface() ) {
					throw new MappingException(
					        "proxy must be either an interface, or the class itself: " + 
					        subclass.getEntityName()
					);
				}
				proxyInterfaces.add( subclassProxy );
			}
		}

		Iterator properties = persistentClass.getPropertyIterator();
		Class clazz = persistentClass.getMappedClass();
		while ( properties.hasNext() ) {
			Property property = (Property) properties.next();
			Method method = property.getGetter(clazz).getMethod();
			if ( method != null && Modifier.isFinal( method.getModifiers() ) ) {
				log.error(
						"Getters of lazy classes cannot be final: " + persistentClass.getEntityName() + 
						"." + property.getName() 
					);
			}
			method = property.getSetter(clazz).getMethod();
            if ( method != null && Modifier.isFinal( method.getModifiers() ) ) {
				log.error(
						"Setters of lazy classes cannot be final: " + persistentClass.getEntityName() + 
						"." + property.getName() 
					);
			}
		}

		Method idGetterMethod = idGetter==null ? null : idGetter.getMethod();
		Method idSetterMethod = idSetter==null ? null : idSetter.getMethod();

		Method proxyGetIdentifierMethod = idGetterMethod==null || proxyInterface==null ? 
				null :
		        ReflectHelper.getMethod(proxyInterface, idGetterMethod);
		Method proxySetIdentifierMethod = idSetterMethod==null || proxyInterface==null  ? 
				null :
		        ReflectHelper.getMethod(proxyInterface, idSetterMethod);

		ProxyFactory pf = new CGLIBProxyFactory();
		try {
			pf.postInstantiate(
					getEntityName(),
					mappedClass,
					proxyInterfaces,
					proxyGetIdentifierMethod,
					proxySetIdentifierMethod,
					persistentClass.hasEmbeddedIdentifier() ?
			                (AbstractComponentType) persistentClass.getIdentifier().getType() :
			                null
			);
		}
		catch ( HibernateException he ) {
			log.warn( "could not create proxy factory for:" + getEntityName(), he );
			pf = null;
		}
		return pf;
	}

	protected Instantiator buildInstantiator(PersistentClass persistentClass) {
		return new PojoInstantiator( persistentClass, fastClass );
	}

	public void setPropertyValues(Object entity, Object[] values) throws HibernateException {
		if ( !getEntityMetamodel().hasLazyProperties() && optimizer != null ) {
			setPropertyValuesWithOptimizer( entity, values );
		}
		else {
			super.setPropertyValues( entity, values );
		}
	}

	public Object[] getPropertyValues(Object entity) throws HibernateException {
		if ( shouldGetAllProperties( entity ) && optimizer != null ) {
			return getPropertyValuesWithOptimizer( entity );
		}
		else {
			return super.getPropertyValues( entity );
		}
	}

	public Object[] getPropertyValuesToInsert(Object entity, Map mergeMap, SessionImplementor session) throws HibernateException {
		if ( shouldGetAllProperties( entity ) && optimizer != null ) {
			return getPropertyValuesWithOptimizer( entity );
		}
		else {
			return super.getPropertyValuesToInsert( entity, mergeMap, session );
		}
	}

	protected void setPropertyValuesWithOptimizer(Object object, Object[] values) {
		try {
			optimizer.setPropertyValues( object, values );
		}
		catch ( Throwable t ) {
			throw new PropertyAccessException( t,
					ReflectHelper.PROPERTY_SET_EXCEPTION,
					true,
					mappedClass,
					ReflectHelper.getPropertyName( t, optimizer ) 
				);
		}
	}

	protected Object[] getPropertyValuesWithOptimizer(Object object) {
		try {
			return optimizer.getPropertyValues( object );
		}
		catch ( Throwable t ) {
			throw new PropertyAccessException( t,
					ReflectHelper.PROPERTY_GET_EXCEPTION,
					false,
					mappedClass,
					ReflectHelper.getPropertyName( t, optimizer ) 
				);
		}
	}

	public EntityMode getEntityMode() {
		return EntityMode.POJO;
	}

	public Class getMappedClass() {
		return mappedClass;
	}

	public boolean isLifecycleImplementor() {
		return lifecycleImplementor;
	}

	public boolean isValidatableImplementor() {
		return validatableImplementor;
	}

	protected Getter buildPropertyGetter(Property mappedProperty, PersistentClass mappedEntity) {
		return mappedProperty.getGetter( mappedEntity.getMappedClass() );
	}

	protected Setter buildPropertySetter(Property mappedProperty, PersistentClass mappedEntity) {
		return mappedProperty.getSetter( mappedEntity.getMappedClass() );
	}

	public Class getConcreteProxyClass() {
		return proxyInterface;
	}

    //TODO: need to make the majority of this functionality into a top-level support class for custom impl support

	public void afterInitialize(Object entity, boolean lazyPropertiesAreUnfetched, SessionImplementor session) {
		//if ( lazyPropertiesAreUnfetched && getEntityMetamodel().hasLazyProperties() ) {
		if ( isInstrumented() ) {
			Set lazyProps = lazyPropertiesAreUnfetched && getEntityMetamodel().hasLazyProperties() ?
					lazyPropertyNames : null;
			//TODO: if we support multiple fetch groups, we would need
			//      to clone the set of lazy properties!
			FieldInterceptor.initFieldInterceptor( entity, getEntityName(), session, lazyProps );
		}
	}

	public boolean hasUninitializedLazyProperties(Object entity) {
		if ( getEntityMetamodel().hasLazyProperties() ) {
			FieldInterceptor callback = FieldInterceptor.getFieldInterceptor(entity);
			return callback != null && !callback.isInitialized();
		}
		else {
			return false;
		}
	}

	public boolean isInstrumented() {
		return InterceptFieldEnabled.class.isAssignableFrom( getMappedClass() );
	}

}

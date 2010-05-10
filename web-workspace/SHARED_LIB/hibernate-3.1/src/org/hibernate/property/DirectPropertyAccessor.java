//$Id: DirectPropertyAccessor.java,v 1.9 2005/07/16 22:20:47 oneovthafew Exp $
package org.hibernate.property;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.PropertyAccessException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.util.ReflectHelper;

/**
 * Accesses fields directly.
 * @author Gavin King
 */
public class DirectPropertyAccessor implements PropertyAccessor {

	public static final class DirectGetter implements Getter {
		private final transient Field field;
		private final Class clazz;
		private final String name;
		DirectGetter(Field field, Class clazz, String name) {
			this.field = field;
			this.clazz = clazz;
			this.name = name;
		}
		public Object get(Object target) throws HibernateException {
			try {
				return field.get(target);
			}
			catch (Exception e) {
				throw new PropertyAccessException(e, "could not get a field value by reflection", false, clazz, name);
			}
		}

		public Object getForInsert(Object target, Map mergeMap, SessionImplementor session) {
			return get( target );
		}

		public Method getMethod() {
			return null;
		}
		public String getMethodName() {
			return null;
		}
		public Class getReturnType() {
			return field.getType();
		}

		Object readResolve() {
			return new DirectGetter( getField(clazz, name), clazz, name );
		}
		
		public String toString() {
			return "DirectGetter(" + clazz.getName() + '.' + name + ')';
		}
	}

	public static final class DirectSetter implements Setter {
		private final transient Field field;
		private final Class clazz;
		private final String name;
		DirectSetter(Field field, Class clazz, String name) {
			this.field = field;
			this.clazz = clazz;
			this.name = name;
		}
		public Method getMethod() {
			return null;
		}
		public String getMethodName() {
			return null;
		}
		public void set(Object target, Object value, SessionFactoryImplementor factory) throws HibernateException {
			try {
				field.set(target, value);
			}
			catch (Exception e) {
				throw new PropertyAccessException(e, "could not set a field value by reflection", true, clazz, name);
			}
		}

		public String toString() {
			return "DirectSetter(" + clazz.getName() + '.' + name + ')';
		}
		
		Object readResolve() {
			return new DirectSetter( getField(clazz, name), clazz, name );
		}
	}

	private static Field getField(Class clazz, String name) throws PropertyNotFoundException {
		if ( clazz==null || clazz==Object.class ) {
			throw new PropertyNotFoundException("field not found: " + name); // TODO: we should report what class we started searching for the property - right now it will always end on Object.class.
		}
		Field field;
		try {
			field = clazz.getDeclaredField(name);
		}
		catch (NoSuchFieldException nsfe) {
			field = getField( clazz.getSuperclass(), name );
		}
		if ( !ReflectHelper.isPublic(clazz, field) ) field.setAccessible(true);
		return field;
	}

	public Getter getGetter(Class theClass, String propertyName)
		throws PropertyNotFoundException {
		return new DirectGetter( getField(theClass, propertyName), theClass, propertyName );
	}

	public Setter getSetter(Class theClass, String propertyName)
		throws PropertyNotFoundException {
		return new DirectSetter( getField(theClass, propertyName), theClass, propertyName );
	}

}

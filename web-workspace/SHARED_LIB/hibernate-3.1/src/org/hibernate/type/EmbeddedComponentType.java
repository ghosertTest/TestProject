//$Id: EmbeddedComponentType.java,v 1.9 2005/07/29 05:36:14 oneovthafew Exp $
package org.hibernate.type;

import java.lang.reflect.Method;

import org.hibernate.EntityMode;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.CascadeStyle;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.tuple.ComponentTuplizer;
import org.hibernate.tuple.TuplizerLookup;

/**
 * @author Gavin King
 */
public class EmbeddedComponentType extends ComponentType {

	public boolean isEmbedded() {
		return true;
	}
	
	public EmbeddedComponentType(
			String[] propertyNames,
			Type[] propertyTypes,
			boolean[] nullabilities, 
			FetchMode[] joinedFetch,
			CascadeStyle[] cascade,
			boolean key,
			TuplizerLookup tuplizers)
	throws MappingException {
		super(
				propertyNames, 
				propertyTypes, 
				nullabilities, 
				joinedFetch,
				cascade,
				key,
				tuplizers
			);
	}
	
	public boolean isMethodOf(Method method) {
		return ( (ComponentTuplizer) tuplizers.getTuplizer(EntityMode.POJO) ).isMethodOf(method);
	}
	
	public Object instantiate(Object parent, SessionImplementor session)
	throws HibernateException {
		final boolean useParent = parent!=null && 
			//TODO: Yuck! This is not quite good enough, it's a quick 
			//hack around the problem of having a to-one association
			//that refers to an embedded component:
			super.getReturnedClass().isInstance(parent);
		
		return useParent ? parent : super.instantiate(parent, session);
	}
}

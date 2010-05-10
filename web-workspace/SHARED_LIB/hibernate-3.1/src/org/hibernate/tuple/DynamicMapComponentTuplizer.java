//$Id: DynamicMapComponentTuplizer.java,v 1.2 2005/07/11 17:31:50 steveebersole Exp $
package org.hibernate.tuple;

import java.util.Map;

import org.hibernate.mapping.Component;
import org.hibernate.mapping.Property;
import org.hibernate.property.Getter;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;

/**
 * @author Gavin King
 */
public class DynamicMapComponentTuplizer extends AbstractComponentTuplizer {
	
	public Class getMappedClass() {
		return Map.class;
	}
	
	protected Instantiator buildInstantiator(Component component) {
		return new DynamicMapInstantiator();
	}
	
	public DynamicMapComponentTuplizer(Component component) {
		super(component);
	}

	private PropertyAccessor buildPropertyAccessor(Property property) {
		return PropertyAccessorFactory.getDynamicMapPropertyAccessor();
	}

	protected Getter buildGetter(Component component, Property prop) {
		return buildPropertyAccessor(prop).getGetter( null, prop.getName() );
	}

	protected Setter buildSetter(Component component, Property prop) {
		return buildPropertyAccessor(prop).getSetter( null, prop.getName() );
	}

}

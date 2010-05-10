//$Id: Component.java,v 1.18 2005/07/21 01:11:51 oneovthafew Exp $
package org.hibernate.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.EntityMode;
import org.hibernate.FetchMode;
import org.hibernate.MappingException;
import org.hibernate.engine.CascadeStyle;
import org.hibernate.tuple.TuplizerLookup;
import org.hibernate.type.ComponentType;
import org.hibernate.type.EmbeddedComponentType;
import org.hibernate.type.Type;
import org.hibernate.util.JoinedIterator;
import org.hibernate.util.ReflectHelper;

/**
 * The mapping for a component, composite element,
 * composite identifier, etc.
 * @author Gavin King
 */
public class Component extends SimpleValue implements MetaAttributable {

	private ArrayList properties = new ArrayList();
	private String componentClassName;
	private boolean embedded;
	private String parentProperty;
	private PersistentClass owner;
	private boolean dynamic;
	private Map metaAttributes;
	private String nodeName;
	private boolean isKey;

	private java.util.Map tuplizerImpls;

	public int getPropertySpan() {
		return properties.size();
	}
	public Iterator getPropertyIterator() {
		return properties.iterator();
	}
	public void addProperty(Property p) {
		properties.add(p);
	}
	public void addColumn(Column column) {
		throw new UnsupportedOperationException("Cant add a column to a component");
	}
	public int getColumnSpan() {
		int n=0;
		Iterator iter = getPropertyIterator();
		while ( iter.hasNext() ) {
			Property p = (Property) iter.next();
			n+= p.getColumnSpan();
		}
		return n;
	}
	public Iterator getColumnIterator() {
		Iterator[] iters = new Iterator[ getPropertySpan() ];
		Iterator iter = getPropertyIterator();
		int i=0;
		while ( iter.hasNext() ) {
			iters[i++] = ( (Property) iter.next() ).getColumnIterator();
		}
		return new JoinedIterator(iters);
	}

	public Component(PersistentClass owner) throws MappingException {
		super( owner.getTable() );
		this.owner = owner;
	}

	public Component(Component component) throws MappingException {
		super( component.getTable() );
		this.owner = component.getOwner();
	}

	public Component(Join join) throws MappingException {
		super( join.getTable() );
		this.owner = join.getPersistentClass();
	}

	public Component(Collection collection) throws MappingException {
		super( collection.getCollectionTable() );
		this.owner = collection.getOwner();
	}

	public void setTypeByReflection(String propertyClass, String propertyName) {}

	public boolean isEmbedded() {
		return embedded;
	}

	public String getComponentClassName() {
		return componentClassName;
	}

	public Class getComponentClass() throws MappingException {
		try {
			return ReflectHelper.classForName(componentClassName);
		}
		catch (ClassNotFoundException cnfe) {
			throw new MappingException("component class not found: " + componentClassName, cnfe);
		}
	}

	public PersistentClass getOwner() {
		return owner;
	}

	public String getParentProperty() {
		return parentProperty;
	}

	public void setComponentClassName(String componentClass) {
		this.componentClassName = componentClass;
	}

	public void setEmbedded(boolean embedded) {
		this.embedded = embedded;
	}

	public void setOwner(PersistentClass owner) {
		this.owner = owner;
	}

	public void setParentProperty(String parentProperty) {
		this.parentProperty = parentProperty;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public Type getType() throws MappingException {
		final int span = getPropertySpan();
		String[] names = new String[span];
		org.hibernate.type.Type[] types = new org.hibernate.type.Type[span];
		boolean[] nullabilities = new boolean[span];
		CascadeStyle[] cascade = new CascadeStyle[span];
		FetchMode[] joinedFetch = new FetchMode[span];
		Iterator props = getPropertyIterator();
		int j=0;
		while ( props.hasNext() ) {
			Property prop = (Property) props.next();
			names[j] = prop.getName();
			types[j] = prop.getType();
			cascade[j] = prop.getCascadeStyle();
			joinedFetch[j] = prop.getValue().getFetchMode();
			nullabilities[j] = prop.isNullable();
			j++;
		}
			
		TuplizerLookup tuplizers = TuplizerLookup.create(this);
		
		if ( isEmbedded() ) {
			return new EmbeddedComponentType(
					names,
					types,
					nullabilities,
					joinedFetch,
					cascade,
					isKey,
					tuplizers
			);
		}
		else {
			return new ComponentType(
				names,
				types,
				nullabilities,
				joinedFetch,
				cascade,
				isKey,
				tuplizers
			);
		}
	
	}

	public void setTypeUsingReflection(String className, String propertyName)
		throws MappingException {
	}
	
	public java.util.Map getMetaAttributes() {
		return metaAttributes;
	}
	public MetaAttribute getMetaAttribute(String attributeName) {
		return (MetaAttribute) metaAttributes.get(attributeName);
	}

	public void setMetaAttributes(java.util.Map metas) {
		this.metaAttributes = metas;
	}
	
	public Object accept(ValueVisitor visitor) {
		return visitor.accept(this);
	}
	
	public boolean[] getColumnInsertability() {
		boolean[] result = new boolean[ getColumnSpan() ];
		Iterator iter = getPropertyIterator();
		int i=0;
		while ( iter.hasNext() ) {
			Property prop = (Property) iter.next();
			boolean[] chunk = prop.getValue().getColumnInsertability();
			if ( prop.isInsertable() ) {
				System.arraycopy(chunk, 0, result, i, chunk.length);
			}
			i+=chunk.length;
		}
		return result;
	}

	public boolean[] getColumnUpdateability() {
		boolean[] result = new boolean[ getColumnSpan() ];
		Iterator iter = getPropertyIterator();
		int i=0;
		while ( iter.hasNext() ) {
			Property prop = (Property) iter.next();
			boolean[] chunk = prop.getValue().getColumnUpdateability();
			if ( prop.isUpdateable() ) {
				System.arraycopy(chunk, 0, result, i, chunk.length);
			}
			i+=chunk.length;
		}
		return result;
	}
	
	public String getNodeName() {
		return nodeName;
	}
	
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public boolean isKey() {
		return isKey;
	}
	
	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}
	
	public boolean hasPojoRepresentation() {
		return componentClassName!=null;
	}

	public void addTuplizer(EntityMode entityMode, String implClassName) {
		if ( tuplizerImpls == null ) {
			tuplizerImpls = new HashMap();
		}
		tuplizerImpls.put( entityMode, implClassName );
	}

	public String getTuplizerImplClassName(EntityMode mode) {
		if ( tuplizerImpls == null ) return null;
		return ( String ) tuplizerImpls.get( mode );
	}

	public Property getProperty(String propertyName) throws MappingException {
		Iterator iter = getPropertyIterator();
		while ( iter.hasNext() ) {
			Property prop = (Property) iter.next();
			if ( prop.getName().equals(propertyName) ) {
				return prop;
			}
		}
		throw new MappingException("component property not found: " + propertyName);
	}

	public String toString() {
		return getClass().getName() + '(' + properties.toString() + ')';
	}

}

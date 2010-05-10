//$Id: MetaType.java,v 1.12 2005/07/25 06:53:09 oneovthafew Exp $
package org.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Node;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.Mapping;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;

/**
 * @author Gavin King
 */
public class MetaType extends AbstractType {

	private final Map values;
	private final Map keys;
	private final Type baseType;

	public MetaType(Map values, Type baseType) {
		this.baseType = baseType;
		this.values = values;
		keys = new HashMap();
		Iterator iter = values.entrySet().iterator();
		while ( iter.hasNext() ) {
			Map.Entry me = (Map.Entry) iter.next();
			keys.put( me.getValue(), me.getKey() );
		}
	}

	public int[] sqlTypes(Mapping mapping) throws MappingException {
		return baseType.sqlTypes(mapping);
	}

	public int getColumnSpan(Mapping mapping) throws MappingException {
		return baseType.getColumnSpan(mapping);
	}

	public Class getReturnedClass() {
		return String.class;
	}

	public Object nullSafeGet(
		ResultSet rs,
		String[] names,
		SessionImplementor session,
		Object owner)
	throws HibernateException, SQLException {
		Object key = baseType.nullSafeGet(rs, names, session, owner);
		return key==null ? null : values.get(key);
	}

	public Object nullSafeGet(
		ResultSet rs,
		String name,
		SessionImplementor session,
		Object owner)
	throws HibernateException, SQLException {
		Object key = baseType.nullSafeGet(rs, name, session, owner);
		return key==null ? null : values.get(key);
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
	throws HibernateException, SQLException {
		baseType.nullSafeSet(st, value==null ? null : keys.get(value), index, session);
	}
	
	public void nullSafeSet(
			PreparedStatement st,
			Object value,
			int index,
			boolean[] settable, 
			SessionImplementor session)
	throws HibernateException, SQLException {
		if ( settable[0] ) nullSafeSet(st, value, index, session);
	}

	public String toLoggableString(Object value, SessionFactoryImplementor factory) throws HibernateException {
		return toXMLString(value, factory);
	}
	
	public String toXMLString(Object value, SessionFactoryImplementor factory)
		throws HibernateException {
		return (String) value; //value is the entity name
	}

	public Object fromXMLString(String xml, Mapping factory)
		throws HibernateException {
		return xml; //xml is the entity name
	}

	public String getName() {
		return baseType.getName(); //TODO!
	}

	public Object deepCopy(Object value, EntityMode entityMode, SessionFactoryImplementor factory) 
	throws HibernateException {
		return value;
	}

	public Object replace(
			Object original, 
			Object target,
			SessionImplementor session, 
			Object owner, 
			Map copyCache
	) {
		return original;
	}
	
	public boolean isMutable() {
		return false;
	}

	public Object fromXMLNode(Node xml, Mapping factory) throws HibernateException {
		return fromXMLString( xml.getText(), factory );
	}

	public void setToXMLNode(Node node, Object value, SessionFactoryImplementor factory) throws HibernateException {
		node.setText( toXMLString(value, factory) );
	}

	public boolean[] toColumnNullness(Object value, Mapping mapping) {
		throw new UnsupportedOperationException();
	}

	public boolean isDirty(Object old, Object current, boolean[] checkable, SessionImplementor session) throws HibernateException {
		return checkable[0] && isDirty(old, current, session);
	}
	
}

//$Id: NullableType.java,v 1.10 2005/09/14 19:54:49 oneovthafew Exp $
package org.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.LogFactory;
import org.dom4j.Node;

import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.engine.Mapping;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.util.ArrayHelper;
import org.hibernate.util.EqualsHelper;
import org.hibernate.util.StringHelper;

/**
 * Superclass of single-column nullable types.
 * @author Gavin King
 */
public abstract class NullableType extends AbstractType {

	private static final boolean IS_TRACE_ENABLED;
	static {
		//cache this, because it was a significant performance cost
		IS_TRACE_ENABLED = LogFactory.getLog( StringHelper.qualifier( Type.class.getName() ) ).isTraceEnabled();
	}
	
	/**
	 * Get a column value from a result set, without worrying about the 
	 * possibility of null values
	 */
	public abstract Object get(ResultSet rs, String name) 
	throws HibernateException, SQLException;
	
	/**
	 * Get a parameter value without worrying about the possibility of null values
	 */
	public abstract void set(PreparedStatement st, Object value, int index) 
	throws HibernateException, SQLException;
	
	public abstract int sqlType();
	
	public abstract String toString(Object value) throws HibernateException;
	
	public abstract Object fromStringValue(String xml) throws HibernateException;

	public final void nullSafeSet(
			PreparedStatement st, 
			Object value, 
			int index, 
			boolean[] settable, 
			SessionImplementor session) 
	throws HibernateException, SQLException {
		if ( settable[0] ) nullSafeSet(st, value, index);
	}

	public final void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) 
	throws HibernateException, SQLException {
		nullSafeSet(st, value, index);
	}

	public final void nullSafeSet(PreparedStatement st, Object value, int index) 
	throws HibernateException, SQLException {
		try {
			if (value==null) {
				if (IS_TRACE_ENABLED) {
					LogFactory.getLog( getClass() )
							.trace("binding null to parameter: " + index);
				}
	
				st.setNull( index, sqlType() );
			}
			else {
				if (IS_TRACE_ENABLED) {
					LogFactory.getLog( getClass() ).trace(
							"binding '" + toString(value) + 
							"' to parameter: " + index
						);
				}
	
				set(st, value, index);
			}
		}
		catch (RuntimeException re) {
			LogFactory.getLog( getClass() ).info(
					"could not bind value '" + toString(value) + 
					"' to parameter: " + index
				);
		}
	}

	public final Object nullSafeGet(
			ResultSet rs, 
			String[] names, 
			SessionImplementor session, 
			Object owner) 
	throws HibernateException, SQLException {
		return nullSafeGet(rs, names[0]);
	}

	public final Object nullSafeGet(ResultSet rs, String[] names) 
	throws HibernateException, SQLException {
		return nullSafeGet(rs, names[0]);
	}

	public final Object nullSafeGet(ResultSet rs, String name) 
	throws HibernateException, SQLException {
		try {
			Object value = get(rs, name);
			if ( value==null || rs.wasNull() ) {
				if (IS_TRACE_ENABLED) {
					LogFactory.getLog( getClass() )
							.trace("returning null as column: " + name);
				}
				return null;
			}
			else {
				if (IS_TRACE_ENABLED) {
					LogFactory.getLog( getClass() ).trace(
							"returning '" + toString(value) + 
							"' as column: " + name
						);
				}
				return value;
			}
		}
		catch (RuntimeException re) {
			LogFactory.getLog( getClass() )
					.info("could not read column value from result set: " + name);
			throw re;
		}
	}

	public final Object nullSafeGet(ResultSet rs, String name, SessionImplementor session, Object owner) 
	throws HibernateException, SQLException {
		return nullSafeGet(rs, name);
	}

	public final String toXMLString(Object value, SessionFactoryImplementor pc) 
	throws HibernateException {
		return toString(value);
	}

	public final Object fromXMLString(String xml, Mapping factory) throws HibernateException {
		return xml==null || xml.length()==0 ? null : fromStringValue(xml);
	}

	public final int getColumnSpan(Mapping session) {
		return 1;
	}

	public final int[] sqlTypes(Mapping session) {
		return new int[] { sqlType() };
	}
	
	public final boolean isEqual(Object x, Object y, EntityMode entityMode) {
		return isEqual(x, y);
	}
	
	public boolean isEqual(Object x, Object y) {
		return EqualsHelper.equals(x, y);
	}

	public String toLoggableString(Object value, SessionFactoryImplementor factory) {
		return value==null ? "null" : toString(value);
	}

	public Object fromXMLNode(Node xml, Mapping factory) throws HibernateException {
		return fromXMLString( xml.getText(), factory );
	}

	public void setToXMLNode(Node xml, Object value, SessionFactoryImplementor factory) 
	throws HibernateException {
		xml.setText( toXMLString(value, factory) );
	}
	
	public boolean[] toColumnNullness(Object value, Mapping mapping) {
		return value==null ? ArrayHelper.FALSE : ArrayHelper.TRUE;
	}
	
	public boolean isDirty(Object old, Object current, boolean[] checkable, SessionImplementor session) 
	throws HibernateException {
		return checkable[0] && isDirty(old, current, session);
	}

}

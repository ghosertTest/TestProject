//$Id: SerializableType.java,v 1.7 2005/03/16 04:45:25 oneovthafew Exp $
package org.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.util.SerializationHelper;

/**
 * <tt>serializable</tt>: A type that maps an SQL VARBINARY to a
 * serializable Java object.
 * @author Gavin King
 */
public class SerializableType extends MutableType {

	private final Class serializableClass;

	public SerializableType(Class serializableClass) {
		this.serializableClass = serializableClass;
	}

	public void set(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		Hibernate.BINARY.set(st, toBytes(value), index);
	}

	public Object get(ResultSet rs, String name) throws HibernateException, SQLException {

		byte[] bytes = (byte[]) Hibernate.BINARY.get(rs, name);
		if ( bytes==null ) {
			return null;
		}
		else {
			return fromBytes(bytes);
		}
	}

	public Class getReturnedClass() {
		return serializableClass;
	}

	public boolean isEqual(Object x, Object y) throws HibernateException {
		if (x==y) return true;
		if (x==null || y==null) return false;
		return Hibernate.BINARY.isEqual( toBytes(x), toBytes(y) );
	}

	public int getHashCode(Object x, EntityMode entityMode) {
		return Hibernate.BINARY.getHashCode( toBytes(x), entityMode );
	}

	public String toString(Object value) throws HibernateException {
		return Hibernate.BINARY.toString( toBytes(value) );
	}

	public Object fromStringValue(String xml) throws HibernateException {
		return fromBytes( (byte[]) Hibernate.BINARY.fromStringValue(xml) );
	}

	public String getName() {
		return (serializableClass==Serializable.class) ? "serializable" : serializableClass.getName();
	}

	public Object deepCopyNotNull(Object value) throws HibernateException {
		return fromBytes( toBytes(value) );
	}

	private static byte[] toBytes(Object object) throws SerializationException {
		return SerializationHelper.serialize( (Serializable) object );
	}

	private static Object fromBytes( byte[] bytes ) throws SerializationException {
		return SerializationHelper.deserialize(bytes);
	}

	public int sqlType() {
		return Hibernate.BINARY.sqlType();
	}

	public Object assemble(Serializable cached, SessionImplementor session, Object owner)
	throws HibernateException {
		return (cached==null) ? null : fromBytes( (byte[]) cached );
	}

	public Serializable disassemble(Object value, SessionImplementor session, Object owner)
	throws HibernateException {
		return (value==null) ? null : toBytes(value);
	}

}








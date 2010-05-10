//$Id: Insert.java,v 1.6 2005/08/10 20:23:55 oneovthafew Exp $
package org.hibernate.sql;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.LiteralType;

/**
 * An SQL <tt>INSERT</tt> statement
 *
 * @author Gavin King
 */
public class Insert {

	public Insert(Dialect dialect) {
		this.dialect = dialect;
	}

	private String comment;
	public Insert setComment(String comment) {
		this.comment = comment;
		return this;
	}

	private Dialect dialect;
	private String tableName;

	private Map columns = new SequencedHashMap();

	public Insert addColumn(String columnName) {
		return addColumn(columnName, "?");
	}

	public Insert addColumns(String[] columnNames) {
		for ( int i=0; i<columnNames.length; i++ ) {
			addColumn( columnNames[i] );
		}
		return this;
	}

	public Insert addColumns(String[] columnNames, boolean[] insertable) {
		for ( int i=0; i<columnNames.length; i++ ) {
			if ( insertable[i] ) addColumn( columnNames[i] );
		}
		return this;
	}

	public Insert addColumn(String columnName, String value) {
		columns.put(columnName, value);
		return this;
	}

	public Insert addColumn(String columnName, Object value, LiteralType type) throws Exception {
		return addColumn( columnName, type.objectToSQLString(value, dialect) );
	}

	public Insert addIdentityColumn(String columnName) {
		String value = dialect.getIdentityInsertString();
		if (value!=null) addColumn(columnName, value);
		return this;
	}

	public Insert setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public String toStatementString() {
		StringBuffer buf = new StringBuffer( columns.size()*15 + tableName.length() + 10 );
		if (comment!=null) buf.append("/* ").append(comment).append(" */ ");
		buf.append("insert into ")
			.append(tableName);
		if ( columns.size()==0 ) {
			buf.append(' ').append( dialect.getNoColumnsInsertString() );
		}
		else {
			buf.append(" (");
			Iterator iter = columns.keySet().iterator();
			while ( iter.hasNext() ) {
				buf.append( iter.next() );
				if ( iter.hasNext() ) buf.append(", ");
			}
			buf.append(") values (");
			iter = columns.values().iterator();
			while ( iter.hasNext() ) {
				buf.append( iter.next() );
				if ( iter.hasNext() ) buf.append(", ");
			}
			buf.append(')');
		}
		return buf.toString();
	}
}

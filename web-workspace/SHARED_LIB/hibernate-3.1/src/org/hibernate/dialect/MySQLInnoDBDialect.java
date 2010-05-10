//$Id: MySQLInnoDBDialect.java,v 1.4 2005/06/12 21:55:12 oneovthafew Exp $
package org.hibernate.dialect;

/**
 * @author Gavin King
 */
public class MySQLInnoDBDialect extends MySQLDialect {

	public boolean supportsCascadeDelete() {
		return true;
	}
	
	public String getTableTypeString() {
		return " type=InnoDB";
	}

	public boolean hasSelfReferentialForeignKeyBug() {
		return true;
	}
	
}

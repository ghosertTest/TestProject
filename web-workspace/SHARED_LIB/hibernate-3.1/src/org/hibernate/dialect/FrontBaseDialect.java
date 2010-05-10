//$Id: FrontBaseDialect.java,v 1.7 2005/08/12 01:57:48 oneovthafew Exp $
package org.hibernate.dialect;

import java.sql.Types;

/**
 * An SQL Dialect for Frontbase.  Assumes you're using the latest version
 * of the FrontBase JDBC driver, available from <tt>http://frontbase.com/</tt>
 * <p>
 * <b>NOTE</b>: The latest JDBC driver is not always included with the
 * latest release of FrontBase.  Download the driver separately, and enjoy
 * the informative release notes.
 * <p>
 * This dialect was tested with JDBC driver version 2.3.1.  This driver
 * contains a bug that causes batches of updates to fail.  (The bug should be
 * fixed in the next release of the JDBC driver.)  If you are using JDBC driver
 * 2.3.1, you can work-around this problem by setting the following in your
 * <tt>hibernate.properties</tt> file: <tt>hibernate.jdbc.batch_size=15</tt>
 *
 * @author Ron Lussier <tt>rlussier@lenscraft.com</tt>
 */
public class FrontBaseDialect extends Dialect {

	public FrontBaseDialect() {
		super();

		registerColumnType( Types.BIT, "bit" );
		registerColumnType( Types.BIGINT, "longint" );
		registerColumnType( Types.SMALLINT, "smallint" );
		registerColumnType( Types.TINYINT, "tinyint" );
		registerColumnType( Types.INTEGER, "integer" );
		registerColumnType( Types.CHAR, "char(1)" );
		registerColumnType( Types.VARCHAR, "varchar($l)" );
		registerColumnType( Types.FLOAT, "float" );
		registerColumnType( Types.DOUBLE, "double precision" );
		registerColumnType( Types.DATE, "date" );
		registerColumnType( Types.TIME, "time" );
		registerColumnType( Types.TIMESTAMP, "timestamp" );
		registerColumnType( Types.VARBINARY, "bit varying($l)" );
		registerColumnType( Types.NUMERIC, "numeric($p,$s)" );
		registerColumnType( Types.BLOB, "blob" );
		registerColumnType( Types.CLOB, "clob" );
	}

	public String getAddColumnString() {
		return "add column";
	}

	public String getCascadeConstraintsString() {
		return " cascade";
	}

	public boolean dropConstraints() {
		return false;
	}

	/**
	 * Does this dialect support the <tt>FOR UPDATE</tt> syntax. No!
	 *
	 * @return false always. FrontBase doesn't support this syntax,
	 * which was dropped with SQL92
	 */
	public String getForUpdateString() {
		return "";
	}

	public String getCurrentTimestampCallString() {
		// TODO : not sure this is correct, could not find docs on how to do this.
		return "{?= call current_timestamp}";
	}

	public boolean isCurrentTimestampSelectStringCallable() {
		return true;
	}
}

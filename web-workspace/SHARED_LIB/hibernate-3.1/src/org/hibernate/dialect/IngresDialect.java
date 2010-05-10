//$Id: IngresDialect.java,v 1.10 2005/10/06 10:49:05 oneovthafew Exp $
package org.hibernate.dialect;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.function.NoArgSQLFunction;
import org.hibernate.dialect.function.StandardSQLFunction;


/**
 * An Ingres SQL dialect
 * @author Ian Booth, Bruce Lunsford
 */
public class IngresDialect extends Dialect {

	public IngresDialect() {
		super();
		registerColumnType( Types.BIT, "tinyint" );
		registerColumnType( Types.TINYINT, "tinyint" );
		registerColumnType( Types.SMALLINT, "smallint" );
		registerColumnType( Types.INTEGER, "integer" );
		registerColumnType( Types.BIGINT, "bigint" );
		registerColumnType( Types.REAL, "real" );
		registerColumnType( Types.FLOAT, "float" );
		registerColumnType( Types.DOUBLE, "float" );
		registerColumnType( Types.NUMERIC, "decimal(19, $l)" );
		registerColumnType( Types.DECIMAL, "decimal(19, $l)" );
		registerColumnType( Types.BINARY, 32000, "byte($l)" );
		registerColumnType( Types.BINARY, "long byte" );
		registerColumnType( Types.VARBINARY, 32000, "varbyte($l)" );
		registerColumnType( Types.VARBINARY, "long byte" );
		registerColumnType( Types.LONGVARBINARY, "long byte" );
		registerColumnType( Types.CHAR, "char(1)" );
		registerColumnType( Types.VARCHAR, 32000, "varchar($l)" );
		registerColumnType( Types.VARCHAR, "long varchar" );
		registerColumnType( Types.LONGVARCHAR, "long varchar" );
		registerColumnType( Types.DATE, "date" );
		registerColumnType( Types.TIME, "date" );
		registerColumnType( Types.TIMESTAMP, "date" );
		registerColumnType( Types.BLOB, "long byte" );
		registerColumnType( Types.CLOB, "long varchar" );

		registerFunction( "abs", new StandardSQLFunction("abs") );
		registerFunction( "atan", new StandardSQLFunction( "atan", Hibernate.DOUBLE ) );
		registerFunction( "bit_add", new StandardSQLFunction("bit_add") );
		registerFunction( "bit_and", new StandardSQLFunction("bit_and") );
		registerFunction( "bit_length", new StandardSQLFunction("bit_length") );
		registerFunction( "bit_not", new StandardSQLFunction("bit_not") );
		registerFunction( "bit_or", new StandardSQLFunction("bit_or") );
		registerFunction( "bit_xor", new StandardSQLFunction("bit_xor") );
		registerFunction( "character_length", new StandardSQLFunction( "character_length", Hibernate.LONG ) );
		registerFunction( "charextract", new StandardSQLFunction( "charextract", Hibernate.STRING ) );
		registerFunction( "concat", new StandardSQLFunction( "concat", Hibernate.STRING ) );
		registerFunction( "cos", new StandardSQLFunction( "cos", Hibernate.DOUBLE ) );
		registerFunction( "current_user", new NoArgSQLFunction( "current_user", Hibernate.STRING, false ) );
		registerFunction( "date_trunc", new StandardSQLFunction( "date_trunc", Hibernate.TIMESTAMP ) );
		registerFunction( "dba", new NoArgSQLFunction( "dba", Hibernate.STRING, true ) );
		registerFunction( "dow", new StandardSQLFunction( "dow", Hibernate.STRING ) );
		registerFunction( "exp", new StandardSQLFunction( "exp", Hibernate.DOUBLE ) );
		registerFunction( "gmt_timestamp", new StandardSQLFunction(  "gmt_timestamp", Hibernate.STRING ) );
		registerFunction( "hash", new StandardSQLFunction( "hash", Hibernate.INTEGER ) );
		registerFunction( "hex", new StandardSQLFunction( "hex", Hibernate.STRING ) );
		registerFunction( "initial_user", new NoArgSQLFunction( "initial_user", Hibernate.STRING, false ) );
		registerFunction( "intextract", new StandardSQLFunction( "intextract", Hibernate.INTEGER ) );
		registerFunction( "left", new StandardSQLFunction( "left", Hibernate.STRING ) );
		registerFunction( "locate", new StandardSQLFunction( "locate", Hibernate.LONG ) );
		registerFunction( "length", new StandardSQLFunction( "length", Hibernate.LONG ) );
		registerFunction( "ln", new StandardSQLFunction( "ln", Hibernate.DOUBLE ) );
		registerFunction( "log", new StandardSQLFunction( "log", Hibernate.DOUBLE ) );
		registerFunction( "lower", new StandardSQLFunction("lower") );
		registerFunction( "lowercase", new StandardSQLFunction("lowercase") );
		registerFunction( "octet_length", new StandardSQLFunction( "octet_length", Hibernate.LONG ) );
		registerFunction( "pad", new StandardSQLFunction( "pad", Hibernate.STRING ) );
		registerFunction( "position", new StandardSQLFunction( "position", Hibernate.LONG ) );
		registerFunction( "power", new StandardSQLFunction( "power", Hibernate.DOUBLE ) );
		registerFunction( "random", new NoArgSQLFunction( "random", Hibernate.LONG, true ) );
		registerFunction( "randomf", new NoArgSQLFunction( "randomf", Hibernate.DOUBLE, true ) );
		registerFunction( "right", new StandardSQLFunction( "right", Hibernate.STRING ) );
		registerFunction( "session_user", new NoArgSQLFunction( "session_user", Hibernate.STRING, false ) );
		registerFunction( "size", new NoArgSQLFunction( "size", Hibernate.LONG, true ) );
		registerFunction( "squeeze", new StandardSQLFunction("squeeze") );
		registerFunction( "sin", new StandardSQLFunction( "sin", Hibernate.DOUBLE ) );
		registerFunction( "soundex", new StandardSQLFunction( "soundex", Hibernate.STRING ) );
		registerFunction( "sqrt", new StandardSQLFunction( "sqrt", Hibernate.DOUBLE ) );
		registerFunction( "system_user", new NoArgSQLFunction( "system_user", Hibernate.STRING, false ) );
		registerFunction( "trim", new StandardSQLFunction("trim") );
		registerFunction( "unhex", new StandardSQLFunction( "unhex", Hibernate.STRING ) );
		registerFunction( "upper", new StandardSQLFunction("upper") );
		registerFunction( "uppercase", new StandardSQLFunction("uppercase") );
		registerFunction( "user", new NoArgSQLFunction( "user", Hibernate.STRING, false ) );
		registerFunction( "usercode", new NoArgSQLFunction( "usercode", Hibernate.STRING, true ) );
		registerFunction( "username", new NoArgSQLFunction( "username", Hibernate.STRING, true ) );
		registerFunction( "uuid_create", new StandardSQLFunction( "uuid_create", Hibernate.BYTE ) );
		registerFunction( "uuid_compare", new StandardSQLFunction( "uuid_compare", Hibernate.INTEGER ) );
		registerFunction( "uuid_from_char", new StandardSQLFunction( "uuid_from_char", Hibernate.BYTE ) );
		registerFunction( "uuid_to_char", new StandardSQLFunction( "uuid_to_char", Hibernate.STRING ) );
	}
	
	/**
	 * Do we need to drop constraints before dropping tables in this dialect?
	 * @return boolean
	 */
	public boolean dropConstraints() {
		return false;
	}
	
	/**
	 * Does this dialect support <tt>FOR UPDATE OF</tt>, allowing
	 * particular rows to be locked?
	 */
	public boolean supportsForUpdateOf() {
		return true;
	}
	
	/**
	 * The syntax used to add a column to a table (optional).
	 */
	public String getAddColumnString() {
		return "add column";
	}
	
	/**
	 * The keyword used to specify a nullable column.
	 * @return String
	 */
	public String getNullColumnString() {
		return " with null";
	}
	
	/**
	 * Does this dialect support sequences?
	 * @return boolean
	 */
	public boolean supportsSequences() {
		return true;
	}

	/**
	 * The syntax that fetches the next value of a sequence, if sequences are supported.
	 * @param sequenceName the name of the sequence
	 * @return String
	 * @throws MappingException if no sequences
	 */
	public String getSequenceNextValString(String sequenceName) {
		return "select nextval for " + sequenceName;
	}

	/**
	 * The syntax used to create a sequence, if sequences are supported.
	 * @param sequenceName the name of the sequence
	 * @return String
	 * @throws MappingException if no sequences
	 */
	public String getCreateSequenceString(String sequenceName) {
		return "create sequence " + sequenceName;
	}

	/**
	 * The syntax used to drop a sequence, if sequences are supported.
	 * @param sequenceName the name of the sequence
	 * @return String
	 * @throws MappingException if no sequences
	 */
	public String getDropSequenceString(String sequenceName) {
		return "drop sequence " + sequenceName + " restrict";
	}

	/**
	 * A query used to find all sequences
	 */
	public String getQuerySequencesString() {
		return "select seq_name from iisequence";
	}
	
	/**
	 * The name of the SQL function that transforms a string to 
	 * lowercase
	 * 
	 * @return String
	 */
	public String getLowercaseFunction() {
		return "lowercase";
	}
	
	/**
	 * Does this <tt>Dialect</tt> have some kind of <tt>LIMIT</tt> syntax?
	 */
	public boolean supportsLimit() {
		return true;
	}
	
	/**
	 * Does this dialect support an offset?
	 */
	public boolean supportsLimitOffset() {
		return false;
	}
	
	/**
	 * Add a <tt>LIMIT</tt> clause to the given SQL <tt>SELECT</tt>
	 * @return the modified SQL
	 */
	public String getLimitString(String querySelect, int offset, int limit) {
		if (offset > 0) {
			throw new UnsupportedOperationException( "offset not supported" );
		}
		return new StringBuffer( querySelect.length()+16 )
				.append(querySelect)
				.insert(6, " first " + limit )
				.toString();	
	}

	public boolean supportsVariableLimit() {
		return false;
	}
	
	/**
	 * Does the <tt>LIMIT</tt> clause take a "maximum" row number instead
	 * of a total number of returned rows?
	 */
	public boolean useMaxForLimit() {
		return true;
	}
	
}

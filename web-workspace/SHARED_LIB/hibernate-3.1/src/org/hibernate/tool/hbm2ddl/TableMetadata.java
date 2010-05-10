//$Id: TableMetadata.java,v 1.5 2005/08/12 01:02:06 oneovthafew Exp $
package org.hibernate.tool.hbm2ddl;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.util.StringHelper;

/**
 * JDBC table metadata
 * @author Christoph Sturm
 */
public class TableMetadata {
	
	private static final Log log = LogFactory.getLog(TableMetadata.class);
	
	private final String catalog;
	private final String schema;
	private final String name;
	private final Map columns = new HashMap();
	private final Map foreignKeys = new HashMap();
	private final Map indexes = new HashMap();

	TableMetadata(ResultSet rs, DatabaseMetaData meta, boolean extras) throws SQLException {
		catalog = rs.getString("TABLE_CAT");
		schema = rs.getString("TABLE_SCHEM");
		name = rs.getString("TABLE_NAME");
		initColumns(meta);
		if (extras) {
			initForeignKeys(meta);
			initIndexes(meta);
		}
		String cat = catalog==null ? "" : catalog + '.';
		String schem = schema==null ? "" : schema + '.';
		log.info( "table found: " + cat + schem + name );
		log.info( "columns: " + columns.keySet() );
		if (extras) {
			log.info( "foreign keys: " + foreignKeys.keySet() );
			log.info( "indexes: " + indexes.keySet() );
		}
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return "TableMetadata(" + name + ')';
	}

	public ColumnMetadata getColumnMetadata(String columnName) {
		return (ColumnMetadata) columns.get( columnName.toLowerCase() );
	}

	public ForeignKeyMetadata getForeignKeyMetadata(String keyName) {
		return (ForeignKeyMetadata) foreignKeys.get( keyName.toLowerCase() );
	}

	public IndexMetadata getIndexMetadata(String indexName) {
		return (IndexMetadata) indexes.get( indexName.toLowerCase() );
	}

	private void addForeignKey(ResultSet rs) throws SQLException {
		String fk = rs.getString("FK_NAME");

		if (fk == null) return;

		ForeignKeyMetadata info = getForeignKeyMetadata(fk);
		if (info == null) {
			info = new ForeignKeyMetadata(rs);
			foreignKeys.put( info.getName().toLowerCase(), info );
		}

		info.addColumn( getColumnMetadata( rs.getString("FKCOLUMN_NAME") ) );
	}

	private void addIndex(ResultSet rs) throws SQLException {
		String index = rs.getString("INDEX_NAME");

		if (index == null) return;

		IndexMetadata info = getIndexMetadata(index);
		if (info == null) {
			info = new IndexMetadata(rs);
			indexes.put( info.getName().toLowerCase(), info );
		}

		info.addColumn( getColumnMetadata( rs.getString("COLUMN_NAME") ) );
	}

	public void addColumn(ResultSet rs) throws SQLException {
		String column = rs.getString("COLUMN_NAME");

		if (column==null) return;

		if ( getColumnMetadata(column) == null ) {
			ColumnMetadata info = new ColumnMetadata(rs);
			columns.put( info.getName().toLowerCase(), info );
		}
	}

	private void initForeignKeys(DatabaseMetaData meta) throws SQLException {
		ResultSet rs = null;

		try {
			if ( meta.storesUpperCaseIdentifiers() ) {
				rs = meta.getImportedKeys( 
						StringHelper.toUpperCase(catalog), 
						StringHelper.toUpperCase(schema), 
						StringHelper.toUpperCase(name)
					);
			}
			else if ( meta.storesLowerCaseIdentifiers() ) {
				rs = meta.getImportedKeys( 
						StringHelper.toLowerCase(catalog), 
						StringHelper.toLowerCase(schema), 
						StringHelper.toLowerCase(name)
					);
			}
			else {
				rs = meta.getImportedKeys(catalog, schema, name);
			}
			
			while ( rs.next() ) addForeignKey(rs);
		}
		finally {
			if (rs != null) rs.close();
		}
	}

	private void initIndexes(DatabaseMetaData meta) throws SQLException {
		ResultSet rs = null;

		try {
			if ( meta.storesUpperCaseIdentifiers() ) {
				rs = meta.getIndexInfo( 
						StringHelper.toUpperCase(catalog), 
						StringHelper.toUpperCase(schema), 
						StringHelper.toUpperCase(name), 
						false, 
						true
					);
			}
			else if ( meta.storesLowerCaseIdentifiers() ) {
				rs = meta.getIndexInfo( 
						StringHelper.toLowerCase(catalog), 
						StringHelper.toLowerCase(schema), 
						StringHelper.toLowerCase(name), 
						false, 
						true
					);
			}
			else {
				rs = meta.getIndexInfo(catalog, schema, name, false, true);
			}

			while ( rs.next() ) {
				if ( rs.getShort("TYPE") == DatabaseMetaData.tableIndexStatistic ) continue;
				addIndex(rs);
			}
		}
		finally {
			if (rs != null) rs.close();
		}
	}

	private void initColumns(DatabaseMetaData meta) throws SQLException {
		ResultSet rs = null;
		
		try {
			if ( meta.storesUpperCaseIdentifiers() ) {
				rs = meta.getColumns( 
						StringHelper.toUpperCase(catalog), 
						StringHelper.toUpperCase(schema), 
						StringHelper.toUpperCase(name), 
						"%"
					);
			}
			else if ( meta.storesLowerCaseIdentifiers() ) {
				rs = meta.getColumns( 
						StringHelper.toLowerCase(catalog), 
						StringHelper.toLowerCase(schema), 
						StringHelper.toLowerCase(name), 
						"%"
					);
			}
			else {
				rs = meta.getColumns(catalog, schema, name, "%");
			}

			while ( rs.next() ) addColumn(rs);
		}
		finally  {
			if (rs != null) rs.close();
		}
	}
	
}







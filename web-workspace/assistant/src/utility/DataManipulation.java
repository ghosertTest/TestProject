/*
 * Created on 2005-1-31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package utility;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.util.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;

public class DataManipulation {
	
	private String dataSourceName = null;
	
	private String[] columnNames = null;
	
	private ArrayList databaseValues = null;
	
	private Connection con = null;
	
	public String getDataSourceName() {
		return dataSourceName;
	}
	
	public String[] getColumnNames() {
		return columnNames;
	}
	
	public ArrayList getDatabaseValues() {
		return databaseValues;
	}
	
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	
	public void setDatabaseValues(ArrayList databaseValues) {
		this.databaseValues = databaseValues;
	}
	
	public void createConnection() {
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx
					.lookup(dataSourceName);
			con = ds.getConnection();
		} catch (NamingException ne) {
			System.out.println("NamingException Exception from DataManipulation class: createConnection()");
			ne.printStackTrace();
		} catch (SQLException sqle) {
			System.out.println("SQLException Exception from DataManipulation class: createConnection()");
			sqle.printStackTrace();
		}
	}
	
	public void destroyConnection() {
		try {
			if (con != null) {
				con.close();
				}
		} catch (SQLException sqle) {
			System.out.println("SQLException Exception from DataManipulation class: destroyConnection()");
		}
	}
	
//	public ArrayList dataSelect (String query) {
//		
//		PreparedStatement prestmt = null;
//		ResultSet rs = null;
//		ResultSetMetaData rsmd = null;
//		
//		if (con == null) {
//			createConnection();
//		}
//		try {
//		    prestmt = con.prepareStatement(query);
//		    rs = prestmt.executeQuery();
//		    rsmd = rs.getMetaData();
//		    int columnCount = rsmd.getColumnCount();
//		    rsmd.getColumnName();
//		    
//		    
//		    
//		    
//		    
//		} catch (SQLException sqle) {
//			System.out.println("SQLException Exception from DataManipulation class: dataSelect()");
//			sqle.printStackTrace();
//		}
//		
//	}

}

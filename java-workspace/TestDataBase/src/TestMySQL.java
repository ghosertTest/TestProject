import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import junit.framework.TestCase;

public class TestMySQL extends TestCase {

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public void testJdbcConnection() throws Exception {
        String driver = "com.mysql.jdbc.Driver";
        String user = "root";
        String password = "1234";
        String databaseName = "assistant";
        String url = "jdbc:mysql://localhost:3306/" + databaseName + "?autoReconnect=true";

        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, user, password);
        printUserDetails(connection);
    }

    public void testJNDIDataSource() throws Exception {
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY,"org.apache.naming.java.javaURLContextFactory");
        properties.put(Context.URL_PKG_PREFIXES,"org.apache.naming");
        properties.put(Context.PROVIDER_URL,"localhost:8080");
        Context context = new InitialContext(properties);
        DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/Assistant");
        Connection connection = ds.getConnection();
        printUserDetails(connection);
    }

    private void printUserDetails(Connection connection) {
        String sql = "select * from users";
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
	        preparedStatement = connection.prepareStatement(sql);
	        rs = preparedStatement.executeQuery();
	        while (rs.next()) {
	            System.out.println(rs.getString(1));
	            System.out.println(rs.getString(2));
	            System.out.println(rs.getString(3));
	            System.out.println(rs.getString(4));
	            System.out.println(rs.getString(5));
	            System.out.println(rs.getString(6));
	            System.out.println(rs.getString(7));
	            System.out.println(rs.getString(8));
	        }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}

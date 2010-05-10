/*
 * Created on 2005-2-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package appletServlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HelloServlet extends HttpServlet
{
    private Connection con = null;
    
	// get Connection in the constructor
	public HelloServlet() {
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx
					.lookup("java:comp/env/jdbc/Assistant");
			con = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public void init() throws ServletException
    {
        
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{
		// Open the I/O streams
		ObjectInputStream in = new ObjectInputStream(req.getInputStream() );
		ObjectOutputStream out = new ObjectOutputStream( res.getOutputStream() );
		try {
		    
		    System.out.println( "coming to HelloServlet.doPost()" );
		    
			// 1. Read in parameters from the applet
			String comm = (String) in.readObject();
		    String blueTooth = (String) in.readObject();
		    String array = (String) in.readObject();
		    String selectText = (String) in.readObject();

			// 2. excute the SQL statement
			// get all kinds of data to be neccessary
			PreparedStatement prestm = null;
			String sql = "insert into suggestions values(null, ?, ?, now(), ?, ?)";

				prestm = con.prepareStatement(sql);
				prestm.setString( 1, comm );
				prestm.setString( 2, blueTooth );
				prestm.setString( 3, array );
				prestm.setString( 4, selectText );
				prestm.executeUpdate();
				prestm.close();
				
			// 3. Write the result to applet
			out.writeObject( new StringBuffer( comm ).reverse().toString());
			
		} 
		catch (Exception e)
		{
		    e.printStackTrace(); 
		}
		finally
		{
			// Close the I/O streams
			in.close();
			out.close();
		}
	}

	// release the Connection
	public void destroy() {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}


}

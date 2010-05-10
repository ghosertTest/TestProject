/*
 * Created on 2005-1-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package servlet;

/**
 * @author Jiawei_Zhang
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;

public class SubSugServlet extends HttpServlet {

	private Connection con = null;

	// get Connection in the constructor
	public SubSugServlet() {
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx
					.lookup("java:comp/env/jdbc/Assistant");
			con = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() throws ServletException {

	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		// get all kinds of data to be neccessary
		PreparedStatement prestm = null;
		String sql = "insert into suggestions values(null, ?, ?, now(), ?, ?)";
		String sugName = req.getParameter("sugname");
		String sugEmail = req.getParameter("sugemail");
		String sugIp = req.getRemoteAddr();
		String suggestion = req.getParameter("suggestion");

		// if there is no null, reset variable to Sting ""
		if (sugName == null) {
			sugName = "";
		}
		if (sugEmail == null) {
			sugEmail = "";
		}
		if (suggestion == null) {
			suggestion = "";
		}

		// set value of variable to database and forward control to "/index.jsp"
		try {
			prestm = con.prepareStatement(sql);
			prestm.setString(1, sugName);
			prestm.setString(2, sugEmail);
			prestm.setString(3, sugIp);
			prestm.setString(4, suggestion);
			prestm.executeUpdate();
			prestm.close();
			RequestDispatcher reqdis = req
					.getRequestDispatcher("/index.jsp");
			reqdis.forward(req, res);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
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


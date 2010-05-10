/*
 * Created on 2005-1-24
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
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import assis.*;

public class ViewSugServlet extends HttpServlet {

	private ServletConfig config = null;
	private Connection con = null;
	
	// get Connection in the constructor
	public ViewSugServlet() {
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx
					.lookup("java:comp/env/jdbc/Assistant");
			con = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
			// get all kinds of data to be neccessary
			PreparedStatement prestm = null;
			ResultSet rs = null;
			String sqlGetTotalRecorders = "select count(*) from suggestions";
			String sqlGetContent = "select * from suggestions order by sug_id desc limit ?, ?";
			String initTotalRecorders = "";
			ArrayList content = null;
			String initRecordersPerPage = "";
			String initAvailablePages = "";
			String initCurrentPage = "";
			
			// get totalRecorders from database
			prestm = con.prepareStatement(sqlGetTotalRecorders);
			rs = prestm.executeQuery();
			if (rs.next()) {
				initTotalRecorders = rs.getString(1);
			}
			
			// get initRecordersPerPage, initAvailablePages from web.xml, get initCurrentPage from request
			initRecordersPerPage = config.getInitParameter("initRecordersPerPage");
			initAvailablePages = config.getInitParameter("initAvailablePages");
			initCurrentPage = req.getParameter("initCurrentPage");
			// initRecordersPerPage must be more than 0
			if (Integer.parseInt(initRecordersPerPage) <=0 ) {
				initRecordersPerPage = "1";
			}
			// initAvailablePages must be more than 0
			if (Integer.parseInt(initAvailablePages) <=0 ) {
				initAvailablePages = "1";
			}
			// initCurrentPage must be more than 0
			if (Integer.parseInt(initCurrentPage) <= 0) {
				initCurrentPage = "1";
			}
            
			// get totalPages and initCurrentPage must be not more than it.
			int totalPages = 0;
			if (Integer.parseInt(initTotalRecorders) == 0 ) {
                totalPages = 1;
            } else {
				if (Integer.parseInt(initTotalRecorders)%Integer.parseInt(initRecordersPerPage) == 0 ) {
					totalPages = Integer.parseInt(initTotalRecorders)/Integer.parseInt(initRecordersPerPage);
				} else {
				    totalPages = Integer.parseInt(initTotalRecorders)/Integer.parseInt(initRecordersPerPage)+1;
				}
            }
			if (Integer.parseInt(initCurrentPage) > totalPages) {
				initCurrentPage = String.valueOf(totalPages);
			}
			
			// get content from database, the size of content 
			// do not equals variable recordersPerPage, it equals the size of 
			// the object (ResultSet)rs
			int firstRecorderPerPage = 0;
			firstRecorderPerPage = (Integer.parseInt(initCurrentPage) - 1)
					* Integer.parseInt(initRecordersPerPage) + 1;
			prestm = con.prepareStatement(sqlGetContent);
			prestm.setInt(1, firstRecorderPerPage - 1);
			prestm.setInt(2, Integer.parseInt(initRecordersPerPage));
			rs = prestm.executeQuery();
			content = new ArrayList();
			while (rs.next()) {
				Suggestion sug = new Suggestion(rs.getString(1), rs
						.getString(2), rs.getString(3), rs.getString(4), rs
						.getString(5), rs.getString(6));
				content.add(sug);
			}

			// set values to JavaBean ShowPages
			ShowPages sp = new ShowPages();
			sp.setTotalRecorders(Integer.parseInt(initTotalRecorders));
			sp.setRecordersPerPage(Integer.parseInt(initRecordersPerPage));
			sp.setAvailablePages(Integer.parseInt(initAvailablePages));
			sp.setCurrentPage(Integer.parseInt(initCurrentPage));
			sp.setContent(content);

			// set attribute to request and forward to "/index.jsp"
			req.setAttribute("sp", sp);
			RequestDispatcher reqdis = req.getRequestDispatcher("/index.jsp");
			reqdis.forward(req, res);
			
			rs.close();
			prestm.close();
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


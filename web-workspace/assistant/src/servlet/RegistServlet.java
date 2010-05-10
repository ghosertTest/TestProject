package servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;

public class RegistServlet extends HttpServlet {

	private Connection con = null;

	public RegistServlet() {

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

		try {

			String sql = "select * from users where username=?";
			PreparedStatement prestm = con.prepareStatement(sql);
			prestm.setString(1, req.getParameter("username"));
			ResultSet rs = prestm.executeQuery();

			if (rs.next()) {

				res.setStatus(601);

			} else {

				String name = req.getParameter("name");
				String email = req.getParameter("email");
				String mobile = req.getParameter("mobile");
				if (name == null)
					name = "";
				if (email == null)
					email = "";
				if (mobile == null)
					mobile = "";

				sql = "insert into users values (null, ?, ?, ?, ?, ?, now(), ?)";
				prestm = con.prepareStatement(sql);
				prestm.setString(1, req.getParameter("username"));
				prestm.setString(2, req.getParameter("password"));
				prestm.setString(3, name);
				prestm.setString(4, email);
				prestm.setString(5, mobile);
				prestm.setString(6, req.getRemoteAddr());
				prestm.executeUpdate();
				RequestDispatcher reqdis = req
						.getRequestDispatcher("/LoginServlet");
				reqdis.forward(req, res);
			}

			rs.close();
			prestm.close();

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public void destroy() {
		try {

			con.close();
		} catch (SQLException sqlexc) {

			sqlexc.printStackTrace();
		}

	}

}
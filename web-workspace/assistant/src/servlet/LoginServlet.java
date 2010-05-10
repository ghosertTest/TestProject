package servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import assis.*;

public class LoginServlet extends HttpServlet {

	private Connection con = null;

	public LoginServlet() {

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
			String sql = "select * from users where username=? and password=?";
			PreparedStatement prestm = con.prepareStatement(sql);
			prestm.setString(1, req.getParameter("username"));
			prestm.setString(2, req.getParameter("password"));
			ResultSet rs = prestm.executeQuery();

			if (rs.next()) {
				User user = new User();
				user.setUserId(convert(rs.getString(1)));
				user.setUsername(convert(rs.getString(2)));
				user.setPassword(convert(rs.getString(3)));
				user.setName(convert(rs.getString(4)));
				user.setEmail(convert(rs.getString(5)));
				user.setMobile(convert(rs.getString(6)));
				user.setRegisterTime(convert(rs.getString(7)));
				user.setIp(convert(rs.getString(8)));
				req.getSession().setAttribute("user", user);
				RequestDispatcher reqdis = req
						.getRequestDispatcher("/index.jsp");
				reqdis.forward(req, res);
			} else {
				res.setStatus(600);
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

	public String convert(String s) {

		try {

			s = new String(s.getBytes("ISO-8859-1"), "GB2312");
		} catch (UnsupportedEncodingException usee) {

			usee.printStackTrace();
		}

		return s;
	}

}
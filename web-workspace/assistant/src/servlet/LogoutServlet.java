package servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LogoutServlet extends HttpServlet {

	public void init() throws ServletException {

	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.getSession().invalidate();
		RequestDispatcher reqdis = req.getRequestDispatcher("/index.jsp");
		reqdis.forward(req, res);
	}

	public void destroy() {

	}

}


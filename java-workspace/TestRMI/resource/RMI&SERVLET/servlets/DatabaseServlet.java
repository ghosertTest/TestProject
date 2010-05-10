// ==================================
// AUTHOR: Scott McPherson
// EMAIL:  scottm@mochamail.com
// DATE:   October 1, 1999
// Copyright (C) 1999 Scott McPherson
// ==================================

import java.io.*;
import java.rmi.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
* This is the super-class servlet that defines code
* common to all of the database access servlets.
*/
public class DatabaseServlet extends HttpServlet
{
	String registry; // The path to the RMI registry
	UserDatabase db; // The database access object

	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);

		// Get the location of the RMI registry.
		registry = config.getInitParameter("registry");

		// If undefined, assume the local host
		if (registry == null) registry = "//localhost";

		try {
			// Now get the database object
			String name = registry + "/DB1";
			db = (UserDatabase)Naming.lookup(name);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}

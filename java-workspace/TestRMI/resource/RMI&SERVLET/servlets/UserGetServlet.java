// ==================================
// AUTHOR: Scott McPherson
// EMAIL:  scottm@mochamail.com
// DATE:   October 1, 1999
// Copyright (C) 1999 Scott McPherson
// ==================================

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * This servlet retrieves an existing database entry.
 */
public class UserGetServlet extends DatabaseServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
	{
		// Open the I/O streams
		ObjectInputStream in = new ObjectInputStream(req.getInputStream() );
		ObjectOutputStream out = new ObjectOutputStream( resp.getOutputStream() );
		try {
			// 1. Read in parameters from the applet
			String id = (String)in.readObject();

			// 2. Execute the RMI call
			UserData data = db.getUser(id);

			// 3. Write the result
			out.writeObject(data);

		} catch (Exception e) { e.printStackTrace(); }

		// Close the I/O streams
		in.close();
		out.close();
	}
}
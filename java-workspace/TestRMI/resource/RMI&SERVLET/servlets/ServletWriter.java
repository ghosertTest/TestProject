// ==================================
// AUTHOR: Scott McPherson
// EMAIL:  scottm@mochamail.com
// DATE:   October 1, 1999
// Copyright (C) 1999 Scott McPherson
// ==================================

import java.net.*;
import java.io.*;

/**
* This class provides a simple method of posting multiple
* Serialized objects to a Java servlet and getting objects
* in return. This code was inspired by code samples from
* the book 'Java Servlet Programming' by Jason Hunter and
* William Crawford (O'Reilly & Associates. 1998).
*/
public class ServletWriter {

	static public ObjectInputStream postObjects(URL servlet, Serializable objs[]) throws Exception
	{
		URLConnection con = servlet.openConnection();
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		// Write the arguments as post data
		ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
		int numObjects = objs.length;
		for (int x = 0; x < numObjects; x++) {
			out.writeObject(objs[x]);
		}

		out.flush();
		out.close();

		return new ObjectInputStream( con.getInputStream() );
	}

}
// ==================================
// AUTHOR: Scott McPherson
// EMAIL:  scottm@mochamail.com
// DATE:   October 1, 1999
// Copyright (C) 1999 Scott McPherson
// ==================================

import java.io.*;
import java.net.*;
import java.util.*;

public class UserDatabaseProxy
{
	URL servlet;  // The fully-qualified URL to the called servlet.
	URL webBase;  // The complete URL to the servlet web server (ex. 'http://www.sun.com:8080').
	ObjectInputStream in; // Input stream from the servlet.

	public UserDatabaseProxy(URL web) 
	{
		webBase = web;
	}

	// Adds a user to the database
	public String createUser(UserData data) throws Exception 
	{
		servlet = new URL(webBase, "servlet/UserCreateServlet");
		Serializable objs[] = { data };
		in = ServletWriter.postObjects(servlet, objs);
		String id = (String)in.readObject();
		in.close();
		return id;
	}

	// Modifies a user in the database
	public void editUser(String userid, UserData data)  throws Exception 
	{
		servlet = new URL(webBase, "servlet/UserEditServlet");
		Serializable objs[] = { userid, data };
		in = ServletWriter.postObjects(servlet, objs);
		in.close();
	}

	// Deletes a user from the datbase
	public void deleteUser(String userid) throws Exception 
	{
		servlet = new URL(webBase, "servlet/UserDeleteServlet");
		Serializable objs[] = { userid };
		in = ServletWriter.postObjects(servlet, objs);
		in.close();
	}

	// Deletes a user from the datbase
	public UserData getUser(String userid) throws Exception 
	{
		servlet = new URL(webBase, "servlet/UserEditServlet");
		Serializable objs[] = { userid };
		in = ServletWriter.postObjects(servlet, objs);
		UserData data = (UserData)in.readObject();
		in.close();
		return data;
	}

	// Deletes a user from the datbase
	public Hashtable listUsers() throws Exception 
	{
		servlet = new URL(webBase, "servlet/UserListServlet");
		Serializable objs[] = { };
		in = ServletWriter.postObjects(servlet, objs);
		Hashtable database = (Hashtable)in.readObject();
		in.close();
		return database;
	}
}
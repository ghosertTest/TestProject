// ==================================
// AUTHOR: Scott McPherson
// EMAIL:  scottm@mochamail.com
// DATE:   October 1, 1999
// Copyright (C) 1999 Scott McPherson
// ==================================

import java.util.Hashtable;
import java.rmi.*;
import java.rmi.server.*;

/**
* This is the database access component of the
* web-based database application. Clients use
* this object to access a server-side database.
*/
public class UserDatabaseImpl extends java.rmi.server.UnicastRemoteObject implements UserDatabase 
{
	Hashtable users;

	public UserDatabaseImpl() throws java.rmi.RemoteException 
	{
		users = new Hashtable();

		// Initialize the database with sample values
		UserData data;
		data = new UserData("AARON", "Hank Aaron", "Outfield", "555-1212 x154", "Hank holds the all-time major league record for home runs with 755.");
		users.put(data.getId(), data);
		data = new UserData("BANKS", "Ernie Banks", "Shortstop", "555-1212 x138", "Ernie drove in 143 runs for the Cubs in 1959.");
		users.put(data.getId(), data);
		data = new UserData("CAREW", "Rod Carew", "First base", "555-1212 x122", "Rod won seven batting titles with the Twins from 1969 to 1978.");
		users.put(data.getId(), data);
		data = new UserData("DIMAGGIO", "Joe DiMaggio", "Center field", "555-1212 x187", "Joe hit safely in 56 consecutive games in 1941.");
		users.put(data.getId(), data);
		data = new UserData("EVERS", "Johnny Evers", "Second base", "555-1212 x104", "Johnny was a key member of Chicago's 1908 championship team.");
		users.put(data.getId(), data);
		data = new UserData("FOXX", "Jimmy Foxx", "First base", "555-1212 x117", "Jimmy hit 58 homers and drove in 169 runs in 1932.");
		users.put(data.getId(), data);
	}

	// Adds a user to the database
	public String createUser(UserData data) 
	{
		String userid = getUserId(data);
		data.setId(userid);
		users.put(userid, data);
		return userid;
	}

	// Adds a user to the database
	public void editUser(String userid, UserData data) 
	{
		users.put(userid, data);
	}

	// Deletes a user from the datbase
	public void deleteUser(String userid) 
	{
		users.remove(userid);
	}

	// Deletes a user from the datbase
	public UserData getUser(String userid) 
	{
		return (UserData)users.get(userid);
	}

	// Deletes a user from the datbase
	public Hashtable listUsers() 
	{
		return users;
	}

	private String getUserId(UserData data) 
	{
		String name = data.getName().toUpperCase();
		int spaceIndex = name.lastIndexOf(' ');
		if (spaceIndex == -1) 
			return name;
		else 
			return name.substring(spaceIndex + 1);
	}

}
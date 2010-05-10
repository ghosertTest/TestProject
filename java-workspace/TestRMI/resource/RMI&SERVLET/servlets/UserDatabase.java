// ==================================
// AUTHOR: Scott McPherson
// EMAIL:  scottm@mochamail.com
// DATE:   October 1, 1999
// Copyright (C) 1999 Scott McPherson
// ==================================

import java.util.Hashtable;

/**
* This interface defines the methods that client can
* execute on the remote database object (UserDatabaseImpl).
*/
public interface UserDatabase extends java.rmi.Remote 
{
	// Adds a user to the database
	public String createUser(UserData data) throws java.rmi.RemoteException;
	// Adds a user to the database
	public void editUser(String userid, UserData data) throws java.rmi.RemoteException;
	// Deletes a user from the datbase
	public void deleteUser(String userid) throws java.rmi.RemoteException;
	// Deletes a user from the datbase
	public UserData getUser(String userid) throws java.rmi.RemoteException;
	// Deletes a user from the datbase
	public Hashtable listUsers() throws java.rmi.RemoteException;
}
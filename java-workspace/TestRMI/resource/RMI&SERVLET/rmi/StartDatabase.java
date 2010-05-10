// ==================================
// AUTHOR: Scott McPherson
// EMAIL:  scottm@mochamail.com
// DATE:   October 1, 1999
// Copyright (C) 1999 Scott McPherson
// ==================================

import java.rmi.*;
import java.rmi.registry.*;
import java.io.*;
import java.util.*;
import java.net.*;

/**
* This application starts the RMI registry, creates an
* instance of the database access object, and binds it
* to the registry.
*/
public class StartDatabase 
{
	public StartDatabase() 
	{
		try {
			// Start up the registry
			Registry reg = java.rmi.registry.LocateRegistry.createRegistry(1099);
			// Create the Database object
			UserDatabaseImpl db = new UserDatabaseImpl();
			// Bind it to the registry
			Naming.rebind("DB1", db);
		} catch (java.rmi.UnknownHostException unx) {
			unx.printStackTrace();
		} catch (java.rmi.RemoteException rex) {
			rex.printStackTrace();
		} catch (java.net.MalformedURLException mfx) {
			mfx.printStackTrace();
		}
	}

	static public void main(String args[]) 
	{
		System.setSecurityManager(new RMISecurityManager());
		StartDatabase starter = new StartDatabase();
	}

}
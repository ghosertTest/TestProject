// ==================================
// AUTHOR: Scott McPherson
// EMAIL:  scottm@mochamail.com
// DATE:   October 1, 1999
// Copyright (C) 1999 Scott McPherson
// ==================================

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.*;
//import java.rmi.*;
import java.util.*;

/**
 * This class is a simple applet that acts as a database
 * client. This version of the applet uses a proxy object
 * to communicate with servlets on the server instead of
 * RMI (note that don't need to import java.rmi.* in this
 * version).
 */
public class DatabaseApplet extends Applet implements ActionListener, ItemListener
{
	public void init()
	{
		// INIT CONTROLS
		setLayout(new BorderLayout(5,5));
		setSize(465,300);
		setBackground(Color.lightGray);
		centerPnl = new java.awt.Panel();
		centerPnl.setLayout(new BorderLayout(5,10));
		add("Center", centerPnl);
		dataPnl = new java.awt.Panel();
		dataPnl.setLayout(new BorderLayout(0,0));
		centerPnl.add("North", dataPnl);
		labelsPnl = new java.awt.Panel();
		labelsPnl.setLayout(new GridLayout(4,1,0,0));
		dataPnl.add("West", labelsPnl);
		idLbl = new java.awt.Label("User ID: ",Label.RIGHT);
		labelsPnl.add(idLbl);
		nameLbl = new java.awt.Label("Name: ",Label.RIGHT);
		labelsPnl.add(nameLbl);
		deptLbl = new java.awt.Label("Department: ",Label.RIGHT);
		labelsPnl.add(deptLbl);
		phoneLbl = new java.awt.Label("Phone #:",Label.RIGHT);
		labelsPnl.add(phoneLbl);
		fieldsPnl = new java.awt.Panel();
		fieldsPnl.setLayout(new GridLayout(4,1,0,0));
		dataPnl.add("Center", fieldsPnl);
		idTxt = new java.awt.TextField();
		idTxt.setEditable(false);
		fieldsPnl.add(idTxt);
		nameTxt = new java.awt.TextField();
		fieldsPnl.add(nameTxt);
		deptTxt = new java.awt.TextField();
		fieldsPnl.add(deptTxt);
		phoneTxt = new java.awt.TextField();
		fieldsPnl.add(phoneTxt);
		commentPnl = new java.awt.Panel();
		commentPnl.setLayout(new BorderLayout(0,0));
		centerPnl.add("Center", commentPnl);
		commentTxt = new java.awt.TextArea();
		commentPnl.add("Center", commentTxt);
		southPnl = new java.awt.Panel();
		southPnl.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		add("South", southPnl);
		saveBtn = new java.awt.Button();
		saveBtn.setActionCommand("button");
		saveBtn.setLabel("Save Data");
		southPnl.add(saveBtn);
		createBtn = new java.awt.Button();
		createBtn.setActionCommand("button");
		createBtn.setLabel("Create Record");
		southPnl.add(createBtn);
		deleteBtn = new java.awt.Button();
		deleteBtn.setActionCommand("button");
		deleteBtn.setLabel("Delete Record");
		southPnl.add(deleteBtn);
		dataLst = new java.awt.List(0,false);
		add("East", dataLst);

		// REGISTER LISTENERS
		dataLst.addItemListener(this);
		createBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		saveBtn.addActionListener(this);

		// Get the database object
		try {
			URL codebase = this.getCodeBase();

			// Get the host, protcol and port
			String host = codebase.getHost();
			String protocol = codebase.getProtocol();
			int port = codebase.getPort();
    		
			// Build the URL for the servlet web server
			URL servletBase = new URL(protocol + "://" + host + ":" + port);
    		
			// Create the proxy object
			db = new UserDatabaseProxy(servletBase);

			// --The old RMI code--
			// String host = this.getCodeBase().getHost();
			// String name = "//" + host + "/DB1";
			// db = (UserDatabase)java.rmi.Naming.lookup(name);
		} catch (Exception ex) {
			db = null;
			ex.printStackTrace();
		}

		// Now load the user list
		if (db != null) try {
			Hashtable users = db.listUsers();
			Enumeration keys = users.keys();
			String nextKey;
			while ( keys.hasMoreElements() ) 
			{
				nextKey = (String)keys.nextElement();
				dataLst.addItem(nextKey);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// DECLARE CONTROLS
	java.awt.Panel centerPnl;
	java.awt.Panel dataPnl;
	java.awt.Panel labelsPnl;
	java.awt.Label idLbl;
	java.awt.Label nameLbl;
	java.awt.Label deptLbl;
	java.awt.Label phoneLbl;
	java.awt.Panel fieldsPnl;
	java.awt.TextField idTxt;
	java.awt.TextField nameTxt;
	java.awt.TextField deptTxt;
	java.awt.TextField phoneTxt;
	java.awt.Panel commentPnl;
	java.awt.TextArea commentTxt;
	java.awt.Panel southPnl;
	java.awt.Button saveBtn;
	java.awt.Button createBtn;
	java.awt.Button deleteBtn;
	java.awt.List dataLst;

	UserDatabaseProxy db; // The proxy database object


	public void itemStateChanged(java.awt.event.ItemEvent event)
	{
		Object object = event.getSource();
		if (object == dataLst)
		dataLst_ItemStateChanged(event);
	}

	void dataLst_ItemStateChanged(java.awt.event.ItemEvent event)
	{
		String selected = dataLst.getSelectedItem();
		UserData data = null;
		try {
			data = db.getUser(selected);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (data!= null) {
			idTxt.setText( data.getId() );
			nameTxt.setText( data.getName() );
			deptTxt.setText( data.getDept() );
			phoneTxt.setText( data.getPhone() );
			commentTxt.setText( data.getComment() );
		}
	}

	public void actionPerformed(java.awt.event.ActionEvent event)
	{
		Object object = event.getSource();
		if (object == createBtn)
			createBtn_Action(event);
		else if (object == deleteBtn)
			deleteBtn_Action(event);
		else if (object == saveBtn)
			saveBtn_Action(event);
	}

	void createBtn_Action(java.awt.event.ActionEvent event)
	{
		idTxt.setText("");
		nameTxt.setText("");
		deptTxt.setText("");
		phoneTxt.setText("");
		commentTxt.setText("");
	}

	void deleteBtn_Action(java.awt.event.ActionEvent event)
	{
		String selected = dataLst.getSelectedItem();
		try {
			db.deleteUser(selected);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		dataLst.remove(selected);
		idTxt.setText("");
		nameTxt.setText("");
		deptTxt.setText("");
		phoneTxt.setText("");
		commentTxt.setText("");
	}

	void saveBtn_Action(java.awt.event.ActionEvent event)
	{
		String id       = idTxt.getText();
		String name     = nameTxt.getText();
		String dept     = deptTxt.getText();
		String phone    = phoneTxt.getText();
		String comment  = commentTxt.getText();

		UserData data = new UserData(id, name, dept, phone, comment);
		try {
			if ( id.trim().equals("") ) {
				String uid = db.createUser(data);
				idTxt.setText(uid);
				dataLst.addItem(uid);
			}
			else {
				db.editUser(id, data);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

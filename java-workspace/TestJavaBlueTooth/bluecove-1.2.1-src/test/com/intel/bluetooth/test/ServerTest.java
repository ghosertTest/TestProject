/*
 Copyright 2004 Intel Corporation

 This file is part of Blue Cove.

 Blue Cove is free software; you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation; either version 2.1 of the License, or
 (at your option) any later version.

 Blue Cove is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with Blue Cove; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.intel.bluetooth.test;

import java.io.DataInputStream;
import java.io.IOException;

import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class ServerTest {
	
	public static final UUID uuid = new UUID(Consts.TEST_UUID, false);

	public ServerTest(String name) {
		
		int connectionsCount = 0;
		while (run(name) && connectionsCount < 10) {
			connectionsCount ++;
		}
		
		System.exit(0);
	}
	
	public boolean run(String name) {
		try {
			StreamConnectionNotifier server = (StreamConnectionNotifier) Connector
					.open("btspp://localhost:"
							+ uuid
							+ ";name="
							+ name
							+ ";authorize=false;authenticate=false;encrypt=false");

			System.out.println("Server started " + name);
			
			StreamConnection conn = server.acceptAndOpen();

			System.out.println("Server received connection");
			
			DataInputStream dis = new DataInputStream(conn.openInputStream());

			System.out.print("Got message[");
			System.out.print(dis.readUTF());
			System.out.println("]");
			
			dis.close();

			conn.close();

			server.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		if (args.length == 1)
			new ServerTest(Consts.TEST_SERVERNAME_PREFIX + args[0]);
		else {
			System.out.println("syntax: ServerTest <service name>");
			new ServerTest(Consts.TEST_SERVERNAME_PREFIX + "1");
		}
	}
}
package com.intel.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;

import javax.bluetooth.UUID;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;

/**
 * 
 * This is renamed class javax.microedition.io.Connector
 * 
 */
public class MicroeditionConnector {
	/*
	 * Access mode READ. The value 1 is assigned to READ.
	 */

	public static final int READ = 1;

	/*
	 * Access mode WRITE. The value 2 is assigned to WRITE.
	 */

	public static final int WRITE = 2;

	/*
	 * Access mode READ_WRITE. The value 3 is assigned to READ_WRITE.
	 */

	public static final int READ_WRITE = 3;

	/*
	 * Create and open a Connection. Parameters: name - The URL for the
	 * connection. Returns: A new Connection object. Throws:
	 * IllegalArgumentException - If a parameter is invalid.
	 * ConnectionNotFoundException - If the requested connection cannot be made,
	 * or the protocol type does not exist. java.io.IOException - If some other
	 * kind of I/O error occurs. SecurityException - If a requested protocol
	 * handler is not permitted.
	 */

	private static String[] params = { "authenticate", "encrypt", "master",
			"name" };

	public static Connection open(String name) throws IOException {
		/*
		 * parse URL
		 */

		String host = null;
		String port = null;

		String[] values = new String[4];

		if (name.substring(0, 8).equals("btspp://")) {
			int colon = name.indexOf(':', 8);

			if (colon > -1) {
				host = name.substring(8, colon);

				StringTokenizer tok = new StringTokenizer(name
						.substring(colon + 1), ";");

				if (tok.hasMoreTokens()) {
					port = tok.nextToken();

					while (tok.hasMoreTokens()) {
						String t = tok.nextToken();

						int equals = t.indexOf('=');

						if (equals > -1) {
							String param = t.substring(0, equals);
							String value = t.substring(equals + 1);

							for (int i = 0; i < 4; i++)
								if (param.equals(params[i])) {
									values[i] = value;

									break;
								}
						}
					}
				}
			}
		} else
			throw new ConnectionNotFoundException();

		if (host == null || port == null)
			throw new IllegalArgumentException();

		/*
		 * create connection
		 */

		try {
			if (host.equals("localhost"))
				return new BluetoothStreamConnectionNotifier(new UUID(port,
						false), values[0] != null && values[0].equals("true"),
						values[1] != null && values[1].equals("true"),
						values[3]);
			else
				return new BluetoothConnection(Long.parseLong(host, 16),
						Integer.parseInt(port), values[0] != null
								&& values[0].equals("true"), values[1] != null
								&& values[1].equals("true"));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException();
		}
	}

	/*
	 * Create and open a Connection. Parameters: name - The URL for the
	 * connection. mode - The access mode. Returns: A new Connection object.
	 * Throws: IllegalArgumentException - If a parameter is invalid.
	 * ConnectionNotFoundException - If the requested connection cannot be made,
	 * or the protocol type does not exist. java.io.IOException - If some other
	 * kind of I/O error occurs. SecurityException - If a requested protocol
	 * handler is not permitted.
	 */

	public static Connection open(String name, int mode) throws IOException {
		return open(name);
	}

	/*
	 * Create and open a Connection. Parameters: name - The URL for the
	 * connection mode - The access mode timeouts - A flag to indicate that the
	 * caller wants timeout exceptions Returns: A new Connection object Throws:
	 * IllegalArgumentException - If a parameter is invalid.
	 * ConnectionNotFoundException - if the requested connection cannot be made,
	 * or the protocol type does not exist. java.io.IOException - If some other
	 * kind of I/O error occurs. SecurityException - If a requested protocol
	 * handler is not permitted.
	 */

	public static Connection open(String name, int mode, boolean timeouts)
			throws IOException {
		return open(name);
	}

	/*
	 * Create and open a connection input stream. Parameters: name - The URL for
	 * the connection. Returns: A DataInputStream. Throws:
	 * IllegalArgumentException - If a parameter is invalid.
	 * ConnectionNotFoundException - If the connection cannot be found.
	 * java.io.IOException - If some other kind of I/O error occurs.
	 * SecurityException - If access to the requested stream is not permitted.
	 */

	public static DataInputStream openDataInputStream(String name)
			throws IOException {
		return new DataInputStream(openInputStream(name));
	}

	/*
	 * Create and open a connection output stream. Parameters: name - The URL
	 * for the connection. Returns: A DataOutputStream. Throws:
	 * IllegalArgumentException - If a parameter is invalid.
	 * ConnectionNotFoundException - If the connection cannot be found.
	 * java.io.IOException - If some other kind of I/O error occurs.
	 * SecurityException - If access to the requested stream is not permitted.
	 */

	public static DataOutputStream openDataOutputStream(String name)
			throws IOException {
		return new DataOutputStream(openOutputStream(name));
	}

	/*
	 * Create and open a connection input stream. Parameters: name - The URL for
	 * the connection. Returns: An InputStream. Throws: IllegalArgumentException -
	 * If a parameter is invalid. ConnectionNotFoundException - If the
	 * connection cannot be found. java.io.IOException - If some other kind of
	 * I/O error occurs. SecurityException - If access to the requested stream
	 * is not permitted.
	 */

	public static InputStream openInputStream(String name) throws IOException {
		throw new NotImplementedIOException();
	}

	/*
	 * Create and open a connection output stream. Parameters: name - The URL
	 * for the connection. Returns: An OutputStream. Throws:
	 * IllegalArgumentException - If a parameter is invalid.
	 * ConnectionNotFoundException - If the connection cannot be found.
	 * java.io.IOException - If some other kind of I/O error occurs.
	 * SecurityException - If access to the requested stream is not permitted.
	 */

	public static OutputStream openOutputStream(String name) throws IOException {
		throw new NotImplementedIOException();
	}

}
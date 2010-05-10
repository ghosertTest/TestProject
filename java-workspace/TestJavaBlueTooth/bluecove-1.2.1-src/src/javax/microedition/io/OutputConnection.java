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
package javax.microedition.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface OutputConnection extends Connection {
	/*
	 * Open and return an output stream for a connection. Returns: An output
	 * stream Throws: IOException - If an I/O error occurs
	 */

	public OutputStream openOutputStream() throws IOException;

	/*
	 * Open and return a data output stream for a connection. Returns: An output
	 * stream Throws: IOException - If an I/O error occurs
	 */

	public DataOutputStream openDataOutputStream() throws IOException;
}
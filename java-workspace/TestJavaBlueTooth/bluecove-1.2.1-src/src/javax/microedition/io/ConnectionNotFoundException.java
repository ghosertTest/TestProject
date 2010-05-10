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

import java.io.IOException;

public class ConnectionNotFoundException extends IOException {
	/*
	 * Creates a new ConnectionNotFoundException without a detail message.
	 */

	private static final long serialVersionUID = 1L;

	public ConnectionNotFoundException() {
	}

	/*
	 * Creates a ConnectionNotFoundException with the specified detail message.
	 * Parameters: msg - the reason for the exception
	 */

	public ConnectionNotFoundException(String msg) {
		super(msg);
	}
}
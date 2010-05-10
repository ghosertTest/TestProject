/*
 * HelloDllStub.java -
 *
 * This file is part of the Jawin Project: http://jawinproject.sourceforge.net/
 * 
 * Please consult the LICENSE file in the project root directory,
 * or at the project site before using this software.
 */

/* $Id: HelloDllStub.java,v 1.3 2004/06/14 20:22:23 arosii_moa Exp $ */

package demos;

import org.jawin.donated.win32.User32;

/**
 * Demo that uses the Win32 MessageBoxW API-method through a stub-class. 
 *
 * @version     $Revision: 1.3 $
 * @author      Stuart Halloway, http://www.relevancellc.com/halloway/weblog/
 */
public class HelloDllStub {
	public static void main(String[] args) {
		try {
			User32.MessageBoxW("Hello from a DLL stub", "Jawin");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

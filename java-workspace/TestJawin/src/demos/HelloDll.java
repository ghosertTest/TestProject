/*
 * HelloDll.java -
 *
 * This file is part of the Jawin Project: http://jawinproject.sourceforge.net/
 * 
 * Please consult the LICENSE file in the project root directory,
 * or at the project site before using this software.
 */

/* $Id: HelloDll.java,v 1.3 2004/06/14 20:16:38 arosii_moa Exp $ */

package demos;

import org.jawin.COMException;
import org.jawin.FuncPtr;
import org.jawin.ReturnFlags;

/**
 * Demo that uses the Win32 MessageBoxW API-method.
 *
 * @version     $Revision: 1.3 $
 * @author      Stuart Halloway, http://www.relevancellc.com/halloway/weblog/
 */
public class HelloDll {

	public static void main(String[] args) throws Exception {
		FuncPtr msgBox = null;
		try {
			msgBox = new FuncPtr("USER32.DLL", "MessageBoxW");
			msgBox.invoke_I(0, "Hello From a DLL", "From Jawin", 0, ReturnFlags.CHECK_FALSE);
		} catch (COMException e) {
			// handle exception
			e.printStackTrace();
			throw e;
		} finally {
			if (msgBox != null) {
				try {
					msgBox.close();
				} catch (COMException e) {
					// handle fatal exception
					e.printStackTrace();
					throw e;
				}
			}
		}
	}
}

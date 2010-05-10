/*
 * HelloDllGeneric.java -
 *
 * This file is part of the Jawin Project: http://jawinproject.sourceforge.net/
 * 
 * Please consult the LICENSE file in the project root directory,
 * or at the project site before using this software.
 */

/* $Id: HelloDllGeneric.java,v 1.1 2004/06/14 20:22:23 arosii_moa Exp $ */

package demos;

import org.jawin.COMException;
import org.jawin.FuncPtr;
import org.jawin.ReturnFlags;
import org.jawin.io.LittleEndianOutputStream;
import org.jawin.io.NakedByteStream;

/**
 * Demo that uses the Win32 MessageBoxW API-method with the generic FuncPtr invoke.
 *
 * @version     $Revision: 1.1 $
 * @author      Morten Andersen, arosii_moa (at) users.sourceforge.net
 */
public class HelloDllGeneric {

	public static void main(String[] args) throws Exception {
		FuncPtr msgBox = null;
		try {
			msgBox = new FuncPtr("USER32.DLL", "MessageBoxW");

			NakedByteStream nbs = new NakedByteStream();
			LittleEndianOutputStream leos = new LittleEndianOutputStream(nbs);
			leos.writeInt(0);
			leos.writeStringUnicode("Generic Hello From a DLL");
			leos.writeStringUnicode("From Jawin");
			leos.writeInt(0);
			msgBox.invoke("IGGI:I:", 16, nbs, null, ReturnFlags.CHECK_FALSE);
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

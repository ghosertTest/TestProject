//$Id: FolderImpl.java,v 1.1 2004/08/22 01:20:07 oneovthafew Exp $
package org.hibernate.test.interfaceproxy;

/**
 * @author Gavin King
 */
public class FolderImpl extends ItemImpl implements Folder {
	private Folder parent;
	/**
	 * @return Returns the parent.
	 */
	public Folder getParent() {
		return parent;
	}
	/**
	 * @param parent The parent to set.
	 */
	public void setParent(Folder parent) {
		this.parent = parent;
	}
}

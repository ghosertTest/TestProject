//$Id: SecureDocument.java,v 1.2 2005/11/25 17:36:29 epbernard Exp $

package org.hibernate.test.mixed;


/**
 * @author Gavin King
 */

public class SecureDocument extends Document {

	private byte permissionBits;

	private String owner;

	/**
	 * @return Returns the owner.
	 */

	public String getOwner() {

		return owner;

	}

	/**
	 * @param owner The owner to set.
	 */

	public void setOwner(String owner) {

		this.owner = owner;

	}

	/**
	 * @return Returns the permissionBits.
	 */

	public byte getPermissionBits() {

		return permissionBits;

	}

	/**
	 * @param permissionBits The permissionBits to set.
	 */

	public void setPermissionBits(byte permissionBits) {

		this.permissionBits = permissionBits;

	}

}


//$Id: SecureDocument.java,v 1.1 2004/08/22 01:20:07 oneovthafew Exp $
package org.hibernate.test.interfaceproxy;

/**
 * @author Gavin King
 */
public interface SecureDocument extends Document {
	/**
	 * @return Returns the owner.
	 */
	public String getOwner();

	/**
	 * @param owner The owner to set.
	 */
	public void setOwner(String owner);

	/**
	 * @return Returns the permissionBits.
	 */
	public byte getPermissionBits();

	/**
	 * @param permissionBits The permissionBits to set.
	 */
	public void setPermissionBits(byte permissionBits);
}
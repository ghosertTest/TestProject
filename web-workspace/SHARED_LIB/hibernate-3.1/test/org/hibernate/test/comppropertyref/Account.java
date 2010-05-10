//$Id: Account.java,v 1.1 2005/07/21 01:22:38 oneovthafew Exp $
package org.hibernate.test.comppropertyref;

public class Account {
	private String number;
	private Person owner;

	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}

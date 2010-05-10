//$Id: BankAccount.java,v 1.1 2005/06/22 17:07:29 oneovthafew Exp $
package org.hibernate.test.joineduid;

public class BankAccount extends Account {
	private String accountNumber;
	private String bsb;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBsb() {
		return bsb;
	}

	public void setBsb(String bsb) {
		this.bsb = bsb;
	}
}

//$Id: J.java,v 1.2 2005/06/19 02:01:05 oneovthafew Exp $
package org.hibernate.test.legacy;

/**
 * @author Gavin King
 */
public class J extends I {
	private float amount;

	void setAmount(float amount) {
		this.amount = amount;
	}

	float getAmount() {
		return amount;
	}
}

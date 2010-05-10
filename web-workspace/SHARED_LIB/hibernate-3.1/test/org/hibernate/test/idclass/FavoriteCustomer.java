//$Id: FavoriteCustomer.java,v 1.1 2005/08/11 21:46:58 epbernard Exp $
package org.hibernate.test.idclass;

/**
 * @author Emmanuel Bernard
 */
public class FavoriteCustomer extends Customer {
	public FavoriteCustomer() {
	}

	public FavoriteCustomer(String orgName, String custName, String add) {
		super( orgName, custName, add );
	}
}

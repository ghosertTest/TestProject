//$Id: Identity.java,v 1.1 2005/07/21 01:22:38 oneovthafew Exp $
package org.hibernate.test.comppropertyref;

public class Identity {
	private String name;
	private String ssn;
	
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String id) {
		this.ssn = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

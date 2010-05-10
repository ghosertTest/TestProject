//$Id: Person.java,v 1.1 2005/07/21 01:22:38 oneovthafew Exp $
package org.hibernate.test.comppropertyref;

public class Person {
	private Long id;
	private Identity identity;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Identity getIdentity() {
		return identity;
	}
	public void setIdentity(Identity identity) {
		this.identity = identity;
	}
}

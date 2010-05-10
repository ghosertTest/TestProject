//$Id: Custom.java,v 1.2 2005/06/22 18:58:15 oneovthafew Exp $
package org.hibernate.test.legacy;


public class Custom implements Cloneable {
	String id;
	private String name;
	
	public Object clone() {
		try {
			return super.clone();
		}
		catch (CloneNotSupportedException cnse) {
			throw new RuntimeException();
		}
	}

	void setName(String name) {
		this.name = name;
	}

	String getName() {
		return name;
	}

}







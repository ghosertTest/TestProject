//$Id: I.java,v 1.2 2005/06/19 02:01:05 oneovthafew Exp $
package org.hibernate.test.legacy;

/**
 * @author Gavin King
 */
public class I {
	private Long id;
	private String name;
	private char type;
	private K parent;
	
	public K getParent() {
		return parent;
	}
	
	public void setParent(K parent) {
		this.parent = parent;
	}

	void setType(char type) {
		this.type = type;
	}

	char getType() {
		return type;
	}

	void setName(String name) {
		this.name = name;
	}

	String getName() {
		return name;
	}
}

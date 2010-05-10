//$Id: Employee.java,v 1.1 2005/04/03 03:56:22 oneovthafew Exp $
package org.hibernate.test.unconstrained;

/**
 * @author Gavin King
 */
public class Employee {
	
	private String id;

	public Employee() {
	}

	public Employee(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	

	public void setId(String id) {
		this.id = id;
	}
	

}

//$Id: Person.java,v 1.1 2005/04/03 03:56:22 oneovthafew Exp $
package org.hibernate.test.unconstrained;

/**
 * @author Gavin King
 */
public class Person {
	
	private String name;
	private String employeeId;
	private Employee employee;

	public Person() {}
	public Person(String name) {
		this.name = name;
	}
	
	public Employee getEmployee() {
		return employee;
	}
	
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	public String getEmployeeId() {
		return employeeId;
	}
	
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	

}

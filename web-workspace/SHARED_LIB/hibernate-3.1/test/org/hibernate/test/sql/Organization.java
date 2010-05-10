//$Id: Organization.java,v 1.6 2005/07/19 18:21:35 oneovthafew Exp $
package org.hibernate.test.sql;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Gavin King
 */
public class Organization {
	private long id;
	private String name;
	private Collection employments;

	public Organization(String name) {
		this.name = name;
		employments = new HashSet();
	}

	public Organization() {}

	/**
	 * @return Returns the employments.
	 */
	public Collection getEmployments() {
		return employments;
	}
	/**
	 * @param employments The employments to set.
	 */
	public void setEmployments(Collection employments) {
		this.employments = employments;
	}
	/**
	 * @return Returns the id.
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}

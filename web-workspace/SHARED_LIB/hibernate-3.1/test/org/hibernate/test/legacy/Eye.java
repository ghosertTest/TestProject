//$Id: Eye.java,v 1.1 2004/09/26 05:18:25 oneovthafew Exp $
package org.hibernate.test.legacy;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Gavin King
 */
public class Eye {
	private long id;
	private String name;
	private Jay jay;
	private Set jays = new HashSet();

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
	 * @return Returns the jay.
	 */
	public Jay getJay() {
		return jay;
	}

	/**
	 * @param jay The jay to set.
	 */
	public void setJay(Jay jay) {
		this.jay = jay;
	}

	/**
	 * @return Returns the jays.
	 */
	public Set getJays() {
		return jays;
	}

	/**
	 * @param jays The jays to set.
	 */
	public void setJays(Set jays) {
		this.jays = jays;
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

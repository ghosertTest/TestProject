// $Id: Silly.java,v 1.1 2005/05/12 17:35:35 steveebersole Exp $
package org.hibernate.test.connections;

import java.io.Serializable;

/**
 * Implementation of Silly.
 *
 * @author Steve Ebersole
 */
public class Silly implements Serializable {
	private Long id;
	private String name;

	public Silly() {
	}

	public Silly(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

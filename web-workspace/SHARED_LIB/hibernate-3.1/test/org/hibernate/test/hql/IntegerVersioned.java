// $Id: IntegerVersioned.java,v 1.1 2005/07/06 17:04:45 steveebersole Exp $
package org.hibernate.test.hql;

/**
 * Implementation of IntegerVersioned.
 *
 * @author Steve Ebersole
 */
public class IntegerVersioned {
	private Long id;
	private int version = -1;
	private String name;

	public IntegerVersioned() {
	}

	public IntegerVersioned(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public int getVersion() {
		return version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

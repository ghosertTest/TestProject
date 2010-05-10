// $Id: KeyManyToOneKeyEntity.java,v 1.1 2005/06/08 19:31:26 steveebersole Exp $
package org.hibernate.test.hql;

/**
 * Implementation of KeyManyToOneKeyEntity.
 *
 * @author Steve Ebersole
 */
public class KeyManyToOneKeyEntity {
	private Long id;
	private String name;

	public KeyManyToOneKeyEntity(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

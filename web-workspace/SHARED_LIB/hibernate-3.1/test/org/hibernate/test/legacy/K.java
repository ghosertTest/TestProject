//$Id: K.java,v 1.2 2005/06/19 02:01:05 oneovthafew Exp $
package org.hibernate.test.legacy;

import java.util.Set;

/**
 * @author Gavin King
 */
public class K {
	private Long id;
	private Set is;
	void setIs(Set is) {
		this.is = is;
	}
	Set getIs() {
		return is;
	}
}

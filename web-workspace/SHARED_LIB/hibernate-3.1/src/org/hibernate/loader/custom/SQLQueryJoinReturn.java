// $Id: SQLQueryJoinReturn.java,v 1.5 2005/06/19 22:16:38 maxcsaucdk Exp $
package org.hibernate.loader.custom;

import java.util.Map;

import org.hibernate.LockMode;

/**
 * Represents a return defined as part of a native sql query which
 * names a fetched role.
 *
 * @author Steve
 */
public class SQLQueryJoinReturn extends SQLQueryReturn {
	private String ownerAlias;
	private String ownerProperty;

	public SQLQueryJoinReturn(String alias, String ownerAlias, String ownerProperty, Map propertyResults, LockMode lockMode) {
		super(alias, propertyResults, lockMode);
		this.ownerAlias = ownerAlias;
		this.ownerProperty = ownerProperty;
	}

	public String getOwnerAlias() {
		return ownerAlias;
	}

	public String getOwnerProperty() {
		return ownerProperty;
	}
}

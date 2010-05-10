//$Id: Sybase11Dialect.java,v 1.1 2004/06/03 16:30:07 steveebersole Exp $
package org.hibernate.dialect;

import org.hibernate.sql.JoinFragment;
import org.hibernate.sql.Sybase11JoinFragment;

/**
 * A SQL dialect suitable for use with Sybase 11.9.2 (specifically: avoids ANSI JOIN syntax)
 * @author Colm O' Flaherty
 */
public class Sybase11Dialect extends SybaseDialect  {
	public Sybase11Dialect() {
		super();
	}

	public JoinFragment createOuterJoinFragment() {
		return new Sybase11JoinFragment();
	}

}

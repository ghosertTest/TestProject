// $Id: DeleteStatement.java,v 1.1 2005/07/12 20:27:16 steveebersole Exp $
package org.hibernate.hql.ast.tree;

import org.hibernate.hql.antlr.HqlSqlTokenTypes;
import org.hibernate.hql.antlr.SqlTokenTypes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Defines a top-level AST node representing an HQL delete statement.
 *
 * @author Steve Ebersole
 */
public class DeleteStatement extends AbstractRestrictableStatement {

	private static final Log log = LogFactory.getLog( DeleteStatement.class );

	/**
	 * @see org.hibernate.hql.ast.tree.Statement#getStatementType()
	 */
	public int getStatementType() {
		return HqlSqlTokenTypes.DELETE;
	}

	/**
	 * @see org.hibernate.hql.ast.tree.Statement#needsExecutor()
	 */
	public boolean needsExecutor() {
		return true;
	}

	protected int getWhereClauseParentTokenType() {
		return SqlTokenTypes.FROM;
	}

	protected Log getLog() {
		return log;
	}
}

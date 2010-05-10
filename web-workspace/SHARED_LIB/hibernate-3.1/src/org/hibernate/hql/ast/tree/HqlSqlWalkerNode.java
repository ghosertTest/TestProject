// $Id: HqlSqlWalkerNode.java,v 1.1 2005/07/12 20:27:16 steveebersole Exp $
package org.hibernate.hql.ast.tree;

import org.hibernate.hql.ast.HqlSqlWalker;
import org.hibernate.hql.ast.util.AliasGenerator;
import org.hibernate.hql.ast.util.SessionFactoryHelper;

import antlr.ASTFactory;

/**
 * A semantic analysis node, that points back to the main analyzer.
 *
 * @author josh Sep 24, 2004 4:08:13 PM
 */
public class HqlSqlWalkerNode extends SqlNode implements InitializeableNode {
	/**
	 * A pointer back to the phase 2 processor.
	 */
	private HqlSqlWalker walker;

	public void initialize(Object param) {
		walker = ( HqlSqlWalker ) param;
	}

	public HqlSqlWalker getWalker() {
		return walker;
	}

	public SessionFactoryHelper getSessionFactoryHelper() {
		return walker.getSessionFactoryHelper();
	}

	public ASTFactory getASTFactory() {
		return walker.getASTFactory();
	}

	public AliasGenerator getAliasGenerator() {
		return walker.getAliasGenerator();
	}
}

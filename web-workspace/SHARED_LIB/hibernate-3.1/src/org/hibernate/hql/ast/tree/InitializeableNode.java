// $Id: InitializeableNode.java,v 1.1 2005/07/12 20:27:16 steveebersole Exp $

package org.hibernate.hql.ast.tree;

/**
 * An interface for initializeable AST nodes.
 */
public interface InitializeableNode {
	/**
	 * Initializes the node with the parameter.
	 *
	 * @param param the initialization parameter.
	 */
	void initialize(Object param);
}

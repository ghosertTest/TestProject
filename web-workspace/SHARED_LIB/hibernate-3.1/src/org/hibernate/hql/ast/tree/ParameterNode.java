// $Id: ParameterNode.java,v 1.2 2005/11/02 18:47:14 steveebersole Exp $
package org.hibernate.hql.ast.tree;

import org.hibernate.param.ParameterSpecification;

/**
 * Implementation of ParameterNode.
 *
 * @author Steve Ebersole
 */
public class ParameterNode extends HqlSqlWalkerNode implements DisplayableNode {
	private ParameterSpecification parameterSpecification;

	public ParameterSpecification getHqlParameterSpecification() {
		return parameterSpecification;
	}

	public void setHqlParameterSpecification(ParameterSpecification parameterSpecification) {
		this.parameterSpecification = parameterSpecification;
	}

	public String getDisplayText() {
		return "{" + ( parameterSpecification == null ? "???" : parameterSpecification.renderDisplayInfo() ) + "}";
	}
}

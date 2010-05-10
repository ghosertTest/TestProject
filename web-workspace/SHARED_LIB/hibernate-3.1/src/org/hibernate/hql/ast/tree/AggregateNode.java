// $Id: AggregateNode.java,v 1.1 2005/07/12 20:27:16 steveebersole Exp $
package org.hibernate.hql.ast.tree;

import org.hibernate.hql.ast.util.ColumnHelper;
import org.hibernate.type.Type;

import antlr.SemanticException;

/**
 * Represents an aggregate function i.e. min, max, sum, avg.
 *
 * @author josh Sep 21, 2004 9:22:02 PM
 */
public class AggregateNode extends AbstractSelectExpression implements SelectExpression {

	public AggregateNode() {
	}

	public Type getDataType() {
		// Get the function return value type, based on the type of the first argument.
		return getSessionFactoryHelper().findFunctionReturnType( getText(), getFirstChild() );
	}

	public void setScalarColumnText(int i) throws SemanticException {
		ColumnHelper.generateSingleScalarColumn( this, i );
	}
}

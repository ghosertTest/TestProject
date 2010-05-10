// $Id: CountNode.java,v 1.1 2005/07/12 20:27:16 steveebersole Exp $
package org.hibernate.hql.ast.tree;

import org.hibernate.hql.ast.util.ColumnHelper;
import org.hibernate.type.Type;

import antlr.SemanticException;

/**
 * Represents a COUNT expression in a select.
 *
 * @author josh Sep 21, 2004 9:23:40 PM
 */
public class CountNode extends AbstractSelectExpression implements SelectExpression {
	
	public Type getDataType() {
		return getSessionFactoryHelper().findFunctionReturnType( getText(), null );
	}

	public void setScalarColumnText(int i) throws SemanticException {
		ColumnHelper.generateSingleScalarColumn( this, i );
	}

}

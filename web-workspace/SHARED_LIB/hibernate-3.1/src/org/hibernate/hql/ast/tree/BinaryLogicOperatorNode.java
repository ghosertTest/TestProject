package org.hibernate.hql.ast.tree;

import org.hibernate.type.Type;
import org.hibernate.Hibernate;
import antlr.SemanticException;

/**
 * Contract for nodes representing binary operators.
 *
 * @author <a href="mailto:steve@hibernate.org">Steve Ebersole </a>
 */
public class BinaryLogicOperatorNode extends SqlNode implements BinaryOperatorNode {
	/**
	 * Performs the operator node initialization by seeking out any parameter
	 * nodes and setting their expected type, if possible.
	 */
	public void initialize() throws SemanticException {
		Node lhs = getLeftHandOperand();
		if ( lhs == null ) {
			throw new SemanticException( "left-hand operand of a binary operator was null" );
		}
		Node rhs = getRightHandOperand();
		if ( rhs == null ) {
			throw new SemanticException( "right-hand operand of a binary operator was null" );
		}
		if ( ParameterNode.class.isAssignableFrom( lhs.getClass() )
		     && SqlNode.class.isAssignableFrom( rhs.getClass() ) ) {
			( ( ParameterNode ) lhs ).getHqlParameterSpecification().setExpectedType(
					( ( SqlNode ) rhs ).getDataType()
			);
		}
		else if ( ParameterNode.class.isAssignableFrom( rhs.getClass() )
		          && SqlNode.class.isAssignableFrom( lhs.getClass() ) ) {
			( ( ParameterNode ) rhs ).getHqlParameterSpecification().setExpectedType(
					( ( SqlNode ) lhs ).getDataType()
			);
		}
	}

	public Type getDataType() {
		// logic operators by definition resolve to booleans
		return Hibernate.BOOLEAN;
	}

	/**
	 * Retrieves the left-hand operand of the operator.
	 *
	 * @return The left-hand operand
	 */
	public Node getLeftHandOperand() {
		return ( Node ) getFirstChild();
	}

	/**
	 * Retrieves the right-hand operand of the operator.
	 *
	 * @return The right-hand operand
	 */
	public Node getRightHandOperand() {
		return ( Node ) getFirstChild().getNextSibling();
	}
}

//$Id: EmptyExpression.java,v 1.9 2005/05/03 20:11:42 steveebersole Exp $
package org.hibernate.criterion;

/**
 * @author Gavin King
 */
public class EmptyExpression extends AbstractEmptinessExpression implements Criterion {

	protected EmptyExpression(String propertyName) {
		super( propertyName );
	}

	protected boolean excludeEmpty() {
		return false;
	}

}

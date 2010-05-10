//$Id: NotEmptyExpression.java,v 1.9 2005/05/03 20:11:42 steveebersole Exp $
package org.hibernate.criterion;

/**
 * @author Gavin King
 */
public class NotEmptyExpression extends AbstractEmptinessExpression implements Criterion {

	protected NotEmptyExpression(String propertyName) {
		super( propertyName );
	}

	protected boolean excludeEmpty() {
		return true;
	}

}

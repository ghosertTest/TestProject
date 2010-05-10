//$Id: PrimitiveArray.java,v 1.2 2004/12/07 09:59:47 maxcsaucdk Exp $
package org.hibernate.mapping;

/**
 * A primitive array has a primary key consisting
 * of the key columns + index column.
 */
public class PrimitiveArray extends Array {

	public PrimitiveArray(PersistentClass owner) {
		super(owner);
	}

	public boolean isPrimitiveArray() {
		return true;
	}

	public Object accept(ValueVisitor visitor) {
		return visitor.accept(this);
	}
}








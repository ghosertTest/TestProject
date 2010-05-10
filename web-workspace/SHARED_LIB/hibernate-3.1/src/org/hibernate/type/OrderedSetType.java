//$Id: OrderedSetType.java,v 1.1 2005/08/01 16:29:32 oneovthafew Exp $
package org.hibernate.type;

import org.hibernate.util.LinkedHashCollectionHelper;

public class OrderedSetType extends SetType {

	public OrderedSetType(String role, String propertyRef, boolean isEmbeddedInXML) {
		super( role, propertyRef, isEmbeddedInXML );
	}

	public Object instantiate() {
		return LinkedHashCollectionHelper.createLinkedHashSet();
	}

}

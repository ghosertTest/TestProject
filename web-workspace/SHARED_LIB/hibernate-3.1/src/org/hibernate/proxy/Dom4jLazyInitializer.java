// $Id: Dom4jLazyInitializer.java,v 1.2 2005/02/12 07:19:45 steveebersole Exp $
package org.hibernate.proxy;

import org.hibernate.engine.SessionImplementor;
import org.dom4j.Element;

import java.io.Serializable;

/**
 * Lazy initializer for "dom4j" entity representations.
 *
 * @author Steve Ebersole
 */
public class Dom4jLazyInitializer extends AbstractLazyInitializer implements Serializable {

	Dom4jLazyInitializer(String entityName, Serializable id, SessionImplementor session) {
		super(entityName, id, session);
	}

	public Element getElement() {
		return (Element) getImplementation();
	}

	public Class getPersistentClass() {
		throw new UnsupportedOperationException("dom4j entity representation");
	}

}

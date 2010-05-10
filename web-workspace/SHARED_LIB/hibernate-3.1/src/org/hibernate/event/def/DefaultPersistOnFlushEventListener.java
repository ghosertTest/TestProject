//$Id: DefaultPersistOnFlushEventListener.java,v 1.1 2005/10/16 13:27:54 epbernard Exp $
package org.hibernate.event.def;

import org.hibernate.engine.CascadingAction;

/**
 * When persust is used as the cascade action, persistOnFlush should be used
 * @author Emmanuel Bernard
 */
public class DefaultPersistOnFlushEventListener extends DefaultPersistEventListener {
	protected CascadingAction getCascadeAction() {
		return CascadingAction.PERSIST_ON_FLUSH;
	}
}

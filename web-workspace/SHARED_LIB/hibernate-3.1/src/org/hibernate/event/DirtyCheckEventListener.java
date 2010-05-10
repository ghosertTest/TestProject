//$Id: DirtyCheckEventListener.java,v 1.3 2005/08/08 23:24:43 oneovthafew Exp $
package org.hibernate.event;

import org.hibernate.HibernateException;

import java.io.Serializable;

/**
 * Defines the contract for handling of session dirty-check events.
 *
 * @author Steve Ebersole
 */
public interface DirtyCheckEventListener extends Serializable {

    /** Handle the given dirty-check event.
     *
     * @param event The dirty-check event to be handled.
     * @throws HibernateException
     */
	public void onDirtyCheck(DirtyCheckEvent event) throws HibernateException;

}

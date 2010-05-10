//$Id: MergeEventListener.java,v 1.2 2005/08/08 23:24:43 oneovthafew Exp $
package org.hibernate.event;

import java.io.Serializable;
import java.util.Map;

import org.hibernate.HibernateException;

/**
 * Defines the contract for handling of merge events generated from a session.
 *
 * @author Gavin King
 */
public interface MergeEventListener extends Serializable {

    /** 
     * Handle the given merge event.
     *
     * @param event The merge event to be handled.
     * @throws HibernateException
     */
	public void onMerge(MergeEvent event) throws HibernateException;

    /** 
     * Handle the given merge event.
     *
     * @param event The merge event to be handled.
     * @throws HibernateException
     */
	public void onMerge(MergeEvent event, Map copiedAlready) throws HibernateException;

}

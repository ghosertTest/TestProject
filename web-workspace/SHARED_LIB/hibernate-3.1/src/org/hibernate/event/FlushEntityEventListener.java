//$Id: FlushEntityEventListener.java,v 1.2 2005/08/08 23:24:43 oneovthafew Exp $
package org.hibernate.event;

import java.io.Serializable;

import org.hibernate.HibernateException;

/**
 * @author Gavin King
 */
public interface FlushEntityEventListener extends Serializable {
	public void onFlushEntity(FlushEntityEvent event) throws HibernateException;
}

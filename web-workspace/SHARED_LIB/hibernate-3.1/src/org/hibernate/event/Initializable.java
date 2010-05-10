//$Id: Initializable.java,v 1.1 2005/08/10 05:06:40 oneovthafew Exp $
package org.hibernate.event;

import org.hibernate.cfg.Configuration;

/**
 * An event listener that requires access to mappings to
 * initialize state at initialization time.
 * @author Gavin King
 */
public interface Initializable {
	public void initialize(Configuration cfg);
}

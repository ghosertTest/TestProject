//$Id: ComponentTuplizer.java,v 1.4 2005/07/11 21:46:57 steveebersole Exp $
package org.hibernate.tuple;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.hibernate.engine.SessionFactoryImplementor;

/**
 * Defines further responsibilities reagarding tuplization based on
 * a mapped components.
 * </p>
 * ComponentTuplizer implementations should have the following constructor signature:
 *      (org.hibernate.mapping.Component)
 * 
 * @author Gavin King
 */
public interface ComponentTuplizer extends Tuplizer, Serializable {
	/**
	 * Retreive the current value of the parent property.
	 *
	 * @param component The component instance from which to extract the parent
	 * property value.
	 * @return The current value of the parent property.
	 */
	public Object getParent(Object component);

    /**
     * Set the value of the parent property.
     *
     * @param component The component instance on which to set the parent.
     * @param parent The parent to be set on the comonent.
     * @param factory The current session factory.
     */
	public void setParent(Object component, Object parent, SessionFactoryImplementor factory);

	/**
	 * Does the component managed by this tuuplizer contain a parent property?
	 *
	 * @return True if the component does contain a parent property; false otherwise.
	 */
	public boolean hasParentProperty();

	/**
	 * Is the given method available via the managed component as a property getter?
	 *
	 * @param method The method which to check against the managed component.
	 * @return True if the managed component is available from the managed component; else false.
	 */
	public boolean isMethodOf(Method method);
}

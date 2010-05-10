//$Id: SQLFunction.java,v 1.3 2005/04/29 15:32:30 oneovthafew Exp $
package org.hibernate.dialect.function;

import java.util.List;

import org.hibernate.QueryException;
import org.hibernate.engine.Mapping;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.type.Type;

/**
 * Provides support routines for the HQL functions as used
 * in the various SQL Dialects
 *
 * Provides an interface for supporting various HQL functions that are
 * translated to SQL. The Dialect and its sub-classes use this interface to
 * provide details required for processing of the function.
 *
 * @author David Channon
 */
public interface SQLFunction {
	/**
	 * The function return type
	 * @param columnType the type of the first argument
	 */
	public Type getReturnType(Type columnType, Mapping mapping) throws QueryException;
	/**
	 * Does this function have any arguments?
	 */
	public boolean hasArguments();
	/**
	 * If there are no arguments, are parens required?
	 */
	public boolean hasParenthesesIfNoArguments();
	/**
	 * Render the function call as SQL
	 */
	public String render(List args, SessionFactoryImplementor factory) throws QueryException;
}

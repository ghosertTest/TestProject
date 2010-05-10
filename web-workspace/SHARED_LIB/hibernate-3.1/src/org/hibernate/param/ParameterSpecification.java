// $Id: ParameterSpecification.java,v 1.3 2005/11/02 18:47:40 steveebersole Exp $
package org.hibernate.param;

import org.hibernate.engine.QueryParameters;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.Type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Maintains information relating to parameters which need to get bound into a
 * JDBC {@link PreparedStatement}.
 *
 * @author Steve Ebersole
 */
public interface ParameterSpecification {
	/**
	 * Bind the appropriate value into the given statement at the specified position.
	 *
	 * @param statement The statement into which the value should be bound.
	 * @param qp The defined values for the current query execution.
	 * @param session The session against which the current execution is occuring.
	 * @param position The position from which to start binding value(s).
	 *
	 * @return The number of sql bind positions "eaten" by this bind operation.
	 */
	public int bind(PreparedStatement statement, QueryParameters qp, SessionImplementor session, int position) throws SQLException;

	public Type getExpectedType();

	public void setExpectedType(Type expectedType);

	public String renderDisplayInfo();
}

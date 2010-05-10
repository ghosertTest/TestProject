//$Id: QueryException.java,v 1.1 2004/06/03 16:30:04 steveebersole Exp $
package org.hibernate;

/**
 * A problem occurred translating a Hibernate query to SQL
 * due to invalid query syntax, etc.
 */
public class QueryException extends HibernateException {

	private String queryString;

	public QueryException(String message) {
		super(message);
	}
	public QueryException(String message, Throwable e) {
		super(message, e);
	}

	public QueryException(String message, String queryString) {
		super(message);
		this.queryString = queryString;
	}

	public QueryException(Exception e) {
		super(e);
	}
	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getMessage() {
		String msg = super.getMessage();
		if ( queryString!=null ) msg += " [" + queryString + ']';
		return msg;
	}

}








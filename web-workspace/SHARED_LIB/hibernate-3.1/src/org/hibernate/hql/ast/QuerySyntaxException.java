// $Id: QuerySyntaxException.java,v 1.1 2005/07/16 22:25:42 oneovthafew Exp $
package org.hibernate.hql.ast;

import antlr.RecognitionException;
import org.hibernate.QueryException;

/**
 * Exception thrown when there is a syntax error in the HQL.
 *
 * @author josh Dec 5, 2004 7:22:54 PM
 */
public class QuerySyntaxException extends QueryException {
	
	public QuerySyntaxException(RecognitionException e) {
		super( e.getMessage() + (
				( e.getLine() > 0 && e.getColumn() > 0 ) ?
				( " near line " + e.getLine() + ", column " + e.getColumn() ) : ""
				), e );
	}

	public QuerySyntaxException(RecognitionException e, String hql) {
		super( e.getMessage() + (
				( e.getLine() > 0 && e.getColumn() > 0 ) ?
				( " near line " + e.getLine() + ", column " + e.getColumn() ) : ""
				), e );
		setQueryString( hql );
	}
	
}

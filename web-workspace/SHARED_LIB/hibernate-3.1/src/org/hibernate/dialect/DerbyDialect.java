//$Id: DerbyDialect.java,v 1.7 2005/04/13 04:44:34 oneovthafew Exp $
package org.hibernate.dialect;

import org.hibernate.Hibernate;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.id.TableHiLoGenerator;
import org.hibernate.sql.CaseFragment;
import org.hibernate.sql.DerbyCaseFragment;

/**
 * @author Simon Johnston
 *
 * Hibernate Dialect for Cloudscape 10 - aka Derby. This implements both an 
 * override for the identity column generator as well as for the case statement
 * issue documented at:
 * http://www.jroller.com/comments/kenlars99/Weblog/cloudscape_soon_to_be_derby
 */
public class DerbyDialect extends DB2Dialect {

	public DerbyDialect() {
		super();
		registerFunction( "concat", new VarArgsSQLFunction( Hibernate.STRING, "(","||",")" ) );
	}

	/**
	 * This is different in Cloudscape to DB2.
	 */
	public String getIdentityColumnString() {
		return "not null generated always as identity"; //$NON-NLS-1
	}

	/**
	 * Return the case statement modified for Cloudscape.
	 */
	public CaseFragment createCaseFragment() {
		return new DerbyCaseFragment();
	}

	public boolean dropConstraints() { 
	      return true; 
	}
	
	public Class getNativeIdentifierGeneratorClass() {
		return TableHiLoGenerator.class;
	}
	
	public boolean supportsSequences() {
		return false;
	}
	
	public boolean supportsLimit() {
		return false;
	}

	public boolean supportsLimitOffset() {
		return false;
	}

	public String getQuerySequencesString() {
	   return null ;
	} 
}

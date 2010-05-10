// $Id: HQLSuite.java,v 1.4 2005/11/23 16:13:11 steveebersole Exp $
package org.hibernate.test.hql;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The full suite of tests against the Antlr grammar
 *
 * @author Steve Ebersole
 */
public class HQLSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite( "Antlr HQL grammar tests" );
		suite.addTest( HQLTest.suite() );
		suite.addTest( ASTParserLoadingTest.suite() );
		suite.addTest( BulkManipulationTest.suite() );
		suite.addTest( AdHocOnTest.suite() );
//		suite.addTest( ASTQueryTranslatorTest.suite() );
		suite.addTest( EJBQLTest.suite() );
		suite.addTest( HqlParserTest.suite() );
		suite.addTest( ScrollableCollectionFetchingTest.suite() );
		suite.addTest( ClassicTranslatorTest.suite() );
		return suite;
	}
}

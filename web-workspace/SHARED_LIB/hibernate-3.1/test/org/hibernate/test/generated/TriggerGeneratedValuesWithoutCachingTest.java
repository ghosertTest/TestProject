// $Id: TriggerGeneratedValuesWithoutCachingTest.java,v 1.2 2005/08/10 17:01:49 steveebersole Exp $
package org.hibernate.test.generated;

import org.hibernate.dialect.Oracle9Dialect;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Implementation of TriggerGeneratedValuesWithoutCachingTest.
 *
 * @author Steve Ebersole
 */
public class TriggerGeneratedValuesWithoutCachingTest extends AbstractGeneratedPropertyTest {

	public TriggerGeneratedValuesWithoutCachingTest(String x) {
		super( x );
	}

	protected boolean acceptsCurrentDialect() {
		// TODO : add more triggers for dialects whicg allow mods in triggers...
		return ( getDialect() instanceof Oracle9Dialect );
	}

	public String getCacheConcurrencyStrategy() {
		return null;
	}

	public static Test suite() {
		return new TestSuite( TriggerGeneratedValuesWithoutCachingTest.class );
	}
}

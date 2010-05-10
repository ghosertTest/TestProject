// $Id$
package org.hibernate.test.sqlinterceptor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.test.TestCase;

/**
 * Implementation of SQLExceptionConversionTest.
 *
 * @author Steve Ebersole
 */
public class SQLInterceptorTest extends TestCase {

	public SQLInterceptorTest(String name) {
		super(name);
	}

	protected String[] getMappings() {
		return new String[] {"sqlinterceptor/User.hbm.xml"};
	}

	static class SQLInterceptor extends EmptyInterceptor {
		
		List preparedSQL = new ArrayList();
		
		public String onPrepareStatement(String sql) {
			preparedSQL.add(sql);
			return "/* sqlinterceptor */ " + sql;
		}
	}
	
	public void testSqlMutation() throws Exception {
		
		SQLInterceptor interceptor = new SQLInterceptor();
		Session session = openSession(interceptor);
		
		User user = new User();
		user.setUsername("Max");
		
		session.save(user);

		session.close();
		
		assertEquals(interceptor.preparedSQL.size(),2); // insert and identity.
	}

	public static Test suite() {
		return new TestSuite(SQLInterceptorTest.class);
	}
}

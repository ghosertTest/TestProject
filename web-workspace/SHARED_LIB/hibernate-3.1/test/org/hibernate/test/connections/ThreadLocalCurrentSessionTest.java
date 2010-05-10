package org.hibernate.test.connections;

import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.context.ThreadLocalSessionContext;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

/**
 * @author <a href="mailto:steve@hibernate.org">Steve Ebersole </a>
 */
public class ThreadLocalCurrentSessionTest extends ConnectionManagementTestCase {

	public ThreadLocalCurrentSessionTest(String name) {
		super( name );
	}

	protected void configure(Configuration cfg) {
		cfg.setProperty( Environment.CURRENT_SESSION_CONTEXT_CLASS, TestableThreadLocalContext.class.getName() );
		cfg.setProperty( Environment.GENERATE_STATISTICS, "true" );
	}

	protected Session getSessionUnderTest() throws Throwable {
		Session session = getSessions().getCurrentSession();
		session.beginTransaction();
		return session;
	}

	protected void release(Session session) {
		long initialCount = getSessions().getStatistics().getSessionCloseCount();
		session.getTransaction().commit();
		long subsequentCount = getSessions().getStatistics().getSessionCloseCount();
		assertEquals( "Session still open after commit", initialCount + 1, subsequentCount );
	}

	protected void reconnect(Session session) throws Throwable {
//		session.reconnect();
		session.beginTransaction();
	}

	protected void checkSerializedState(Session session) {
		assertFalse( "session still bound after serialize", TestableThreadLocalContext.isSessionBound( session ) );
	}

	protected void checkDeserializedState(Session session) {
		assertTrue( "session not bound after deserialize", TestableThreadLocalContext.isSessionBound( session ) );
	}

	public void testTransactionProtection() {
		Session session = getSessions().getCurrentSession();
		try {
			session.createQuery( "from Silly" );
			fail( "method other than beginTransaction{} allowed" );
		}
		catch( HibernateException e ) {
			// ok
		}
	}

	public static class TestableThreadLocalContext extends ThreadLocalSessionContext {
		private static TestableThreadLocalContext me;
		public TestableThreadLocalContext(SessionFactoryImplementor factory) {
			super( factory );
			me = this;
		}
		public static boolean isSessionBound(Session session) {
			return sessionMap() != null && sessionMap().containsKey( me.factory )
					&& sessionMap().get( me.factory ) == session;
		}
	}
}

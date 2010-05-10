package org.hibernate.test.hql;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.hql.classic.ClassicQueryTranslatorFactory;
import org.hibernate.Session;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Some simple test queries using the classic translator explicitly
 * to ensure that code is not broken in changes for the new translator.
 * <p/>
 * Only really checking translation and syntax, not results.
 *
 * @author <a href="mailto:steve@hibernate.org">Steve Ebersole </a>
 */
public class ClassicTranslatorTest extends QueryTranslatorTestCase {

	public ClassicTranslatorTest(String x) {
		super( x );
	}

	public static Test suite() {
		return new TestSuite( ClassicTranslatorTest.class );
	}

	protected boolean recreateSchema() {
		return true;
	}

	protected void configure(Configuration cfg) {
		cfg.setProperty( Environment.QUERY_TRANSLATOR, ClassicQueryTranslatorFactory.class.getName() );
	}

	public void testQueries() {
		Session session = openSession();
		session.beginTransaction();

		session.createQuery( "from Animal" ).list();

		session.createQuery( "select a from Animal as a" ).list();
		session.createQuery( "select a.mother from Animal as a" ).list();
		session.createQuery( "select m from Animal as a inner join a.mother as m" ).list();
		session.createQuery( "select a from Animal as a inner join fetch a.mother" ).list();

		session.createQuery( "from Animal as a where a.description = ?" ).setString( 0, "jj" ).list();
		session.createQuery( "from Animal as a where a.description = :desc" ).setString( "desc", "jr" ).list();
		session.createQuery( "from Animal as a where a.description = ? or a.description = :desc" )
				.setString( 0, "jj" )
				.setString( "desc", "jr" )
				.list();

		session.getTransaction().commit();
		session.close();
	}
}

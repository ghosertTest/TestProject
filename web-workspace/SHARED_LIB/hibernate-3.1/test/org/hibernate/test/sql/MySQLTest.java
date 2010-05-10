//$Id: MySQLTest.java,v 1.1 2005/12/08 02:39:37 oneovthafew Exp $
package org.hibernate.test.sql;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.SybaseDialect;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class MySQLTest extends TestCase {
	
	public MySQLTest(String str) {
		super(str);
	}
		
	public void testHandSQL() {
		
		if (!(getDialect() instanceof MySQLDialect)) return;
		
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Organization ifa = new Organization("IFA");
		Organization jboss = new Organization("JBoss");
		Person gavin = new Person("Gavin");
		Employment emp = new Employment(gavin, jboss, "AU");
		Serializable orgId = s.save(jboss);
		Serializable orgId2 = s.save(ifa);
		s.save(gavin);
		s.save(emp);
		t.commit();
		
		t = s.beginTransaction();
		Person christian = new Person("Christian");
		s.save(christian);
		Employment emp2 = new Employment(christian, jboss, "EU");
		s.save(emp2);
		t.commit();
		s.close();
		
		getSessions().evict(Organization.class);
		getSessions().evict(Person.class);
		getSessions().evict(Employment.class);
		
		s = openSession();
		t = s.beginTransaction();
		jboss = (Organization) s.get(Organization.class, orgId);
		assertEquals( jboss.getEmployments().size(), 2 );
		emp = (Employment) jboss.getEmployments().iterator().next();
		gavin = emp.getEmployee();
		assertEquals( gavin.getName(), "GAVIN" );
		assertEquals( s.getCurrentLockMode(gavin), LockMode.UPGRADE );
		emp.setEndDate( new Date() );
		Employment emp3 = new Employment(gavin, jboss, "US");
		s.save(emp3);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		Iterator iter = s.getNamedQuery("allOrganizationsWithEmployees").list().iterator();
		assertTrue ( iter.hasNext() );
		Organization o = (Organization) iter.next();
		assertEquals( o.getEmployments().size(), 3 );
		Iterator iter2 = o.getEmployments().iterator();
		while ( iter2.hasNext() ) {
			Employment e = (Employment) iter2.next();
			s.delete(e);
		}
		iter2 = o.getEmployments().iterator();
		while ( iter2.hasNext() ) {
			Employment e = (Employment) iter2.next();
			s.delete( e.getEmployee() );
		}
		s.delete(o);
		assertFalse ( iter.hasNext() );
		s.delete(ifa);
		t.commit();
		s.close();
	}
	
	public void testSQLQueryInterface() {
		
		if (!(getDialect() instanceof MySQLDialect)) return;
		
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Organization ifa = new Organization("IFA");
		Organization jboss = new Organization("JBoss");
		Person gavin = new Person("Gavin");
		Employment emp = new Employment(gavin, jboss, "AU");
		
		s.persist(ifa);
		s.persist(jboss);
		s.persist(gavin);
		s.persist(emp);
		
		List l = s.createSQLQuery("select {org.*}, {emp.*}, emp.regionCode from organization org left outer join employment emp on org.orgid = emp.employer")
			.addEntity("org", Organization.class)
			.addJoin("emp", "org.employments")
			.addScalar("regionCode", Hibernate.STRING)
			.list();
		
		assertEquals( l.size(), 2 );
		
		l = s.createSQLQuery("select {org.*}, {emp.*}, {pers.*} from organization org join employment emp on org.orgid = emp.employer join person pers on pers.perid = emp.employee")
			.addEntity("org", Organization.class)
			.addJoin("emp", "org.employments")
			.addJoin("pers", "emp.employee")
			.list();		
		
		assertEquals( l.size(), 1 );
		
		s.delete(emp);
		s.delete(gavin);
		s.delete(ifa);
		s.delete(jboss);
		
		t.commit();
		s.close();		
		
	}

	public void testScalarValues() throws Exception {		

		if ( getDialect() instanceof DB2Dialect ) return; //DB2 no like upper(?)
		
		Session s = openSession();
		Transaction t = s.beginTransaction();
		
		Organization ifa = new Organization("IFA");
		Organization jboss = new Organization("JBoss");
		
		Serializable idIfa = s.save(ifa);
		Serializable idJBoss = s.save(jboss);
		
		s.flush();
		
		List result = s.getNamedQuery("orgNamesOnly").list();
		assertTrue(result.contains("IFA"));
		assertTrue(result.contains("JBOSS"));
		
		t.commit();
		s.close();		

		s = openSession();
		t = s.beginTransaction();
		
		Iterator iter = s.getNamedQuery("orgNamesAndOrgs").list().iterator();
		Object[] o = (Object[]) iter.next();
		assertEquals(o[0], "IFA");
		assertEquals(((Organization)o[1]).getName(), "IFA"); 
		o = (Object[]) iter.next();
		assertEquals(o[0], "JBOSS");
		assertEquals(((Organization)o[1]).getName(), "JBOSS");
				
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		
		iter = s.getNamedQuery("orgsAndOrgNames").list().iterator();
		o = (Object[]) iter.next();
		assertEquals(o[0], "IFA");
		assertEquals(((Organization)o[1]).getName(), "IFA");
		o = (Object[]) iter.next();
		assertEquals(o[0], "JBOSS");
		assertEquals(((Organization)o[1]).getName(), "JBOSS");
				
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		
		iter = s.getNamedQuery("orgIdsAndOrgNames").list().iterator();
		o = (Object[]) iter.next();
		assertEquals(o[1], "IFA");
		assertEquals(o[0], idIfa);
		o = (Object[]) iter.next();
		assertEquals(o[1], "JBOSS");
		assertEquals(o[0], idJBoss);
				
		t.commit();
		s.close();
				
	}
	
	public void testScalarStoredProcedure() throws HibernateException, SQLException {
		
		if (!(getDialect() instanceof MySQLDialect)) return;
		
		Session s = openSession();
		
		Query namedQuery = s.getNamedQuery("simpleScalar");
		namedQuery.setLong("number", 43);
		List list = namedQuery.list();
		Object o[] = (Object[]) list.get(0);
		assertEquals(o[0], "getAll");
		assertEquals(o[1], new Long(43));
		s.close();
		
	}
	
	public void testParameterHandling() throws HibernateException, SQLException {
		
		if ( ! ( getDialect() instanceof SybaseDialect ) && !(getDialect() instanceof MySQLDialect)) return;
		Session s = openSession();
		
		Query namedQuery = s.getNamedQuery("paramHandling");
		namedQuery.setLong(0, 10);
		namedQuery.setLong(1, 20);
		List list = namedQuery.list();
		Object o[] = (Object[]) list.get(0);
		assertEquals(o[0], new Long(10));
		assertEquals(o[1], new Long(20));
		s.close();
	}

	
	public void testEntityStoredProcedure() throws HibernateException, SQLException {
		
		if (!(getDialect() instanceof MySQLDialect)) return;
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Organization ifa = new Organization("IFA");
		Organization jboss = new Organization("JBoss");
		Person gavin = new Person("Gavin");
		Employment emp = new Employment(gavin, jboss, "AU");
		s.persist(ifa);
		s.persist(jboss);
		s.persist(gavin);
		s.persist(emp);
		
		Query namedQuery = s.getNamedQuery("selectAllEmployees");
		List list = namedQuery.list();
		assertTrue(list.get(0) instanceof Employment);
		
		t.commit();
		
		s.close();
	}


	protected String[] getMappings() {
		return new String[] { "sql/MySQLEmployment.hbm.xml" };	
	}

	public static Test suite() {
		return new TestSuite(MySQLTest.class);
	}

	public String getCacheConcurrencyStrategy() {
		return null;
	}
}


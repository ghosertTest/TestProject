//$Id$
package org.hibernate.test.sql;

import java.io.Serializable;
import java.math.BigInteger;
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
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.Oracle9Dialect;
import org.hibernate.test.TestCase;

/**
 * @author Gavin King
 */
public class OracleSQLTest extends TestCase {
	
	public OracleSQLTest(String str) {
		super(str);
	}
		
	public void testHandSQL() {
		
		if ( getDialect() instanceof DB2Dialect ) return;
		
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
		s.flush();
		s.refresh(christian);
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
		
		if ( getDialect() instanceof DB2Dialect ) return; //DB2 no like upper(?)
		
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
		
		// autodetect scalar types
		SQLQuery sqlQuery = s.createSQLQuery("select emp.regionCode, emp.empid from employment emp");
		l = sqlQuery
	//	.addScalar("regionCode", Hibernate.STRING)
		.list();

		assertEquals( l.size(), 1 );
		Object[] o = (Object[]) l.get(0);
		assertEquals( o.length, 2);
		assertNotNull( o[0] );
		assertTrue( o[0] instanceof String);
		assertNotNull( o[1] );
		assertTrue( o[1] instanceof BigInteger);
		
		l = s.createSQLQuery("select {org.*}, {emp.*}, emp.regionCode from organization org left outer join employment emp on org.orgid = emp.employer")
		.addEntity("org", Organization.class)
		.addJoin("emp", "org.employments")
		.addScalar("regionCode")
		.list();

		assertEquals( l.size(), 2 );
		o = (Object[]) l.get(1); // TODO: item 1 has non-nulls, ordering might be different on other dbs (works on hsqldb)
		assertEquals( o.length, 3);
		assertNotNull( o[0] );
		assertTrue( o[0] instanceof String);
		assertNotNull( o[1] );
		assertTrue( o[1] instanceof Organization);
		assertNotNull( o[2] );
		assertTrue( o[2] instanceof Employment);

		ScrollableResults scrollable = s.createSQLQuery("select {org.*}, {emp.*}, emp.regionCode from organization org left outer join employment emp on org.orgid = emp.employer")
		.addEntity("org", Organization.class)
		.addJoin("emp", "org.employments")
		.addScalar("regionCode")
		.scroll();
				
		scrollable.next();
		scrollable.next();		
		Object[] data = scrollable.get();
		assertEquals( data.length, 3);
		assertNotNull( data[0] );
		assertTrue( data[0] instanceof String);
		assertNotNull( data[1] );
		assertTrue( data[1] instanceof Organization);
		assertNotNull( data[2] );
		assertTrue( data[2] instanceof Employment);
		scrollable.close();

		s.delete(emp);
		s.delete(gavin);
		s.delete(ifa);
		s.delete(jboss);
		
		t.commit();
		s.close();		
		
	}

	public void testResultSetMappingDefinition() {

		if ( getDialect() instanceof DB2Dialect ) return; //DB2 no like upper(?)

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
			.setResultSetMapping( "org-emp-regionCode")
			.list();

		assertEquals( l.size(), 2 );

		l = s.createSQLQuery("select {org.*}, {emp.*}, {pers.*} from organization org join employment emp on org.orgid = emp.employer join person pers on pers.perid = emp.employee")
			.setResultSetMapping( "org-emp-person" )
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
		
		s.delete(ifa);
		s.delete(jboss);
		t.commit();
		s.close();
		
	}

	public void testFailOnNoAddEntityOrScalar() {
		Session s = openSession();
		try {
			s.createSQLQuery("select {org.*}, {emp.*}, emp.regionCode from organization org left outer join employment emp on org.orgid = emp.employer").list();
			fail("Should throw an exception since no addEntity nor addScalar has been performed.");
		} catch(HibernateException he) {
			
		}
		s.close();
	
	}
	public void testScalarStoredProcedure() throws HibernateException, SQLException {
		
		if( !(getDialect() instanceof Oracle9Dialect)) return;
		
		Session s = openSession();

		Query namedQuery = s.getNamedQuery("simpleScalar");
		namedQuery.setLong("number", 43);
		List list = namedQuery.list();
		Object o[] = (Object[]) list.get(0);
		assertEquals(o[0], "getAll");
		assertEquals(o[1], new Long(43));
		s.close();		
	}	
	
	public void testEntityStoredProcedure() throws HibernateException, SQLException {
		
		if( !(getDialect() instanceof Oracle9Dialect)) return;
		
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
		Query namedQuery = s.getNamedQuery("allEmployments");
		List list = namedQuery.list();
		assertTrue(list.get(0) instanceof Employment);
		s.delete(emp);
		s.delete(ifa);
		s.delete(jboss);
		s.delete(gavin);
		t.commit();
		
		s.close();
	}

	public void testParameterHandling() throws HibernateException, SQLException {
		if( !(getDialect() instanceof Oracle9Dialect)) return;
		
		Session s = openSession();
		
		Query namedQuery = s.getNamedQuery("paramhandling");
		namedQuery.setLong(0, 10);
		namedQuery.setLong(1, 20);
		List list = namedQuery.list();
		Object[] o = (Object[]) list.get(0);
		assertEquals(o[0], new Long(10));
		assertEquals(o[1], new Long(20));

		namedQuery = s.getNamedQuery("paramhandling_mixed");
		namedQuery.setLong(0, 10);
		namedQuery.setLong("second", 20);
		list = namedQuery.list();
		o = (Object[]) list.get(0);
		assertEquals(o[0], new Long(10));
		assertEquals(o[1], new Long(20));
		s.close();
	}

	public void testMappedAliasStrategy() {
		if ( getDialect() instanceof DB2Dialect ) return; //DB2 no like upper(?)
		
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
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		Query namedQuery = s.getNamedQuery("AllEmploymentAsMapped");
		
		List list = namedQuery.list();
		assertEquals(1,list.size());
		
		Employment emp2 = (Employment) list.get(0);
		assertEquals(emp2.getEmploymentId(), emp.getEmploymentId() );
		assertEquals(emp2.getStartDate().getDate(), emp.getStartDate().getDate() );
		assertEquals(emp2.getEndDate(), emp.getEndDate() );
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		namedQuery = s.getNamedQuery("EmploymentAndPerson");
		
		list = namedQuery.list();
		assertEquals(1,list.size() );
		Object[] objs = (Object[]) list.get(0);
		assertEquals(2, objs.length);
		emp2 = (Employment) objs[0];
		gavin = (Person) objs[1];
		s.delete(emp2);
		s.delete(jboss);
		s.delete(gavin);
		s.delete(ifa);
		t.commit();
		s.close();
	}
	
	public void testAutoDetectAliasing() {
		if ( getDialect() instanceof DB2Dialect ) return; //DB2 no like upper(?)
		
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
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		Query namedQuery = s.createSQLQuery("SELECT * FROM EMPLOYMENT").addEntity( Employment.class.getName() );
		
		List list = namedQuery.list();
		assertEquals( 1,list.size() );
		
		Employment emp2 = (Employment) list.get(0);
		assertEquals(emp2.getEmploymentId(), emp.getEmploymentId() );
		assertEquals(emp2.getStartDate().getDate(), emp.getStartDate().getDate() );
		assertEquals(emp2.getEndDate(), emp.getEndDate() );
		
		s.clear();
		
		Query queryWithCollection = s.getNamedQuery("organizationEmploymentsExplicitAliases");
		queryWithCollection.setLong("id",  jboss.getId() );
		list = queryWithCollection.list();
		assertEquals(list.size(),1);
		
		s.clear();
		
		Query queryWithJoin = s.createSQLQuery(
		"SELECT org.orgid as {org.id}, org.name as {org.name}, {emp.*}  FROM ORGANIZATION org LEFT OUTER JOIN EMPLOYMENT emp ON org.ORGID = emp.EMPLOYER ")
		.addEntity("org", Organization.class).addJoin("emp", "org.employments");

		queryWithJoin.list();
		list = queryWithJoin.list();
		assertEquals( 2,list.size() );
		
		s.clear();
		
		queryWithJoin = s.createSQLQuery(
				"SELECT org.orgid as {org.id}, org.name as {org.name}, emp.employer as {emp.key}, emp.empid as {emp.element}, {emp.element.*}  FROM ORGANIZATION org LEFT OUTER JOIN EMPLOYMENT emp ON org.ORGID = emp.EMPLOYER ")
				.addEntity("org", Organization.class).addJoin("emp", "org.employments");
		
		list = queryWithJoin.list();
		assertEquals( 2,list.size() );
		
		s.clear();
		
		queryWithJoin = s.createSQLQuery(
				"SELECT org.orgid as {org.id}, org.name as {org.name}, emp.employer as {emp.key}, emp.empid as {emp.element}, {emp.element.*}  FROM ORGANIZATION org LEFT OUTER JOIN EMPLOYMENT emp ON org.ORGID = emp.EMPLOYER ")
				.addEntity("org", Organization.class).addJoin("emp", "org.employments");
		
		list = queryWithJoin.list();
		assertEquals( 2,list.size() );

		s.clear();
		
		queryWithJoin = s.getNamedQuery("organizationreturnproperty");
		queryWithJoin.list();
		
		list = queryWithJoin.list();
		assertEquals( 2,list.size() );

		s.clear();
		
		queryWithJoin = s.getNamedQuery("organizationautodetect");
		queryWithJoin.list();
		
		list = queryWithJoin.list();
		assertEquals( 2,list.size() );

		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		s.delete(emp2);
		
		s.delete(jboss);
		s.delete(gavin);
		s.delete(ifa);
		t.commit();
		s.close();
		
	}
	
	protected String[] getMappings() {
		return new String[] { "sql/OracleEmployment.hbm.xml" };	
	}

	public static Test suite() {
		return new TestSuite(OracleSQLTest.class);
	}

	public String getCacheConcurrencyStrategy() {
		return null;
	}
}


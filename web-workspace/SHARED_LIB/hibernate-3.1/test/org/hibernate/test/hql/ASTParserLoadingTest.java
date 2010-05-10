// $Id: ASTParserLoadingTest.java,v 1.43 2005/11/18 22:40:23 steveebersole Exp $
package org.hibernate.test.hql;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.QueryException;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.Oracle9Dialect;
import org.hibernate.test.TestCase;
import org.hibernate.test.cid.Customer;
import org.hibernate.test.cid.LineItem;
import org.hibernate.test.cid.Order;
import org.hibernate.test.cid.Product;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.Type;
import org.hibernate.util.StringHelper;

/**
 * Tests the integration of the new AST parser into the loading of query results using
 * the Hibernate persisters and loaders.
 *
 * @author Steve
 */
public class ASTParserLoadingTest extends TestCase {

	public ASTParserLoadingTest(String name) {
		super( name );
	}

	private List createdAnimalIds = new ArrayList();

	protected String[] getMappings() {
		// Make sure we are using the new AST parser translator...
		System.setProperty( Environment.QUERY_TRANSLATOR, "org.hibernate.hql.ast.ASTQueryTranslatorFactory" );
		return new String[]{
			"hql/Animal.hbm.xml",
			"batchfetch/ProductLine.hbm.xml",
			"cid/Customer.hbm.xml",
			"cid/Order.hbm.xml",
			"cid/LineItem.hbm.xml",
			"cid/Product.hbm.xml"
		};
	}


	public void testInvalidFetchSemantics() {
		Session s = openSession();
		s.beginTransaction();

		try {
			s.createQuery( "select mother from Human a left join fetch a.mother mother" ).list();
			fail( "invalid fetch semantic allowed!" );
		}
		catch( QueryException e ) {
		}

		try {
			s.createQuery( "select mother from Human a left join fetch a.mother mother" ).list();
			fail( "invalid fetch semantic allowed!" );
		}
		catch( QueryException e ) {
		}

		s.getTransaction().commit();
		s.close();
	}

	public void testArithmetic() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Zoo zoo = new Zoo();
		zoo.setName("Melbourne Zoo");
		s.persist(zoo);
		s.createQuery("select 2*2*2*2*(2*2) from Zoo").uniqueResult();
		s.createQuery("select 2 / (1+1) from Zoo").uniqueResult();
		int result0 = ( (Integer) s.createQuery("select 2 - (1+1) from Zoo").uniqueResult() ).intValue();
		int result1 = ( (Integer) s.createQuery("select 2 - 1 + 1 from Zoo").uniqueResult() ).intValue();
		int result2 = ( (Integer) s.createQuery("select 2 * (1-1) from Zoo").uniqueResult() ).intValue();
		int result3 = ( (Integer) s.createQuery("select 4 / (2 * 2) from Zoo").uniqueResult() ).intValue();
		int result4 = ( (Integer) s.createQuery("select 4 / 2 * 2 from Zoo").uniqueResult() ).intValue();
		int result5 = ( (Integer) s.createQuery("select 2 * (2/2) from Zoo").uniqueResult() ).intValue();
		int result6 = ( (Integer) s.createQuery("select 2 * (2/2+1) from Zoo").uniqueResult() ).intValue();
		assertEquals(result0, 0);
		assertEquals(result1, 2);
		assertEquals(result2, 0);
		assertEquals(result3, 1);
		assertEquals(result4, 4);
		assertEquals(result5, 2);
		assertEquals(result6, 4);
		s.delete(zoo);
		t.commit();
		s.close();
	}

	public void testNestedCollectionFetch() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		s.createQuery("from Animal a left join fetch a.offspring o left join fetch o.offspring where a.mother.id = 1 order by a.description").list();
		s.createQuery("from Zoo z left join fetch z.animals a left join fetch a.offspring where z.name ='MZ' order by a.description").list();
		s.createQuery("from Human h left join fetch h.pets a left join fetch a.offspring where h.name.first ='Gavin' order by a.description").list();
		t.commit();
		s.close();
	}
	
	public void testSelectClauseSubselect() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Zoo zoo = new Zoo();
		zoo.setName("Melbourne Zoo");
		zoo.setMammals( new HashMap() );
		zoo.setAnimals( new HashMap() );
		Mammal plat = new Mammal();
		plat.setBodyWeight( 11f );
		plat.setDescription( "Platypus" );
		plat.setZoo(zoo);
		plat.setSerialNumber("plat123");
		zoo.getMammals().put("Platypus", plat);
		zoo.getAnimals().put("plat123", plat);
		s.persist( plat );
		s.persist(zoo);
		
		s.createQuery("select (select max(z.id) from a.zoo z) from Animal a").list();
		s.createQuery("select (select max(z.id) from a.zoo z where z.name=:name) from Animal a")
			.setParameter("name", "Melbourne Zoo").list();

		s.delete(plat);
		s.delete(zoo);
		t.commit();
		s.close();
	}

	public void testInitProxy() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Mammal plat = new Mammal();
		plat.setBodyWeight( 11f );
		plat.setDescription( "Platypus" );
		s.persist( plat );
		s.flush();
		s.clear();
		plat = (Mammal) s.load(Mammal.class, plat.getId() );
		assertFalse( Hibernate.isInitialized(plat) );
		Object plat2 = s.createQuery("from Animal a").uniqueResult();
		assertSame(plat, plat2);
		assertTrue( Hibernate.isInitialized(plat) );
		s.delete(plat);
		t.commit();
		s.close();
	}

	public void testSelectClauseImplicitJoin() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Zoo zoo = new Zoo();
		zoo.setName("The Zoo");
		zoo.setMammals( new HashMap() );
		zoo.setAnimals( new HashMap() );
		Mammal plat = new Mammal();
		plat.setBodyWeight( 11f );
		plat.setDescription( "Platypus" );
		plat.setZoo(zoo);
		plat.setSerialNumber("plat123");
		zoo.getMammals().put("Platypus", plat);
		zoo.getAnimals().put("plat123", plat);
		s.persist( plat );
		s.persist(zoo);
		s.flush();
		s.clear();
		Query q = s.createQuery("select distinct a.zoo from Animal a where a.zoo is not null");
		Type type = q.getReturnTypes()[0];
		assertTrue( type instanceof ManyToOneType );
		assertEquals( ( (ManyToOneType) type ).getAssociatedEntityName(), "org.hibernate.test.hql.Zoo" );
		zoo = (Zoo) q.list().get(0);
		assertEquals( zoo.getMammals().size(), 1 );
		assertEquals( zoo.getAnimals().size(), 1 );
		s.clear();
		s.delete(plat);
		s.delete(zoo);
		t.commit();
		s.close();
	}

	public void testSelectClauseImplicitJoinWithIterate() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Zoo zoo = new Zoo();
		zoo.setName("The Zoo");
		zoo.setMammals( new HashMap() );
		zoo.setAnimals( new HashMap() );
		Mammal plat = new Mammal();
		plat.setBodyWeight( 11f );
		plat.setDescription( "Platypus" );
		plat.setZoo(zoo);
		plat.setSerialNumber("plat123");
		zoo.getMammals().put("Platypus", plat);
		zoo.getAnimals().put("plat123", plat);
		s.persist( plat );
		s.persist(zoo);
		s.flush();
		s.clear();
		Query q = s.createQuery("select distinct a.zoo from Animal a where a.zoo is not null");
		Type type = q.getReturnTypes()[0];
		assertTrue( type instanceof ManyToOneType );
		assertEquals( ( (ManyToOneType) type ).getAssociatedEntityName(), "org.hibernate.test.hql.Zoo" );
		zoo = (Zoo) q
			.iterate().next();
		assertEquals( zoo.getMammals().size(), 1 );
		assertEquals( zoo.getAnimals().size(), 1 );
		s.clear();
		s.delete(plat);
		s.delete(zoo);
		t.commit();
		s.close();
	}

	public void testComponentOrderBy() {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		Long id1 = ( Long ) s.save( genSimpleHuman( "John", "Jacob" ) );
		Long id2 = ( Long ) s.save( genSimpleHuman( "Jingleheimer", "Schmidt" ) );

		s.flush();

		// the component is defined with the firstName column first...
		List results = s.createQuery( "from Human as h order by h.name" ).list();
		assertEquals( "Incorrect return count", 2, results.size() );

		Human h1 = ( Human ) results.get( 0 );
		Human h2 = ( Human ) results.get( 1 );

		assertEquals( "Incorrect ordering", id2, h1.getId() );
		assertEquals( "Incorrect ordering", id1, h2.getId() );

		s.delete( h1 );
		s.delete( h2 );

		t.commit();
		s.close();
	}

	private Human genSimpleHuman(String fName, String lName) {
		Human h = new Human();
		h.setName( new Name() );
		h.getName().setFirst( fName );
		h.getName().setLast( lName );
		h.getName().setInitial('X');

		return h;
	}

	public void testCastInSelect() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Animal a = new Animal();
		a.setBodyWeight(12.4f);
		a.setDescription("an animal");
		s.persist(a);
		Integer bw = (Integer) s.createQuery("select cast(bodyWeight as integer) from Animal").uniqueResult();
		bw = (Integer) s.createQuery("select cast(a.bodyWeight as integer) from Animal a").uniqueResult();
		bw.toString();
		s.delete(a);
		t.commit();
		s.close();		
	}

	public void testAliases() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Animal a = new Animal();
		a.setBodyWeight(12.4f);
		a.setDescription("an animal");
		s.persist(a);
		String[] aliases1 = s.createQuery("select a.bodyWeight as abw, a.description from Animal a").getReturnAliases();
		assertEquals(aliases1[0], "abw");
		assertEquals(aliases1[1], "1");
		String[] aliases2 = s.createQuery("select count(*), avg(a.bodyWeight) as avg from Animal a").getReturnAliases();
		assertEquals(aliases2[0], "0");
		assertEquals(aliases2[1], "avg");
		s.delete(a);
		t.commit();
		s.close();		
	}

	public void testIndexParams() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		s.createQuery("from Zoo zoo where zoo.mammals[:name] = :id")
			.setParameter("name", "Walrus")
			.setParameter("id", new Long(123))
			.list();
		s.createQuery("from Zoo zoo where zoo.mammals[:name].bodyWeight > :w")
			.setParameter("name", "Walrus")
			.setParameter("w", new Float(123.32))
			.list();
		s.createQuery("from Zoo zoo where zoo.animals[:sn].mother.bodyWeight < :mw")
			.setParameter("sn", "ant-123")
			.setParameter("mw", new Float(23.32))
			.list();
		/*s.createQuery("from Zoo zoo where zoo.animals[:sn].description like :desc and zoo.animals[:sn].bodyWeight > :wmin and zoo.animals[:sn].bodyWeight < :wmax")
			.setParameter("sn", "ant-123")
			.setParameter("desc", "%big%")
			.setParameter("wmin", new Float(123.32))
			.setParameter("wmax", new Float(167.89))
			.list();*/
		/*s.createQuery("from Human where addresses[:type].city = :city and addresses[:type].country = :country")
			.setParameter("type", "home")
			.setParameter("city", "Melbourne")
			.setParameter("country", "Australia")
			.list();*/
		t.commit();
		s.close();		
	}
	
	public void testAggregation() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Human h = new Human();
		h.setBodyWeight( (float) 74.0 );
		h.setHeight(120.5);
		h.setDescription("Me");
		h.setName( new Name("Gavin", 'A', "King") );
		h.setNickName("Oney");
		s.persist(h);
		Float sum = (Float) s.createQuery("select sum(h.bodyWeight) from Human h").uniqueResult();
		Double avg = (Double) s.createQuery("select avg(h.height) from Human h").uniqueResult();
		assertEquals(sum.floatValue(), 74.0, 0.01);
		assertEquals(avg.doubleValue(), 120.5, 0.01);
		Long id = (Long) s.createQuery("select max(a.id) from Animal a").uniqueResult();
		s.delete(h);
		t.commit();
		s.close();
	}
	
	public void testSelectClauseCase() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Human h = new Human();
		h.setBodyWeight( (float) 74.0 );
		h.setHeight(120.5);
		h.setDescription("Me");
		h.setName( new Name("Gavin", 'A', "King") );
		h.setNickName("Oney");
		s.persist(h);
		String name = (String) s.createQuery("select case nickName when 'Oney' then 'gavin' when 'Turin' then 'christian' else nickName end from Human").uniqueResult();
		assertEquals(name, "gavin");
		String result = (String) s.createQuery("select case when bodyWeight > 100 then 'fat' else 'skinny' end from Human").uniqueResult();
		assertEquals(result, "skinny");
		s.delete(h);
		t.commit();
		s.close();
	}

	public void testImplicitPolymorphism() {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		Product product = new Product();
		product.setDescription( "My Product" );
		product.setNumberAvailable( 10 );
		product.setPrice( new BigDecimal( 123 ) );
		product.setProductId( "4321" );
		s.save( product );

		List list = s.createQuery("from java.lang.Comparable").list();
		assertEquals( list.size(), 0 );
		
		list = s.createQuery("from java.lang.Object").list();
		assertEquals( list.size(), 1 );
		
		s.delete(product);
		
		list = s.createQuery("from java.lang.Object").list();
		assertEquals( list.size(), 0 );
		
		t.commit();
		s.close();
	}
	
	public void testCoalesce() {
		Session session = openSession();
		Transaction txn = session.beginTransaction();
		session.createQuery("from Human h where coalesce(h.nickName, h.name.first, h.name.last) = 'max'").list();
		session.createQuery("select nullif(nickName, '1e1') from Human").list();
		txn.commit();
		session.close();		
	}
	
	public void testStr() {
		Session session = openSession();
		Transaction txn = session.beginTransaction();
		Animal an = new Animal();
		an.setBodyWeight(123.45f);
		session.persist(an);
		String str = (String) session.createQuery("select str(an.bodyWeight) from Animal an where str(an.bodyWeight) like '123%' or str(an.bodyWeight) like '1.23%'").uniqueResult();
		if ( getDialect() instanceof DB2Dialect ) {
			assertTrue( str.startsWith("1.234") );
		}
		else {
			assertTrue( str.startsWith("123.4") );
		}
		String dateStr1 = (String) session.createQuery("select str(current_date) from Animal").uniqueResult();
		String dateStr2 = (String) session.createQuery("select str(year(current_date))||'-'||str(month(current_date))||'-'||str(day(current_date)) from Animal").uniqueResult();
		System.out.println(dateStr1 + '=' + dateStr2);
		if ( ! ( getDialect() instanceof Oracle9Dialect ) ) { //Oracle renders the name of the month :(
			String[] dp1 = StringHelper.split("-", dateStr1);
			String[] dp2 = StringHelper.split("-", dateStr2);
			for (int i=0; i<3; i++) {
				if ( dp1[i].startsWith("0") ) dp1[i] = dp1[i].substring(1);
				assertEquals( dp1[i], dp2[i] );
			}
		}
		session.delete(an);
		txn.commit();
		session.close();		
	}
	
	public void testCast() {
		if ( (getDialect() instanceof MySQLDialect) || (getDialect() instanceof DB2Dialect) ) return;
		Session session = openSession();
		Transaction txn = session.beginTransaction();
		session.createQuery("from Human h where h.nickName like 'G%'").list();
		session.createQuery("from Animal a where cast(a.bodyWeight as string) like '1.%'").list();
		session.createQuery("from Animal a where cast(a.bodyWeight as integer) = 1").list();
		txn.commit();
		session.close();		
	}
	
	public void testExtract() {
		Session session = openSession();
		Transaction txn = session.beginTransaction();
		session.createQuery("select second(current_timestamp()), minute(current_timestamp()), hour(current_timestamp()) from Mammal m").list();
		session.createQuery("select day(m.birthdate), month(m.birthdate), year(m.birthdate) from Mammal m").list();
		if ( !(getDialect() instanceof DB2Dialect) ) { //no ANSI extract
			session.createQuery("select extract(second from current_timestamp()), extract(minute from current_timestamp()), extract(hour from current_timestamp()) from Mammal m").list();
			session.createQuery("select extract(day from m.birthdate), extract(month from m.birthdate), extract(year from m.birthdate) from Mammal m").list();
		}
		txn.commit();
		session.close();		
	}

	public void testOneToManyFilter() throws Throwable {
		Session session = openSession();
		Transaction txn = session.beginTransaction();

		Product product = new Product();
		product.setDescription( "My Product" );
		product.setNumberAvailable( 10 );
		product.setPrice( new BigDecimal( 123 ) );
		product.setProductId( "4321" );
		session.save( product );

		Customer customer = new Customer();
		customer.setCustomerId( "123456789" );
		customer.setName( "My customer" );
		customer.setAddress( "somewhere" );
		session.save( customer );

		Order order = customer.generateNewOrder( new BigDecimal( 1234 ) );
		session.save( order );

		LineItem li = order.generateLineItem( product, 5 );
		session.save( li );

		session.flush();

		assertEquals( session.createFilter( customer.getOrders(), "" ).list().size(), 1 );

		assertEquals( session.createFilter( order.getLineItems(), "" ).list().size(), 1 );
		assertEquals( session.createFilter( order.getLineItems(), "where this.quantity > :quantity" ).setInteger( "quantity", 5 ).list().size(), 0 );
		
		session.delete(li);
		session.delete(order);
		session.delete(product);
		session.delete(customer);
		txn.commit();
		session.close();
	}

	public void testManyToManyFilter() throws Throwable {
		Session session = openSession();
		Transaction txn = session.beginTransaction();

		Human human = new Human();
		human.setName( new Name() );
		human.getName().setFirst( "Steve" );
		human.getName().setInitial( 'L' );
		human.getName().setLast( "Ebersole" );
		session.save( human );

		Human friend = new Human();
		friend.setName( new Name() );
		friend.getName().setFirst( "John" );
		friend.getName().setInitial( 'Q' );
		friend.getName().setLast( "Doe" );
		friend.setBodyWeight( 11.0f );
		session.save( friend );

		human.setFriends( new ArrayList() );
		friend.setFriends( new ArrayList() );
		human.getFriends().add( friend );
		friend.getFriends().add( human );

		session.flush();

		assertEquals( session.createFilter( human.getFriends(), "" ).list().size(), 1 );
		assertEquals( session.createFilter( human.getFriends(), "where this.bodyWeight > ?" ).setFloat( 0, 10f ).list().size(), 1 );
		assertEquals( session.createFilter( human.getFriends(), "where this.bodyWeight < ?" ).setFloat( 0, 10f ).list().size(), 0 );
		
		session.delete(human);
		session.delete(friend);
		
		txn.commit();
		session.close();
	}
	
	public void testSelectExpressions() {
		createTestBaseData();
		Session session = openSession();
		Transaction txn = session.beginTransaction();
		Human h = new Human();
		h.setName( new Name("Gavin", 'A', "King") );
		h.setNickName("Oney");
		h.setBodyWeight(1.0f);
		session.persist(h);
		List results = session.createQuery("select 'found', lower(h.name.first) from Human h where lower(h.name.first) = 'gavin'").list();
		results = session.createQuery("select 'found', lower(h.name.first) from Human h where concat(h.name.first, ' ', h.name.initial, ' ', h.name.last) = 'Gavin A King'").list();
		results = session.createQuery("select 'found', lower(h.name.first) from Human h where h.name.first||' '||h.name.initial||' '||h.name.last = 'Gavin A King'").list();
		results = session.createQuery("select a.bodyWeight + m.bodyWeight from Animal a join a.mother m").list();
		results = session.createQuery("select 2.0 * (a.bodyWeight + m.bodyWeight) from Animal a join a.mother m").list();
		results = session.createQuery("select sum(a.bodyWeight + m.bodyWeight) from Animal a join a.mother m").list();
		results = session.createQuery("select sum(a.mother.bodyWeight * 2.0) from Animal a").list();
		results = session.createQuery("select concat(h.name.first, ' ', h.name.initial, ' ', h.name.last) from Human h").list();
		results = session.createQuery("select h.name.first||' '||h.name.initial||' '||h.name.last from Human h").list();
		results = session.createQuery("select nickName from Human").list();
		results = session.createQuery("select lower(nickName) from Human").list();
		results = session.createQuery("select abs(bodyWeight*-1) from Human").list();
		results = session.createQuery("select upper(h.name.first||' ('||h.nickName||')') from Human h").list();
		results = session.createQuery("select abs(a.bodyWeight-:param) from Animal a").setParameter("param", new Float(2.0)).list();
		results = session.createQuery("select abs(:param - a.bodyWeight) from Animal a").setParameter("param", new Float(2.0)).list();
		results = session.createQuery("select lower(upper('foo')) from Animal").list();
		results = session.createQuery("select lower(upper('foo') || upper('bar')) from Animal").list();
		results = session.createQuery("select sum(abs(bodyWeight - 1.0) * abs(length('ffobar')-3)) from Animal").list();
		session.delete(h);
		txn.commit();
		session.close();
		destroyTestBaseData();
	}

	private void createTestBaseData() {
		Session session = openSession();
		Transaction txn = session.beginTransaction();

		Mammal m1 = new Mammal();
		m1.setBodyWeight( 11f );
		m1.setDescription( "Mammal #1" );

		session.save( m1 );

		Mammal m2 = new Mammal();
		m2.setBodyWeight( 9f );
		m2.setDescription( "Mammal #2" );
		m2.setMother( m1 );

		session.save( m2 );

		txn.commit();
		session.close();

		createdAnimalIds.add( m1.getId() );
		createdAnimalIds.add( m2.getId() );
	}

	private void destroyTestBaseData() {
		Session session = openSession();
		Transaction txn = session.beginTransaction();

		for ( int i = 0; i < createdAnimalIds.size(); i++ ) {
			Animal animal = ( Animal ) session.load( Animal.class, ( Long ) createdAnimalIds.get( i ) );
			session.delete( animal );
		}

		txn.commit();
		session.close();
	}
	
	public void testImplicitJoin() throws Exception {
		Session session = openSession();
		Transaction t = session.beginTransaction();
		Animal a = new Animal();
		a.setBodyWeight(0.5f);
		a.setBodyWeight(1.5f);
		Animal b = new Animal();
		Animal mother = new Animal();
		mother.setBodyWeight(10.0f);
		mother.addOffspring(a);
		mother.addOffspring(b);
		session.persist(a);
		session.persist(b);
		session.persist(mother);
		List list = session.createQuery("from Animal a where a.mother.bodyWeight < 2.0 or a.mother.bodyWeight > 9.0").list();
		assertEquals( list.size(), 2 );
		list = session.createQuery("from Animal a where a.mother.bodyWeight > 2.0 and a.mother.bodyWeight > 9.0").list();
		assertEquals( list.size(), 2 );
		session.delete(b);
		session.delete(a);
		session.delete(mother);
		t.commit();
		session.close();
	}

	public void testFromOnly() throws Exception {

		createTestBaseData();

		Session session = openSession();

		List results = session.createQuery( "from Animal" ).list();
		assertEquals( "Incorrect result size", 2, results.size() );
		assertTrue( "Incorrect result return type", results.get( 0 ) instanceof Animal );

		session.close();

		destroyTestBaseData();
	}

	public void testSimpleSelect() throws Exception {

		createTestBaseData();

		Session session = openSession();

		List results = session.createQuery( "select a from Animal as a" ).list();
		assertEquals( "Incorrect result size", 2, results.size() );
		assertTrue( "Incorrect result return type", results.get( 0 ) instanceof Animal );

		session.close();

		destroyTestBaseData();
	}

	public void testEntityPropertySelect() throws Exception {

		createTestBaseData();

		Session session = openSession();

		List results = session.createQuery( "select a.mother from Animal as a" ).list();
//		assertEquals("Incorrect result size", 2, results.size());
		assertTrue( "Incorrect result return type", results.get( 0 ) instanceof Animal );

		session.close();

		destroyTestBaseData();
	}

	public void testWhere() throws Exception {

		createTestBaseData();

		Session session = openSession();
		List results = null;

		results = session.createQuery( "from Animal an where an.bodyWeight > 10" ).list();
		assertEquals( "Incorrect result size", 1, results.size() );

		results = session.createQuery( "from Animal an where not an.bodyWeight > 10" ).list();
		assertEquals( "Incorrect result size", 1, results.size() );

		results = session.createQuery( "from Animal an where an.bodyWeight between 0 and 10" ).list();
		assertEquals( "Incorrect result size", 1, results.size() );

		results = session.createQuery( "from Animal an where an.bodyWeight not between 0 and 10" ).list();
		assertEquals( "Incorrect result size", 1, results.size() );

		results = session.createQuery( "from Animal an where sqrt(an.bodyWeight)/2 > 10" ).list();
		assertEquals( "Incorrect result size", 0, results.size() );

		results = session.createQuery( "from Animal an where (an.bodyWeight > 10 and an.bodyWeight < 100) or an.bodyWeight is null" ).list();
		assertEquals( "Incorrect result size", 1, results.size() );

		session.close();

		destroyTestBaseData();
	}

	public void testEntityFetching() throws Exception {

		createTestBaseData();

		Session session = openSession();

		List results = session.createQuery( "from Animal an join fetch an.mother" ).list();
		assertEquals( "Incorrect result size", 1, results.size() );
		assertTrue( "Incorrect result return type", results.get( 0 ) instanceof Animal );
		Animal mother = ( ( Animal ) results.get( 0 ) ).getMother();
		assertTrue( "fetch uninitialized", mother != null && Hibernate.isInitialized( mother ) );

		results = session.createQuery( "select an from Animal an join fetch an.mother" ).list();
		assertEquals( "Incorrect result size", 1, results.size() );
		assertTrue( "Incorrect result return type", results.get( 0 ) instanceof Animal );
		mother = ( ( Animal ) results.get( 0 ) ).getMother();
		assertTrue( "fetch uninitialized", mother != null && Hibernate.isInitialized( mother ) );

		session.close();

		destroyTestBaseData();
	}

	public void testCollectionFetching() throws Exception {

		createTestBaseData();

		Session session = openSession();
		List results = session.createQuery( "from Animal an join fetch an.offspring" ).list();
		assertEquals( "Incorrect result size", 1, results.size() );
		assertTrue( "Incorrect result return type", results.get( 0 ) instanceof Animal );
		Collection os = ( ( Animal ) results.get( 0 ) ).getOffspring();
		assertTrue( "fetch uninitialized", os != null && Hibernate.isInitialized( os ) && os.size() == 1 );

		results = session.createQuery( "select an from Animal an join fetch an.offspring" ).list();
		assertEquals( "Incorrect result size", 1, results.size() );
		assertTrue( "Incorrect result return type", results.get( 0 ) instanceof Animal );
		os = ( ( Animal ) results.get( 0 ) ).getOffspring();
		assertTrue( "fetch uninitialized", os != null && Hibernate.isInitialized( os ) && os.size() == 1 );

		session.close();

		destroyTestBaseData();
	}

	public void testProjectionQueries() throws Exception {

		createTestBaseData();

		Session session = openSession();

		List results = session.createQuery( "select an.mother.id, max(an.bodyWeight) from Animal an group by an.mother.id" ).list();
		// mysql returns nulls in this group by
		assertEquals( "Incorrect result size", 2, results.size() );
		assertTrue( "Incorrect return type", results.get( 0 ) instanceof Object[] );
		assertEquals( "Incorrect return dimensions", 2, ( ( Object[] ) results.get( 0 ) ).length );

		session.close();

		destroyTestBaseData();

	}
	
	public void testStandardFunctions() throws Exception {
		Session session = openSession();
		Transaction t = session.beginTransaction();
		Product p = new Product();
		p.setDescription("a product");
		p.setPrice( new BigDecimal(1.0) );
		p.setProductId("abc123");
		session.persist(p);
		Object[] result = (Object[]) session
			.createQuery("select current_time(), current_date(), current_timestamp() from Product")
			.uniqueResult();
		assertTrue( result[0] instanceof Time );
		assertTrue( result[1] instanceof Date );
		assertTrue( result[2] instanceof Timestamp );
		assertNotNull( result[0] );
		assertNotNull( result[1] );
		assertNotNull( result[2] );
		session.delete(p);
		t.commit();
		session.close();
		
	}

	public void testDynamicInstantiationQueries() throws Exception {

		createTestBaseData();

		Session session = openSession();

		List results = session.createQuery( "select new Animal(an.description, an.bodyWeight) from Animal an" ).list();
		assertEquals( "Incorrect result size", 2, results.size() );
		assertTrue( "Incorrect return type", results.get( 0 ) instanceof Animal );

		Iterator iter = session.createQuery( "select new Animal(an.description, an.bodyWeight) from Animal an" ).iterate();
		assertTrue( "Incorrect result size", iter.hasNext() );
		assertTrue( "Incorrect return type", iter.next() instanceof Animal );

		results = session.createQuery( "select new list(an.description, an.bodyWeight) from Animal an" ).list();
		assertEquals( "Incorrect result size", 2, results.size() );
		assertTrue( "Incorrect return type", results.get( 0 ) instanceof List );
		assertEquals( "Incorrect return type", ( (List) results.get( 0 ) ).size(), 2 );

		iter = session.createQuery( "select new list(an.description, an.bodyWeight) from Animal an" ).iterate();
		assertTrue( "Incorrect result size", iter.hasNext() );
		Object obj = iter.next();
		assertTrue( "Incorrect return type", obj instanceof List );
		assertEquals( "Incorrect return type", ( (List) obj ).size(), 2 );
		
		results = session.createQuery( "select new map(an.description, an.bodyWeight) from Animal an" ).list();
		assertEquals( "Incorrect result size", 2, results.size() );
		assertTrue( "Incorrect return type", results.get( 0 ) instanceof Map );
		assertEquals( "Incorrect return type", ( (Map) results.get( 0 ) ).size(), 2 );
		assertTrue( ( (Map) results.get( 0 ) ).containsKey("0") );
		assertTrue( ( (Map) results.get( 0 ) ).containsKey("1") );

		results = session.createQuery( "select new map(an.description as descr, an.bodyWeight as bw) from Animal an" ).list();
		assertEquals( "Incorrect result size", 2, results.size() );
		assertTrue( "Incorrect return type", results.get( 0 ) instanceof Map );
		assertEquals( "Incorrect return type", ( (Map) results.get( 0 ) ).size(), 2 );
		assertTrue( ( (Map) results.get( 0 ) ).containsKey("descr") );
		assertTrue( ( (Map) results.get( 0 ) ).containsKey("bw") );

		iter = session.createQuery( "select new map(an.description, an.bodyWeight) from Animal an" ).iterate();
		assertTrue( "Incorrect result size", iter.hasNext() );
		obj = iter.next();
		assertTrue( "Incorrect return type", obj instanceof Map );
		assertEquals( "Incorrect return type", ( (Map) obj ).size(), 2 );
		
		ScrollableResults sr = session.createQuery( "select new map(an.description, an.bodyWeight) from Animal an" ).scroll();
		assertTrue( "Incorrect result size", sr.next() );
		obj = sr.get(0);
		assertTrue( "Incorrect return type", obj instanceof Map );
		assertEquals( "Incorrect return type", ( (Map) obj ).size(), 2 );
		sr.close();
		
		sr = session.createQuery( "select new Animal(an.description, an.bodyWeight) from Animal an" ).scroll();
		assertTrue( "Incorrect result size", sr.next() );
		assertTrue( "Incorrect return type", sr.get(0) instanceof Animal );
		sr.close();

		session.close();

		destroyTestBaseData();
	}

	public void testEJBQLFunctions() throws Exception {
		Session session = openSession();

		String hql = "from Animal a where a.description = concat('1', concat('2','3'), '4'||'5')||'0'";
		session.createQuery(hql).list();

		hql = "from Animal a where substring(a.description, 1, 3) = 'cat'";
		session.createQuery(hql).list();

		hql = "select substring(a.description, 1, 3) from Animal a";
		session.createQuery(hql).list();

		hql = "from Animal a where lower(a.description) = 'cat'";
		session.createQuery(hql).list();

		hql = "select lower(a.description) from Animal a";
		session.createQuery(hql).list();

		hql = "from Animal a where upper(a.description) = 'CAT'";
		session.createQuery(hql).list();

		hql = "select upper(a.description) from Animal a";
		session.createQuery(hql).list();

		hql = "from Animal a where length(a.description) = 5";
		session.createQuery(hql).list();

		hql = "select length(a.description) from Animal a";
		session.createQuery(hql).list();
		
		//note: postgres and db2 don't have a 3-arg form, it gets transformed to 2-args
		hql = "from Animal a where locate('abc', a.description, 2) = 2";
		session.createQuery(hql).list();

		hql = "from Animal a where locate('abc', a.description) = 2";
		session.createQuery(hql).list();

		hql = "select locate('cat', a.description, 2) from Animal a";
		session.createQuery(hql).list();
		
		if ( !( getDialect() instanceof DB2Dialect ) ) {
			hql = "from Animal a where trim(trailing '_' from a.description) = 'cat'";
			session.createQuery(hql).list();

			hql = "select trim(trailing '_' from a.description) from Animal a";
			session.createQuery(hql).list();

			hql = "from Animal a where trim(leading '_' from a.description) = 'cat'";
			session.createQuery(hql).list();

			hql = "from Animal a where trim(both from a.description) = 'cat'";
			session.createQuery(hql).list();
		}

		if ( !(getDialect() instanceof HSQLDialect) ) { //HSQL doesn't like trim() without specification
			hql = "from Animal a where trim(a.description) = 'cat'";
			session.createQuery(hql).list();
		}

		hql = "from Animal a where abs(a.bodyWeight) = sqrt(a.bodyWeight)";
		session.createQuery(hql).list();

		hql = "from Animal a where mod(16, 4) = 4";
		session.createQuery(hql).list();

		hql = "from Animal a where bit_length(a.bodyWeight) = 24";
		session.createQuery(hql).list();

		hql = "select bit_length(a.bodyWeight) from Animal a";
		session.createQuery(hql).list();

		/*hql = "select object(a) from Animal a where CURRENT_DATE = :p1 or CURRENT_TIME = :p2 or CURRENT_TIMESTAMP = :p3";
		session.createQuery(hql).list();*/

		// todo the following is not supported
		//hql = "select CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP from Animal a";
		//parse(hql, true);
		//System.out.println("sql: " + toSql(hql));

		hql = "from Animal a where a.description like '%a%'";
		session.createQuery(hql).list();

		hql = "from Animal a where a.description not like '%a%'";
		session.createQuery(hql).list();

		hql = "from Animal a where a.description like 'x%ax%' escape 'x'";
		session.createQuery(hql).list();
		
		session.close();
	}
	
	private void assertResultSize(String hql, int size) {
		Session session = openSession();
		Transaction txn = session.beginTransaction();
		assertEquals( size, session.createQuery(hql).list().size() );
		txn.commit();
		session.close();
	}

	public void testSubselectBetween() {
		assertResultSize("from Animal x where (select max(a.bodyWeight) from Animal a) in (1,2,3)", 0);
		assertResultSize("from Animal x where (select max(a.bodyWeight) from Animal a) between 0 and 100", 0);
		assertResultSize("from Animal x where (select max(a.description) from Animal a) like 'big%'", 0);
		assertResultSize("from Animal x where (select max(a.bodyWeight) from Animal a) is not null", 0);
		assertResultSize("from Animal x where exists (select max(a.bodyWeight) from Animal a)", 0);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite( ASTParserLoadingTest.class );
		return suite;
	}

}

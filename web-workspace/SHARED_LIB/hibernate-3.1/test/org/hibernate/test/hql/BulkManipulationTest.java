// $Id: BulkManipulationTest.java,v 1.43 2005/12/12 08:57:26 maxcsaucdk Exp $
package org.hibernate.test.hql;

import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hibernate.QueryException;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.hql.ast.HqlSqlWalker;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.test.TestCase;


/**
 * Tests execution of bulk UPDATE/DELETE statements through the new AST parser.
 *
 * @author Steve Ebersole
 */
public class BulkManipulationTest extends TestCase {

	public BulkManipulationTest(String name) {
		super( name );
	}

	public static Test suite() {
		return new TestSuite( BulkManipulationTest.class );
	}

	protected String[] getMappings() {
		return new String[] {
				"hql/Animal.hbm.xml",
		        "hql/Vehicle.hbm.xml",
		        "hql/KeyManyToOneEntity.hbm.xml",
		        "hql/Versions.hbm.xml",
				"hql/FooBarCopy.hbm.xml",
				"legacy/Multi.hbm.xml"
		};
	}


	// Non-exists ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void testDeleteNonExistentEntity() {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		try {
			s.createQuery( "delete NonExistentEntity" ).executeUpdate();
			fail( "no exception thrown" );
		}
		catch( QueryException e ) {
			System.out.println( "Caught expected error type : " + e.getMessage() );
		}

		t.commit();
		s.close();
	}

	public void testUpdateNonExistentEntity() {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		try {
			s.createQuery( "update NonExistentEntity e set e.someProp = ?" ).executeUpdate();
			fail( "no exception thrown" );
		}
		catch( QueryException e ) {
			System.out.println( "Caught expected error type : " + e.getMessage() );
		}

		t.commit();
		s.close();
	}


	// INSERTS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void testSimpleInsert() {
		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction t = s.beginTransaction();

		s.createQuery( "insert into Pickup (id, vin, owner) select id, vin, owner from Car" ).executeUpdate();
		
		t.commit();
		t = s.beginTransaction();

		s.createQuery( "delete Vehicle" ).executeUpdate();
		
		t.commit();
		s.close();

		data.cleanup();
	}

	public void testSimpleNativeSQLInsert() {
		TestData data = new TestData();
		data.prepare();

		try {
			Session s = openSession();
			Transaction t = s.beginTransaction();
			
			List l = s.createQuery("from Vehicle").list();
			assertEquals(l.size(),4);
			
			s.createSQLQuery( "insert into Pickup (id, vin, owner) select id, vin, owner from Car" ).executeUpdate();
			
			l = s.createQuery("from Vehicle").list();
			assertEquals(l.size(),5);
			
			t.commit();
			t = s.beginTransaction();
			
			s.createSQLQuery( "delete Vehicle" ).executeUpdate();
			
			l = s.createQuery("from Vehicle").list();
			assertEquals(l.size(),0);
			
			t.commit();
			s.close();
			fail("native sql bulk manipulation should fail with UnsupportedOperationException");
		} catch(UnsupportedOperationException eo) {
			// should happen
		}
		
		

		data.cleanup();
	}
	
	public void testInsertWithManyToOneFailureExpected() {
		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction t = s.beginTransaction();

		s.createQuery( "insert into Animal (description, bodyWeight, mother.id) select description, bodyWeight, mother.id from Human" ).executeUpdate();

		t.commit();
		t = s.beginTransaction();

		t.commit();
		s.close();

		data.cleanup();
	}

	public void testInsertWithMismatchedTypes() {
		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction t = s.beginTransaction();
		try {
			s.createQuery( "insert into Pickup (owner, vin, id) select id, vin, owner from Car" ).executeUpdate();
			fail( "mismatched types did not error" );
		}
		catch( QueryException e ) {
			// expected result
		}

		t.commit();
		t = s.beginTransaction();

		s.createQuery( "delete Vehicle" ).executeUpdate();

		t.commit();
		s.close();

		data.cleanup();
	}

	public void testInsertIntoSuperclassPropertiesFails() {
		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction t = s.beginTransaction();

		try {
			s.createQuery( "insert into Human (id, bodyWeight) select id, bodyWeight from Lizard" ).executeUpdate();
			fail( "superclass prop insertion did not error" );
		}
		catch( QueryException e ) {
			// expected result
		}

		t.commit();
		t = s.beginTransaction();

		s.createQuery( "delete Animal where mother is not null" ).executeUpdate();
		s.createQuery( "delete Animal" ).executeUpdate();

		t.commit();
		s.close();

		data.cleanup();
	}

	public void testInsertAcrossMappedJoinFails() {
		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction t = s.beginTransaction();

		try {
			s.createQuery( "insert into Joiner (name, joinedName) select vin, owner from Car" ).executeUpdate();
			fail( "mapped-join insertion did not error" );
		}
		catch( QueryException e ) {
			// expected result
		}

		t.commit();
		t = s.beginTransaction();

		s.createQuery( "delete Joiner" ).executeUpdate();
		s.createQuery( "delete Vehicle" ).executeUpdate();

		t.commit();
		s.close();

		data.cleanup();
	}

	public void testInsertWithGeneratedId() {
		// Make sure the env supports bulk inserts with generated ids...
		EntityPersister persister = sfi().getEntityPersister( PettingZoo.class.getName() );
		IdentifierGenerator generator = persister.getIdentifierGenerator();
		if ( !HqlSqlWalker.supportsIdGenWithBulkInsertion( generator ) ) {
			return;
		}

		// create a Zoo
		Zoo zoo = new Zoo();
		zoo.setName( "zoo" );

		Session s = openSession();
		Transaction t = s.beginTransaction();
		s.save( zoo );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		int count = s.createQuery( "insert into PettingZoo (name) select name from Zoo" ).executeUpdate();
		t.commit();
		s.close();

		assertEquals( "unexpected insertion count", 1, count );

		s = openSession();
		t = s.beginTransaction();
		PettingZoo pz = ( PettingZoo ) s.createQuery( "from PettingZoo" ).uniqueResult();
		t.commit();
		s.close();

		assertEquals( zoo.getName(), pz.getName() );
		assertTrue( zoo.getId() != pz.getId() );

		s = openSession();
		t = s.beginTransaction();
		s.createQuery( "delete Zoo" ).executeUpdate();
		t.commit();
		s.close();
	}

	public void testInsertWithGeneratedVersionAndId() {
		// Make sure the env supports bulk inserts with generated ids...
		EntityPersister persister = sfi().getEntityPersister( IntegerVersioned.class.getName() );
		IdentifierGenerator generator = persister.getIdentifierGenerator();
		if ( !HqlSqlWalker.supportsIdGenWithBulkInsertion( generator ) ) {
			return;
		}

		Session s = openSession();
		Transaction t = s.beginTransaction();

		IntegerVersioned entity = new IntegerVersioned( "int-vers" );
		s.save( entity );
		s.createQuery( "select id, name, version from IntegerVersioned" ).list();
		t.commit();
		s.close();

		Long initialId = entity.getId();
		int initialVersion = entity.getVersion();

		s = openSession();
		t = s.beginTransaction();
		int count = s.createQuery( "insert into IntegerVersioned ( name ) select name from IntegerVersioned" ).executeUpdate();
		t.commit();
		s.close();

		assertEquals( "unexpected insertion count", 1, count );

		s = openSession();
		t = s.beginTransaction();
		IntegerVersioned created = ( IntegerVersioned ) s.createQuery( "from IntegerVersioned where id <> :initialId" )
				.setLong( "initialId", initialId.longValue() )
				.uniqueResult();
		t.commit();
		s.close();

		assertEquals( "version was not seeded", initialVersion, created.getVersion() );

		s = openSession();
		t = s.beginTransaction();
		s.createQuery( "delete IntegerVersioned" ).executeUpdate();
		t.commit();
		s.close();
	}

	public void testInsertWithGeneratedTimestampVersion() {
		// Make sure the env supports bulk inserts with generated ids...
		EntityPersister persister = sfi().getEntityPersister( TimestampVersioned.class.getName() );
		IdentifierGenerator generator = persister.getIdentifierGenerator();
		if ( !HqlSqlWalker.supportsIdGenWithBulkInsertion( generator ) ) {
			return;
		}

		Session s = openSession();
		Transaction t = s.beginTransaction();

		TimestampVersioned entity = new TimestampVersioned( "int-vers" );
		s.save( entity );
		s.createQuery( "select id, name, version from TimestampVersioned" ).list();
		t.commit();
		s.close();

		Long initialId = entity.getId();
		//Date initialVersion = entity.getVersion();

		s = openSession();
		t = s.beginTransaction();
		int count = s.createQuery( "insert into TimestampVersioned ( name ) select name from TimestampVersioned" ).executeUpdate();
		t.commit();
		s.close();

		assertEquals( "unexpected insertion count", 1, count );

		s = openSession();
		t = s.beginTransaction();
		TimestampVersioned created = ( TimestampVersioned ) s.createQuery( "from TimestampVersioned where id <> :initialId" )
				.setLong( "initialId", initialId.longValue() )
				.uniqueResult();
		t.commit();
		s.close();

		assertNotNull( created.getVersion() );
		//assertEquals( "version was not seeded", initialVersion, created.getVersion() );

		s = openSession();
		t = s.beginTransaction();
		s.createQuery( "delete TimestampVersioned" ).executeUpdate();
		t.commit();
		s.close();
	}

	// UPDATES ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void testIncrementCounterVersion() {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		IntegerVersioned entity = new IntegerVersioned( "int-vers" );
		s.save( entity );
		t.commit();
		s.close();

		int initialVersion = entity.getVersion();

		s = openSession();
		t = s.beginTransaction();
		int count = s.createQuery( "update versioned IntegerVersioned set name = name" ).executeUpdate();
		assertEquals( "incorrect exec count", 1, count );
		t.commit();

		t = s.beginTransaction();
		entity = ( IntegerVersioned ) s.load( IntegerVersioned.class, entity.getId() );
		assertEquals( "version not incremented", initialVersion + 1, entity.getVersion() );

		s.delete( entity );
		t.commit();
		s.close();
	}

	public void testIncrementTimestampVersion() {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		TimestampVersioned entity = new TimestampVersioned( "ts-vers" );
		s.save( entity );
		t.commit();
		s.close();

		Date initialVersion = entity.getVersion();

		synchronized (this) {
			try {
				wait(1500);
			}
			catch (InterruptedException ie) {}
		}

		s = openSession();
		t = s.beginTransaction();
		int count = s.createQuery( "update versioned TimestampVersioned set name = name" ).executeUpdate();
		assertEquals( "incorrect exec count", 1, count );
		t.commit();

		t = s.beginTransaction();
		entity = ( TimestampVersioned ) s.load( TimestampVersioned.class, entity.getId() );
		assertTrue( "version not incremented", entity.getVersion().after( initialVersion ) );

		s.delete( entity );
		t.commit();
		s.close();
	}

	public void testUpdateOnComponent() {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		Human human = new Human();
		human.setName( new Name() );
		human.getName().setFirst("Stevee");
		human.getName().setInitial('X');
		human.getName().setLast("Ebersole");

		s.save( human );
		s.flush();

		t.commit();

		String correctName = "Steve";

		t = s.beginTransaction();

		int count = s.createQuery( "update Human set name.first = :correction where id = :id" )
				.setString( "correction", correctName )
				.setLong( "id", human.getId().longValue() )
				.executeUpdate();

		assertEquals( "Incorrect update count", 1, count );

		t.commit();

		t = s.beginTransaction();

		s.refresh( human );

		assertEquals( "Update did not execute properly", correctName, human.getName().getFirst() );

		s.createQuery( "delete Human" ).executeUpdate();
		t.commit();

		s.close();
	}

	public void testUpdateOnManyToOneFailureExpected() {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		s.createQuery( "update Animal a set a.mother = (select mother from a.mother.mother)" ).executeUpdate();

		t.commit();
		s.close();
	}

	public void testUpdateOnImplicitJoinFails() {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		Human human = new Human();
		human.setName( new Name() );
		human.getName().setFirst( "Steve" );
		human.getName().setInitial('E');

		Human mother = new Human();
		mother.setName( new Name() );
		mother.getName().setFirst( "Jane" );
		mother.getName().setInitial('E');
		human.setMother( mother );

		s.save( human );
		s.save( mother );
		s.flush();

		t.commit();

		t = s.beginTransaction();
		try {
			s.createQuery( "update Human set mother.name.initial = :initial" ).setString( "initial", "F" ).executeUpdate();
			fail( "update allowed across implicit join" );
		}
		catch( QueryException e ) {
			System.out.println( "TEST (OK) : " + e.getMessage() );
			// expected condition
		}

		s.createQuery( "delete Human where mother is not null" ).executeUpdate();
		s.createQuery( "delete Human" ).executeUpdate();
		t.commit();
		s.close();
	}

	public void testUpdateOnDiscriminatorSubclass() {
		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction t = s.beginTransaction();

		int count = s.createQuery( "update PettingZoo set name = name" ).executeUpdate();
		assertEquals( "Incorrect discrim subclass update count", 1, count );

		t.rollback();
		t = s.beginTransaction();

		count = s.createQuery( "update PettingZoo pz set pz.name = pz.name where pz.id = :id" )
				.setLong( "id", data.pettingZoo.getId().longValue() )
				.executeUpdate();
		assertEquals( "Incorrect discrim subclass update count", 1, count );

		t.rollback();
		t = s.beginTransaction();

		count = s.createQuery( "update Zoo as z set z.name = z.name" ).executeUpdate();
		assertEquals( "Incorrect discrim subclass update count", 2, count );

		t.rollback();
		t = s.beginTransaction();

		// TODO : not so sure this should be allowed.  Seems to me that if they specify an alias,
		// property-refs should be required to be qualified.
		count = s.createQuery( "update Zoo as z set name = name where id = :id" )
				.setLong( "id", data.zoo.getId().longValue() )
				.executeUpdate();
		assertEquals( "Incorrect discrim subclass update count", 1, count );

		t.commit();
		s.close();

		data.cleanup();
	}

	public void testUpdateOnAnimal() {
		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction t = s.beginTransaction();
		int count = s.createQuery( "update Animal set description = description where description = :desc" )
				.setString( "desc", data.frog.getDescription() )
				.executeUpdate();
		assertEquals( "Incorrect entity-updated count", 1, count );

		count = s.createQuery( "update Animal set description = :newDesc where description = :desc" )
				.setString( "desc", data.polliwog.getDescription() )
				.setString( "newDesc", "Tadpole" )
				.executeUpdate();
		assertEquals( "Incorrect entity-updated count", 1, count );

		Animal tadpole = ( Animal ) s.load( Animal.class, data.polliwog.getId() );
		assertEquals( "Update did not take effect", "Tadpole", tadpole.getDescription() );

		count = s.createQuery( "update Animal set bodyWeight = bodyWeight + :w1 + :w2" )
				.setDouble( "w1", 1 )
				.setDouble( "w2", 2 )
				.executeUpdate();
		assertEquals( "incorrect count on 'complex' update assignment", count, 6 );

		count = s.createQuery( "update Animal set bodyWeight = ( select max(bodyWeight) from Animal )" )
			.executeUpdate();

		t.commit();
		s.close();

		data.cleanup();
	}

	public void testUpdateOnMammal() {
		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction t = s.beginTransaction();

		int count = s.createQuery( "update Mammal set description = description" ).executeUpdate();
		assertEquals( "incorrect update count against 'middle' of joined-subclass hierarchy", 2, count );

		count = s.createQuery( "update Mammal set bodyWeight = ( select max(bodyWeight) from Animal )" ).executeUpdate();
		assertEquals( "incorrect update count against 'middle' of joined-subclass hierarchy", 2, count );

		t.commit();
		s.close();

		data.cleanup();
	}

	public void testUpdateSetNullUnionSubclass() {
		TestData data = new TestData();
		data.prepare();

		// These should reach out into *all* subclass tables...
		Session s = openSession();
		Transaction t = s.beginTransaction();

		int count = s.createQuery( "update Vehicle set owner = 'Steve'" ).executeUpdate();
		assertEquals( "incorrect restricted update count", 4, count );
		count = s.createQuery( "update Vehicle set owner = null where owner = 'Steve'" ).executeUpdate();
		assertEquals( "incorrect restricted update count", 4, count );

		count = s.createQuery( "delete Vehicle where owner is null" ).executeUpdate();
		assertEquals( "incorrect restricted update count", 4, count );

		t.commit();
		s.close();

		data.cleanup();
	}

	public void testUpdateSetNullOnDiscriminatorSubclass() {
		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction t = s.beginTransaction();

		int count = s.createQuery( "update PettingZoo set address.city = null" ).executeUpdate();
		assertEquals( "Incorrect discrim subclass delete count", 1, count );
		count = s.createQuery( "delete Zoo where address.city is null" ).executeUpdate();
		assertEquals( "Incorrect discrim subclass delete count", 1, count );

		count = s.createQuery( "update Zoo set address.city = null" ).executeUpdate();
		assertEquals( "Incorrect discrim subclass delete count", 1, count );
		count = s.createQuery( "delete Zoo where address.city is null" ).executeUpdate();
		assertEquals( "Incorrect discrim subclass delete count", 1, count );

		t.commit();
		s.close();

		data.cleanup();
	}

	public void testUpdateSetNullOnJoinedSubclass() {
		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction t = s.beginTransaction();

		int count = s.createQuery( "update Mammal set bodyWeight = null" ).executeUpdate();
		assertEquals( "Incorrect deletion count on joined subclass", 2, count );

		count = s.createQuery( "delete Animal where bodyWeight = null" ).executeUpdate();
		assertEquals( "Incorrect deletion count on joined subclass", 2, count );

		t.commit();
		s.close();

		data.cleanup();
	}


	// DELETES ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void testSimpleDeleteOnAnimal() {
		if ( getDialect().hasSelfReferentialForeignKeyBug() ) {
			return;
		}

		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction t = s.beginTransaction();

		int count = s.createQuery( "delete from Animal as a where a.id = :id" )
				.setLong( "id", data.polliwog.getId().longValue() )
				.executeUpdate();
		assertEquals( "Incorrect delete count", 1, count );

		count = s.createQuery( "delete Animal where id = :id" )
				.setLong( "id", data.catepillar.getId().longValue() )
				.executeUpdate();
		assertEquals( "incorrect delete count", 1, count );

		// HHH-873...
		count = s.createQuery( "delete from User u where u not in (select u from User u)" ).executeUpdate();
		assertEquals( 0, count );

		count = s.createQuery( "delete Animal a" ).executeUpdate();
		assertEquals( "Incorrect delete count", 4, count );

		List list = s.createQuery( "select a from Animal as a" ).list();
		assertTrue( "table not empty", list.isEmpty() );

		t.commit();
		s.close();
		data.cleanup();
	}

	public void testDeleteOnDiscriminatorSubclass() {
		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction t = s.beginTransaction();

		int count = s.createQuery( "delete PettingZoo" ).executeUpdate();
		assertEquals( "Incorrect discrim subclass delete count", 1, count );

		count = s.createQuery( "delete Zoo" ).executeUpdate();
		assertEquals( "Incorrect discrim subclass delete count", 1, count );

		t.commit();
		s.close();

		data.cleanup();
	}

	public void testDeleteOnJoinedSubclass() {
		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction t = s.beginTransaction();

		int count = s.createQuery( "delete Mammal where bodyWeight > 150" ).executeUpdate();
		assertEquals( "Incorrect deletion count on joined subclass", 1, count );

		count = s.createQuery( "delete Mammal" ).executeUpdate();
		assertEquals( "Incorrect deletion count on joined subclass", 1, count );

		count = s.createQuery( "delete SubMulti" ).executeUpdate();
		assertEquals( "Incorrect deletion count on joined subclass", 0, count );

		t.commit();
		s.close();

		data.cleanup();
	}

	public void testDeleteOnMappedJoin() {
		TestData data = new TestData();
		data.prepare();

		Session s = openSession();
		Transaction t = s.beginTransaction();

		int count = s.createQuery( "delete Joiner where joinedName = :joinedName" ).setString( "joinedName", "joined-name" ).executeUpdate();
		assertEquals( "Incorrect deletion count on joined subclass", 1, count );

		t.commit();
		s.close();

		data.cleanup();
	}
	
	public void testDeleteUnionSubclassAbstractRoot() {
		TestData data = new TestData();
		data.prepare();

		// These should reach out into *all* subclass tables...
		Session s = openSession();
		Transaction t = s.beginTransaction();

		int count = s.createQuery( "delete Vehicle where owner = :owner" ).setString( "owner", "Steve" ).executeUpdate();
		assertEquals( "incorrect restricted update count", 1, count );

		count = s.createQuery( "delete Vehicle" ).executeUpdate();
		assertEquals( "incorrect update count", 3, count );
		t.commit();
		s.close();

		data.cleanup();
	}

	public void testDeleteUnionSubclassConcreteSubclass() {
		TestData data = new TestData();
		data.prepare();

		// These should only affect the given table
		Session s = openSession();
		Transaction t = s.beginTransaction();

		int count = s.createQuery( "delete Truck where owner = :owner" ).setString( "owner", "Steve" ).executeUpdate();
		assertEquals( "incorrect restricted update count", 1, count );

		count = s.createQuery( "delete Truck" ).executeUpdate();
		assertEquals( "incorrect update count", 2, count );
		t.commit();
		s.close();

		data.cleanup();
	}

	public void testDeleteUnionSubclassLeafSubclass() {
		TestData data = new TestData();
		data.prepare();

		// These should only affect the given table
		Session s = openSession();
		Transaction t = s.beginTransaction();

		int count = s.createQuery( "delete Car where owner = :owner" ).setString( "owner", "Kirsten" ).executeUpdate();
		assertEquals( "incorrect restricted update count", 1, count );

		count = s.createQuery( "delete Car" ).executeUpdate();
		assertEquals( "incorrect update count", 0, count );
		t.commit();
		s.close();

		data.cleanup();
	}

	public void testDeleteWithMetadataWhereFragments() throws Throwable {
		Session s = openSession();
		Transaction t = s.beginTransaction();

		// Note: we are just checking the syntax here...
		s.createQuery("delete from Bar").executeUpdate();
		s.createQuery("delete from Bar where barString = 's'").executeUpdate();

		t.commit();
		s.close();
	}

	private class TestData {

		private Animal polliwog;
		private Animal catepillar;
		private Animal frog;
		private Animal butterfly;

		private Zoo zoo;
		private Zoo pettingZoo;

		private void prepare() {
			Session s = openSession();
			Transaction txn = s.beginTransaction();

			polliwog = new Animal();
			polliwog.setBodyWeight( 12 );
			polliwog.setDescription( "Polliwog" );

			catepillar = new Animal();
			catepillar.setBodyWeight( 10 );
			catepillar.setDescription( "Catepillar" );

			frog = new Animal();
			frog.setBodyWeight( 34 );
			frog.setDescription( "Frog" );

			polliwog.setFather( frog );
			frog.addOffspring( polliwog );

			butterfly = new Animal();
			butterfly.setBodyWeight( 9 );
			butterfly.setDescription( "Butterfly" );

			catepillar.setMother( butterfly );
			butterfly.addOffspring( catepillar );

			s.save( frog );
			s.save( polliwog );
			s.save( butterfly );
			s.save( catepillar );

			Dog dog = new Dog();
			dog.setBodyWeight( 200 );
			dog.setDescription( "dog" );
			s.save( dog );

			Cat cat = new Cat();
			cat.setBodyWeight( 100 );
			cat.setDescription( "cat" );
			s.save( cat );

			zoo = new Zoo();
			zoo.setName( "Zoo" );
			Address add = new Address();
			add.setCity("MEL");
			add.setCountry("AU");
			add.setStreet("Main st");
			add.setPostalCode("3000");
			zoo.setAddress(add);
			
			pettingZoo = new PettingZoo();
			pettingZoo.setName( "Petting Zoo" );
			Address addr = new Address();
			addr.setCity("Sydney");
			addr.setCountry("AU");
			addr.setStreet("High st");
			addr.setPostalCode("2000");
			pettingZoo.setAddress(addr);

			s.save( zoo );
			s.save( pettingZoo );

			Joiner joiner = new Joiner();
			joiner.setJoinedName( "joined-name" );
			joiner.setName( "name" );
			s.save( joiner );

			Car car = new Car();
			car.setVin( "123c" );
			car.setOwner( "Kirsten" );
			s.save( car );

			Truck truck = new Truck();
			truck.setVin( "123t" );
			truck.setOwner( "Steve" );
			s.save( truck );

			SUV suv = new SUV();
			suv.setVin( "123s" );
			suv.setOwner( "Joe" );
			s.save( suv );

			Pickup pickup = new Pickup();
			pickup.setVin( "123p" );
			pickup.setOwner( "Cecelia" );
			s.save( pickup );

			txn.commit();
			s.close();
		}

		private void cleanup() {
			Session s = openSession();
			Transaction txn = s.beginTransaction();

			s.createQuery("delete from Animal where mother is not null or father is not null").executeUpdate();
			String[] entityNames = new String[] { "Animal", "Zoo", "Joiner", "Vehicle" };
			for ( int i = 0; i < entityNames.length; i++ ) {
				s.createQuery( "delete from " + entityNames[i] ).executeUpdate();
			}

			txn.commit();
			s.close();
		}
	}

	private SessionFactoryImplementor sfi() {
		return ( SessionFactoryImplementor ) getSessions();
	}
}

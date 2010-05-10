//$Id: AllTests.java,v 1.84 2005/12/08 02:39:37 oneovthafew Exp $
package org.hibernate.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.hibernate.test.array.ArrayTest;
import org.hibernate.test.ast.ASTIteratorTest;
import org.hibernate.test.ast.ASTUtilTest;
import org.hibernate.test.batchfetch.BatchFetchTest;
import org.hibernate.test.bidi.AuctionTest;
import org.hibernate.test.bidi.AuctionTest2;
import org.hibernate.test.cache.SecondLevelCacheTest;
import org.hibernate.test.cascade.RefreshTest;
import org.hibernate.test.cid.CompositeIdTest;
import org.hibernate.test.collection.CollectionTest;
import org.hibernate.test.component.ComponentTest;
import org.hibernate.test.compositeelement.CompositeElementTest;
import org.hibernate.test.comppropertyref.ComponentPropertyRefTest;
import org.hibernate.test.connections.ConnectionsSuite;
import org.hibernate.test.criteria.CriteriaQueryTest;
import org.hibernate.test.cuk.CompositePropertyRefTest;
import org.hibernate.test.cut.CompositeUserTypeTest;
import org.hibernate.test.discriminator.DiscriminatorTest;
import org.hibernate.test.dom4j.Dom4jAccessorTest;
import org.hibernate.test.dom4j.Dom4jManyToOneTest;
import org.hibernate.test.dom4j.Dom4jTest;
import org.hibernate.test.dynamic.DynamicClassTest;
import org.hibernate.test.dynamicentity.interceptor.InterceptorDynamicEntityTest;
import org.hibernate.test.dynamicentity.tuplizer.TuplizerDynamicEntityTest;
import org.hibernate.test.ecid.EmbeddedCompositeIdTest;
import org.hibernate.test.entity.MultiRepresentationTest;
import org.hibernate.test.exception.SQLExceptionConversionTest;
import org.hibernate.test.extralazy.ExtraLazyTest;
import org.hibernate.test.filter.DynamicFilterTest;
import org.hibernate.test.formulajoin.FormulaJoinTest;
import org.hibernate.test.generated.TimestampGeneratedValuesWithCachingTest;
import org.hibernate.test.generated.TriggerGeneratedValuesWithCachingTest;
import org.hibernate.test.generated.TriggerGeneratedValuesWithoutCachingTest;
import org.hibernate.test.generatedkeys.oracle.OracleGeneratedKeysTest;
import org.hibernate.test.hql.HQLSuite;
import org.hibernate.test.id.MultipleHiLoPerTableGeneratorTest;
import org.hibernate.test.idbag.IdBagTest;
import org.hibernate.test.idclass.IdClassTest;
import org.hibernate.test.immutable.ImmutableTest;
import org.hibernate.test.instrument.InstrumentTest;
import org.hibernate.test.interceptor.InterceptorTest;
import org.hibernate.test.interfaceproxy.InterfaceProxyTest;
import org.hibernate.test.iterate.IterateTest;
import org.hibernate.test.join.JoinTest;
import org.hibernate.test.joinedsubclass.JoinedSubclassTest;
import org.hibernate.test.joinfetch.JoinFetchTest;
import org.hibernate.test.lazycache.InstrumentCacheTest;
import org.hibernate.test.lazycache.InstrumentCacheTest2;
import org.hibernate.test.lazyonetoone.LazyOneToOneTest;
import org.hibernate.test.legacy.ABCProxyTest;
import org.hibernate.test.legacy.ABCTest;
import org.hibernate.test.legacy.CacheTest;
import org.hibernate.test.legacy.ComponentNotNullTest;
import org.hibernate.test.legacy.ConfigurationPerformanceTest;
import org.hibernate.test.legacy.FooBarTest;
import org.hibernate.test.legacy.FumTest;
import org.hibernate.test.legacy.IJ2Test;
import org.hibernate.test.legacy.IJTest;
import org.hibernate.test.legacy.MapTest;
import org.hibernate.test.legacy.MasterDetailTest;
import org.hibernate.test.legacy.MultiTableTest;
import org.hibernate.test.legacy.NonReflectiveBinderTest;
import org.hibernate.test.legacy.OneToOneCacheTest;
import org.hibernate.test.legacy.ParentChildTest;
import org.hibernate.test.legacy.QueryByExampleTest;
import org.hibernate.test.legacy.SQLFunctionsTest;
import org.hibernate.test.legacy.SQLLoaderTest;
import org.hibernate.test.legacy.StatisticsTest;
import org.hibernate.test.manytomany.ManyToManyTest;
import org.hibernate.test.map.MapIndexFormulaTest;
import org.hibernate.test.mapcompelem.MapCompositeElementTest;
import org.hibernate.test.mapelemformula.MapElementFormulaTest;
import org.hibernate.test.mapping.PersistentClassVisitorTest;
import org.hibernate.test.mapping.ValueVisitorTest;
import org.hibernate.test.mixed.MixedTest;
import org.hibernate.test.naturalid.NaturalIdTest;
import org.hibernate.test.ondelete.OnDeleteTest;
import org.hibernate.test.onetomany.OneToManyTest;
import org.hibernate.test.onetoone.joined.OneToOneTest;
import org.hibernate.test.onetooneformula.OneToOneFormulaTest;
import org.hibernate.test.ops.CreateTest;
import org.hibernate.test.ops.GetLoadTest;
import org.hibernate.test.ops.MergeTest;
import org.hibernate.test.ops.SaveOrUpdateTest;
import org.hibernate.test.optlock.OptimisticLockTest;
import org.hibernate.test.ordered.OrderByTest;
import org.hibernate.test.orphan.OrphanIdRollbackTest;
import org.hibernate.test.orphan.OrphanTest;
import org.hibernate.test.pagination.PaginationTest;
import org.hibernate.test.propertyref.PropertyRefTest;
import org.hibernate.test.proxy.ProxyTest;
import org.hibernate.test.querycache.QueryCacheTest;
import org.hibernate.test.readonly.ReadOnlyTest;
import org.hibernate.test.rowid.RowIdTest;
import org.hibernate.test.sorted.SortTest;
import org.hibernate.test.sql.MSSQLTest;
import org.hibernate.test.sql.MySQLTest;
import org.hibernate.test.sql.OracleSQLTest;
import org.hibernate.test.stats.SessionStatsTest;
import org.hibernate.test.stats.StatsTest;
import org.hibernate.test.subclassfilter.DiscrimSubclassFilterTest;
import org.hibernate.test.subclassfilter.JoinedSubclassFilterTest;
import org.hibernate.test.subclassfilter.UnionSubclassFilterTest;
import org.hibernate.test.subclasspropertyref.SubclassPropertyRefTest;
import org.hibernate.test.subselect.SubselectTest;
import org.hibernate.test.subselectfetch.SubselectFetchTest;
import org.hibernate.test.ternary.TernaryTest;
import org.hibernate.test.timestamp.TimestampTest;
import org.hibernate.test.tm.CMTTest;
import org.hibernate.test.typedmanytoone.TypedManyToOneTest;
import org.hibernate.test.typedonetoone.TypedOneToOneTest;
import org.hibernate.test.typeparameters.TypeParameterTest;
import org.hibernate.test.unconstrained.UnconstrainedTest;
import org.hibernate.test.unidir.BackrefTest;
import org.hibernate.test.unionsubclass.UnionSubclassTest;
import org.hibernate.test.version.VersionTest;
import org.hibernate.test.version.db.DbVersionTest;
import org.hibernate.test.version.sybase.SybaseTimestampVersioningTest;
import org.hibernate.test.where.WhereTest;

/**
 * @author Gavin King
 */
public class AllTests {

	/**
	 * Returns the entire test suite (both legacy and new
	 *
	 * @return the entire test suite
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest( NewTests.suite() );
		suite.addTest( LegacyTests.suite() );
		return suite;
	}

	/**
	 * Runs the entire test suite.
	 * <p/>
	 * @see #suite
	 * @param args n/a
	 */
	public static void main(String args[]) {
		TestRunner.run( suite() );
	}

	/**
	 * An inner class representing the legacy test suite.
	 */
	public static class LegacyTests {

		/**
		 * Returns the legacy test suite
		 *
		 * @return the legacy test suite
		 */
		public static Test suite() {
			TestSuite suite = new TestSuite("Legacy tests suite");
			suite.addTest( FumTest.suite() );
			suite.addTest( MasterDetailTest.suite() );
			suite.addTest( ParentChildTest.suite() );
			suite.addTest( ABCTest.suite() );
			suite.addTest( ABCProxyTest.suite() );
			suite.addTest( SQLFunctionsTest.suite() );
			suite.addTest( SQLLoaderTest.suite() );
			suite.addTest( MultiTableTest.suite() );
			suite.addTest( MapTest.suite() );
			suite.addTest( QueryByExampleTest.suite() );
			suite.addTest( ComponentNotNullTest.suite() );
			suite.addTest( IJTest.suite() );
			suite.addTest( IJ2Test.suite() );
			suite.addTest( FooBarTest.suite() );
			suite.addTest( StatisticsTest.suite() );
			suite.addTest( CacheTest.suite() );
			suite.addTest( OneToOneCacheTest.suite() );
			suite.addTest( NonReflectiveBinderTest.suite() );
			suite.addTest( ConfigurationPerformanceTest.suite() ); // Added to ensure we can utilize the recommended performance tips ;)
			return suite;
		}

		/**
		 * Run the legacy test suite
		 *
		 * @param args n/a
		 */
		public static void main(String[] args) {
			TestRunner.run( suite() );
		}
	}

	/**
	 * An inner class representing the new test suite.
	 */
	public static class NewTests {

		/**
		 * Returns the new test suite
		 *
		 * @return the new test suite
		 */
		public static Test suite() {
			TestSuite suite = new TestSuite("New tests suite");
			suite.addTest( GetLoadTest.suite() );
			suite.addTest( CreateTest.suite() );
			suite.addTest( SaveOrUpdateTest.suite() );
			suite.addTest( MergeTest.suite() );
			suite.addTest( NaturalIdTest.suite() );
			suite.addTest( ComponentTest.suite() );
			suite.addTest( ProxyTest.suite() );
			suite.addTest( VersionTest.suite() );
			suite.addTest( TimestampTest.suite() );
			suite.addTest( InterceptorTest.suite() );
			suite.addTest( EmbeddedCompositeIdTest.suite() );
			suite.addTest( ImmutableTest.suite() );
			suite.addTest( ReadOnlyTest.suite() );
			suite.addTest( IdClassTest.suite() );
			suite.addTest( ArrayTest.suite() );
			suite.addTest( TernaryTest.suite() );
			suite.addTest( CollectionTest.suite() );
			suite.addTest( IdBagTest.suite() );
			suite.addTest( MapCompositeElementTest.suite() );
			suite.addTest( MapIndexFormulaTest.suite() );
			suite.addTest( MapElementFormulaTest.suite() );
			suite.addTest( BackrefTest.suite() );
			suite.addTest( BatchFetchTest.suite() );
			suite.addTest( CompositeIdTest.suite() );
			suite.addTest( CompositeElementTest.suite() );
			suite.addTest( CompositePropertyRefTest.suite() );
			suite.addTest( FormulaJoinTest.suite() );
			suite.addTest( DiscriminatorTest.suite() );
			suite.addTest( DynamicClassTest.suite() );
			suite.addTest( DynamicFilterTest.suite() );
			suite.addTest( InterfaceProxyTest.suite() );
			suite.addTest( OrphanTest.suite() );
			suite.addTest( OrphanIdRollbackTest.suite() );
			suite.addTest( JoinTest.suite() );
			suite.addTest( JoinedSubclassTest.suite() );
			suite.addTest( org.hibernate.test.unionsubclass2.UnionSubclassTest.suite() );
			suite.addTest( MixedTest.suite() );
			suite.addTest( OneToManyTest.suite() );
			suite.addTest( ManyToManyTest.suite() );
			suite.addTest( OneToOneFormulaTest.suite() );
			suite.addTest( OneToOneTest.suite() );
			suite.addTest( org.hibernate.test.onetoone.singletable.OneToOneTest.suite() );
			suite.addTest( org.hibernate.test.onetoonelink.OneToOneTest.suite() );
			suite.addTest( OptimisticLockTest.suite() );
			suite.addTest( PropertyRefTest.suite() );
			suite.addTest( ComponentPropertyRefTest.suite() );
			suite.addTest( org.hibernate.test.joineduid.PropertyRefTest.suite() );
			suite.addTest( org.hibernate.test.orphan.PropertyRefTest.suite() );
			suite.addTest( SubclassPropertyRefTest.suite() );
			suite.addTest( OracleSQLTest.suite() );
			suite.addTest( MSSQLTest.suite() );
			suite.addTest( MySQLTest.suite() );
			suite.addTest( CriteriaQueryTest.suite() );
			suite.addTest( SubselectTest.suite() );
			suite.addTest( SubselectFetchTest.suite() );
			suite.addTest( JoinFetchTest.suite() );
			suite.addTest( UnionSubclassTest.suite() );
			suite.addTest( ASTIteratorTest.suite() );
			suite.addTest( HQLSuite.suite() );
			suite.addTest( ASTUtilTest.suite() );
			suite.addTest( SecondLevelCacheTest.suite() );
			suite.addTest( QueryCacheTest.suite() );
			suite.addTest( CompositeUserTypeTest.suite() );
			suite.addTest( TypeParameterTest.suite() );
			suite.addTest( TypedOneToOneTest.suite() );
			suite.addTest( TypedManyToOneTest.suite() );
			suite.addTest( CMTTest.suite() );
			suite.addTest( MultipleHiLoPerTableGeneratorTest.suite() );
			suite.addTest( MultiRepresentationTest.suite() );
			suite.addTest( Dom4jAccessorTest.suite() );
			suite.addTest( Dom4jTest.suite() );
			suite.addTest( Dom4jManyToOneTest.suite() );
			suite.addTest( UnionSubclassFilterTest.suite() );
			suite.addTest( JoinedSubclassFilterTest.suite() );
			suite.addTest( DiscrimSubclassFilterTest.suite() );
			suite.addTest( UnconstrainedTest.suite() );
			suite.addTest( RowIdTest.suite() );
			suite.addTest( OnDeleteTest.suite() );
			suite.addTest( OrderByTest.suite() );
			suite.addTest( SortTest.suite() );
			suite.addTest( WhereTest.suite() );
			suite.addTest( IterateTest.suite() );
			suite.addTest( RefreshTest.suite() );
			suite.addTest( ExtraLazyTest.suite() );
			suite.addTest( StatsTest.suite() );
			suite.addTest( SessionStatsTest.suite() );
			suite.addTest( ConnectionsSuite.suite() );
			suite.addTest( SQLExceptionConversionTest.suite() );
			suite.addTest( ValueVisitorTest.suite() );
			suite.addTest( PersistentClassVisitorTest.suite() );
			suite.addTest( AuctionTest.suite() );
			suite.addTest( AuctionTest2.suite() );
			suite.addTest( PaginationTest.suite() );
			if ( InstrumentTest.isRunnable() ) suite.addTest( InstrumentTest.suite() );
			if ( LazyOneToOneTest.isRunnable() ) suite.addTest( LazyOneToOneTest.suite() );
			if ( InstrumentCacheTest.isRunnable() ) suite.addTest( InstrumentCacheTest.suite() );
			if ( InstrumentCacheTest2.isRunnable() ) suite.addTest( InstrumentCacheTest2.suite() );
			suite.addTest( SybaseTimestampVersioningTest.suite() );
			suite.addTest( DbVersionTest.suite() );
			suite.addTest( TimestampGeneratedValuesWithCachingTest.suite() );
			suite.addTest( TriggerGeneratedValuesWithCachingTest.suite() );
			suite.addTest( TriggerGeneratedValuesWithoutCachingTest.suite() );
			suite.addTest( OracleGeneratedKeysTest.suite() );
			suite.addTest( InterceptorDynamicEntityTest.suite() );
			suite.addTest( TuplizerDynamicEntityTest.suite() );
			return suite;
		}

		/**
		 * Runs the new test suite
		 *
		 * @param args n/a
		 */
		public static void main(String[] args) {
			TestRunner.run( suite() );
		}
	}

}
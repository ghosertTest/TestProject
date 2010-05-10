//$Id: AuctionTest2.java,v 1.3 2005/05/24 19:29:21 oneovthafew Exp $
package org.hibernate.test.bidi;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.dialect.Oracle9Dialect;
import org.hibernate.test.TestCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Gavin King
 */
public class AuctionTest2 extends TestCase {
	
	public AuctionTest2(String str) {
		super(str);
	}

	public void testLazy() {
		
		if ( getDialect() instanceof Oracle9Dialect ) return; //ora doesn't like exists() in the select clause
		
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Auction a = new Auction();
		a.setDescription("an auction for something");
		a.setEnd( new Date() );
		Bid b = new Bid();
		b.setAmount( new BigDecimal(123.34) );
		b.setSuccessful(true);
		b.setDatetime( new Date() );
		b.setItem(a);
		a.getBids().add(b);
		a.setSuccessfulBid(b);
		s.persist(b);
		t.commit();
		s.close();
		
		Long aid = a.getId();
		Long bid = b.getId();
		
		s = openSession();
		t = s.beginTransaction();
		b = (Bid) s.load( Bid.class, bid );
		assertFalse( Hibernate.isInitialized(b) );
		a = (Auction) s.get( Auction.class, aid );
		assertFalse( Hibernate.isInitialized( a.getBids() ) );
		assertFalse( Hibernate.isInitialized( a.getSuccessfulBid() ) );
		assertSame( a.getBids().iterator().next(), b );
		assertSame( b, a.getSuccessfulBid() );
		assertTrue( Hibernate.isInitialized(b) );
		assertTrue( b.isSuccessful() );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		b = (Bid) s.load( Bid.class, bid );
		assertFalse( Hibernate.isInitialized(b) );
		a = (Auction) s.createQuery("from Auction a left join fetch a.bids").uniqueResult();
		assertTrue( Hibernate.isInitialized(b) );
		assertTrue( Hibernate.isInitialized( a.getBids() ) );
		assertSame( b, a.getSuccessfulBid() );
		assertSame( a.getBids().iterator().next(), b );
		assertTrue( b.isSuccessful() );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		b = (Bid) s.load( Bid.class, bid );
		a = (Auction) s.load( Auction.class, aid );
		assertFalse( Hibernate.isInitialized(b) );
		assertFalse( Hibernate.isInitialized(a) );
		s.createQuery("from Auction a left join fetch a.successfulBid").list();
		assertTrue( Hibernate.isInitialized(b) );
		assertTrue( Hibernate.isInitialized(a) );
		assertSame( b, a.getSuccessfulBid() );
		assertFalse( Hibernate.isInitialized( a.getBids() ) );
		assertSame( a.getBids().iterator().next(), b );
		assertTrue( b.isSuccessful() );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		b = (Bid) s.load( Bid.class, bid );
		a = (Auction) s.load( Auction.class, aid );
		assertFalse( Hibernate.isInitialized(b) );
		assertFalse( Hibernate.isInitialized(a) );
		assertSame( s.get( Bid.class, bid ), b );
		assertTrue( Hibernate.isInitialized(b) );
		assertSame( s.get(Auction.class, aid ), a );
		assertTrue( Hibernate.isInitialized(a) );
		assertSame( b, a.getSuccessfulBid() );
		assertFalse( Hibernate.isInitialized( a.getBids() ) );
		assertSame( a.getBids().iterator().next(), b );
		assertTrue( b.isSuccessful() );
		t.commit();
		s.close();
	}
	
	protected String[] getMappings() {
		return new String[] { "bidi/Auction2.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(AuctionTest2.class);
	}

}

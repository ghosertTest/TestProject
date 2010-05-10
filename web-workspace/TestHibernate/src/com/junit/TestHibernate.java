package com.junit;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import com.loadtrend.TAddress;
import com.loadtrend.TGroup;
import com.loadtrend.TPassport;
import com.loadtrend.TUser;

import junit.framework.TestCase;

public class TestHibernate extends TestCase
{
    private Session session = null;
    
    private SessionFactory sessionFactory = null;
    
    protected void setUp() throws Exception
    {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
    }

    protected void tearDown() throws Exception
    {
        session.close();
    }
    
    public void testInsert()
    {
        TUser user = new TUser();
        user.setName( "Jiawei Zhang" );
        user.setAge( new Integer( 22 ) );
        user.setGroup( (TGroup) session.load( TGroup.class, new Integer(1) ) );
        Transaction transaction = session.beginTransaction();
        session.save( user );
        session.flush();
        transaction.commit();
    }

    public void testCriteria()
    {
        Criteria criteria = session.createCriteria( TUser.class );
        criteria.add( Restrictions.and( Restrictions.eq( "name", "Jiawei Zhang" ),
                                        Restrictions.eq( "age", new Integer( 22 ) ) ) );
        Iterator it = criteria.list().iterator();
        while ( it.hasNext() )
        {
            TUser user = (TUser) it.next();
            System.out.println( "UserName: " + user.getName() + " " + "ID: " + user.getId() );
        }
    }
    
    public void testDelete()
    {
        // The first way to delete a item.
        // TUser user = (TUser) session.get( TUser.class, new Integer(1) );
        // session.delete( user );
        
        // The second way to delete item(s). Better way to delete items. Recommend.
        String hql = "delete TUser where name='Jiawei Zhang'";

        Transaction transaction = session.beginTransaction();
        session.createQuery( hql ).executeUpdate();
        session.flush();
        transaction.commit();
    }
    
    public void testSelect()
    {
        // The first way to get POJO
        String hql = "from TUser where name='Henry Hua'";
        
        List userList = session.createQuery( hql ).list();
        Iterator it = userList.iterator();
        
        while ( it.hasNext() )
        {
            TUser user = (TUser) it.next();
            System.out.println( "Username: " + user.getName() + " " + "Age: " + user.getAge() );
        }
        
        // The second way to get POJO
        TPassport passport = (TPassport) session.load( TPassport.class, new Integer(1) );
        System.out.println( "ID: " + passport.getId() + " " + "Expiry: " + passport.getExpiry() );
    }
    
    public void testOneToOne()
    {        
        TUser user = new TUser();
        user.setName( "Henry Hua" );
        user.setAge( new Integer( 22 ) );
        user.setGroup( (TGroup) session.load( TGroup.class, new Integer(1) ) );
        
        TPassport passport = new TPassport();
        passport.setSerial( "420194" );
        passport.setExpiry( new Integer( 2006 ) );
        
        passport.setUser( user );
        user.setPassport( passport );
        
        Transaction transaction = session.beginTransaction();
        session.save( user );
        session.flush();
        transaction.commit();
    }
    
    public void testManyToOne()
    {
        TUser user = (TUser) session.load( TUser.class, new Integer( 1 ) );
        System.out.println( "User: " + user.getName() + " belong to the group named: " + user.getGroup().getName() );
    }
    
    public void testOneToMany()
    {
        TGroup group = (TGroup) session.load( TGroup.class, new Integer(1) );
        Iterator it = group.getUsers().iterator();
        while ( it.hasNext() )
        {
            TUser user = (TUser) it.next();
            System.out.println( "Group named: " + group.getName() + " has the member named: " + user.getName() );
        }
        
        // Save "one" cascade to save "many"
        TUser user = new TUser();
        user.setName( "Sunny Mao" );
        user.setAge( new Integer( 45 ) );
        
        group.getUsers().add( user );
        
        // Because TGroup.hbm.xml: inverse="true", so we should maintain the relation in TUser
        // We suggest set inverse="true" in "one" side not "many" side.
        user.setGroup( group );
        
        Transaction transaction = session.beginTransaction();
        
        // Because TGroup.hbm.xml: cascade="all", so we could save user by saving group.
        // Two SQLs will be execute.
        session.save( group );
        
        // But the best way to save user is the method below:
        // Just one SQL will be execute.
        // session.save(user);
        
        session.flush();
        transaction.commit();
        
    }
    
    public void testManyToMany()
    {
        
    }
    
    public void testHQL()
    {
//        String hql = "from TUser as user where user.name='Zhang Jiawei'";
//        String hql = "from TUser as user where user.age>20;
//        String hql = "from TUser as user where user.age between 20 and 30;
//        String hql = "from TUser as user where user.age in (18, 38);
//        String hql = "from TUser as user where user.name is null;
//        String hql = "from TUser as user where user.name like 'Zhang%_';
        
//        String hql = "select count(*), min(user.age) from TUser as user";
//        String hql = "select upper(user.name) from TUser as user";
//        String hql = "select distinct user.name from TUser as user";
//        String hql = "from TUser user order by user.name, user.age desc";
//        String hql = "select count(user), user.age from TUser user group by user.age having count(user)<10";        
        
        String hql = "select user.name, user.age from TUser user";
        
        Query query = session.createQuery( hql );
        List list = query.list();
        Iterator it = list.iterator();
        while ( it.hasNext() )
        {
            Object[] results = (Object[]) it.next();
            System.out.println( results[0] );
            System.out.println( results[1] );
        }
        
        Transaction transaction = session.beginTransaction();
        String updateHQL = "update TUser set age=18 where id=1";
        Query query2 = session.createQuery( updateHQL );
        query2.executeUpdate();
        transaction.commit();
        
        // No result will be show.
        String hqlParam = "from TUser user where user.name=? and user.age>?";
        Query query3 = session.createQuery( hqlParam );
        query3.setString( 0, "Zhang Jiawei" );
        query3.setInteger( 1, 10 );
        
        String hqlParam2 = "from TUser user where user.name=:name and user.age>:age";
        Query query4 = session.createQuery( hqlParam2 );
        query4.setString( "name", "Zhang Jiawei" );
        query4.setInteger( "age", 10 );
        
        String hqlParam3 = "from TUser user where user.name=:name and user.age>:age";
        Query query5 = session.createQuery( hqlParam3 );
        TUser user = new TUser();
        user.setName( "Zhang Jiawei" );
        user.setAge( new Integer(10) );
        query5.setProperties( user );
    }
    
    public void testQueryInMappingFile()
    {
        // See the HQL clause in TUser.hbm.xml
        Query query = session.getNamedQuery( "queryByName" );
        query.setString( 0, "Zhang Jiawei" );
        
        Iterator it = query.iterate();
        while ( it.hasNext() )
        {
            TUser user = (TUser) it.next();
            System.out.println( "Result from method testQueryInMappingFile(): " +
                                user.getName() );
        }
    }
    
    public void testOptimisticLock()
    {
        TUser user = (TUser) session.createQuery( "from TUser where name='Zhang Jiawei'" ).list().get( 0 );
        
        System.out.println( "Start to test optimistic-lock......" );
        System.out.println( "Before updating the TUser, value of column version: " + user.getVersion() );
        
        Transaction transaction = session.beginTransaction();
        user.setAge( new Integer( 25 ) );
        transaction.commit();
        
        System.out.println( "After updating the TUser, value of column version: " + user.getVersion() );
    }
    
    public void testLazyLoadingForCollection()
    {
        // See TUser.hbm.xml and TAddress.hbm.xml
        TUser user = (TUser) session.load( TUser.class, new Integer( 1 ) );
        
        // Lazy loading... no data in addSet Collection
        Collection addSet = user.getAddresses();
        
        // If you want to load the data immediately
        // Hibernate.initialize( user.getAddresses() );
        
        // Now the method iterator begin to read data from database actually.
        // Hibernate rewrite the implementation of JDK Collection class such as Set, Map, List
        Iterator it = addSet.iterator();
        while ( it.hasNext() )
        {
            TAddress address = (TAddress) it.next();
            System.out.println( address.getAddress() );
        }
        
        System.out.println( "The second time to load data with a different session instance..." );
        
        // Cache the Entity index and object for TAddress
        // See TUser.hbm.xml and TAddress.hbm.xml
        // Please attention the log output
        
        Session session2 = sessionFactory.openSession();
        user = (TUser) session2.load( TUser.class, new Integer( 1 ) );

        addSet = user.getAddresses();
        
        it = addSet.iterator();
        while ( it.hasNext() )
        {
            TAddress address = (TAddress) it.next();
            System.out.println( address.getAddress() );
        }
    }
    
    
    
}

package com.loadtrend.daos;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.loadtrend.beans.User;

public class UserHibernateDAO extends HibernateDaoSupport implements IUserDAO
{
    public void insertUser( User user )
    {
        getHibernateTemplate().saveOrUpdate( user );
    }
    
    public List getUsers()
    {
//        Some thing wrong here.
//        List list = new ArrayList();
//        Iterator it = getHibernateTemplate().iterate( "from User" );
//        while ( it.hasNext() )
//        {
//            list.add( (User) it.next() );
//        }
//        return list;
        return getHibernateTemplate().find( "from User" );
    }
}
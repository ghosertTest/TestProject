package com.loadtrend.daos;

import java.util.List;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import com.loadtrend.beans.User;

public class UserDAOProxy implements IUserDAO
{
    private IUserDAO userDAO = null;
    
    public UserDAOProxy ( IUserDAO userDAO )
    {
        this.userDAO = userDAO;
    }
    
    public void insertUser( User user )
    {
        UserTransaction tx = null;
        try
        {
            tx = (UserTransaction) ( new InitialContext().lookup( "java/tx" ) );
            
            userDAO.insertUser( user );
            
            tx.commit();
        }
        catch ( Exception ex )
        {
            if ( null != tx )
            {
                try
                {
                    tx.rollback();
                }
                catch ( Exception e )
                {
                    System.out.println( e.getMessage() );
                }
            }
        }
    }

    public List getUsers()
    {
        return userDAO.getUsers();
    }
}

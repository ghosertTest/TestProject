/*
 * Created on 2005-2-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UserDAO extends BaseDAO
{
    private String CLASS_NAME = "UserDAO";
    
    private static UserDAO userDAO = null;
    
    /**
     * Constructor for BaseDAO.
     *
     * @param  dataSourceName     Data Source name.
     * @param  environment environment used to create the initial context.
     *         Null indicates an empty environment.
     * @throws Exception
     */
    private UserDAO( String dataSourceName, Hashtable environment  ) throws Exception
    {
        super( dataSourceName, environment );
    }
    
    /**
     * Get the instance of UserDAO
     * 
     * @param  dataSourceName     Data Source name.
     * @param  environment environment used to create the initial context.
     *         Null indicates an empty environment.
     * @return instance of UserDAO
     * @throws Exception
     */
    public static UserDAO getInstance( String dataSourceName, Hashtable environment ) throws Exception
    {
        if ( userDAO == null )
        {
            userDAO = new UserDAO( dataSourceName, environment );
        }
        return userDAO;
    }
    
    public ArrayList getUserData( String userid ) throws Exception
    {
        UserData data = null;
        ArrayList list = new ArrayList();
        Connection connection = null; 
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        String user_id = null;
        String username = null;
        String password = null;
        String name = null;
        String email = null;
        String mobile = null;
        String registerTime = null;
        String ip = null;
        
        // this sql statement should be load from properties file
        String sqlQuery = "SELECT * FROM users WHERE user_id > ?";
        
        try
        {
            connection = super.getConnection();
            preparedStatement = connection.prepareStatement( sqlQuery );
            preparedStatement.setString( 1, userid );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() )
            {
                user_id = resultSet.getString( TestDAOConstants.USERS_FIELD_USER_ID );
                username = resultSet.getString( TestDAOConstants.USERS_FIELD_USERNAME );
                password = resultSet.getString( TestDAOConstants.USERS_FIELD_PASSWORD );
                name = resultSet.getString( TestDAOConstants.USERS_FIELD_NAME );
                email = resultSet.getString( TestDAOConstants.USERS_FIELD_EMAIL );
                mobile = resultSet.getString( TestDAOConstants.USERS_FIELD_MOBILE );
                registerTime = resultSet.getString( TestDAOConstants.USERS_FIELD_REGISTERTIME );
                ip = resultSet.getString( TestDAOConstants.USERS_FIELD_IP );
                
                data = new UserData( user_id, username, password, name, email, mobile, registerTime, ip );
                list.add( data );          
            }
        }
        catch ( Exception exception)
        {
            throw exception;
        }
        finally
        {
            closeResultSet( resultSet );
            closePreparedStatement( preparedStatement );
            closeConnection( connection );
        }
        
        return list;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}

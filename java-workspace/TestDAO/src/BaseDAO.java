/*****************************************************************************
*
*   Revision History
*   ----------------
*
*   Name          :   BaseDAO.java
*
*   Author        :   Zhang Jiawei
*
*   Date          :   Feb 2005
*
*   Description   :   This class provides all common methods for database
*                     operations.
*
*
*****************************************************************************/

//Package Name
package src ;

//Java imports
import java.util.Hashtable ;
import java.sql.PreparedStatement ;
import java.sql.Connection ;
import java.sql.SQLException ;
import java.sql.ResultSet ;
import javax.sql.DataSource ;
import javax.naming.InitialContext ;
import javax.naming.NamingException ;



/**
* This class provides all the common methods for database
* operations
*
* @author Infosys
*
*/
public class BaseDAO 
{

   /**
    * Stores name of this class. This is used for logging.
    */
   private static final String CLASS_NAME = "BaseDAO";

   /**
    * The static Hashtable object that will act as a cache of 
    * DataSource objects.
    */
   private static Hashtable dataSourceCache = new Hashtable() ;

   /**
    * The data source assigned to an instance of this class
    */
   protected DataSource dataSource = null ;




   /**
    * Constructor for BaseDAO. Creates the datasource object.
    * Gets the dataSource object and assigns it to
    * the instance variable.
    *
    * @param  dataSourceName     Data Source name.
    * @param  environment environment used to create the initial context.
    *         Null indicates an empty environment.
    * @throws Exception
    */
   protected BaseDAO( String dataSourceName, Hashtable environment ) throws Exception
   {
       InitialContext initialContext   = null;
       DataSource dataSourceCached     = null;

       try
       {

           // Try getting DataSource object from the cache
           dataSourceCached = 
               (DataSource) dataSourceCache.get( dataSourceName );
           if ( dataSourceCached != null )
           {

               // The data source object exists in the cache
               dataSource = dataSourceCached;
           }
           else
           {
               // The data source object does not exist in the cache
               initialContext = new InitialContext( environment );
               dataSource = 
                   (DataSource) initialContext.lookup( dataSourceName );

               // Add the object to the cache for use later
               dataSourceCache.put( dataSourceName, dataSource );
           }
       }
       catch ( NamingException namingException )
       {
           System.out.println( "NamingException from: " + CLASS_NAME );
           throw namingException;
       }
       catch ( Exception exception )
       {
           System.out.println( "Exception from: " + CLASS_NAME );
           throw exception;
       }
       finally
       {
           // Release reference
           dataSourceCached = null;
           initialContext = null;

       }

   }



   /**
    * Closes the database connection provided as a parameter.
    *
    * @param  connection         The connection that must be closed.
    * @throws TIServiceException
    */
   protected void closeConnection( Connection connection ) 
   {
       try
       {
           if ( connection != null && !connection.isClosed() )
           {
               connection.close();
           }
       }
       catch ( SQLException sqlException )
       {
           sqlException.printStackTrace();
       }
       catch ( Exception exception )
       {
           exception.printStackTrace();
       }
       finally
       {
           connection = null;
       }

   }



   /**
    * Closes the ResultSet object provided as a parameter.
    *
    * @param resultSet the <code>ResultSet</code> object which holds the data
    *                  fetched from database for the query criteria
    * @throws TIServiceException
    */
   protected void closeResultSet( ResultSet resultSet ) 
   {
       try
       {

           if ( resultSet != null )
           {
               resultSet.close();
           }
       }
       catch ( SQLException sqlException )
       {
           sqlException.printStackTrace();
       }
       catch ( Exception exception )
       {
           exception.printStackTrace();
       }
       finally
       {
           resultSet = null;

       }

   }



   /**
    * Closes the PreparedStatement object created during database query.
    *
    * @param preparedStatement    <code>PreparedStatement</code> object
    * @throws TIServiceException
    */
   protected void closePreparedStatement( 
                       PreparedStatement preparedStatement ) 
   {
       try
       {
           if ( preparedStatement != null )
           {
               preparedStatement.close();
           }
       }
       catch ( SQLException sqlException )
       {
           sqlException.printStackTrace();
       }
       catch ( Exception exception )
       {
           exception.printStackTrace();
       }
       finally
       {
           preparedStatement = null;

       }

   }



   /**
    * Creates a connection to the database. It creates a connection using 
    * the dataSource and returns it to the caller.
    *
    * @return Connection  Returns a <code>Connection</code> object used for 
    *                     all further database operations.
    * @throws TIServiceException
    */
   protected Connection getConnection() throws Exception
   {
       Connection dbConnection = null ;

       try
       {
           dbConnection = dataSource.getConnection() ;
       }
       catch ( SQLException sqlException )
       {
           System.out.println( "SQLException from: " + CLASS_NAME );
           throw sqlException;
       }
       catch ( Exception exception )
       {
           System.out.println( "Exception from: " + CLASS_NAME );
           throw exception;
       }

       return dbConnection ;
   }



}

/*
 * Created on 2005-2-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package src;

//java imports
import java.util.Properties ;
import java.util.Hashtable ;
import java.util.ArrayList ;
import java.util.Enumeration ;
import java.util.StringTokenizer ;
import java.util.PropertyResourceBundle ;
import java.util.MissingResourceException ;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestProperties
{
    // Defined constant value
    public static final String FILE_DEFINED_SUBFILES = "mockallocation";
	public static final String KEY_DEFINED_SUBFILES = "application_properties_files";
	// Stores the value of all the application specific properties
    private static Hashtable propertiesTable = null ;

    // This is required for the singleton implementation
    private static TestProperties reader = null ;
   /**
    * This method is used to get an instance of PropertiesGenerator class
    * This class uses singleton implementation
    *
    *
    * @throws   MissingResourceException    If thrown by
    *                                       <code>PropertiesReader</code>
    * @return   PropertiesGenerator object.
    */
    public static TestProperties getInstance() throws MissingResourceException
    {
        if( reader == null )
        {
            reader = new TestProperties() ;
        }
        return reader ;
    }
    
    /**
     * This is the private constructor of the class. 
     *
     * @throws   MissingResourceException    If thrown by
     *                                       <code>PropertiesReader</code>
     *
     */
    private TestProperties() throws MissingResourceException 
    {
        String fileList = null ;
        String fileName = null ;
        ArrayList listOfFiles = null ;
        Properties properties = null ;

        propertiesTable = new Hashtable() ;
        Properties nameofPropertiesFiles = this.readProperties( FILE_DEFINED_SUBFILES ) ;
        fileList = nameofPropertiesFiles.getProperty( KEY_DEFINED_SUBFILES ) ;
        listOfFiles = readFurtherFiles( fileList ) ;

        for ( int i=0; i < listOfFiles.size(); i++ )
        {
            fileName = ( String ) listOfFiles.get( i ) ;
            properties = this.readProperties( fileName ) ;
            propertiesTable.put( fileName, properties ) ;
        }
    }

    public static void main( String[] args )
    {
        TestProperties tp = TestProperties.getInstance();
        String sessionTimeout = tp.getProperty( "mockallocation_application", "SESSION_TIMEOUT" );
        System.out.println( sessionTimeout );
    }

   /**
    * This method is used to read the properties from properties file specific
    * to the application.
    *
    *
    * @param fileName      The file name
    *
    * @return   Properties object, with key-value pairs read from properties
    *           file.
    *
    * @throws   MissingResourceException    If properties file is not found
    * 
    */
    private Properties readProperties( String fileName )
                                            throws MissingResourceException
    {
        PropertyResourceBundle esProperties = null;
        Properties properties           = null;
        Enumeration keys                = null;

        String keyTemp                  = null;

        try
        {
            // Create object of PropertyResourceBundle for the filename

            esProperties =
            ( PropertyResourceBundle )
                 PropertyResourceBundle.getBundle( fileName );
        }
        catch( MissingResourceException missingResource )
        {
            throw missingResource;
        }

        properties = new Properties();

        // Call getKeys method of PropertyResourceBundle to get all keys
        keys = esProperties.getKeys();

        // Using the key name, get corresponding value and populate
        // properties object
        while( keys.hasMoreElements() )
        {
            keyTemp = ( String )keys.nextElement();
            properties.setProperty( keyTemp,
                                    esProperties.getString( keyTemp ) );
            keyTemp = null;
        }

        return properties;
    }

	private final static ArrayList readFurtherFiles( String fileList ) 
    {
        String fileName = null;
        ArrayList listOfFiles = new ArrayList();
        StringTokenizer st = new StringTokenizer( fileList, "," );
        while  ( st.hasMoreTokens() )
        {
              fileName=st.nextToken( "," ) ;
              listOfFiles.add( fileName );
        }
        return listOfFiles;
    }
	
   /**
    * This method is used to get the properties object.
    *
    * @param file     The file from which the properties are to read
    *
    * @return   Properties object which is a private attribute of this class.
    * 
    */
    public Properties getProperties( String file )
    {
        return ( Properties ) propertiesTable.get( file );
    }



   /**
    * This method is used to get the properties object for a given file and key.
    *
    * @param file     The file from which the property is to be read.
    * @param key      The key that is used to get the property
    *
    * @return   Properties object which is a private attribute of this class.
    */
    public String getProperty( String file, String key )
    {
        Properties properties = getProperties( file );

        return properties.getProperty( key );
    }

}
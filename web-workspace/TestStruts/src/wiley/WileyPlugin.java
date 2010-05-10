package wiley;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.action.ActionServlet;

public class WileyPlugin implements PlugIn
{
    public static final String PROPERTIES = "PROPERTIES";

    //  Constructor is mandatory.
    public WileyPlugin()
    {
    }
    
    public void init(ActionServlet servlet, ModuleConfig config)
            throws javax.servlet.ServletException
    {
        System.err.println( "<-------The Plugin is starting------->" );
        Properties properties = new Properties();
        try
        {
            //         Build a file object referening the properties file
            //         to be loaded
            File file = new File( "ApplicationResources.properties" );
            //         Create an input stream
            FileInputStream fis = new FileInputStream( file );
            //         load the properties
            properties.load( fis );
            //         Get a reference to the ServletContext
            ServletContext context = servlet.getServletContext();
            //         Add the loaded properties to the ServletContext
            //         for retrieval throughout the rest of the Application
            context.setAttribute( PROPERTIES, properties );
        }
        catch ( FileNotFoundException fnfe )
        {
            System.out.println( "Plugin error: file not found." );
            // throw new ServletException( fnfe.getMessage() );
        }
        catch ( IOException ioe )
        {
            throw new ServletException( ioe.getMessage() );
        }
    }

    public void destroy()
    {
        //         We don't have anything to clean up, so
        //         just log the fact that the Plugin is shutting down
        System.err.println( "<-------The Plugin is stopping------->" );
    }
}
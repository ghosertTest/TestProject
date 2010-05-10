/*
 * Created on 2005-2-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package src;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.naming.Context;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestDAO {
    
//    // WebLogic Configuration
//    public static final String dataSourceName = "MockDS";
//    public static final String INITIAL_CONTEXT_FACTORY = "weblogic.jndi.WLInitialContextFactory";
//    public static final String PROVIDER_NAME = Context.PROVIDER_URL;
//    public static final String PROVIDER_URL = "t3://localhost:7005";
    
    // Tomcat Configuration
    public static final String dataSourceName = "java:comp/env/jdbc/Assistant";
    public static final String INITIAL_CONTEXT_FACTORY = "org.apache.naming.java.javaURLContextFactory";
    public static final String PROVIDER_NAME = Context.URL_PKG_PREFIXES;
    public static final String PROVIDER_URL = "org.apache.naming";

    public static void main(String[] args)
    {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(PROVIDER_NAME, PROVIDER_URL);
        String userid = "1";
        try
        {
            // please set env as null VALUE, if this method is used inside the webapplication
            // in servlet or JSP page
            UserDAO userDAO = UserDAO.getInstance( dataSourceName, env );
            ArrayList list = userDAO.getUserData(userid);
            Iterator iterator = list.iterator();
            while ( iterator.hasNext() )
            {
                UserData data = (UserData) iterator.next();
                System.out.println( data.getName() );
            }
            
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}

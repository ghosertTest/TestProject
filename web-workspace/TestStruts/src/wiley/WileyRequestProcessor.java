package wiley;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.util.Enumeration;
import org.apache.struts.action.RequestProcessor;

public class WileyRequestProcessor extends RequestProcessor
{
    // Constructor is mandatory.
    public WileyRequestProcessor()
    {
    }
    
    public boolean processPreprocess( HttpServletRequest request,
            HttpServletResponse response )
    {
        log( "----------processPreprocess Logging----------" );
        log( "Request URI = " + request.getRequestURI() );
        log( "Context Path = " + request.getContextPath() );
        Cookie cookies[] = request.getCookies();
        if ( cookies != null )
        {
            for ( int i = 0; i < cookies.length; i++ )
            {
                log( "Cookie = " + cookies[i].getName() + " = "
                        + cookies[i].getValue() );
            }
        }
        Enumeration headerNames = request.getHeaderNames();
        while ( headerNames.hasMoreElements() )
        {
            String headerName = (String) headerNames.nextElement();
            Enumeration headerValues = request.getHeaders( headerName );
            while ( headerValues.hasMoreElements() )
            {
                String headerValue = (String) headerValues.nextElement();
                log( "Header = " + headerName + " = " + headerValue );
            }
        }
        log( "Locale = " + request.getLocale() );
        log( "Method = " + request.getMethod() );
        log( "Path Info = " + request.getPathInfo() );
        log( "Protocol = " + request.getProtocol() );
        log( "Remote Address = " + request.getRemoteAddr() );
        log( "Remote Host = " + request.getRemoteHost() );
        log( "Remote User = " + request.getRemoteUser() );
        log( "Requested Session Id = " + request.getRequestedSessionId() );
        log( "Scheme = " + request.getScheme() );
        log( "Server Name = " + request.getServerName() );
        log( "Server Port = " + request.getServerPort() );
        log( "Servlet Path = " + request.getServletPath() );
        log( "Secure = " + request.isSecure() );
        log( "-------------------------------------------------" );
        return true;
    }
}
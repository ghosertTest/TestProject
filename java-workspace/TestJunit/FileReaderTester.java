import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;
/*
 * Created on 2005-5-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileReaderTester extends TestCase
{
    FileReader reader = null;
    
    public static void main( String[] args )
    {
        junit.textui.TestRunner.run( FileReaderTester.class );
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        reader = new FileReader( "data.txt" );
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        reader.close();
    }
    
    public void testRead() throws IOException
    {
        int c = 0;
        for ( int i = 0; i < 5; i++ )
        {
            c = reader.read();
        }
        assertEquals( 'f', (char) c );
    }
    
    public void testAtEnd() throws IOException
    {
        int c = 0;
        while ( true )
        {
            c = reader.read();
            if ( c == -1 ) break;
        }
        assertEquals( "read at end", -1, c );
    }
    
}

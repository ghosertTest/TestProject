package com.test;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/*
 * Created on 2005-4-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Jiawei_zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Test
{
	public static void main( String[] args )
	{
		String string = null;
		try
		{
			string = URLEncoder.encode( "ÄãºÃ°¡", "UTF-8" );
            InputStream inputStream = new Test().getClass().getClassLoader().getResourceAsStream( "ss/tt.java" );
            try
            {
                System.out.println( inputStream.read() );
            }
            catch ( IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            // Sort method.
            int[] values = new int[] { 2, 3, 6, 1, 12, 8, 9, 5 };
            for ( int i = 0; i < values.length; i++ )
            {
                for ( int j = 0; j < values.length - i - 1; j++ )
                {
                    if ( values[j] > values[j + 1 ] )
                    {
                        int temp = values[j];
                        values[j] = values[j+1];
                        values[j+1] = temp;
                    }
                }
            }
            
            for ( int i = 0; i < values.length; i++ )
            {
                System.out.println( values[i] );
            }
            
		}
		catch ( UnsupportedEncodingException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println( string );
	}
}
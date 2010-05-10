/*
 * Created on 2005-8-2
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

/**
 * @author Jiawei_zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch7_Images
{
    public static void main( String[] args )
    {
        int numRows = 6, numCols = 11, pix = 20;
        PaletteData pd = new PaletteData( new RGB[] {
                new RGB( 0x00, 0x00, 0x00 ), new RGB( 0x80, 0x80, 0x80 ),
                new RGB( 0xFF, 0xFF, 0xFF ) } );
        ImageData[] flagArray = new ImageData[3];
        for ( int frame = 0; frame < flagArray.length; frame++ )
        {
            flagArray[frame] = new ImageData( pix * numCols, pix * numRows, 4,
                    pd );
            flagArray[frame].delayTime = 10;
            for ( int x = 0; x < pix * numCols; x++ )
            {
                for ( int y = 0; y < pix * numRows; y++ )
                {
                    int value = ( ( ( x / pix ) % 3 )
                            + ( 3 - ( ( y / pix ) % 3 ) ) + frame ) % 3;
                    flagArray[frame].setPixel( x, y, value );
                }
            }
        }
        ImageLoader gifloader = new ImageLoader();
        ByteArrayOutputStream flagByte[] = new ByteArrayOutputStream[3];
        byte[][] gifarray = new byte[3][];
        gifloader.data = flagArray;
        for ( int i = 0; i < 3; i++ )
        {
            flagByte[i] = new ByteArrayOutputStream();
            flagArray[0] = flagArray[i];
            gifloader.save( flagByte[i], SWT.IMAGE_GIF );
            gifarray[i] = flagByte[i].toByteArray();
        }
        byte[] gif = new byte[4628];
        System.arraycopy( gifarray[0], 0, gif, 0, 61 );
        System.arraycopy( new byte[] { 33, (byte) 255, 11 }, 0, gif, 61, 3 );
        System.arraycopy( new String( "NETSCAPE2.0" ).getBytes(), 0, gif, 64,
                11 );
        System.arraycopy( new byte[] { 3, 1, -24, 3, 0, 33, -7, 4, -24 }, 0,
                gif, 75, 9 );
        System.arraycopy( gifarray[0], 65, gif, 84, 1512 );
        for ( int i = 1; i < 3; i++ )
        {
            System.arraycopy( gifarray[i], 61, gif, 1516 * i + 80, 3 );
            gif[1516 * i + 83] = (byte) -24;
            System.arraycopy( gifarray[i], 65, gif, 1516 * i + 84, 1512 );
        }
        try
        {
            DataOutputStream in = new DataOutputStream(
                    new BufferedOutputStream( new FileOutputStream( new File(
                            "FlagGIF.gif" ) ) ) );
            in.write( gif, 0, gif.length );
        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }
}

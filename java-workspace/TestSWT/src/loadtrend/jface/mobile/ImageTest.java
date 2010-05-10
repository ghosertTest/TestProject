/*
 * Created on 2005-8-2
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Jiawei_zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ImageTest extends Composite
{
    public ImageTest( Composite parent )
    {
        super( parent, SWT.NONE );
        parent.setSize( 320, 190 );
        InputStream is = this.getClass().getResourceAsStream( "eclipse_lg.gif" );
        final ImageData eclipseData = new ImageData( is ).scaledTo( 87, 123 );
        this.addPaintListener( new PaintListener()
        {
            public void paintControl( PaintEvent pe )
            {
                GC gc = pe.gc;
                Image eclipse = new Image( pe.display, eclipseData );
                gc.drawImage( eclipse, 20, 20 );
                gc.drawText( "The image height is: " + eclipseData.height
                        + " pixels.", 120, 30 );
                gc.drawText( "The image width is: " + eclipseData.width
                        + " pixels.", 120, 70 );
                gc.drawText( "The image depth is: " + eclipseData.depth
                        + " bits per pixel.", 120, 110 );
                eclipse.dispose();
            }
        } );
    }
}

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
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * @author Jiawei_zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch7_Composite extends Canvas
{
    public Ch7_Composite( Composite parent )
    {
        super( parent, SWT.BORDER );
        Ch7_Colors drawing = new Ch7_Colors(this);
        drawing.setBounds(20,20,200,100);
        Ch7_Fonts fontbox = new Ch7_Fonts( this );
        fontbox.setBounds( 0, 150, 500, 200 );
        Ch7_Images flagmaker = new Ch7_Images();
        addPaintListener( new PaintListener()
        {
            public void paintControl( PaintEvent pe )
            {
                Display disp = pe.display;
                GC gc = pe.gc;
                InputStream is = getClass().getResourceAsStream( "FlagGIF.gif" );
                Image flag = new Image( disp, is );
                gc.drawImage( flag, 255, 10 );
                flag.dispose();
            }
        } );
    }
}

/*
 * Created on 2005-8-1
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Jiawei_zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DrawExample
{
    public static void main( String[] args )
    {
        Display display = new Display();
        Shell shell = new Shell( display );
        shell.setText( "Drawing Example" );
        
        // new canvas before invoking shell.open()
        Canvas canvas = new Canvas( shell, SWT.NONE );
        canvas.setSize( 150, 150 );
        canvas.setLocation( 20, 20 );
        canvas.addPaintListener( new PaintListener()
        {
            public void paintControl( PaintEvent e )
            {
                GC gc = e.gc;
                gc.drawRectangle( 10, 10, 40, 45 );
                gc.drawOval( 65, 10, 30, 35 );
                gc.drawLine( 130, 10, 90, 80 );
                gc.drawPolygon( new int[] { 20, 70, 45, 90, 70, 70 } );
                gc.drawPolyline( new int[] { 10, 120, 70, 100, 100, 130, 130, 75 } );
                // you can invoke gc before shell.open() in event, and no need to worry 
                // about gc.dispose();
            }
        });
        shell.open();
        shell.setSize( 200, 220 );
//        GC gc = new GC( canvas );
//        gc.drawRectangle( 10, 10, 40, 45 );
//        gc.drawOval( 65, 10, 30, 35 );
//        gc.drawLine( 130, 10, 90, 80 );
//        gc.drawPolygon( new int[] { 20, 70, 45, 90, 70, 70 } );
//        gc.drawPolyline( new int[] { 10, 120, 70, 100, 100, 130, 130, 75 } );
//        // gc.dispose() after gc usage
//        gc.dispose();
        while ( !shell.isDisposed() )
        {
            if ( !display.readAndDispatch() )
                display.sleep();
        }
        display.dispose();
    }
}

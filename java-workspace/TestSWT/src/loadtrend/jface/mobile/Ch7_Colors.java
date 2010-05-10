/*
 * Created on 2005-8-1
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch7_Colors extends Canvas
{
    public Ch7_Colors( Composite parent )
    {
        super( parent, SWT.NONE );
        setBackground( this.getDisplay().getSystemColor( SWT.COLOR_DARK_GRAY ) );
        addPaintListener( drawListener );
        Text text = new Text( this, SWT.NONE );
        text.setText( "Color information will be show here after clicking button." );
        text.setLocation( 0, 0 );
        JFaceColors.setColors( text, getDisplay().getSystemColor( SWT.COLOR_WHITE ),
                               getDisplay().getSystemColor( SWT.COLOR_BLACK ) );
        text.pack();
        ColorSelector cs = new ColorSelector( this );
        Button bt = cs.getButton();
        bt.setText( "Select Color" );
        bt.setLocation( 0, 50 );
        bt.pack();
        RGB rgb = cs.getColorValue();
        
    }

    PaintListener drawListener = new PaintListener()
    {
        public void paintControl( PaintEvent pe )
        {
            Display disp = pe.display;
            Color light_gray = new Color( disp, 0xE0, 0xE0, 0xE0 );
            GC gc = pe.gc;
            gc.setBackground( light_gray );
            gc.fillPolygon( new int[] { 20, 20, 60, 50, 100, 20 } );
            gc.fillOval( 120, 30, 50, 50 );
            light_gray.dispose();
        }
    };
}

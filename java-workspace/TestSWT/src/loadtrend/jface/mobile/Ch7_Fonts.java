/*
 * Created on 2005-8-2
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Jiawei_zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch7_Fonts extends Canvas
{
    static Shell mainShell;

    static Composite comp;

    FontData fontdata;
    
    RGB rgb;
    
    public Ch7_Fonts( Composite parent )
    {
        super( parent, SWT.BORDER );
        parent.setSize( 600, 200 );
        addPaintListener( DrawListener );
        comp = this;
        mainShell = parent.getShell();
        Button fontChoice = new Button( this, SWT.CENTER );
        fontChoice.setBounds( 20, 20, 100, 20 );
        fontChoice.setText( "Choose font" );
        fontChoice.addMouseListener( new MouseAdapter()
        {
            public void mouseDown( MouseEvent me )
            {
                FontDialog fd = new FontDialog( mainShell );
                fontdata = fd.open();
                rgb = fd.getRGB();
                comp.redraw();
            }
        } );
    }

    PaintListener DrawListener = new PaintListener()
    {
        public void paintControl( PaintEvent pe )
        {
            Display disp = pe.display;
            GC gc = pe.gc;
            gc.setBackground( pe.display.getSystemColor( SWT.COLOR_DARK_GRAY ) );
            if ( rgb != null )
            {
                Color color = new Color( disp, rgb );
                gc.setForeground( color ); 
                color.dispose();
            }
            if ( fontdata != null )
            {
                Font GCFont = new Font( disp, fontdata );
                gc.setFont( GCFont );
                FontMetrics fm = gc.getFontMetrics();
                gc.drawText( "The average character width for this font is "
                        + fm.getAverageCharWidth() + " pixels.", 20, 60 );
                gc.drawText( "The ascent for this font is " + fm.getAscent()
                        + " pixels.", 20, 100, true );
                gc.drawText( "The &descent for this font is " + fm.getDescent()
                        + " pixels.", 20, 140, SWT.DRAW_MNEMONIC
                        | SWT.DRAW_TRANSPARENT );
                GCFont.dispose();
            }
        }
    };
}

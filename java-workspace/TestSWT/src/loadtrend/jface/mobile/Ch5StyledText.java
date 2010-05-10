/*
 * Created on 2005-7-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Jiawei_zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch5StyledText extends Composite
{
    private boolean doBold = false;
    private StyledText styledText = null;
    
    public Ch5StyledText( Composite parent )
    {
        super( parent, SWT.NONE );
        
        this.setLayout( new FillLayout() );
        
        styledText = new StyledText( this, SWT.V_SCROLL | SWT.WRAP);
        styledText.setText( "1. Press Ctrl+Q 2. Enters values, select them and press F1 " +
        		            "3. Enter values once again after press F1" );
        
        styledText.setKeyBinding( 'Q' | SWT.CONTROL, ST.PASTE );
        styledText.invokeAction( ST.SELECT_LINE_END );
        
        Color foreground = new Color( this.getDisplay(), 231, 33, 21 );
        Color background = new Color( this.getDisplay(), 255, 255, 0 );
        StyleRange styleRange = new StyleRange( 0, 30, foreground, background, SWT.BOLD );
        styledText.setStyleRange( styleRange );
        
        styledText.addKeyListener( new KeyAdapter()
        {
            public void keyPressed( KeyEvent e )
            {
                if ( e.keyCode == SWT.F1 )
                {
                    toggleBold();
                }
            }
        } );
        
        styledText.addExtendedModifyListener( new ExtendedModifyListener()
        {
            public void modifyText( ExtendedModifyEvent event )
            {
                if ( doBold )
                {
                    StyleRange style = new StyleRange( event.start, event.length, null, null, SWT.BOLD );
                    styledText.setStyleRange( style ); 
                }
            }
        });
        
        StyleRange[] styles = styledText.getStyleRanges();
        for(int i = 0; i < styles.length; i++)
        {
            System.out.println(styles[i].start + " " + styles[i].length);
        }
    }
    
    private void toggleBold()
    {
        doBold = !doBold;
        if ( styledText.getSelectionCount() > 0 )
        {
            Point selectionRange = styledText.getSelectionRange();
            StyleRange style = new StyleRange( selectionRange.x,
                    selectionRange.y, null, null, doBold ? SWT.BOLD
                            : SWT.NORMAL );
            styledText.setStyleRange( style );
        }
    }
}

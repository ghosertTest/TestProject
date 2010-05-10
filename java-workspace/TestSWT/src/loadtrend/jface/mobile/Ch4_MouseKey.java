/*
 * Created on 2005-7-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch4_MouseKey extends Composite
{
    public Ch4_MouseKey( Composite parent )
    {
        super( parent, SWT.NONE );
        
        Button bt1 = new Button( this, SWT.CENTER );
        bt1.setLocation( 0, 0 );
        bt1.setText( "Mouse Listener" );
        bt1.addMouseListener( new MouseListener()
        {
            public void mouseDown( MouseEvent me )
            {
                System.out.println( "bt1 " + me.button + " Mouse Down now!" );
            }
            
            public void mouseUp( MouseEvent me )
            {
                System.out.println( "bt1 " + me.button + " Mouse Up now!" );
            }
            
            public void mouseDoubleClick( MouseEvent me )
            {
                System.out.println( "bt1 " + me.button + " Mouse Double Click now!" );
            }
        }
        );
        bt1.pack();
        
        
        Button bt2 = new Button( this, SWT.CENTER );
        bt2.setLocation( 100, 0 );
        bt2.setText( "Mouse Adapter" );
        bt2.addMouseListener( new MouseAdapter()
        {
            public void mouseDoubleClick( MouseEvent me )
            {
                System.out.println( "bt2 " + me.button + " Mouse Double Click now!" );
            }
        } 
        );
        bt2.pack();
        
        
        Button bt3 = new Button( this, SWT.CENTER );
        bt3.setLocation( 200, 0 );
        bt3.setText( "Key Adapter" );
        bt3.addTraverseListener( new TraverseListener()
        {
            public void keyTraversed( TraverseEvent e )
            {
                e.doit = false;
                System.out.println( e.detail + " tab are not allowed." );
            }
            
        }
        );
        bt3.addKeyListener( new KeyAdapter()
        {
            public void keyPressed( KeyEvent e )
            {
                String string = "";
                if ( ( e.stateMask & SWT.ALT ) != 0 )
                    string += "ALT-";
                if ( ( e.stateMask & SWT.CTRL ) != 0 )
                    string += "CTRL-";
                if ( ( e.stateMask & SWT.COMMAND ) != 0 )
                    string += "COMMAND-";
                if ( ( e.stateMask & SWT.SHIFT ) != 0 )
                    string += "SHIFT-";
                switch ( e.keyCode )
                {
                case SWT.BS:
                    string += "BACKSPACE";
                    break;
                case SWT.CR:
                    string += "CARRIAGE RETURN";
                    break;
                case SWT.DEL:
                    string += "DELETE";
                    break;
                case SWT.ESC:
                    string += "ESCAPE";
                    break;
                case SWT.LF:
                    string += "LINE FEED";
                    break;
                case SWT.TAB:
                    string += "TAB";
                    break;
                default:
                    string += e.character;
                    break;
                }
                System.out.println( string );
            }
        } );
        bt3.pack();
        
        Text text = new Text( this, SWT.LEFT | SWT.MULTI );
        text.setBounds( 100, 40, 100, 20 );
        text.addVerifyListener( new VerifyListener()
        {
            public void verifyText( VerifyEvent e )
            {
                System.out.println( "Start:" + e.start + " Content:" + e.text + " End:" + e.end + " " );
                if ( e.text.equals( "sss" ) )
                {
                    e.doit = false;
                }
            }
        }
        );
        pack();
        
    }
}

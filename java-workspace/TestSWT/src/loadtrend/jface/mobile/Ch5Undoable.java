/*
 * Created on 2005-7-29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Jiawei_zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch5Undoable extends Composite
{
    private static final int MAX_STACK_SIZE = 25;
    private List undoStack;
    private List redoStack;
    private StyledText styledText;

    public Ch5Undoable( Composite parent )
    {
        super( parent, SWT.NONE );
        undoStack = new LinkedList();
        redoStack = new LinkedList();
        buildControls();
    }

    private void buildControls()
    {
        this.setLayout( new FillLayout() );
        styledText = new StyledText( this, SWT.MULTI | SWT.V_SCROLL );
        styledText.setText( "Enter values, press F1 to undo, press F2 to redo" );
        styledText.addExtendedModifyListener( new ExtendedModifyListener()
        {
            public void modifyText( ExtendedModifyEvent event )
            {
                String currText = styledText.getText();
                String newText = currText.substring( event.start, event.start
                        + event.length );
                if ( newText != null && newText.length() > 0 )
                {
                    if ( undoStack.size() == MAX_STACK_SIZE )
                    {
                        undoStack.remove( undoStack.size() - 1 );
                    }
                    undoStack.add( 0, newText );
                }
            }
        } );
        styledText.addKeyListener( new KeyAdapter()
        {
            public void keyPressed( KeyEvent e )
            {
                switch ( e.keyCode )
                {
                case SWT.F1:
                    undo();
                    break;
                case SWT.F2:
                    redo();
                    break;
                default:
                //ignore everything else
                }
            }
        } );
    }

    private void undo()
    {
        if ( undoStack.size() > 0 )
        {
            String lastEdit = (String) undoStack.remove( 0 );
            int editLength = lastEdit.length();
            String currText = styledText.getText();
            int startReplaceIndex = currText.length() - editLength;
            styledText.replaceTextRange( startReplaceIndex, editLength, "" );
            redoStack.add( 0, lastEdit );
        }
    }

    private void redo()
    {
        if ( redoStack.size() > 0 )
        {
            String text = (String) redoStack.remove( 0 );
            moveCursorToEnd();
            styledText.append( text );
            moveCursorToEnd();
        }
    }

    private void moveCursorToEnd()
    {
        styledText.setCaretOffset( styledText.getText().length() );
    }
}


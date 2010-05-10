/*
 * Created on 2005-7-31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch6RowLayoutComposite extends Composite
{
    public Ch6RowLayoutComposite( Composite parent )
    {
        super( parent, SWT.NONE );
        RowLayout layout = new RowLayout( SWT.HORIZONTAL );
        // layout.wrap = false;
        setLayout( layout );
        for ( int i = 0; i < 16; ++i )
        {
            Button button = new Button( this, SWT.NONE );
            button.setText( "Sample Text" );
            button.setLayoutData( new RowData( 200 + 5 * i, 20 + i ) );
        }
    }
}

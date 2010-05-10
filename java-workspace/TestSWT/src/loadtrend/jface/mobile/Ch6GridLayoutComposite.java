/*
 * Created on 2005-7-31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch6GridLayoutComposite extends Composite
{
    public Ch6GridLayoutComposite( Composite parent )
    {
        super( parent, SWT.NONE );
        GridLayout layout = new GridLayout( 4, false );
        setLayout( layout );
        for ( int i = 0; i < 16; ++i )
        {
            Button button = new Button( this, SWT.NONE );
            button.setText( "Cell " + i );
            button.setLayoutData( new GridData( GridData.FILL_BOTH ) );
        }
    }
}

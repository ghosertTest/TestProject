/*
 * Created on 2005-7-31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch6FillLayoutComposite extends Composite
{
    public Ch6FillLayoutComposite( Composite parent )
    {
        super( parent, SWT.NONE );
        FillLayout layout = new FillLayout( SWT.VERTICAL );
        setLayout( layout );
        for ( int i = 0; i < 8; ++i )
        {
            Button button = new Button( this, SWT.NONE );
            button.setText( "Sample Text" );
        }
    }
}

/*
 * Created on 2005-7-31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch6FormLayoutComposite extends Composite
{
    public Ch6FormLayoutComposite( Composite parent )
    {
        super( parent, SWT.NONE );
        FormLayout layout = new FormLayout();
        setLayout( layout );
        Text t = new Text( this, SWT.MULTI );
        FormData data = new FormData();
        data.top = new FormAttachment( 0, 0 );
        data.left = new FormAttachment( 0, 0 );
        data.right = new FormAttachment( 100 );
        data.bottom = new FormAttachment( 75 );
        t.setLayoutData( data );
        Button ok = new Button( this, SWT.NONE );
        ok.setText( "Ok" );
        Button cancel = new Button( this, SWT.NONE );
        cancel.setText( "Cancel" );
        data = new FormData();
        data.top = new FormAttachment( t );
        data.right = new FormAttachment( cancel );
        ok.setLayoutData( data );
        data = new FormData();
        data.top = new FormAttachment( t );
        data.right = new FormAttachment( 100 );
        cancel.setLayoutData( data );
    }
}

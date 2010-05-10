/*
 * Created on 2005-8-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UsernamePasswordDialog extends Dialog
{
    private static final int RESET_ID = IDialogConstants.NO_TO_ALL_ID + 1;
    private Text usernameField;
    private Text passwordField;

    public UsernamePasswordDialog( Shell parentShell )
    {
        super( parentShell );
    }

    protected Control createDialogArea( Composite parent )
    {
        Composite comp = (Composite) super.createDialogArea( parent );
        GridLayout layout = (GridLayout) comp.getLayout();
        layout.numColumns = 2;
        Label usernameLabel = new Label( comp, SWT.RIGHT );
        usernameLabel.setText( "Username: " );
        usernameField = new Text( comp, SWT.SINGLE );
        GridData data = new GridData( GridData.FILL_HORIZONTAL );
        usernameField.setLayoutData( data );
        Label passwordLabel = new Label( comp, SWT.RIGHT );
        passwordLabel.setText( "Password: " );
        passwordField = new Text( comp, SWT.SINGLE | SWT.PASSWORD );
        data = new GridData( GridData.FILL_HORIZONTAL );
        passwordField.setLayoutData( data );
        return comp;
    }

    protected void createButtonsForButtonBar( Composite parent )
    {
        super.createButtonsForButtonBar( parent );
        createButton( parent, RESET_ID, "Reset All", false );
    }

    protected void buttonPressed( int buttonId )
    {
        if ( buttonId == RESET_ID )
        {
            usernameField.setText( "" );
            passwordField.setText( "" );
        }
        else
        {
            super.buttonPressed( buttonId );
        }
    }
}

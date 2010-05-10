/*
 * Created on 2005-7-29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Jiawei_zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch5ComboComposite extends Composite
{
    public Ch5ComboComposite( Composite parent )
    {
        super( parent, SWT.NONE );
        buildControls();
        buildButton();
    }

    protected void buildControls()
    {
        setLayout( new RowLayout() );
        int[] comboStyles = { SWT.SIMPLE, SWT.DROP_DOWN, SWT.READ_ONLY };
        for ( int idxComboStyle = 0; idxComboStyle < comboStyles.length; ++idxComboStyle )
        {
            Combo combo = new Combo( this, comboStyles[idxComboStyle] );
            combo.add( "Option #1" );
            combo.add( "Option #2" );
            combo.add( "Option #3" );
        }
    }
    
    protected void buildButton()
    {
        Button bt = new Button( this, SWT.PUSH );
        bt.setText( "Launch" );
        bt.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                Ch5Contributions win = new Ch5Contributions();
                win.open();
            }
        });
    }
}

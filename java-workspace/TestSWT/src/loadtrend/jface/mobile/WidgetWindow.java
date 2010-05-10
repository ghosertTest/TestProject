/*
 * Created on 2005-7-22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WidgetWindow extends ApplicationWindow
{
    public WidgetWindow()
    {
        super( null );
    }
    
    protected Control createContents( Composite parent )
    {
        Shell shell = super.getShell();
        shell.setText( "Zhang Jiawei!!!" );
        
//        parent.setLocation( 200, 200 );
//        parent.setSize( 400, 400 );
        
//        Text text = new Text( parent, SWT.CENTER );
//        text.setText( shell.getBounds().toString() );
        
//        Label shadow_label = new Label( parent, SWT.SEPARATOR | SWT.SHADOW_IN );
//        shadow_label.setText( "SWT.SHADOW_IN" );
//        shadow_label.setBounds( 250, 250, 50, 15 );
        
        // Button btPush = new Button( parent, SWT.ARROW | SWT.RIGHT );
        // Button btPush = new Button( parent, SWT.PUSH | SWT.RIGHT );
        // Button btPush = new Button( parent, SWT.TOGGLE | SWT.RIGHT );
        // btPush.setText( "push" );
        // btPush.pack();
        
//        Button check = new Button( parent, SWT.CHECK );
//        check.setText( "check 1" );
//        check.setLocation( 10, 5 );
//        check.setSelection( true );
//        check.pack();
        
//        Button radio = new Button( parent, SWT.RADIO );
//        radio.setText( "radio 1" );
//        radio.setLocation( 10, 5 );
//        radio.setSelection( true );
//        radio.pack();
        
        // explain for the constructor: name, label, number of column, option names - values
        // shell, isGrouped
//        RadioGroupFieldEditor rgfe = new RadioGroupFieldEditor( "UserChoice",
//                "Choose an option:", 3, new String[][]{
//                                                       { "Choice 1", "ch1" },
//                                                       { "Choice 2", "ch2" },
//                                                       { "Choice 3", "ch3" }
//                                                      }, parent, true );

//        Ch3_Group mg = new Ch3_Group( parent );
        
//        Ch3_SashForm msf = new Ch3_SashForm( parent );
        
        TabFolder tf = new TabFolder( parent, SWT.NONE );
        TabItem ti = new TabItem( tf, SWT.NONE );
        ti.setText( "Chapter 3" );
        ti.setControl( new Ch3_Composite( tf ) );
        
        
        TabItem ti2 = new TabItem( tf, SWT.NONE );
        ti2.setText( "Chapter 4" );
        ti2.setControl( new Ch4_Composite( tf ) );
        
        TabItem ti3 = new TabItem( tf, SWT.NONE );
        ti3.setText( "Ch5Capitalizer" );
        ti3.setControl( new Ch5Capitalizer(tf) );
        
        TabItem ti4 = new TabItem( tf, SWT.NONE );
        ti4.setText( "Ch5StyledText" );
        ti4.setControl( new Ch5StyledText(tf) );

        TabItem ti5 = new TabItem( tf, SWT.NONE );
        ti5.setText( "Ch5Undoable" );
        ti5.setControl( new Ch5Undoable(tf) );
        
        TabItem ti6 = new TabItem( tf, SWT.NONE );
        ti6.setText( "Ch5ComboComposite" );
        ti6.setControl( new Ch5ComboComposite(tf) );
        
        TabItem ti7 = new TabItem( tf, SWT.NONE );
        ti7.setText( "Ch5Slider" );
        ti7.setControl( new Ch5Slider(tf) );
        
        TabItem ti8 = new TabItem( tf, SWT.NONE );
        ti8.setText( "Ch6FillLayoutComposite" );
        ti8.setControl( new Ch6FillLayoutComposite(tf) );
        
        TabItem ti9 = new TabItem( tf, SWT.NONE );
        ti9.setText( "Ch6RowLayoutComposite" );
        ti9.setControl( new Ch6RowLayoutComposite(tf) );
        
        TabItem ti10 = new TabItem( tf, SWT.NONE );
        ti10.setText( "Ch6GridLayoutComposite" );
        ti10.setControl( new Ch6GridLayoutComposite(tf) );
        
        TabItem ti11 = new TabItem( tf, SWT.NONE );
        ti11.setText( "Ch6FormLayoutComposite" );
        ti11.setControl( new Ch6FormLayoutComposite(tf) );
        
        TabItem ti12 = new TabItem( tf, SWT.NONE );
        ti12.setText( "ImageTest" );
        ti12.setControl( new ImageTest(tf) );
        
        TabItem ti13 = new TabItem( tf, SWT.NONE );
        ti13.setText( "Ch7_Composite" );
        ti13.setControl( new Ch7_Composite(tf) );
        
        TabItem ti14 = new TabItem( tf, SWT.NONE );
        ti14.setText( "Ch8TreeComposite" );
        ti14.setControl( new Ch8TreeComposite(tf) );
        
        TabItem ti15 = new TabItem( tf, SWT.NONE );
        ti15.setText( "Ch8ListComposite" );
        ti15.setControl( new Ch8ListComposite(tf) );
        
        TabItem ti16 = new TabItem( tf, SWT.NONE );
        ti16.setText( "Ch9TableComposite" );
        ti16.setControl( new Ch9TableComposite(tf) );
        
        TabItem ti17 = new TabItem( tf, SWT.NONE );
        ti17.setText( "Ch9TableEditorComposite" );
        ti17.setControl( new Ch9TableEditorComposite(tf) );
        
        TabItem ti18 = new TabItem( tf, SWT.NONE );
        ti18.setText( "Ch10AllDialogs" );
        ti18.setControl( new Ch10AllDialogs(tf) );
        
        TabItem ti19 = new TabItem( tf, SWT.NONE );
        ti19.setText( "Ch10CustomDialogComposite" );
        ti19.setControl( new Ch10CustomDialogComposite(tf) );
        
        TabItem ti20 = new TabItem( tf, SWT.NONE );
        ti20.setText( "Ch12FileBrowserComposite" );
        ti20.setControl( new Ch12FileBrowserComposite(tf) );
        
        TabItem ti21 = new TabItem( tf, SWT.NONE );
        ti21.setText( "Ch12LabelDecorator" );
        ti21.setControl( new Ch12LabelDecorator(tf) );
        
        TabItem ti22 = new TabItem( tf, SWT.NONE );
        ti22.setText( "Ch12WebBrowserComposite" );
        ti22.setControl( new Ch12WebBrowserComposite(tf) );
        
        TabItem ti23 = new TabItem( tf, SWT.NONE );
        ti23.setText( "FormToolKitComposite" );
        ti23.setControl( new FormToolKitComposite(tf) );
        
        shell.setText( "Widget Window" );
        
        return parent;
    }
    
    public static void main( String[] args )
    {
        WidgetWindow wid = new WidgetWindow();
        wid.setBlockOnOpen( true );
        wid.open();
        Display.getCurrent().dispose();
    }
}

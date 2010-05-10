/*
 * Created on 2005-7-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.swt.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MobileImageApplication
{
    public static void main( String[] args )
    {
        final Display display = new Display();
        // Shell shell = new Shell( display ); equals
        // Shell shell = new Shell( display, SWT.SHELL_TRIM ); equals
        // Shell shell = new Shell( display, SWT.TITLE|SWT.MIN|SWT.MAX|SWT.RESIZE|SWT.CLOSE );
        Shell shell = new Shell( display, SWT.TITLE|SWT.MIN|SWT.MAX|SWT.RESIZE|SWT.CLOSE );
        shell.setLayout(new FillLayout());
        FormToolkit kit = new FormToolkit(display);
        ScrolledForm scrolledForm = kit.createScrolledForm(shell);
        scrolledForm.getBody().setLayout(new FillLayout());
        FormText formText = kit.createFormText(scrolledForm.getBody(), false);
        formText.setText("<form><p><control href=\"imageComposite\" fill=\"true\"/><a href=\"\">aaaaa</a></p></form>", true, true);
        
        FormColors formColors = new FormColors(display);
        final Color blueColor = formColors.getColor(FormColors.TITLE);
        
        Composite composite = kit.createComposite(formText);
        RowLayout layout = new RowLayout();
        // layout.topMargin = 10;
        composite.setLayout(layout);
        
        MobileImage mobileImage = new MobileImage(composite, 128, 128);
        mobileImage.setLayoutData(new RowData(mobileImage.getMobileImageWidth(), mobileImage.getMobileImageHeight()));
        mobileImage.setBackground(blueColor);
        // mobileImage.paintImageAsyn("images/tiger2.gif", false);
        // mobileImage.paintImageAsyn("images/baby.jpg", false);
        // mobileImage.paintImageAsyn("images/123.gif", false, "向前进!", "点击下载此图片", null);
        mobileImage.paintImageAsyn("http://www.m2p.com.cn/MediaRes/1801/041229214721-R.gif",
                                   true,
                                   "两个小姑娘呀",
                                   "点击下载此图片",
                                   new MouseAdapter() {
							           public void mouseDown(MouseEvent e) {
							               System.out.println("I'm clicking.");
							       }
        });
        
        formText.setControl("imageComposite", composite);
        
        shell.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                blueColor.dispose();
            }
        });
        
        shell.open();
        
        while ( !shell.isDisposed() )
        {
            if ( !display.readAndDispatch() )
            {
                display.sleep();
            }
        }
        
        display.dispose();
    }
}

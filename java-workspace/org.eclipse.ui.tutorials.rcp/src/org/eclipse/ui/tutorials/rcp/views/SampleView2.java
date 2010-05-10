package org.eclipse.ui.tutorials.rcp.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.*;
import org.eclipse.swt.SWT;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class SampleView2 extends ViewPart
{
	private Text text = null;
	/*
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	/**
	 * The constructor.
	 */
	public SampleView2()
	{
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl( Composite parent )
	{
		text = new Text( parent, SWT.V_SCROLL );
		// text.pack();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()
	{
	}
	
	public void setText( Text text )
	{
		this.text = text;
	}
	
	public Text getText()
	{
		return this.text;
	}
	
}
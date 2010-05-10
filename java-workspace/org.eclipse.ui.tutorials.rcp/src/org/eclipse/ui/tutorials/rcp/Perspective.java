package org.eclipse.ui.tutorials.rcp;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.tutorials.rcp.views.SampleView;
import org.eclipse.ui.tutorials.rcp.views.SampleView2;

public class Perspective implements IPerspectiveFactory
{

	public void createInitialLayout( IPageLayout layout )
	{
	    layout.setEditorAreaVisible( true );
        
        // You can't add the same view id to addFastView and addView at the same time.
//        layout.addFastView( SampleView.class.getName(), 0.3f );
		layout.addView( SampleView.class.getName(), IPageLayout.LEFT, 0.3f, IPageLayout.ID_EDITOR_AREA );
		layout.addView( SampleView2.class.getName(), IPageLayout.BOTTOM, IPageLayout.DEFAULT_VIEW_RATIO, IPageLayout.ID_EDITOR_AREA );
        // See the corresponding code in ApplicationActionBarAdvisor.java
		layout.addPerspectiveShortcut( getClass().getName() );
		layout.addShowViewShortcut( SampleView.class.getName() );
		layout.addShowViewShortcut( SampleView2.class.getName() );
	}
}

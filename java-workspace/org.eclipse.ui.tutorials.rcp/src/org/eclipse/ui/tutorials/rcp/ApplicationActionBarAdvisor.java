package org.eclipse.ui.tutorials.rcp;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor
{
    private IWorkbenchAction quitAction = null;
    private IWorkbenchWindow window = null;
    
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer)
    {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window)
    {
    	this.window = window;
        quitAction = ActionFactory.QUIT.create( window );
        register( quitAction );
    }

    protected void fillMenuBar(IMenuManager menuBar)
    {
        IMenuManager fileMenu = new MenuManager( "&File" );
        fileMenu.add( new MyAction() );
        fileMenu.add( new Separator() );
        fileMenu.add( quitAction );
        menuBar.add( fileMenu );
        
        IMenuManager windowMenu = new MenuManager( "&Window" );
        
		MenuManager perspectiveMenu = new MenuManager( "Open Perspective" ); //$NON-NLS-1$ //$NON-NLS-2$
		IContributionItem perspectiveList = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create( window );
		perspectiveMenu.add( perspectiveList );
		windowMenu.add( perspectiveMenu );

		MenuManager viewMenu = new MenuManager( "Show View" ); //$NON-NLS-1$
		IContributionItem viewList = ContributionItemFactory.VIEWS_SHORTLIST.create( window );
		viewMenu.add( viewList );
		windowMenu.add( viewMenu );
		
		menuBar.add( windowMenu );
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar)
    {
        IToolBarManager toolBar = new ToolBarManager();
        toolBar.add( new MyAction() );
        toolBar.add( quitAction );
        coolBar.add( toolBar );
    }
}

class MyAction extends Action
{
    public MyAction()
    {
        super( "&MyAction", AS_PUSH_BUTTON );
        setToolTipText( "MyAction" );
        setImageDescriptor( ImageDescriptor.createFromFile( this.getClass(), "sample.gif" ) );
    }
    
    public void run()
    {
        System.out.println( "Click Me!" );
    }
}

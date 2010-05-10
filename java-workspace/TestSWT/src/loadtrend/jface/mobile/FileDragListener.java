/*
 * Created on 2005-8-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileDragListener implements DragSourceListener
{
    private FileBrowser browser;

    public FileDragListener( FileBrowser browser )
    {
        this.browser = browser;
    }

    public void dragStart( DragSourceEvent event )
    {
        event.doit = true;
    }

    public void dragSetData( DragSourceEvent event )
    {
        event.data = browser.getSelectedFiles();
    }

    public void dragFinished( DragSourceEvent event )
    {
    }
}
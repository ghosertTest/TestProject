/*
 * Created on 2005-8-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
final class FileDropListener implements DropTargetListener
{
    private final FileBrowser browser;

    FileDropListener( FileBrowser browser )
    {
        this.browser = browser;
    }

    public void dragEnter( DropTargetEvent event )
    {
        event.detail = DND.DROP_COPY;
    }

    public void dragLeave( DropTargetEvent event )
    {
    }

    public void dragOperationChanged( DropTargetEvent event )
    {
    }

    public void dragOver( DropTargetEvent event )
    {
    }

    public void dropAccept( DropTargetEvent event )
    {
    }

    public void drop( DropTargetEvent event )
    {
        String[] sourceFileList = (String[]) event.data;
        browser.copyFiles( sourceFileList );
    }
}

/*
 * Created on 2005-8-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileBrowser
{
    private ListViewer viewer;
    private File currentDirectory;

    public FileBrowser( Composite parent )
    {
        super();
        buildListViewer( parent );
        Transfer[] types = new Transfer[] { FileTransfer.getInstance() };
        viewer.addDropSupport( DND.DROP_COPY, types,
                new FileDropListener( this ) );
        viewer.addDragSupport( DND.DROP_COPY, types,
                new FileDragListener( this ) );
    }

    private void buildListViewer( Composite parent )
    {
        viewer = new ListViewer( parent );
        viewer.setLabelProvider( new LabelProvider()
        {
            public String getText( Object element )
            {
                File file = (File) element;
                String name = file.getName();
                return file.isDirectory() ? "<Dir> " + name : name;
            }
        } );
        viewer.setContentProvider( new IStructuredContentProvider()
        {
            public Object[] getElements( Object inputElement )
            {
                File file = (File) inputElement;
                if ( file.isDirectory() )
                {
                    return file.listFiles();
                }
                else
                {
                    return new Object[] { file.getName() };
                }
            }

            public void dispose()
            {
            }

            public void inputChanged( Viewer viewer, Object oldInput,
                    Object newInput )
            {
            }
        } );
        viewer.setSorter( new ViewerSorter()
        {
            public int category( Object element )
            {
                return ( (File) element ).isDirectory() ? 0 : 1;
            }

            public int compare( Viewer viewer, Object e1, Object e2 )
            {
                int cat1 = category( e1 );
                int cat2 = category( e2 );
                if ( cat1 != cat2 )
                    return cat1 - cat2;
                return ( (File) e1 ).getName().compareTo(
                        ( (File) e2 ).getName() );
            }
        } );
        viewer.addDoubleClickListener( new IDoubleClickListener()
        {
            public void doubleClick( DoubleClickEvent event )
            {
                IStructuredSelection selection = (IStructuredSelection) event
                        .getSelection();
                setCurrentDirectory( (File) selection.getFirstElement() );
            }
        } );
        setCurrentDirectory( File.listRoots()[0] );
    }

    private void setCurrentDirectory( File directory )
    {
        if ( !directory.isDirectory() )
            throw new RuntimeException( directory + " is not a directory!" );
        currentDirectory = directory;
        viewer.setInput( directory );
    }

    String[] getSelectedFiles()
    {
        IStructuredSelection selection = (IStructuredSelection) viewer
                .getSelection();
        List fileNameList = new LinkedList();
        Iterator iterator = selection.iterator();
        while ( iterator.hasNext() )
        {
            File file = (File) iterator.next();
            fileNameList.add( file.getAbsoluteFile().toString() );
        }
        return (String[]) fileNameList
                .toArray( new String[fileNameList.size()] );
    }

    void copyFiles( String[] sourceFileList )
    {
        for ( int i = 0; i < sourceFileList.length; i++ )
        {
            File sourceFile = new File( sourceFileList[i] );
            if ( sourceFile.canRead() && currentDirectory.canWrite() )
            {
                File destFile = new File( currentDirectory, sourceFile
                        .getName() );
                if ( !destFile.exists() )
                {
                    FileOutputStream out;
                    FileInputStream in;
                    try
                    {
                        out = new FileOutputStream( destFile );
                        in = new FileInputStream( sourceFile );
                        byte[] buffer = new byte[1024];
                        while ( ( in.read( buffer ) ) != -1 )
                        {
                            out.write( buffer );
                        }
                        out.flush();
                        out.close();
                        in.close();
                        viewer.refresh();
                    }
                    catch ( FileNotFoundException e )
                    {
                        e.printStackTrace();
                    }
                    catch ( IOException e )
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    System.out.println( destFile
                            + " already exists, refusing to clobber" );
                }
            }
            else
            {
                System.out
                        .println( "Sorry, either your source file is not readable "
                                + "or the target directory is not writable" );
            }
        }
    }
}

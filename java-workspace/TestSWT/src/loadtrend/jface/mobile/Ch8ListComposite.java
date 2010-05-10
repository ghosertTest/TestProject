/*
 * Created on 2005-8-3
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch8ListComposite extends Composite
{
    public Ch8ListComposite( Composite parent )
    {
        super( parent, SWT.NULL );
        populateControl();
    }

    protected void populateControl()
    {
        FillLayout compositeLayout = new FillLayout();
        setLayout( compositeLayout );
        int[] styles = { SWT.SINGLE, SWT.MULTI };
        for ( int style = 0; style < styles.length; style++ )
        {
            createListViewer( styles[style] );
        }
    }

    private void createListViewer( int style )
    {
        ListViewer viewer = new ListViewer( this, style );
        viewer.setLabelProvider( new LabelProvider()
        {
            public String getText( Object element )
            {
                return ( (ListItem) element ).name;
            }
        } );
        viewer.addFilter( new ViewerFilter()
        {
            public boolean select( Viewer viewer, Object parent, Object element )
            {
                return ( (ListItem) element ).value % 2 == 0;
            }
        } );
        viewer.setSorter( new ViewerSorter()
        {
            public int compare( Viewer viewer, Object obj1, Object obj2 )
            {
                return ( (ListItem) obj2 ).value - ( (ListItem) obj1 ).value;
            }
        } );
        viewer.setContentProvider( new IStructuredContentProvider()
        {
            public Object[] getElements( Object inputElement )
            {
                return ( (List) inputElement ).toArray();
            }

            public void dispose()
            {
            }

            public void inputChanged( Viewer viewer, Object oldInput,
                    Object newInput )
            {
            }
        } );
        List input = new ArrayList();
        for ( int i = 0; i < 20; i++ )
        {
            input.add( new ListItem( "item " + i, i ) );
        }
        viewer.setInput( input );
    }
}

class ListItem
{
    public String name;

    public int value;

    public ListItem( String n, int v )
    {
        name = n;
        value = v;
    }
}
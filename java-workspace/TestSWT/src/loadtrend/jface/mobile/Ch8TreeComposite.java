/*
 * Created on 2005-8-3
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch8TreeComposite extends Composite
{
    public Ch8TreeComposite( Composite parent )
    {
        super( parent, SWT.NULL );
        populateControl();
    }

    protected void populateControl()
    {
        FillLayout compositeLayout = new FillLayout();
        setLayout( compositeLayout );
        int[] selectionStyle = { SWT.SINGLE, SWT.MULTI };
        int[] checkStyle = { SWT.NONE, SWT.CHECK };
        for ( int selection = 0; selection < selectionStyle.length; selection++ )
        {
            for ( int check = 0; check < checkStyle.length; check++ )
            {
                int style = selectionStyle[selection] | checkStyle[check];
                createTreeViewer( style );
            }
        }
    }

    private void createTreeViewer( int style )
    {
        TreeViewer viewer = new TreeViewer( this, style );
        viewer.setContentProvider( new ITreeContentProvider()
        {
            public Object[] getChildren( Object parentElement )
            {
                return ( (TreeNode) parentElement ).getChildren().toArray();
            }

            public Object getParent( Object element )
            {
                return ( (TreeNode) element ).getParent();
            }

            public boolean hasChildren( Object element )
            {
                return ( (TreeNode) element ).getChildren().size() > 0;
            }

            public Object[] getElements( Object inputElement )
            {
                return ( (TreeNode) inputElement ).getChildren().toArray();
            }

            public void dispose()
            {
            }

            public void inputChanged( Viewer viewer, Object oldInput,
                    Object newInput )
            {
            }
        } );
        viewer.setInput( getRootNode() );
    }

    private TreeNode getRootNode()
    {
        TreeNode root = new TreeNode( "root" );
        root.addChild( new TreeNode( "child 1" ).addChild( new TreeNode(
                "subchild 1" ) ) );
        root.addChild( new TreeNode( "child 2" ).addChild( new TreeNode(
                "subchild 2" ).addChild( new TreeNode( "grandchild 1" ) ) ) );
        return root;
    }
}

class TreeNode
{
    private String name;

    private List children = new ArrayList();

    private TreeNode parent;

    public TreeNode( String n )
    {
        name = n;
    }

    protected Object getParent()
    {
        return parent;
    }

    public TreeNode addChild( TreeNode child )
    {
        children.add( child );
        child.parent = this;
        return this;
    }

    public List getChildren()
    {
        return children;
    }

    public String toString()
    {
        return name;
    }
}
/*
 * Created on 2005-8-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch12LabelDecorator extends Composite
{
    public Ch12LabelDecorator( Composite parent )
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
                return ( (FamilyTreeNode) parentElement ).getChildren().toArray();
            }

            public Object getParent( Object element )
            {
                return ( (FamilyTreeNode) element ).getParent();
            }

            public boolean hasChildren( Object element )
            {
                return ( (FamilyTreeNode) element ).getChildren().size() > 0;
            }

            public Object[] getElements( Object inputElement )
            {
                return ( (FamilyTreeNode) inputElement ).getChildren().toArray();
            }

            public void dispose()
            {
            }

            public void inputChanged( Viewer viewer, Object oldInput,
                    Object newInput )
            {
            }
        } );
        viewer.setLabelProvider(
                new DecoratingLabelProvider(
                (ILabelProvider)viewer.getLabelProvider(),
                new FamilyDecorator(getShell())));
        viewer.setInput( getRootNode() );
    }

    private FamilyTreeNode getRootNode()
    {
        FamilyTreeNode root = new FamilyTreeNode( "Jiawei", "Zhang", true );
        root.addChild( new FamilyTreeNode( "Jiawei", "Zhang", true ).addChild( new FamilyTreeNode(
                "Jingli", "Ni", false ) ) );
        root.addChild( new FamilyTreeNode( "Kewu", "Zhang", true ).addChild( new FamilyTreeNode(
                "Lihua", "Deng", false ).addChild( new FamilyTreeNode( "Jiawei", "Zhang", true ) ) ) );
        return root;
    }
}

class FamilyDecorator extends LabelProvider implements ILabelDecorator
{
    private static final String MALE_IMAGE_KEY = "male";
    private static final String FEMALE_IMAGE_KEY = "female";
    private ImageRegistry imageRegistry;

    public FamilyDecorator( Shell s )
    {
        imageRegistry = new ImageRegistry( s.getDisplay() );
        Image maleImage = new Image( s.getDisplay(), this.getClass().getResourceAsStream("male.bmp") );
        Image femaleImage = new Image( s.getDisplay(), this.getClass().getResourceAsStream("female.bmp") );
        imageRegistry.put( FEMALE_IMAGE_KEY, femaleImage );
        imageRegistry.put( MALE_IMAGE_KEY, maleImage );
    }

    public Image decorateImage( Image image, Object element )
    {
        if ( element == null )
            return null;
        FamilyTreeNode node = (FamilyTreeNode) element;
        if ( node.isMale() )
        {
            return imageRegistry.get( MALE_IMAGE_KEY );
        }
        else
        {
            return imageRegistry.get( FEMALE_IMAGE_KEY );
        }
    }

    public String decorateText( String text, Object element )
    {
        if ( element == null )
            return null;
        FamilyTreeNode node = (FamilyTreeNode) element;
        return text + " [" + node.getFamilyName() + "]";
    }
}



class FamilyTreeNode
{
    private String firstName;
    private boolean isMale = false;
    private String familyName;
    private List children = new ArrayList();
    private FamilyTreeNode parent;

    public FamilyTreeNode( String firstName, String familyName, boolean male )
    {
        this.firstName = firstName;
        this.familyName = familyName;
        isMale = male;
    }
    
    protected Object getParent()
    {
        return parent;
    }

    public FamilyTreeNode addChild( FamilyTreeNode child )
    {
        children.add( child );
        child.parent = this;
        return this;
    }

    public List getChildren()
    {
        return children;
    }
    
    public String getFamilyName()
    {
        return familyName;
    }
    public void setFamilyName( String familyName )
    {
        this.familyName = familyName;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }
    public boolean isMale()
    {
        return isMale;
    }
    public void setMale( boolean isMale )
    {
        this.isMale = isMale;
    }
    
    public String toString()
    {
        return this.firstName + this.familyName;
    }
}
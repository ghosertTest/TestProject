/*
 * Created on 2005-8-5
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author Jiawei_Zhang
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Ch9TableEditorComposite extends Composite
{
    private static final Object[] CONTENT = new Object[] {
            new EditableTableItem( "item 1", new Integer( 0 ) ),
            new EditableTableItem( "item 2", new Integer( 1 ) ) };

    private static final String[] VALUE_SET = new String[] { "xxx", "yyy",
            "zzz" };

    private static final String NAME_PROPERTY = "name";

    private static final String VALUE_PROPERTY = "value";

    private TableViewer viewer;


    public Ch9TableEditorComposite( Composite parent )
    {
        super( parent, SWT.NULL );
        buildControls();
    }

    private class NewRowAction extends Action
    {
        public NewRowAction()
        {
            super( "Insert New Row" );
        }

        public void run()
        {
            EditableTableItem newItem = new EditableTableItem( "new row",
                    new Integer( 2 ) );
            viewer.add( newItem );
        }
    }

    protected void buildControls()
    {
        FillLayout compositeLayout = new FillLayout();
        setLayout( compositeLayout );
        final Table table = new Table( this, SWT.FULL_SELECTION );
        viewer = buildAndLayoutTable( table );
        attachContentProvider( viewer );
        attachLabelProvider( viewer );
        attachCellEditors( viewer, table );
        MenuManager popupMenu = new MenuManager();
        IAction newRowAction = new NewRowAction();
        popupMenu.add( newRowAction );
        Menu menu = popupMenu.createContextMenu( table );
        table.setMenu( menu );
        viewer.setInput( CONTENT );
    }

    private void attachLabelProvider( TableViewer viewer )
    {
        viewer.setLabelProvider( new ITableLabelProvider()
        {
            public Image getColumnImage( Object element, int columnIndex )
            {
                return null;
            }

            public String getColumnText( Object element, int columnIndex )
            {
                switch ( columnIndex )
                {
                case 0:
                    return ( (EditableTableItem) element ).name;
                case 1:
                    Number index = ( (EditableTableItem) element ).value;
                    return VALUE_SET[index.intValue()];
                default:
                    return "Invalid column: " + columnIndex;
                }
            }

            public void addListener( ILabelProviderListener listener )
            {
            }

            public void dispose()
            {
            }

            public boolean isLabelProperty( Object element, String property )
            {
                return false;
            }

            public void removeListener( ILabelProviderListener lpl )
            {
            }
        } );
    }

    private void attachContentProvider( TableViewer viewer )
    {
        viewer.setContentProvider( new IStructuredContentProvider()
        {
            public Object[] getElements( Object inputElement )
            {
                return (Object[]) inputElement;
            }

            public void dispose()
            {
            }

            public void inputChanged( Viewer viewer, Object oldInput,
                    Object newInput )
            {
            }
        } );
    }

    private TableViewer buildAndLayoutTable( final Table table )
    {
        TableViewer tableViewer = new TableViewer( table );
        TableLayout layout = new TableLayout();
        layout.addColumnData( new ColumnWeightData( 50, 75, true ) );
        layout.addColumnData( new ColumnWeightData( 50, 75, true ) );
        table.setLayout( layout );
        TableColumn nameColumn = new TableColumn( table, SWT.CENTER );
        nameColumn.setText( "Name" );
        TableColumn valColumn = new TableColumn( table, SWT.CENTER );
        valColumn.setText( "Value" );
        table.setHeaderVisible( true );
        return tableViewer;
    }

    private void attachCellEditors( final TableViewer viewer, Composite parent )
    {
        viewer.setCellModifier( new ICellModifier()
        {
            public boolean canModify( Object element, String property )
            {
                return true;
            }

            public Object getValue( Object element, String property )
            {

                if ( NAME_PROPERTY.equals( property ) )
                    return ( (EditableTableItem) element ).name;
                else
                    return ( (EditableTableItem) element ).value;
            }

            public void modify( Object element, String property, Object value )
            {
                TableItem tableItem = (TableItem) element;
                EditableTableItem data = (EditableTableItem) tableItem
                        .getData();
                if ( NAME_PROPERTY.equals( property ) )
                    data.name = value.toString();
                else
                    data.value = (Integer) value;
                viewer.refresh( data );
            }
        } );
        viewer.setCellEditors( new CellEditor[] { new TextCellEditor( parent ),
                new ComboBoxCellEditor( parent, VALUE_SET ) } );
        viewer.setColumnProperties( new String[] { NAME_PROPERTY,
                VALUE_PROPERTY } );
    }
}

class EditableTableItem
{
    public String name;

    public Integer value;

    public EditableTableItem( String n, Integer v )
    {
        name = n;
        value = v;
    }
}
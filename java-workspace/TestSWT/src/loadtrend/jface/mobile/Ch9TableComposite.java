/*
 * Created on 2005-8-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch9TableComposite extends Composite
{
    public Ch9TableComposite( Composite parent )
    {
        super( parent, SWT.NONE );
        
//      Set up the table layout
        TableLayout layout = new TableLayout();
        layout.addColumnData(new ColumnWeightData(33, 75, true));
        layout.addColumnData(new ColumnWeightData(33, 75, true));
        layout.addColumnData(new ColumnWeightData(33, 75, true));
        Table table = new Table( this, SWT.SINGLE);
        table.setLayout(layout);
        table.setLocation( 100, 0 );
        table.setSize( 200, 200 );
        table.setHeaderVisible( true );
        table.setLinesVisible( true );
        
//      Add columns to the table
        TableColumn column1 = new TableColumn(table, SWT.CENTER);
        TableColumn column2 = new TableColumn(table, SWT.CENTER);
        TableColumn column3 = new TableColumn(table, SWT.CENTER);
        TableItem item = new TableItem(table, SWT.NONE);
        item.setText( new String[] { "Zhang Jiawei",
        "male",
        "25" } );
        item = new TableItem(table, SWT.NONE);
        item.setText( new String[] { "Henny", "male", "23" } );
        
        column1.setText( "Name");
        column2.setText( "Sex");
        column3.setText( "Age");
        
        column1.pack();
        column2.pack();
        column3.pack();
        
        
    }
}

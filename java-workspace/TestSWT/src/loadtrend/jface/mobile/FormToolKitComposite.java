package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.HyperlinkGroup;
import org.eclipse.ui.forms.HyperlinkSettings;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;

public class FormToolKitComposite extends Composite {
	
	public FormToolKitComposite( Composite parent )
	{
		super( parent, SWT.NONE );
		this.buildForm();
	}
	
	private void buildForm()
	{
		FormToolkit kit = new FormToolkit( this.getDisplay() );
		Form form = kit.createForm( this );
		ColumnLayout layout = new ColumnLayout();
		form.getBody().setLayout( layout );
		ExpandableComposite exComp = kit.createExpandableComposite( form.getBody(), ExpandableComposite.TWISTIE );
		exComp.setText( "The Eclipse Forms toolset is:" );
		exComp.setExpanded( true );
		FormText ft = kit.createFormText( exComp, true );
		exComp.setClient( ft );
		String html = "<form><li>Useful</li><li>Powerful</li>" + "<li>Simple</li></form>";
		ft.setText( html, true, false );
        
		Label sep = kit.createSeparator( form.getBody(), SWT.HORIZONTAL );
        
		final Button button = kit.createButton( form.getBody(), "Favorite color?", SWT.NULL );
		HyperlinkGroup hg = kit.getHyperlinkGroup();
		hg.setHyperlinkUnderlineMode( HyperlinkSettings.UNDERLINE_HOVER );
		hg.setForeground( this.getDisplay().getSystemColor( SWT.COLOR_RED ) );
		String[] cnames = { "red", "green", "yellow", "blue" };
		Hyperlink[] hl = new Hyperlink[4];
		String name;
		for (int i=0; i<4; i++)
		{
			name = "My favorite color is "+cnames[i]+".";
		    hl[i] = kit.createHyperlink( form.getBody(), name, SWT.NULL );
		    hg.add(hl[i]);
		    hl[i].setHref( cnames[i] );
		    hl[i].addHyperlinkListener( new HyperlinkAdapter()
		    {
		    	public void linkActivated(HyperlinkEvent e)
		    	{
		    		button.setText("My favorite color is " + (String)e.getHref() + ".");
		    		button.redraw();
		    	}
		    });
		}
        
		Section section = kit.createSection( form.getBody(), SWT.HORIZONTAL );
        
		form.pack();
	}
}

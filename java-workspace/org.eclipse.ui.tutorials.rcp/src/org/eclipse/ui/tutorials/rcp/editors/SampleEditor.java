package org.eclipse.ui.tutorials.rcp.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class SampleEditor extends EditorPart
{
	private Text text = null;
	
	public void doSave( IProgressMonitor monitor )
	{
		// TODO Auto-generated method stub

	}

	public void doSaveAs()
	{
		// TODO Auto-generated method stub

	}

	public void init( IEditorSite site, IEditorInput input ) throws PartInitException
	{
		setSite( site );
		setInput( input );
//		设置Editor标题栏的显示名称。不要，则名称用plugin.xml中的name属性
//		setPartName(input.getName());
//		设置Editor标题栏的图标。不要，则会自动使用一个默认的图标
//		setTitleImage(input.getImageDescriptor().createImage());
	}

	public boolean isDirty()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSaveAsAllowed()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public void createPartControl( Composite parent )
	{
		this.text = new Text( parent, SWT.V_SCROLL );
	}

	public void setFocus()
	{
		// TODO Auto-generated method stub

	}
	
	public void setText( Text text )
	{
		this.text = text;
	}
	
	public Text getText()
	{
		return this.text;
	}
}

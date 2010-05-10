package org.eclipse.ui.tutorials.rcp.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class EditorInput implements IEditorInput
{

	/**
	* 返回true，则打开该编辑器后它会出现在Eclipse主菜单“文件”
	* 最下部的最近打开的文档栏中。返回false则不出现在其中
	*/
	public boolean exists()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	* 编辑器标题栏的图标，不过它还需要在ChinaEditor中用
	* setTitleImage方法设置，才能出现在标题栏中
	*/
	public ImageDescriptor getImageDescriptor()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	* 编辑器标题栏的显示名称，和上面的getImageDescriptor
	* 一样也要在ChinaEditor中用setPartName方法设置，才能出现在标题栏中
	*/
	public String getName()
	{
		// TODO Auto-generated method stub
		return "New Names";
	}

	/**
	* 返回一个可以用做保存本编辑输入数据状态的对象，本例让它空实现
	*/
	public IPersistableElement getPersistable()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	* 编辑器标题栏的小黄条提示文字，不需像getName那样在ChinaEditor中再设置
	*/
	public String getToolTipText()
	{
		// TODO Auto-generated method stub
		return "New Tips";
	}

	/**
	* 得到一个编辑器的适配器，本例让它空实现
	* IAdaptable a = new ChinaEditorInput();
	* IFoo x = (IFoo)a.getAdapter(IFoo.class);
    * if (x != null)
    * [用x来做IFoo的事情]
    */
	public Object getAdapter( Class adapter )
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}

package org.eclipse.ui.tutorials.rcp.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class EditorInput implements IEditorInput
{

	/**
	* ����true����򿪸ñ༭�������������Eclipse���˵����ļ���
	* ���²�������򿪵��ĵ����С�����false�򲻳���������
	*/
	public boolean exists()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	* �༭����������ͼ�꣬����������Ҫ��ChinaEditor����
	* setTitleImage�������ã����ܳ����ڱ�������
	*/
	public ImageDescriptor getImageDescriptor()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	* �༭������������ʾ���ƣ��������getImageDescriptor
	* һ��ҲҪ��ChinaEditor����setPartName�������ã����ܳ����ڱ�������
	*/
	public String getName()
	{
		// TODO Auto-generated method stub
		return "New Names";
	}

	/**
	* ����һ�������������汾�༭��������״̬�Ķ��󣬱���������ʵ��
	*/
	public IPersistableElement getPersistable()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	* �༭����������С������ʾ���֣�������getName������ChinaEditor��������
	*/
	public String getToolTipText()
	{
		// TODO Auto-generated method stub
		return "New Tips";
	}

	/**
	* �õ�һ���༭����������������������ʵ��
	* IAdaptable a = new ChinaEditorInput();
	* IFoo x = (IFoo)a.getAdapter(IFoo.class);
    * if (x != null)
    * [��x����IFoo������]
    */
	public Object getAdapter( Class adapter )
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}

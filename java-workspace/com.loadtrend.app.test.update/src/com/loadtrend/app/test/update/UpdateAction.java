package com.loadtrend.app.test.update;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchWindow;

public class UpdateAction extends Action implements IAction {
    
    private UpdateJobChangeListener jobListener = null;
    
	public UpdateAction(IWorkbenchWindow window) {
		this.jobListener = UpdateJobChangeListener.getInstance(window);
		setText("检查更新...");
		setToolTipText("搜索可升级组件");
	}

	public void run() {
		jobListener.start(false);
	}
}
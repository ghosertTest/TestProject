package com.test.classloader;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.classloader.CreatClass;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(400, 300));
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
        
        
        Thread thread = Thread.currentThread();
        ClassLoader threadClassLoader = thread.getContextClassLoader();
        ClassLoader thisClassLoader = this.getClass().getClassLoader();
        CreatClass cc = new CreatClass();
        ClassLoader ccClassLoader = cc.getClass().getClassLoader();
        this.getClassLoaderName("thread classloader: ", threadClassLoader);
        this.getClassLoaderName("this classloader: ", thisClassLoader);
        this.getClassLoaderName("CreatClass: ", ccClassLoader);
        
        thread.setContextClassLoader(thisClassLoader);
        // "com.test.classloader.ClassloaderPlugin" can be loaded by thisClassLoader which is not the same to ccClassLoader, so there is error here.
        // String string = cc.createClassByForName("com.test.classloader.ClassloaderPlugin").getName();
        // "com.test.classloader.ClassloaderPlugin" can be loaded by thisClassLoader, so there is no error here.
        // "This is bad method to load spring ApplicationContext in Rich Client Program"
        String string = cc.createClassByLoader("com.test.classloader.ClassloaderPlugin").getName();
        configurer.setTitle(string);
        thread.setContextClassLoader(threadClassLoader);
        
        // To sovle this problem without the bad smell code above:
        // The following line added to com.classloader plugin's MANIFEST.MF makes that
        // Eclipse-BuddyPolicy: registered
        // Then in each plug-in that supplies fullname class, add the following line to the MANIFEST.MF to register the plug-in as a buddy of com.classloader plugin:
        // Eclipse-RegisterBuddy: com.test.classloader
        
    }
    
    private void getClassLoaderName(String desc, ClassLoader classLoader) {
       System.out.println(desc + classLoader); 
    }
}

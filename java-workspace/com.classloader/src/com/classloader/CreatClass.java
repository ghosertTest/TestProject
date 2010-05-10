package com.classloader;

public class CreatClass {
    private static final String className = "CreatClass: ";
    public Class createClassByForName(String className) {
        try {
            System.out.println(CreatClass.className + this.getClass().getClassLoader());
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public Class createClassByLoader(String className) {
        try {
            System.out.println(CreatClass.className + Thread.currentThread().getContextClassLoader());
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}

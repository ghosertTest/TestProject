package com.designpatten.observer;

import java.util.Observable;
import java.util.Observer;

public class NameObserver implements Observer
{
    private String name = null;
    
    public void update( Observable o, Object arg )
    {
        if ( arg instanceof String )
        {
            name = (String) arg;
            System.out.println( "Name changed to: " + name );
        }
    }
}

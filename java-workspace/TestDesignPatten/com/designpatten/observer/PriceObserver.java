package com.designpatten.observer;

import java.util.Observable;
import java.util.Observer;

public class PriceObserver implements Observer
{
    private float price = 0.0f;
    
    public void update( Observable o, Object arg )
    {
        if ( arg instanceof Float )
        {
            price = ( (Float) arg ).floatValue();
            System.out.println( "Price changed to: " + price );
        }
    }

}

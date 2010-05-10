package com.designpatten.observer;

import java.util.Observable;

public class Product extends Observable
{
    private String name = null;
    
    private float price = 0.0f;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
        
        // Set changes point and notify observer.
        this.setChanged();
        this.notifyObservers( name );
    }

    public float getPrice()
    {
        return price;
    }

    public void setPrice( float price )
    {
        this.price = price;
        
        // Set changes point and notify observer.
        this.setChanged();
        this.notifyObservers( new Float( price ) );
    }
    
    
}

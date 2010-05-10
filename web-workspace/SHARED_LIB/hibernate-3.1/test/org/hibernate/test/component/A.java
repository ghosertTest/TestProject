package org.hibernate.test.component;

import java.util.Map;

public class A {

    public int id;
    public Map dynComp;

    public A() {
        this(-1);
    }

    public A(int id) {
        this.id = id;
    }
    
    public Map getDynComp() {
        return dynComp;
    }
    
    public void setDynComp(Map dynComp) {
        this.dynComp = dynComp;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public boolean equals(Object obj) {
        if ( !( obj instanceof A ) ) return false;
        return id == ( (A) obj ).id;
    }
    
    public int hashCode() {
        return id == -1 ? super.hashCode() : id;
    }
}

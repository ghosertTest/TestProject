package org.hibernate.test.component;


public class C extends B {

    public C() {
        super(-1);
    }
    
    public C(int id) {
        super(id);
    }
}

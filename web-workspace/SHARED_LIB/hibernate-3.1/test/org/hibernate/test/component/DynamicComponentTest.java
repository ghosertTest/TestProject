package org.hibernate.test.component;

import java.util.HashMap;
import java.util.List;

import org.hibernate.classic.Session;
import org.hibernate.test.TestCase;

public class DynamicComponentTest extends TestCase {

    public DynamicComponentTest(String x) {
        super(x);
    }

    public void testQuery() throws Exception {
        Session session = openSession();
        
        A a = new A(1);
        a.setDynComp(new HashMap());
        a.getDynComp().put("a", "a-a");
        session.save(a);
        
        B b = new B(2);
        b.setDynComp(new HashMap());
        b.getDynComp().put("a", "b-a");
        b.getDynComp().put("b", "b-b");
        session.save(b);
        
        C c = new C(3);
        c.setDynComp(new HashMap());
        c.getDynComp().put("a", "c-a");
        c.getDynComp().put("b", "c-b");
        c.getDynComp().put("c", "c-c");
        session.save(c);
        
        session.flush();
        
        List list = session.createQuery("from A a where a.dynComp.a = ?")
            .setParameter(0, "a-a")
            .list();
        
        assertEquals(1, list.size());
        assertTrue(a.equals(list.get(0)));
        
        list = session.createQuery("from B b where b.dynComp.b = ?")
            .setParameter(0, "b-b")
            .list();

        assertEquals(1, list.size());
        assertTrue(b.equals(list.get(0)));
        
        list = session.createQuery("from B b where b.dynComp.a = ?")
            .setParameter(0, "b-a")
            .list();

        assertEquals(1, list.size());
        assertTrue(b.equals(list.get(0)));
        
        list = session.createQuery("from C c where c.dynComp.c = ?")
            .setParameter(0, "c-c")
            .list();
        
        list = session.createQuery("from C c where c.dynComp.b = ?")
            .setParameter(0, "c-b")
            .list();
        
        list = session.createQuery("from C c where c.dynComp.a = ?")
            .setParameter(0, "c-a")
            .list();
        
        assertEquals(1, list.size());
        assertTrue(c.equals(list.get(0)));
        
        session.delete(c);
        session.delete(b);
        session.delete(a);
        session.close();
    }
    
    protected String[] getMappings() {
        return new String[] { "component/ABC.hbm.xml" };
    }

}

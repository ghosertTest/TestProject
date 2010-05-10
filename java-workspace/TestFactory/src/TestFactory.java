/*
 * Created on 2005-2-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package src;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestFactory {

    public static void main(String[] args) {
        try {
        Shape shape = null;
        MultiShapeFactory msf = new MultiShapeFactory();
        
        // You can load Shape class from properties files instead of pass String variable.
        shape = msf.createShape( "src.Circle" );
        shape.draw();
        shape.erase();
        shape = msf.createShape( "src.Square" );
        shape.draw();
        shape.erase();
        shape = msf.createShape( "src.Other" );
        shape.draw();
        shape.erase();
        } catch (ShapeException se) {
            // do something here.
        }

    }
}

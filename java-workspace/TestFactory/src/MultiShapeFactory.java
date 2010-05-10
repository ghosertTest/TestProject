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
public class MultiShapeFactory extends ShapeFactory {
    
    public Shape createShape(String shapeInfo) throws ShapeException{
        
        try {
            Shape shape = null;
            shape = ( Shape )Class.forName( shapeInfo ).newInstance();
            return shape;
        } catch ( ClassNotFoundException cnfe ) {
            System.out.println( "Class: " + shapeInfo + " is not found.");
            throw new ShapeException();
        } catch ( IllegalAccessException iae ) {
            System.out.println( "Class:" + shapeInfo + "can not be created.");
            throw new ShapeException();
        } catch ( InstantiationException ie ) {
            System.out.println( "Class:" + shapeInfo + "can not be created.");
            throw new ShapeException();
        }   
    }

}

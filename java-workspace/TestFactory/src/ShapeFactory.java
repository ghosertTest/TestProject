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
public abstract class ShapeFactory {
    
    /**
     * create Shape from Factory calss
     * @param shapeInfo
     * @return Shape
     * @throws ShapeException
     */
    public abstract Shape createShape(String shapeInfo) throws ShapeException;
}

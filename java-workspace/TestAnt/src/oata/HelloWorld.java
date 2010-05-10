
/*
 * Created on Dec 22, 2009
 */

/**
 * @author jiawzhang
 */

package oata;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class HelloWorld {
    static Logger logger = Logger.getLogger(HelloWorld.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        logger.info("Hello World");          // the old SysO-statement
    }
}

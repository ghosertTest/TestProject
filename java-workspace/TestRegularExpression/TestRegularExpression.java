import junit.framework.TestCase;

public class TestRegularExpression extends TestCase {

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    public void testRegularExpression() {
        System.out.println("jiawei".matches("[jiawei]"));
        System.out.println("".matches("[jiawei]+"));
        System.out.println("j".matches("[jiawei]+"));
        System.out.println("iewaij".matches("[jiawei]+"));
        System.out.println("".matches("[jiawei]*"));
        System.out.println("iewaij".matches("[jiawei]*"));
        System.out.println("".matches("[jiawei]?"));
        System.out.println("w".matches("[jiawei]?"));
        System.out.println("ji".matches("[jiawei]?"));
        System.out.println("!@#$%^&*()_+=-098jiawei7654321fhjvbjlaueg".matches("jiawei"));
        System.out.println("!@#$%^&*()_+=-098jiawei7654321fhjvbjlaueg".matches(".*jiawei.*"));
        // do not understand ?<! ?! ?<= ?=
        System.out.println("jiaweisss".matches("(?<!jiawei).*"));
        // super.assertEquals("")
        System.out.println("4243433".matches("[0-9]+"));
    }

}

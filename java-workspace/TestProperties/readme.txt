1 Put properties file to the package root path, and all
the filename should be lower case.
For example: "mockallocation.properties" is located at
the path of "src" package

2 You should implement the singleton for the class which 
read key-value from properties file, because once you read
the key-value from file, you needn't to read it once again
normally.

public class TestProperties
{
    private static TestProperties reader = null ;
    
    private TestProperties() throws MissingResourceException 
    {
    }
    public static TestProperties getInstance() throws MissingResourceException
    {
        if( reader == null )
        {
            reader = new TestProperties() ;
        }
        return reader ;
    }

}
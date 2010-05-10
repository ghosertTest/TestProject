package org.hibernate.test;

import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.BuildException;

import java.io.File;
import java.util.Enumeration;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * A custom Ant FileSelector used to limit the tests run from the Ant
 * build script to only those defined in the {@link AllTests} suite.
 * <p/>
 * {@link AllTests} is used/maintained by the developers to easily
 * run the test suite in all IDEs.  It represents all the tests
 * which should actually be run and included in test results.
 * 
 * @author <a href="mailto:steve@hibernate.org">Steve Ebersole </a>
 */
public class TestSelector implements FileSelector {

	private final Set allTestClassNames = new HashSet();

	public TestSelector() {
		handleTestSuite( ( TestSuite ) AllTests.suite() );
	}

	private void handleTestSuite(TestSuite suite) {
		Enumeration tests = suite.tests();
		while ( tests.hasMoreElements() ) {
			Test test = ( Test ) tests.nextElement();
			if ( test instanceof TestSuite ) {
				handleTestSuite( ( TestSuite ) test );
			}
			else {
				allTestClassNames.add( test.getClass().getName() );
			}
		}
	}

	public static void main(String[] args) {
		TestSelector s = new TestSelector();
		Iterator itr = s.allTestClassNames.iterator();
		while( itr.hasNext() ) {
			System.out.println( "Test class : " + itr.next() );
		}
		boolean selected = s.isSelected( new File( "blah blah blah" ), "org/hibernate/test/hql/HQLTest.class", new File( "blah blah blah" ) );
		System.out.println( "Valid selected ? " + selected );
		selected = s.isSelected( new File(""), "hithere.class", new File("") );
		System.out.println( "Invalid selected ? " + selected );
	}

	public boolean isSelected(File dir, String fileFromDir, File fullFile) throws BuildException {
		String correspondingClassName = determineClassName( fileFromDir );
		return allTestClassNames.contains( correspondingClassName );
	}

	private String determineClassName(String file) {
		if ( file.endsWith( ".class" ) ) {
			file = file.substring( 0, file.length() - 6 );
		}
		else {
			return null;
		}
		file = file.replace( '\\', '.' );
		file = file.replace( '/', '.' );
		return file;
	}
}

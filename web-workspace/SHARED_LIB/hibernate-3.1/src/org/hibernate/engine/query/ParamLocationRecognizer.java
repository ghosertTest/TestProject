package org.hibernate.engine.query;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Implements a parameter parser recognizer specifically for the purpose
 * of journaling parameter locations.
 *
 * @author <a href="mailto:steve@hibernate.org">Steve Ebersole </a>
 */
public class ParamLocationRecognizer implements ParameterParser.Recognizer {
	private Map namedParameterLocationMap = new HashMap();
	private List ordinalParameterLocationList = new ArrayList();

	/**
	 * Convenience method for creating a param location recognizer and
	 * initiating the parse.
	 *
	 * @param query The query to be parsed for parameter locations.
	 * @return The generated recognizer, with journaled location info.
	 */
	public static ParamLocationRecognizer parseLocations(String query) {
		ParamLocationRecognizer recognizer = new ParamLocationRecognizer();
		ParameterParser.parse( query, recognizer );
		return recognizer;
	}

	/**
	 * Returns the map of named parameter locations.  The map is keyed by
	 * parameter name; the corresponding value is an Integer list.
	 *
	 * @return The map of named parameter locations.
	 */
	public Map getNamedParameterLocationMap() {
		return namedParameterLocationMap;
	}

	/**
	 * Returns the list of ordinal parameter locations.  The list elements
	 * are Integers, representing the location for that given ordinal.  Thus
	 * {@link #getOrdinalParameterLocationList()}.elementAt(n) represents the
	 * location for the nth parameter.
	 *
	 * @return The list of ordinal parameter locations.
	 */
	public List getOrdinalParameterLocationList() {
		return ordinalParameterLocationList;
	}


	// Recognition code ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void ordinalParameter(int position) {
		ordinalParameterLocationList.add( new Integer( position ) );
	}

	public void namedParameter(String name, int position) {
		List locations = ( List ) namedParameterLocationMap.get( name );
		if ( locations == null ) {
			locations = new ArrayList();
			namedParameterLocationMap.put( name, locations );
		}
		locations.add( new Integer( position ) );
	}

	public void ejb3PositionalParameter(String name, int position) {
		namedParameter( name, position );
	}

	public void other(char character) {
		// don't care...
	}

	public void outParameter(int position) {
		// don't care...
	}
}

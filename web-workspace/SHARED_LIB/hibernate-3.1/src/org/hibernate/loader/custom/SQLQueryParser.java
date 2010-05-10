//$Id: SQLQueryParser.java,v 1.9 2005/11/04 21:28:49 steveebersole Exp $
package org.hibernate.loader.custom;

import org.hibernate.QueryException;
import org.hibernate.engine.query.ParameterParser;
import org.hibernate.persister.collection.SQLLoadableCollection;
import org.hibernate.persister.entity.SQLLoadable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gavin King, Max Andersen
 */
public class SQLQueryParser {

	private final String sqlQuery;

	private final Map entityPersisterByAlias;
	private final String[] aliases;
	private final String[] suffixes;
	
	private final SQLLoadableCollection[] collectionPersisters;
	private final String[] collectionAliases;
	private final String[] collectionSuffixes;

	private final Map returnByAlias;
	private final Map namedParameters = new HashMap();

	private long aliasesFound = 0;

	public SQLQueryParser(
			String sqlQuery,
	        Map alias2Persister,
	        Map alias2Return,
	        String[] aliases,
	        String[] collectionAliases,
	        SQLLoadableCollection[] collectionPersisters,
	        String[] suffixes,
	        String[] collectionSuffixes) {
		this.sqlQuery = sqlQuery;
		this.entityPersisterByAlias = alias2Persister;
		this.returnByAlias = alias2Return; // TODO: maybe just fieldMaps ?
		this.collectionAliases = collectionAliases;
		this.collectionPersisters = collectionPersisters;
		this.suffixes = suffixes;
		this.aliases = aliases;
		this.collectionSuffixes = collectionSuffixes;
	}

	private SQLLoadable getPersisterByResultAlias(String aliasName) {
		return (SQLLoadable) entityPersisterByAlias.get(aliasName);
	}
	
	private Map getPropertyResultByResultAlias(String aliasName) {
		SQLQueryReturn sqr = (SQLQueryReturn) returnByAlias.get(aliasName);
		return sqr.getPropertyResultsMap();				
	}
	
	private boolean isEntityAlias(String aliasName) {
		return entityPersisterByAlias.containsKey(aliasName);
	}

	private int getPersisterIndex(String aliasName) {
		for ( int i = 0; i < aliases.length; i++ ) {
			if ( aliasName.equals( aliases[i] ) ) {
				return i;
			}
		}
		return -1;
	}

	public Map getNamedParameters() {
		return namedParameters;
	}

	public boolean queryHasAliases() {
		return aliasesFound>0;
	}

	public String process() {
		return substituteParams( substituteBrackets() );
	}

	// TODO: should "record" how many properties we have reffered to - and if we 
	//       don't get'em'all we throw an exception! Way better than trial and error ;)
	private String substituteBrackets() throws QueryException {

		StringBuffer result = new StringBuffer( sqlQuery.length() + 20 );
		int left, right;

		// replace {....} with corresponding column aliases
		for ( int curr = 0; curr < sqlQuery.length(); curr = right + 1 ) {
			if ( ( left = sqlQuery.indexOf( '{', curr ) ) < 0 ) {
				// No additional open braces found in the string, append the
				// rest of the string in its entirty and quit this loop
				result.append( sqlQuery.substring( curr ) );
				break;
			}

			// apend everything up until the next encountered open brace
			result.append( sqlQuery.substring( curr, left ) );

			if ( ( right = sqlQuery.indexOf( '}', left + 1 ) ) < 0 ) {
				throw new QueryException( "Unmatched braces for alias path", sqlQuery );
			}

			String aliasPath = sqlQuery.substring( left + 1, right );
			int firstDot = aliasPath.indexOf( '.' );
			if ( firstDot == -1 ) {
				if ( isEntityAlias(aliasPath) ) {
					// it is a simple table alias {foo}
					result.append(aliasPath);
					aliasesFound++;
				} 
				else {
					// passing through anything we do not know to support jdbc escape sequences HB-898
					result.append( '{' ).append(aliasPath).append( '}' );					
				}
			}
			else {
				String aliasName = aliasPath.substring(0, firstDot);
				int collectionIndex = Arrays.binarySearch(collectionAliases, aliasName);
				boolean isCollection = collectionIndex>-1;
				boolean isEntity = isEntityAlias(aliasName);
				
				if (isCollection) {
					// The current alias is referencing the collection to be eagerly fetched
					String propertyName = aliasPath.substring( firstDot + 1 );
					result.append(
							resolveCollectionProperties(
									aliasName,
							        propertyName,
							        getPropertyResultByResultAlias(aliasName),
							        getPersisterByResultAlias(aliasName),
							        collectionPersisters[collectionIndex],
							        collectionSuffixes[collectionIndex],
							        suffixes[getPersisterIndex(aliasName)]
							)
					);
					aliasesFound++;
				} 
				else if (isEntity) {
					// it is a property reference {foo.bar}
					String propertyName = aliasPath.substring( firstDot + 1 );
					result.append(
							resolveProperties(
									aliasName,
							        propertyName,
							        getPropertyResultByResultAlias(aliasName),
							        getPersisterByResultAlias(aliasName),
							        suffixes[getPersisterIndex(aliasName)] // TODO: guard getPersisterIndex
							)
					);
					aliasesFound++;
				}
				
				if ( !isEntity && !isCollection ) {
					// passing through anything we do not know to support jdbc escape sequences HB-898
					result.append( '{' ).append(aliasPath).append( '}' );
				}
	
			}
		}

		// Possibly handle :something parameters for the query ?

		return result.toString();
	}	

	private String resolveCollectionProperties(
			String aliasName,
			String propertyName,
			Map fieldResults,
	        SQLLoadable elementPersister,
	        SQLLoadableCollection currentPersister,
	        String suffix,
	        String persisterSuffix) {
		
		if ( "*".equals( propertyName ) ) {
			if( !fieldResults.isEmpty() ) {
				throw new QueryException("Using return-propertys together with * syntax is not supported.");
			}
			
			String selectFragment = currentPersister.selectFragment( aliasName, suffix );
			aliasesFound++;
			return selectFragment 
						+ ", " 
						+ resolveProperties(aliasName, propertyName, fieldResults, elementPersister, persisterSuffix );
		}
		else if ( "element.*".equals( propertyName ) ) {
			return resolveProperties(aliasName, "*", fieldResults, elementPersister, persisterSuffix);
			
		}
		else {
					
			String[] columnAliases;

			// Let return-propertys override whatever the persister has for aliases.
			columnAliases = (String[]) fieldResults.get(propertyName);
			if(columnAliases==null) {
				columnAliases = currentPersister.getCollectionPropertyColumnAliases( propertyName, suffix );				
			}
			
			if ( columnAliases == null || columnAliases.length == 0 ) {
				throw new QueryException( "No column name found for property [" +
						propertyName +
						"] for alias [" + aliasName + "]",
						sqlQuery );
			}
			if ( columnAliases.length != 1 ) {
				// TODO: better error message since we actually support composites if names are explicitly listed.
				throw new QueryException( "SQL queries only support properties mapped to a single column - property [" +
						propertyName +
						"] is mapped to " +
						columnAliases.length +
						" columns.",
						sqlQuery );
			}
			aliasesFound++;
			return columnAliases[0];
		
		}
	}
	private String resolveProperties(
			String aliasName,
	        String propertyName,
	        Map fieldResults,
	        SQLLoadable currentPersister,
	        String suffix) {
		/*int currentPersisterIndex = getPersisterIndex( aliasName );

		if ( !aliasName.equals( aliases[currentPersisterIndex] ) ) {
			throw new QueryException( "Alias [" +
					aliasName +
					"] does not correspond to return alias " +
					aliases[currentPersisterIndex],
					sqlQuery );
		}*/

		if ( "*".equals( propertyName ) ) {
			if( !fieldResults.isEmpty() ) {
				throw new QueryException("Using return-propertys together with * syntax is not supported.");
			}			
			aliasesFound++;
			return currentPersister.selectFragment( aliasName, suffix ) ;
		}
		else {

			String[] columnAliases;

			// Let return-propertys override whatever the persister has for aliases.
			columnAliases = (String[]) fieldResults.get(propertyName);
			if(columnAliases==null) {
				columnAliases = currentPersister.getSubclassPropertyColumnAliases( propertyName, suffix );
			}

			if ( columnAliases == null || columnAliases.length == 0 ) {
				throw new QueryException( "No column name found for property [" +
						propertyName +
						"] for alias [" + aliasName + "]",
						sqlQuery );
			}
			if ( columnAliases.length != 1 ) {
				// TODO: better error message since we actually support composites if names are explicitly listed.
				throw new QueryException( "SQL queries only support properties mapped to a single column - property [" +
						propertyName +
						"] is mapped to " +
						columnAliases.length +
						" columns.",
						sqlQuery );
			}			
			aliasesFound++;
			return columnAliases[0];
		}
	}

	/**
	 * Substitues JDBC parameter placeholders (?) for all encountered
	 * parameter specifications.  It also tracks the positions of these
	 * parameter specifications within the query string.  This accounts for
	 * ordinal-params, named-params, and ejb3-positional-params.
	 *
	 * @param sqlString The query string.
	 * @return The SQL query with parameter substitution complete.
	 */
	private String substituteParams(String sqlString) {
		ParameterSubstitutionRecognizer recognizer = new ParameterSubstitutionRecognizer();
		ParameterParser.parse( sqlString, recognizer );

		namedParameters.clear();
		namedParameters.putAll( recognizer.namedParameterBindPoints );

		return recognizer.result.toString();
	}

	public static class ParameterSubstitutionRecognizer implements ParameterParser.Recognizer {
		StringBuffer result = new StringBuffer();
		Map namedParameterBindPoints = new HashMap();
		int parameterCount = 0;

		public void outParameter(int position) {
			result.append( '?' );
		}

		public void ordinalParameter(int position) {
			result.append( '?' );
		}

		public void namedParameter(String name, int position) {
			addNamedParameter( name );
			result.append( '?' );
		}

		public void ejb3PositionalParameter(String name, int position) {
			namedParameter( name, position );
		}

		public void other(char character) {
			result.append( character );
		}

		private void addNamedParameter(String name) {
			Integer loc = new Integer( parameterCount++ );
			Object o = namedParameterBindPoints.get( name );
			if ( o == null ) {
				namedParameterBindPoints.put( name, loc );
			}
			else if ( o instanceof Integer ) {
				ArrayList list = new ArrayList( 4 );
				list.add( o );
				list.add( loc );
				namedParameterBindPoints.put( name, list );
			}
			else {
				( ( List ) o ).add( loc );
			}
		}
	}
}

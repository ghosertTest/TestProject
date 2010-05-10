//$Id: SQLCustomQuery.java,v 1.17 2005/10/14 22:43:44 maxcsaucdk Exp $
package org.hibernate.loader.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.loader.BasicLoader;
import org.hibernate.loader.DefaultEntityAliases;
import org.hibernate.loader.EntityAliases;
import org.hibernate.loader.ColumnEntityAliases;
import org.hibernate.loader.CollectionAliases;
import org.hibernate.loader.GeneratedCollectionAliases;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.collection.SQLLoadableCollection;
import org.hibernate.persister.entity.SQLLoadable;
import org.hibernate.type.Type;
import org.hibernate.util.ArrayHelper;
import org.hibernate.util.StringHelper;

/**
 * Implements Hibernate's built-in support for
 * native SQL queries.
 * 
 * @author Gavin King, Max Andersen
 */
public class SQLCustomQuery implements CustomQuery {
	
	private final String[] entityNames;
	private final String[] collectionRoles;
	private final int[] collectionOwners;
	private final int[] entityOwners;
	private final LockMode[] lockModes;
	private final String sql;
	private final Set querySpaces = new HashSet();
	private final Map namedParameters;
	private final Type[] scalarTypes;
	private final String[] scalarColumnAliases;
	private final EntityAliases[] entityDescriptors;
	private final CollectionAliases[] collectionDescriptors;

	public String getSQL() {
		return sql;
	}
	
	public Map getNamedParameterBindPoints() {
		return namedParameters;
	}
	
	public String[] getCollectionRoles() {
		return collectionRoles;
	}

	public String[] getEntityNames() {
		return entityNames;
	}

	public LockMode[] getLockModes() {
		return lockModes;
	}

	public EntityAliases[] getEntityAliases() {
		return entityDescriptors;
	}

	public CollectionAliases[] getCollectionAliases() {
		return collectionDescriptors;
	}

	public Set getQuerySpaces() {
		return querySpaces;
	}

	public int[] getCollectionOwner() {
		return collectionOwners;
	}

	public int[] getEntityOwners() {
		return entityOwners;
	}
	
	public String[] getScalarColumnAliases() {
		return scalarColumnAliases;
	}
	
	public Type[] getScalarTypes() {
		return scalarTypes;
	}
	
	public SQLCustomQuery(
			final SQLQueryReturn[] queryReturns,
			final SQLQueryScalarReturn[] scalarQueryReturns,
			final String sqlQuery,
			final Collection additionalQuerySpaces,
			final SessionFactoryImplementor factory)
	throws HibernateException {

		SQLQueryReturnProcessor processor = new SQLQueryReturnProcessor(queryReturns, scalarQueryReturns, factory);
		processor.process();
		
		Map[] propertyResultMaps =  (Map[]) processor.getPropertyResults().toArray( new Map[0] );
		Map[] collectionResultMaps =  (Map[]) processor.getCollectionPropertyResults().toArray( new Map[0] );
		
		List collectionSuffixes = new ArrayList();
		List collectionOwnerAliases = processor.getCollectionOwnerAliases();
		List collectionPersisters = processor.getCollectionPersisters();
		int size = collectionPersisters.size();
		if (size!=0) {
			collectionOwners = new int[size];
			collectionRoles = new String[size];
			//collectionDescriptors = new CollectionAliases[size];
			for ( int i=0; i<size; i++ ) {
				CollectionPersister collectionPersister = (CollectionPersister) collectionPersisters.get(i);
				collectionRoles[i] = ( collectionPersister ).getRole();
				collectionOwners[i] = processor.getAliases().indexOf( collectionOwnerAliases.get(i) );
				String suffix = i + "__";
				collectionSuffixes.add(suffix);
				//collectionDescriptors[i] = new GeneratedCollectionAliases( collectionResultMaps[i], collectionPersister, suffix );
			}
		}
		else {
			collectionRoles = null;
			//collectionDescriptors = null;
			collectionOwners = null;
		}

		String[] aliases = ArrayHelper.toStringArray( processor.getAliases() );
		String[] collAliases = ArrayHelper.toStringArray( processor.getCollectionAliases() );
		String[] collSuffixes = ArrayHelper.toStringArray(collectionSuffixes);
		
		SQLLoadable[] entityPersisters = (SQLLoadable[]) processor.getPersisters().toArray( new SQLLoadable[0] );
		SQLLoadableCollection[] collPersisters = (SQLLoadableCollection[]) collectionPersisters.toArray( new SQLLoadableCollection[0] );
        lockModes = (LockMode[]) processor.getLockModes().toArray( new LockMode[0] );

        scalarColumnAliases = ArrayHelper.toStringArray( processor.getScalarColumnAliases() );
		scalarTypes = ArrayHelper.toTypeArray( processor.getScalarTypes() );

		String[] suffixes = BasicLoader.generateSuffixes(entityPersisters.length);

		SQLQueryParser parser = new SQLQueryParser(
				sqlQuery,
				processor.getAlias2Persister(),
				processor.getAlias2Return(),
				aliases,
				collAliases,
				collPersisters,
				suffixes,
				collSuffixes
		);

		sql = parser.process();
		
		namedParameters = parser.getNamedParameters();

		// Populate entityNames, entityDescrptors and querySpaces
		entityNames = new String[entityPersisters.length];
		entityDescriptors = new EntityAliases[entityPersisters.length];
		for (int i = 0; i < entityPersisters.length; i++) {
			SQLLoadable persister = entityPersisters[i];
			//alias2Persister.put( aliases[i], persister );
			//TODO: Does not consider any other tables referenced in the query
			ArrayHelper.addAll( querySpaces, persister.getQuerySpaces() );
			entityNames[i] = persister.getEntityName();
			if ( parser.queryHasAliases() ) {
				entityDescriptors[i] = new DefaultEntityAliases( 
						propertyResultMaps[i], 
						entityPersisters[i], 
						suffixes[i] 
					);
			} 
			else {
				entityDescriptors[i] = new ColumnEntityAliases( 
						propertyResultMaps[i], 
						entityPersisters[i], 
						suffixes[i] 
					);
			}
		}
		if (additionalQuerySpaces!=null) {
			querySpaces.addAll(additionalQuerySpaces);
		}
				
		if (size!=0) {
			collectionDescriptors = new CollectionAliases[size];
			for ( int i=0; i<size; i++ ) {
				CollectionPersister collectionPersister = (CollectionPersister) collectionPersisters.get(i);
				String suffix = i + "__";
				if( parser.queryHasAliases() ) {
					collectionDescriptors[i] = new GeneratedCollectionAliases( collectionResultMaps[i], collectionPersister, suffix );
				} else {
					collectionDescriptors[i] = new ColumnCollectionAliases( collectionResultMaps[i], (SQLLoadableCollection) collectionPersister );
				}
			}
		}
		else {
			collectionDescriptors = null;			
		}


		// Resolve owners
		Map alias2OwnerAlias = processor.getAlias2OwnerAlias();
		int[] ownersArray = new int[entityPersisters.length];
		for ( int j=0; j < aliases.length; j++ ) {
			String ownerAlias = (String) alias2OwnerAlias.get( aliases[j] );
			if ( StringHelper.isNotEmpty(ownerAlias) ) {
				ownersArray[j] =  processor.getAliases().indexOf( ownerAlias );
			}
			else {
				ownersArray[j] = -1;
			}
		}
		if ( ArrayHelper.isAllNegative(ownersArray) ) {
			ownersArray = null;
		}
		this.entityOwners = ownersArray;

	}

}

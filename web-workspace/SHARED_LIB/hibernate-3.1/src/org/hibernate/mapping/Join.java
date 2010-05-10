//$Id: Join.java,v 1.11 2005/02/12 07:19:26 steveebersole Exp $
package org.hibernate.mapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.sql.Alias;

/**
 * @author Gavin King
 */
public class Join implements Serializable {

	private static final Alias PK_ALIAS = new Alias(15, "PK");

	private ArrayList properties = new ArrayList();
	private Table table;
	private KeyValue key;
	private PersistentClass persistentClass;
	private boolean sequentialSelect;
	private boolean inverse;
	private boolean optional;

	// Custom SQL
	private String customSQLInsert;
	private String customSQLUpdate;
	private String customSQLDelete;
	private boolean customInsertCallable;
	private boolean customUpdateCallable;
	private boolean customDeleteCallable;

	public void addProperty(Property prop) {
		properties.add(prop);
		prop.setPersistentClass( getPersistentClass() );
	}
	public boolean containsProperty(Property prop) {
		return properties.contains(prop);
	}
	public Iterator getPropertyIterator() {
		return properties.iterator();
	}

	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}

	public KeyValue getKey() {
		return key;
	}
	public void setKey(KeyValue key) {
		this.key = key;
	}

	public PersistentClass getPersistentClass() {
		return persistentClass;
	}

	public void setPersistentClass(PersistentClass persistentClass) {
		this.persistentClass = persistentClass;
	}

	public void createForeignKey() {
		getKey().createForeignKeyOfEntity( persistentClass.getEntityName() );
	}

	public void createPrimaryKey() {
		//Primary key constraint
		PrimaryKey pk = new PrimaryKey();
		pk.setTable(table);
		pk.setName( PK_ALIAS.toAliasString( table.getName() ) );
		table.setPrimaryKey(pk);

		pk.addColumns( getKey().getColumnIterator() );
	}

	public int getPropertySpan() {
		return properties.size();
	}

	public String getCustomSQLDelete() {
		return customSQLDelete;
	}

	public void setCustomSQLDelete(String customSQLDelete, boolean callable) {
		this.customSQLDelete = customSQLDelete;
		this.customDeleteCallable = callable;
	}

	public String getCustomSQLInsert() {
		return customSQLInsert;
	}

	public void setCustomSQLInsert(String customSQLInsert, boolean callable) {
		this.customSQLInsert = customSQLInsert;
		this.customInsertCallable = callable;
	}

	public String getCustomSQLUpdate() {
		return customSQLUpdate;
	}

	public void setCustomSQLUpdate(String customSQLUpdate, boolean callable) {
		this.customSQLUpdate = customSQLUpdate;
		this.customUpdateCallable = callable;
	}

	public boolean isCustomDeleteCallable() {
		return customDeleteCallable;
	}

	public boolean isCustomInsertCallable() {
		return customInsertCallable;
	}

	public boolean isCustomUpdateCallable() {
		return customUpdateCallable;
	}
	public boolean isSequentialSelect() {
		return sequentialSelect;
	}
	public void setSequentialSelect(boolean deferred) {
		this.sequentialSelect = deferred;
	}

	public boolean isInverse() {
		return inverse;
	}
	
	public void setInverse(boolean leftJoin) {
		this.inverse = leftJoin;
	}

	public String toString() {
		return getClass().getName() + '(' + table.toString() + ')';
	}
	
	public boolean isLazy() {
		Iterator iter = getPropertyIterator();
		while ( iter.hasNext() ) {
			Property prop = (Property) iter.next();
			if ( !prop.isLazy() ) return false;
		}
		return true;
	}
	
	public boolean isOptional() {
		return optional;
	}
	public void setOptional(boolean nullable) {
		this.optional = nullable;
	}
}

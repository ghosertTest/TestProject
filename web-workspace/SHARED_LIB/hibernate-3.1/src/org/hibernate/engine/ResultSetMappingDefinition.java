//$Id: ResultSetMappingDefinition.java,v 1.1 2005/06/23 14:59:53 oneovthafew Exp $
package org.hibernate.engine;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

import org.hibernate.loader.custom.SQLQueryReturn;
import org.hibernate.loader.custom.SQLQueryScalarReturn;

/**
 * Keep a description of the resultset mapping
 *
 * @author Emmanuel Bernard
 */
public class ResultSetMappingDefinition implements Serializable {
	/** List<SQLQueryReturn> */
	private List entityQueryReturns = new ArrayList();
	/** List<SQLQueryScalarReturn> */
	private List scalarQueryReturns = new ArrayList();
	private String name;

	public String getName() {
		return name;
	}

	public ResultSetMappingDefinition(String name) {
		this.name = name;
	}

	public void addEntityQueryReturn(SQLQueryReturn entityQueryReturn) {
		entityQueryReturns.add(entityQueryReturn);
	}

	public void addScalarQueryReturn(SQLQueryScalarReturn scalarQueryReturn) {
		scalarQueryReturns.add(scalarQueryReturn);
	}

	public SQLQueryReturn[] getEntityQueryReturns() {
		return (SQLQueryReturn[]) entityQueryReturns.toArray( new SQLQueryReturn[0] );
	}

	public SQLQueryScalarReturn[] getScalarQueryReturns() {
		return (SQLQueryScalarReturn[]) scalarQueryReturns.toArray( new SQLQueryScalarReturn[0] );
	}
}

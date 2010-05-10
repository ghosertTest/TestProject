//$Id: Formula.java,v 1.9 2005/07/19 18:28:35 maxcsaucdk Exp $
package org.hibernate.mapping;

import java.io.Serializable;

import org.hibernate.dialect.Dialect;
import org.hibernate.sql.Template;

/**
 * A formula is a derived column value
 * @author Gavin King
 */
public class Formula implements Selectable, Serializable {
	private static int formulaUniqueInteger=0;

	private String formula;
	private int uniqueInteger;

	public Formula() {
		uniqueInteger = formulaUniqueInteger++;
	}

	public String getTemplate(Dialect dialect) {
		return Template.renderWhereStringTemplate(formula, dialect);
	}
	public String getText(Dialect dialect) {
		return getFormula();
	}
	public String getText() {
		return getFormula();
	}
	public String getAlias(Dialect dialect) {
		return "formula" + Integer.toString(uniqueInteger) + '_';
	}
	public String getAlias(Dialect dialect, Table table) {
		return getAlias(dialect);
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String string) {
		formula = string;
	}
	public boolean isFormula() {
		return true;
	}

	public String toString() {
		return this.getClass().getName() + "( " + formula + " )";
	}
}

//$Id: Selectable.java,v 1.3 2005/07/19 18:28:35 maxcsaucdk Exp $
package org.hibernate.mapping;

import org.hibernate.dialect.Dialect;

public interface Selectable {
	public String getAlias(Dialect dialect);
	public String getAlias(Dialect dialect, Table table);
	public boolean isFormula();
	public String getTemplate(Dialect dialect);
	public String getText(Dialect dialect);
	public String getText();
}

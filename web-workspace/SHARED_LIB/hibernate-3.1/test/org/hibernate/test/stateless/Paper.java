//$Id: Paper.java,v 1.1 2005/08/30 21:27:17 epbernard Exp $
package org.hibernate.test.stateless;

/**
 * @author Emmanuel Bernard
 */
public class Paper {
	private Integer id;
	private String color;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}

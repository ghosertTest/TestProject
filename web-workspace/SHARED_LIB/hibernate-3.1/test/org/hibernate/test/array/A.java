//$Id: A.java,v 1.1 2005/04/26 16:58:52 oneovthafew Exp $
package org.hibernate.test.array;

/**
 * @author Emmanuel Bernard
 */
public class A {
	private Integer id;
	private B[] bs;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public B[] getBs() {
		return bs;
	}

	public void setBs(B[] bs) {
		this.bs = bs;
	}
}

//$Id: PublicationDate.java,v 1.1 2005/07/19 23:22:27 oneovthafew Exp $
package org.hibernate.test.optlock;

public class PublicationDate {
	private int year;
	private Integer month;
	
	public PublicationDate(int year) {
		this.year = year;
	}
	
	PublicationDate() {}
	
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
}

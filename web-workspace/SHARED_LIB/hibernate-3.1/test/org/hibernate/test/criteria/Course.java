//$Id: Course.java,v 1.2 2005/02/12 07:27:21 steveebersole Exp $
package org.hibernate.test.criteria;

/**
 * @author Gavin King
 */
public class Course {
	private String courseCode;
	private String description;
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}

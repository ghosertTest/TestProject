//$Id: Search.java,v 1.1 2005/08/05 23:03:46 oneovthafew Exp $
package org.hibernate.test.sorted;

import java.util.SortedSet;
import java.util.TreeSet;

public class Search {
	private String searchString;
	private SortedSet searchResults = new TreeSet();
	
	Search() {}
	
	public Search(String string) {
		searchString = string;
	}
	
	public SortedSet getSearchResults() {
		return searchResults;
	}
	public void setSearchResults(SortedSet searchResults) {
		this.searchResults = searchResults;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
}

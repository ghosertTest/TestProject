/*
 * Created on 2005-1-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package assis;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import java.util.*;

public class ShowPages {
	private int totalRecorders;

	private int recordersPerPage;

	private int availablePages;

	private int currentPage;

	private ArrayList content;
	
/**
 * @param totalRecorders the number(>=0) indicate how many recorders that retrieve from database or anywhere else
 * @param recordersPerPage the number(>0) indicate how many recorders per page will show 
 * @param availablePages the number(>0) indicate how many pages can be "clicked" one time
 * @param currentPage the number(>0&<=totalPages) indicate the page that is showing now
 * @param content contains the data of recorders per page that will be shown
 */
	// ShowPages.class is a JavaBean if have a constructor without parameter.
	public ShowPages() {
	}

	/**
	 * @param totalRecorders the number(>=0) indicate how many recorders that retrieve from database or anywhere else
	 */
	public void setTotalRecorders(int totalRecorders) {
		if (totalRecorders < 0) {
			this.totalRecorders = 0;
		} else {
			this.totalRecorders = totalRecorders;
		}
	}
	
	/**
	 * @param recordersPerPage the number(>0) indicate how many recorders per page will show
	 */
	public void setRecordersPerPage(int recordersPerPage) {
		if (recordersPerPage <= 0) {
			this.recordersPerPage = 1;
		} else {
			this.recordersPerPage = recordersPerPage;
		}
	}
	
	/**
	 * @param availablePages the number(>0) indicate how many pages can be "clicked" one time
	 */
	public void setAvailablePages(int availablePages) {
		if (availablePages <= 0) {
			this.availablePages = 1;
		} else {
			this.availablePages = availablePages;
		}
	}
	
	/**
	 * @param currentPage the number(>0&<=totalPages) indicate the page that is showing now
	 */
	public void setCurrentPage(int currentPage) {
		if (currentPage > getTotalPages()) {
			this.currentPage = getTotalPages();
		} else if (currentPage <= 0) {
			this.currentPage = 1;
		} else {
			this.currentPage = currentPage;
		}
	}
	
	/**
	 * @param content contains the data of recorders per page that will be shown,
	 * it means if recordersPerPage = 10, you should allocate the number 10 to the array Object[], it will be Object[10]
	 */
	public void setContent(ArrayList content) {
		this.content = content;
	}
	
	/**
	 * @return totalRecorders
	 */
	public int getTotalRecorders() {
		return totalRecorders;
	}
	
	/**
	 * @return recordersPerPage
	 */
	public int getRecordersPerPage() {
		return recordersPerPage;
	}
	
	/**
	 * @return total pages, it is decided by totalRecorders and recordersPerPage
	 */
	public int getTotalPages() {
		int totalPages = 0;
		if (totalRecorders == 0 ) return 1;
		if (totalRecorders%recordersPerPage == 0 ) {
			totalPages = totalRecorders/recordersPerPage;
		} else {
		    totalPages = totalRecorders/recordersPerPage+1;
		}
		return totalPages;
	}
	
	/**
	 * @return availablePages
	 */
	public int getAvailablePages() {
		return availablePages;
	}
	
	/**
	 * @return get minimal number of the page can be "clicked"
	 */
	public int getMinAvailablePage() {
		int minAvailablePage = 0;
		if (currentPage%availablePages ==0 ) {
			minAvailablePage = currentPage - availablePages + 1;
		} else {
		    minAvailablePage = currentPage/availablePages*availablePages+1;
		}
		return minAvailablePage;
	}
	
	/**
	 * @return get maximal number of the page can be "clicked"
	 */
	public int getMaxAvailablePage() {
		int maxAvailablePage = 0;
		if (getMinAvailablePage() + availablePages - 1 > getTotalPages()) {
			maxAvailablePage = getTotalPages();
		} else {
			maxAvailablePage = getMinAvailablePage() + availablePages - 1;
		}
		return maxAvailablePage;
	}
	
	/**
	 * @return currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}
	
	/**
	 * @return the number indicate the next page will be shown
	 */
	public int getNextPage() {
		if (currentPage == getTotalPages()) {
			return currentPage;
		} else {
		    return currentPage+1;
		}
	}
	
	/**
	 * @return the number indicate the page before current page
	 */
	public int getPrePage() {
		if (currentPage == 1) {
			return currentPage;
		} else {
		    return currentPage-1;
		}
	}
	
	/**
	 * @return content
	 */
	public ArrayList getContent() {
		return content;
	}
	
}
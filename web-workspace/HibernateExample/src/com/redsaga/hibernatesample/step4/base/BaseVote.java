package com.redsaga.hibernatesample.step4.base;

import java.io.Serializable;


/**
 * This class has been automatically generated by Hibernate Synchronizer.
 * For more information or documentation, visit The Hibernate Synchronizer page
 * at http://www.binamics.com/hibernatesync or contact Joe Hudson at joe@binamics.com.
 *
 * This is an object that contains data related to the vote table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="vote"
 */
public abstract class BaseVote  extends com.redsaga.hibernatesample.step4.Article  implements Serializable {

	public static String PROP_ID = "id";
	public static String PROP_COUNT = "count";


	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer _id;

	// fields
	private java.lang.Integer _count;

	// collections
	private java.util.Set _optionSet;


	// constructors
	public BaseVote () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseVote (java.lang.Integer _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public BaseVote (
		java.lang.Integer _id,
		com.redsaga.hibernatesample.step4.User _createBy,
		com.redsaga.hibernatesample.step4.Article _parent,
		com.redsaga.hibernatesample.step4.Board _board,
		com.redsaga.hibernatesample.step4.User _lastUpdateBy,
		java.util.Date _lastUpdateTime,
		java.util.Date _createTime,
		java.lang.String _title,
		int _hits,
		java.lang.Integer _articleType) {

		super (
			_id,
			_createBy,
			_parent,
			_board,
			_lastUpdateBy,
			_lastUpdateTime,
			_createTime,
			_title,
			_hits,
			_articleType);
	}



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="increment"
     *  column="id"
     */
	public java.lang.Integer getId () {
		return _id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param _id the new ID
	 */
	public void setId (java.lang.Integer _id) {
		this._id = _id;
		this.hashCode = Integer.MIN_VALUE;
	}


	/**
	 * Return the value associated with the column: count
	 */
	public java.lang.Integer getCount () {
		return _count;
	}

	/**
	 * Set the value related to the column: count
	 * @param _count the count value
	 */
	public void setCount (java.lang.Integer _count) {
		this._count = _count;
	}


	/**
	 * Return the value associated with the column: optionSet
	 */
	public java.util.Set getOptionSet () {
		return this._optionSet;
	}

	/**
	 * Set the value related to the column: optionSet
	 * @param _optionSet the optionSet value
	 */
	public void setOptionSet (java.util.Set _optionSet) {
		this._optionSet = _optionSet;
	}
	
	public void addToOptionSet (Object obj) {
		if (null == this._optionSet) this._optionSet = new java.util.HashSet();
		this._optionSet.add(obj);
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.redsaga.hibernatesample.step4.base.BaseVote)) return false;
		else {
			com.redsaga.hibernatesample.step4.base.BaseVote mObj = (com.redsaga.hibernatesample.step4.base.BaseVote) obj;
			if (null == this.getId() || null == mObj.getId()) return false;
			else return (this.getId().equals(mObj.getId()));
		}
	}


	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


	public String toString () {
		return super.toString();
	}

}
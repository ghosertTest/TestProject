package com.loadtrend.bbs.po;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

public class Note implements Serializable
{
	private String id = null;
	
	/**
	 * 0: not solved. 1: solved.
	 */
	private Integer status = null;
	
	private String title = null;
	
	private String content = null;
	
	private Timestamp postTime = null;
	
	private String author = null;
	
	private String email = null;
	
	private Set replyNotes = null;

	/**
	 * @return Returns the author.
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author The author to set.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }
    
	/**
     * @return Returns the postTime.
     */
    public Timestamp getPostTime() {
        return postTime;
    }

    /**
     * @param postTime The postTime to set.
     */
    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }

    /**
	 * @return Returns the status.
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the replyNotes.
	 */
	public Set getReplyNotes() {
		return replyNotes;
	}

	/**
	 * @param replyNotes The replyNotes to set.
	 */
	public void setReplyNotes(Set replyNotes) {
		this.replyNotes = replyNotes;
	}
}

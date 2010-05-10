package com.loadtrend.bbs.info;

public class ReplyNoteInfo
{
	private String id = null;
	
	private String content = null;
	
	private String postTime = null;
	
	private String author = null;
	
	private String email = null;

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
    public String getPostTime() {
        return postTime;
    }

    /**
     * @param postTime The postTime to set.
     */
    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }
	
}

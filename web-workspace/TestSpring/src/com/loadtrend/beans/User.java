package com.loadtrend.beans;

public class User {
	
	private String username = null;
    
    private String password = null;
    
    private Integer id = null;

    /**
     * @return Returns the id.
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId( Integer id )
    {
        this.id = id;
    }

    /**
     * @return Returns the password.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @param password The password to set.
     */
    public void setPassword( String password )
    {
        this.password = password;
    }

    /**
     * @return Returns the username.
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @param username The username to set.
     */
    public void setUsername( String username )
    {
        this.username = username;
    }
}

/*
 * Created on 2005-2-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package src;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class UserData
{
	private String userId;

	private String username;

	private String password;

	private String name;

	private String email;

	private String mobile;

	private String registerTime;

	private String ip;
	
	public UserData()
	{
	    
	}
	
	public UserData( String userId,
	                 String username,
	                 String password,
	                 String name,
	                 String email,
	                 String mobile,
	                 String registerTime,
	                 String ip )
	{
	    this.userId = userId;
	    this.username = username;
	    this.password = password;
	    this.name = name;
	    this.email = email;
	    this.mobile = mobile;
	    this.registerTime = registerTime;
	    this.ip = ip;
	}

	public String getUserId() {

		return userId;
	}

	public String getUsername() {

		return username;
	}

	public String getPassword() {

		return password;
	}

	public String getName() {

		return name;
	}

	public String getEmail() {

		return email;
	}

	public String getMobile() {

		return mobile;
	}

	public String getRegisterTime() {

		return registerTime;
	}

	public String getIp() {

		return ip;
	}

}

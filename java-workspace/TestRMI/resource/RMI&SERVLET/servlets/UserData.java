// ==================================
// AUTHOR: Scott McPherson
// EMAIL:  scottm@mochamail.com
// DATE:   October 1, 1999
// Copyright (C) 1999 Scott McPherson
// ==================================

/**
* This class defines a simple data object for use
* with the sample database application.
*/
public class UserData implements java.io.Serializable 
{
	String id;
	String name;
	String dept;
	String phone;
	String comment;

	public UserData(String i, String n, String d, String p, String c) 
	{
		id = i;
		name = n;
		dept = d;
		phone = p;
		comment = c;
	}

	public UserData() 
	{
		this("", "", "", "", "");
	}

	public String getId()       { return id;        }
	public String getName()     { return name;      }
	public String getDept()     { return dept;      }
	public String getPhone()    { return phone;     }
	public String getComment()  { return comment;   }

	public void setId(String i)     { id = i;       }
	public void setName(String n)   { name = n;     }
	public void setDept(String d)   { dept = d;     }
	public void setPhone(String p)  { phone = p;    }
	public void setComment(String c){ comment = c;  }

}
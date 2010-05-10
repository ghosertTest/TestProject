package com.loadtrend.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.loadtrend.beans.User;

public class UserDAO
{
	private DataSource dataSource = null;
    
    private JdbcTemplate jdbcTemplate = null;
	
	public DataSource executeTestSource()
	{
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void insertUser()
	{
        jdbcTemplate.update( "insert into users (username) values ('xiaxin');" );
        jdbcTemplate.update( "insert into users (username) values ('erica');" );
        
        // avoid SQL injection.
        jdbcTemplate.update( "UPDATE users SET password = ? WHERE username = ?",
                             new PreparedStatementSetter()
                             {
                                public void setValues( PreparedStatement ps ) throws SQLException
                                {
                                    ps.setString( 1, "123456" );
                                    ps.setString( 2, "erica" );
                                }
                             }
        );
	}
    
    public List getUserList()
    {
        final List userList = new ArrayList();
        jdbcTemplate.query( "SELECT id, username, password FROM users WHERE id > 0",
        new RowCallbackHandler()
        {
            public void processRow( ResultSet rs ) throws SQLException
            {
                User user = new User();
                user.setId( new Integer( rs.getInt( "id" ) ) );
                user.setUsername( rs.getString( "username" ) );
                user.setPassword( rs.getString( "password" ) );
                userList.add(user);
            }
        }
        );
        return userList;
    }
}



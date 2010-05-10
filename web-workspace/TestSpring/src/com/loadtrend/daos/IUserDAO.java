package com.loadtrend.daos;

import java.util.List;

import com.loadtrend.beans.User;

public interface IUserDAO
{
    public void insertUser( User user );
    
    public List getUsers();
}

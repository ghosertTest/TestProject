package com.packtpub.t5first.pages;

import org.apache.tapestry.annotations.ApplicationState;

import com.packtpub.t5first.model.User;
import com.packtpub.t5first.util.Security;

public class Start2 {
    private String userName;

    private String password;

    @ApplicationState
    private User user;
    

    /**
     * Validate the input and then redirect to the ShowAll/Registration page due to whether the validation is successful or not.
     * @return
     */
    Object onSubmitFromLoginForm() {
        Class nextPage = null;
        User authenticatedUser = null;
        authenticatedUser = Security.authenticate(userName, password);
        if (authenticatedUser != null) {
            user = authenticatedUser;
            nextPage = ShowAll.class;
        } else {
            nextPage = Registration.class;
        }
        return nextPage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
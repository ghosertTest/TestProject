package com.packtpub.t5first.pages;

import org.apache.tapestry.annotations.ApplicationState;

import com.packtpub.t5first.util.User;

// import org.apache.tapestry.annotations.Persist;

/*
 * Created on Mar 13, 2009
 */

/**
 * @author jiawzhang
 */

public class Another {
    /**
     * Use @Persist to persist the value after setting it so that we can re-get it later.
     * Here, the passedMessage is set in the Start page and value is persisted, then we can re-get it when we render Another page
     * by getPassedMessage method.
     */
    // @Persist
    private String passedMessage;
    
    @ApplicationState
    private User myUser;
    
    public User getMyuser() {
        return this.myUser;
    }

    public String getPassedMessage() {
        return passedMessage;
    }

    public void setPassedMessage(String passedMessage) {
        this.passedMessage = passedMessage;
    }
    
    /**
     * Use page activation context to persist the value instead of @Persist above, since the latter 1. occupy the memory 2. can't bookmark.
     * This method will be invoked if only user request this page and onPassivate() below has been invoked before, that is, there is a url.
     * @param message
     */
    void onActivate(String message) {
        System.out.println("Another page is activated! The message is: "
                + message);
        this.passedMessage = message;
    }

    /**
     * This method will be invoked once tapestry return this page instance to the memory pool
     * to passivate the parameter passedMessage below as url.
     * That will happen after invoking Start.onSubmitFromUserInputForm()
     * @return
     */
    String onPassivate() {
        System.out.println("Another page is passivated...");
        return passedMessage;
    }
}

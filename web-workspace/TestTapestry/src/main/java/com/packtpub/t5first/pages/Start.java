package com.packtpub.t5first.pages;

import java.util.Date;

import org.apache.tapestry.annotations.ApplicationState;
import org.apache.tapestry.annotations.InjectPage;

import com.packtpub.t5first.util.User;

// import org.apache.tapestry.annotations.OnEvent;

/**
 * Start page of application t5first.
 */
public class Start {
    private int someValue = 12345;

    private String message = "initial value";
    
    @InjectPage
    private Another another;
    
    /**
     * In Tapestry, an object that is made available for every page of the application is termed Application State Object (ASO).
     * Whatever the property name is user or not if only it is an instance of Class, they are the same instance in the different pages.
     * See myUser in Another page.
     */
    @ApplicationState
    private User user;
    
    /**
     * Use this property to check whether ASO User is instanced or not.
     * "Object name" + "Exists"
     */
    private boolean userExists;

    public int getSomeValue() {
        return someValue;
    }

    public void setSomeValue(int value) {
        this.someValue = value;
    }
    
    public User getUser(){
        return user;
    }
    
    public boolean getUserExists() {
        return userExists;
    }

    // Tapestry doesn't actually care whether the private field someValue exists
    // in the page class.
    // It just needs an appropriate getter method.
    public Date getCurrentTime() {
        return new Date();
    }

    /**
     * The annotation below means, when the event "submit" happens on the component "userInputForm",
     * "onFormSubmit" event handler will be invoked. The name of the event handler could be any.
     * Notice: use "default/package" access level on this method.
     */
//    @OnEvent(value = "submit", component = "userInputForm")
//    void onFormSubmit() {
//        System.out.println("Handling form submission!");
//    }
    
    /**
     * The event handler without annotation should be named start with "on" and end with "eventName".
     * Limitation: Every forms in the page submitted will invoke this event handler.
     */
    // void onSubmit() {}
    
    /**
     * To release the limitation above use thie name conversion below:
     * "on" + "eventName" + "From" + "componentId"
     * This is the equivalent of the annotation version above, and is a preferred way in Tapestry.
     * Notice: What can be returned from an event handler?
     * Nothing(void or null means re-display the current page)/String/Class/Page/Link/Stream(StreamResponse object: a binary stream like PDF)
     */
    Object onSubmitFromUserInputForm() {
        System.out.println("Handling form submission!");
        
        // Set the application state object.
        String[] words = message.split(" ");
        if (words.length > 0) {
            user.setFirstName(words[0]);
            if (words.length > 1) {
                user.setLastName(words[1]);
            }
        }
        
        this.another.setPassedMessage(this.message);
        return this.another;
    }

    /**
     * When rendering the start page, it will invoke Start.getMessage() method.
     */
    public String getMessage() {
        return message;
    }

    /**
     * When submitting the start page, it will invoke Start.setMessage(String message) method.
     * 
     * @param message
     */
    public void setMessage(String message) {
        System.out.println("Setting the message: " + message);
        this.message = message;
    }
}
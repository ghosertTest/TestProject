package com.packtpub.t5first.pages;

import org.apache.tapestry.SelectModel;
import org.apache.tapestry.annotations.ApplicationState;
import org.apache.tapestry.annotations.OnEvent;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.ioc.Messages;
import org.apache.tapestry.ioc.annotations.Inject;
import org.apache.tapestry.util.EnumSelectModel;

import com.packtpub.t5first.model.Country;
import com.packtpub.t5first.model.Gender;
import com.packtpub.t5first.model.User;

public class Registration {

    @Persist
    private boolean subscribe;
    
    private boolean unsubscribe;
    
    private String email;

    private String userName;

    private String password;

    private String password2;

    private Gender gender = Gender.FEMALE;

    private Class nextPage;
    
    private Country country;
    
    @ApplicationState
    private User user;
    
    /**
     * This is a 'submit' event handler for the form 'RegistrationForm', it will be invoked after 'submitButton' or 'resetButton' event handlers below.
     * @return
     */
    Object onSubmitFromRegistrationForm() {
        System.out.println("The form was submitted!");
        if (unsubscribe) subscribe = false;
        return nextPage;
    }
    
    /**
     * Event handler on submitButton, after this button handler,
     * onSubmitFromRegistrationForm handler for form above will be invoked.
     * You can not return the nextPage in this method to redirect the current page to another page.
     * The nextPage should be returned in 'submit' event handler for the form above (onSubmitFromRegistrationForm).
     */
    @OnEvent(component="submitButton")
    void onSubmitButton() {
        System.out.println("Submit button was pressed!");
        User newUser = new User("John", "Johnson");
        this.user = newUser;
        nextPage = ShowAll.class;
    }
    
    /**
     * Event handler on resetButton, after this button handler,
     * onSubmitFromRegistrationForm handler for form above will be invoked.
     * You can not return the nextPage in this method to redirect the current page to another page.
     * The nextPage should be returned in 'submit' event handler for the form above (onSubmitFromRegistrationForm).
     */
    @OnEvent(component="resetButton")
    void onResetButton() {
	    subscribe = false;
        nextPage = null; // The next page is the current page, which can be different from the one in onSubmitButton() above.
                         // So That, We can have more than one submit button for different purpose.
    }
    
    /**
     * Messages here comes from the properties under WEB-INF/app.properties
     */
    @Inject
    private Messages messages;
    
    public SelectModel getCountries() {
        return new EnumSelectModel(Country.class, messages);
    }
    
    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        System.out.println("Setting subscribe: " + subscribe);
        this.subscribe = subscribe;
    }
    
    public boolean isUnsubscribe() {
        return unsubscribe;
    }
    
    public void setUnsubscribe(boolean unsubscribe) {
        this.unsubscribe = unsubscribe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        System.out.println("Setting user name: " + userName);
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        System.out.println("Setting password: " + password);
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        System.out.println("Setting gender: " + gender);
        this.gender = gender;
    }

    public Gender getMale() {
        return Gender.MALE;
    }

    public Gender getFemale() {
        return Gender.FEMALE;
    }
    
    public Country getCountry() {
        return country;
    }
    
    public void setCountry(Country country) {
        this.country = country;
    }
}
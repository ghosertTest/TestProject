package com.loadtrend.bbs.util;

import java.util.List;

public class PostMailInfo
{
    private String hostName = null;
    
    private String username = null;
    
    private String password = null;
    
    private List addressList = null;
    
    private String fromAddress = null;
    
    private String mailContentEncoding = null;
    
    private String mailTitleEncoding = null;

    /**
     * @return Returns the addressList.
     */
    public List getAddressList() {
        return addressList;
    }

    /**
     * @param addressList The addressList to set.
     */
    public void setAddressList(List addressList) {
        this.addressList = addressList;
    }

    /**
     * @return Returns the fromAddress.
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * @param fromAddress The fromAddress to set.
     */
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    /**
     * @return Returns the hostName.
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName The hostName to set.
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return Returns the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return Returns the mailContentEncoding.
     */
    public String getMailContentEncoding() {
        return mailContentEncoding;
    }

    /**
     * @param mailContentEncoding The mailContentEncoding to set.
     */
    public void setMailContentEncoding(String mailContentEncoding) {
        this.mailContentEncoding = mailContentEncoding;
    }

    /**
     * @return Returns the mailTitleEncoding.
     */
    public String getMailTitleEncoding() {
        return mailTitleEncoding;
    }

    /**
     * @param mailTitleEncoding The mailTitleEncoding to set.
     */
    public void setMailTitleEncoding(String mailTitleEncoding) {
        this.mailTitleEncoding = mailTitleEncoding;
    }
}

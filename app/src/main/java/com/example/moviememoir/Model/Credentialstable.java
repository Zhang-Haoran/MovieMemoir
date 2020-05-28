package com.example.moviememoir.Model;

public class Credentialstable {
    private Integer credentialsid;
    private String username;
    private String passwordhash;
    private String signupdate;
    private Usertable userid;

    public Credentialstable(Integer credentialsid, String username, String passwordhash, String signupdate, Usertable userid) {
        this.credentialsid = credentialsid;
        this.username = username;
        this.passwordhash = passwordhash;
        this.signupdate = signupdate;
        this.userid = userid;
    }

    public Credentialstable() {
    }

    public Integer getCredentialsid() {
        return credentialsid;
    }

    public void setCredentialsid(Integer credentialsid) {
        this.credentialsid = credentialsid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public String getSignupdate() {
        return signupdate;
    }

    public void setSignupdate(String signupdate) {
        this.signupdate = signupdate;
    }

    public Usertable getUserid() {
        return userid;
    }

    public void setUserid(Usertable userid) {
        this.userid = userid;
    }
}

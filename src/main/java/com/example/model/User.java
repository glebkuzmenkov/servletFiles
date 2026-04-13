package com.example.model;

import java.io.Serializable;

public class User implements Serializable {
    private String login;
    private String passHash;
    private String email;

    public User(){}
    public User(String login, String passHash, String email){
        this.login = login;
        this.passHash = passHash;
        this.email = email;
    }
    public String getLogin(){
        return login;
    }
    public String getPassHash(){
        return passHash;
    }
    public String getEmail(){
        return email;
    }
    public void setLogin(String login){
        this.login = login;
    }
    public void setPassHash(String passHash){
        this.passHash = passHash;
    }
    public void setEmail(String email){
        this.email = email;
    }
}

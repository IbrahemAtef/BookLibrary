package com.example.booklibrary;

import android.net.Uri;


public class User {
    private String email, userName, pass, phone;
    private String userAvatar;

    public User() {
    }

//    public User(String email, String userName, String pass, String phone) {
//        this.email = email;
//        this.userName = userName;
//        this.pass = pass;
//        this.phone = phone;
//    }

    public User(String email, String userName, String pass, String phone, String userAvatar) {
        this.email = email;
        this.userName = userName;
        this.pass = pass;
        this.phone = phone;
        this.userAvatar = userAvatar;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
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
        this.userName = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

package com.webim;

import java.net.URL;

public class UserInfo {

    private String firstName;
    private String lastName;
    private URL photoUrl;
    private int id;

    public UserInfo(String firstName, String lastName, URL photoUrl, int id){
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUrl = photoUrl;
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public URL getPhotoUrl() {
        return photoUrl;
    }

    public int getId() {
        return id;
    }
}

package com.example.login.Model;

public class Comments {
    private String description,username,dateString;

    public Comments(String description, String username,String dateString) {
        this.description = description;
        this.username = username;
        this.dateString = dateString;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}

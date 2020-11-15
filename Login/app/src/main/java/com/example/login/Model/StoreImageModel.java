package com.example.login.Model;

import java.util.Date;

public class StoreImageModel {


    private String img;
    private String title,description,username;
    private int all_issue_id;
    private String dateString;



    public StoreImageModel(String img, String title, String description,String username,int all_issue_id,String dateString) {
        this.img = img;
        this.title = title;
        this.description = description;
        this.username = username;
        this.all_issue_id = all_issue_id;
        this.dateString = dateString;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getAll_issue_id() {
        return all_issue_id;
    }

    public void setAll_issue_id(int all_issue_id) {
        this.all_issue_id = all_issue_id;
    }

    public String getDateString() {
        return dateString;
    }
}

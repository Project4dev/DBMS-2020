package com.example.login.Model;

public class IssueItems {
    private int ItemImages;
    private String ItemName;

    public IssueItems(int itemImages, String itemName) {
        ItemImages = itemImages;
        ItemName = itemName;
    }

    public int getItemImages() {
        return ItemImages;
    }

    public void setItemImages(int itemImages) {
        ItemImages = itemImages;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }
}

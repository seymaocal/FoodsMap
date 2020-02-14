package com.example.foodsmap.model;

public class RestaurantComment {

    private String comment;
    private String sender;

    public RestaurantComment() {
    }

    public RestaurantComment(String comment, String sender) {
        this.comment = comment;
        this.sender = sender;
    }

    public String getComment() {
        return comment;
    }

    public String getSender() {
        return sender;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}

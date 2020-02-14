package com.example.foodsmap.model;

public class Comment {
    private String comment;
    private String sender;

    public Comment() {
    }

    public Comment(String comment, String sender) {
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

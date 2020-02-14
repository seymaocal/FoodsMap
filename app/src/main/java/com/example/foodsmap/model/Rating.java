package com.example.foodsmap.model;

public class Rating {
    private int rating;
    private String sender;

    public Rating() {
    }

    public Rating(int rating, String sender) {
        this.rating = rating;
        this.sender = sender;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}

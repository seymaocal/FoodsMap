package com.example.foodsmap.model;

public class User {
    private String id;
    private String username;
    private String name;
    private String photoUrl;
    private String bio;

    public User() {
    }

    public User(String id, String username, String name, String photoUrl, String bio) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.photoUrl = photoUrl;
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getphotoUrl() {
        return photoUrl;
    }

    public void setphotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
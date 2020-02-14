package com.example.foodsmap.model;

public class Post {
    private String postId;
    private String postPhoto;
    private String postContent;
    private String sender;//gönderen

    public Post() {
    }

    public Post(String postId, String postPhoto, String postContent, String sender) {
        this.postId = postId;
        this.postPhoto = postPhoto;
        this.postContent = postContent;
        this.sender = sender;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostPhoto() {
        return postPhoto;
    }

    public void setPostPhoto(String postPhoto) {
        this.postPhoto = postPhoto;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}

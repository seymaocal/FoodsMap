package com.example.foodsmap.model;

public class MyCommission {
    private String sender;
    private String senderId;
    private String address;
    private String phone;
    private String food;
    private String pay;
    private String restaurant_name;
    public MyCommission() {
    }

    public MyCommission(String sender, String senderId, String address, String phone, String food, String pay,String restaurant_name) {
        this.sender = sender;
        this.senderId = senderId;
        this.address = address;
        this.phone = phone;
        this.food = food;
        this.pay = pay;
        this.restaurant_name = restaurant_name;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}

package com.example.codehub.models;

import java.sql.Timestamp;

public class Order {
    private String name;
    private int fee;
    private Timestamp datetime;
    public String image;

    public Order() {
    }

    public Order(String name, int fee, Timestamp datetime, String image) {
        this.name = name;
        this.fee = fee;
        this.datetime = datetime;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

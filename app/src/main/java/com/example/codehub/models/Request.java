package com.example.codehub.models;

import java.sql.Timestamp;

public class Request {
    private int id;
    private String name;
    private String desc;
    private int status;
    private Timestamp datetime;


    public Request() {
    }

    public Request(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public Request(int id, String name, String desc, int status, Timestamp datetime) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.status = status;
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }
}

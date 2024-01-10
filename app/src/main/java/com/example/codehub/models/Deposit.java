package com.example.codehub.models;


import java.sql.Date;
import java.sql.Timestamp;

public class Deposit {
    private boolean type;
    private String desc;
    private int coin;
    private Timestamp datetime;

    public Deposit() {
    }

    public Deposit(boolean type, String desc, int coin, Timestamp datetime) {
        this.type = type;
        this.desc = desc;
        this.coin = coin;
        this.datetime = datetime;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }
}

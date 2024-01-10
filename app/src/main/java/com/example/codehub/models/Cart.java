package com.example.codehub.models;

public class Cart {
    private int id;
    private int idSource;
    private String name;
    private int fee;
    private String language;
    private String url;

    public Cart() {
    }

    public Cart(int id, int idSource, String name, int fee, String language, String url) {
        this.id = id;
        this.idSource = idSource;
        this.name = name;
        this.fee = fee;
        this.language = language;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSource() {
        return idSource;
    }

    public void setIdSource(int idSource) {
        this.idSource = idSource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

}

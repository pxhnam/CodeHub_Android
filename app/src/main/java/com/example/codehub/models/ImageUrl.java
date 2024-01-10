package com.example.codehub.models;

public class ImageUrl {
    private int id;
    private int idSource;
    private String url;

    public ImageUrl() {
    }

    public ImageUrl(String url) {
        this.url = url;
    }

    public ImageUrl(int id, String url) {
        this.id = id;
        this.url = url;
    }

    public ImageUrl(int id, int idSource, String url) {
        this.id = id;
        this.idSource = idSource;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

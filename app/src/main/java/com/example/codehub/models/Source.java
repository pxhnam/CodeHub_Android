package com.example.codehub.models;

import java.util.List;

public class Source {
    private int id;
    private String name;
    private String coder;
    private int fee;
    private String type;
    private String language;
    private String linkVideo;
    private String image;
    private List<ImageUrl> imageUrls;
    private String description;
    private int views;
    private int purchases;

    public Source() {
    }

    public Source(int id, String name, int fee, String image) {
        this.id = id;
        this.name = name;
        this.fee = fee;
        this.image = image;
    }

    public Source(int id, String name, int fee, String language, String image) {
        this.id = id;
        this.name = name;
        this.fee = fee;
        this.language = language;
        this.image = image;
    }

    public Source(int id, String name, String coder, int fee, String type, String language, String linkVideo, String description) {
        this.id = id;
        this.name = name;
        this.coder = coder;
        this.fee = fee;
        this.type = type;
        this.language = language;
        this.linkVideo = linkVideo;
        this.description = description;
    }

    public Source(int id, String name, String coder, int fee, String type, String language, String linkVideo, String description, int views, int purchases) {
        this.id = id;
        this.name = name;
        this.coder = coder;
        this.fee = fee;
        this.type = type;
        this.language = language;
        this.linkVideo = linkVideo;
        this.description = description;
        this.views = views;
        this.purchases = purchases;
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

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLinkVideo() {
        return linkVideo;
    }

    public void setLinkVideo(String linkVideo) {
        this.linkVideo = linkVideo;
    }

    public List<ImageUrl> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<ImageUrl> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCoder() {
        return coder;
    }

    public void setCoder(String coder) {
        this.coder = coder;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getPurchases() {
        return purchases;
    }

    public void setPurchases(int purchases) {
        this.purchases = purchases;
    }
}

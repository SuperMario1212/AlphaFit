package com.giruba.huaweicourse.alphafit;

public class MyModel {

    String title, description, duration, url;
    int image;

    public MyModel(String title, String description, String duration, String url, int image) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.image = image;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }
}

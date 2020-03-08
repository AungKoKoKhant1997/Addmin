package com.akkk.adminwatch;

public class MovieModel {
    public  String name;
    public String imglink;
    public String videolink;
    public String category;
    public String series;

    public MovieModel(String name, String imglink, String videolink, String category, String series) {
        this.name = name;
        this.imglink = imglink;
        this.videolink = videolink;
        this.category = category;
        this.series = series;
    }

    public MovieModel(String name, String imglink, String videolink, String category) {
        this.name = name;
        this.imglink = imglink;
        this.videolink = videolink;
        this.category = category;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImglink() {
        return imglink;
    }

    public void setImglink(String imglink) {
        this.imglink = imglink;
    }

    public String getVideolink() {
        return videolink;
    }

    public void setVideolink(String videolink) {
        this.videolink = videolink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public MovieModel() {
    }
}

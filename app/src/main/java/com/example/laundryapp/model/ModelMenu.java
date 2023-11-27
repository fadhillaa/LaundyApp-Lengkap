package com.example.laundryapp.model;

public class ModelMenu {
    private String tvTitle;
    private int imageResource;

    public ModelMenu(String tvTitle, int imageResource) {
        this.tvTitle = tvTitle;
        this.imageResource = imageResource;
    }

    public String getTvTitle() {
        return tvTitle;
    }

    public int getImageResource() {
        return imageResource;
    }
}

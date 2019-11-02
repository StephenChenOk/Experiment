package com.chen.fy.experiment.ex_7;

import android.graphics.Bitmap;

public class News {

    private String title;
    private String author;
    private String content;
    private int imageId;
    private Bitmap image;


    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public int getImageId() { return imageId; }

    public void setImageId(int imageId) { this.imageId = imageId; }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}

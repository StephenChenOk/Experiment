package com.chen.fy.experiment.ex_12;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class News {

    @Expose(serialize = false,deserialize = false)   //指定属性是否进行序列化或者接序列化
    private Integer mId;

    @SerializedName("title")    //当类中的属性名与Json中字符串的属性名不同时使用
   private String mTitle;

    @SerializedName("description")
    private String mSource;

    @SerializedName("picUrl")
    private String mPicUrl;

    @SerializedName("url")
    private String mContentUrl;

    @SerializedName("ctime")
    private String mPublishTime;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String mSource) {
        this.mSource = mSource;
    }

    public String getPicUrl() {
        return mPicUrl;
    }

    public void setPicUrl(String mPicUrl) {
        this.mPicUrl = mPicUrl;
    }

    public String getContentUrl() {
        return mContentUrl;
    }

    public void setContentUrl(String mContentUrl) {
        this.mContentUrl = mContentUrl;
    }

    public String getPublishTime() {
        return mPublishTime;
    }

    public void setPublishTime(String mPublishTime) {
        this.mPublishTime = mPublishTime;
    }
}

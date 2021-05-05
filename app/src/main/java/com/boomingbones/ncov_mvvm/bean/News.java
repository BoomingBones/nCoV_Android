package com.boomingbones.ncov_mvvm.bean;

import com.google.gson.annotations.SerializedName;

public class News {

    public String title;

    @SerializedName("pubDate")
    public String pubTime;

    @SerializedName("summary")
    public String content;

    public String infoSource;

    public String sourceUrl;
}

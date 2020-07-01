package com.boomingbones.ncov;

import com.google.gson.annotations.SerializedName;

public class News {

    String title;

    @SerializedName("pubDate")
    String pubTime;

    @SerializedName("summary")
    String content;

    String infoSource;

    String sourceUrl;

}

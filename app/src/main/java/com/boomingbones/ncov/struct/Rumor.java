package com.boomingbones.ncov.struct;

import com.google.gson.annotations.SerializedName;

public class Rumor {

    public String title;

    @SerializedName("mainSummary")
    public String summary;

    @SerializedName("body")
    public String content;
}

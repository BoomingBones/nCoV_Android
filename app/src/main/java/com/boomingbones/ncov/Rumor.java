package com.boomingbones.ncov;

import com.google.gson.annotations.SerializedName;

class Rumor {

    String title;

    @SerializedName("mainSummary")
    String summary;

    @SerializedName("body")
    String content;
}

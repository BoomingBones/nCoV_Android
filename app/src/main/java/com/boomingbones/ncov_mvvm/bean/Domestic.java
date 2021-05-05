package com.boomingbones.ncov_mvvm.bean;

import com.google.gson.annotations.SerializedName;

public class Domestic {

    public String currentConfirmedCount;

    public String confirmedCount;

    @SerializedName("suspectedCount")
    public String importedCount;

    public String curedCount;

    public String deadCount;

    @SerializedName("seriousCount")
    public String suspectCount;

    @SerializedName("suspectedIncr")
    public String importedIncr;

    public String currentConfirmedIncr;

    public String confirmedIncr;

    public String curedIncr;

    public String deadIncr;

    @SerializedName("seriousIncr")
    public String suspectIncr;
}

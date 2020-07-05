package com.boomingbones.ncov.struct;

import com.google.gson.annotations.SerializedName;

public class Domestic extends Overall {

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

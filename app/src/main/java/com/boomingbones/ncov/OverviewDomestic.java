package com.boomingbones.ncov;

import com.google.gson.annotations.SerializedName;

public class OverviewDomestic {

    String currentConfirmedCount;

    String confirmedCount;

    @SerializedName("suspectedCount")
    String importedCount;

    String curedCount;

    String deadCount;

    @SerializedName("seriousCount")
    String suspectCount;

    @SerializedName("suspectedIncr")
    String importedIncr;

    String currentConfirmedIncr;

    String confirmedIncr;

    String curedIncr;

    String deadIncr;

    @SerializedName("seriousIncr")
    String suspectIncr;
}

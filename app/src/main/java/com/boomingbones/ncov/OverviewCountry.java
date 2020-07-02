package com.boomingbones.ncov;

import com.google.gson.annotations.SerializedName;

public class OverviewCountry {

    @SerializedName("provinceName")
    String countryName;

    String currentConfirmedCount;

    String confirmedCount;

    String curedCount;

    String deadCount;
}

package com.boomingbones.ncov;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OverviewArea implements Serializable {

    @SerializedName("provinceName")
    String areaName;

    String currentConfirmedCount;

    String confirmedCount;

    String curedCount;

    String deadCount;
}

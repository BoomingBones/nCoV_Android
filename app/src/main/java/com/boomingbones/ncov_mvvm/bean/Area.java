package com.boomingbones.ncov_mvvm.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Area implements Serializable {

    @SerializedName("provinceName")
    public String areaName;

    public String currentConfirmedCount;

    public String confirmedCount;

    public String curedCount;

    public String deadCount;
}

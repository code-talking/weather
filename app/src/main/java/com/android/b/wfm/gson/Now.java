package com.android.b.wfm.gson;

import com.google.gson.annotations.SerializedName;

public class Now {
    @SerializedName("cloud")
    public String cloud;

    @SerializedName("cond_txt")
    public String contTxt;

    @SerializedName("hum")
    public String humidity;

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("fl")
    public String feelingTemperature;
}

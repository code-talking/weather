package com.android.b.wfm.gson;

import com.google.gson.annotations.SerializedName;

public class Suggestion {

    @SerializedName("type")
    public String type;

    /**
     *  type类型
     *      comf:   舒适度
     *      drsg:   穿衣建议
     *      flue:   感冒指数
     *      sport:  运动建议
     *      trav:   旅行建议
     *      uv:     紫外线指数
     *      air:    空气污染扩散条件指数
     */
    private String[] types = {"comf", "drsg", "flu", "sport", "trav", "uv", "cw", "air"};

    @SerializedName("brf")
    public String brief;

    @SerializedName("txt")
    public String txt;
}

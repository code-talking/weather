package com.android.b.wfm.db;


import java.util.ArrayList;
import java.util.List;

public class City{

    private int id;
    private String cityName;
    private int provinceId;
    private int cityCode;

    private List<County> allCounties;
    private final String SPLITOR = "@@";


    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public void setProvinceId(int provinceId) {

        this.provinceId = provinceId;
    }

    public void setCityName(String cityName) {

        this.cityName = cityName;
    }

    public void setId(int id) {

        this.id = id;
    }

    public int getCityCode() {

        return cityCode;
    }

    public int getProvinceId() {

        return provinceId;
    }

    public String getCityName() {

        return cityName;
    }

    public int getId() {

        return id;
    }
}

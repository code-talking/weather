package com.android.b.wfm.db;


import java.util.ArrayList;
import java.util.List;

public class County {

    private int id;
    private String countyName;
    private String weatherId;
    private int cityId;
    private String cityName;



    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setWeatherId(String weatherId) {

        this.weatherId = weatherId;
    }

    public void setCountyName(String countyName) {

        this.countyName = countyName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public String getCountyName() {
        return countyName;
    }

    public int getId() {
        return id;
    }
}

package com.android.b.wfm.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class County extends DataSupport {

    private int id;
    private String countyName;
    private String weatherId;
    private int cityId;
    private String cityName;

    private String SPLITOR = "@@";

    /**
     * 从一个乡镇返回改乡镇所属省的所有地级市
     * @param county
     * @param countyNames
     * @param cityNames
     * @return
     */
    public List<City> backToCities(County county, String[] countyNames, String[] cityNames) {
        List<City> cityList = new ArrayList<>();
        String cityName = new String();
        String provinceName = new String();
        for (String s : countyNames) {
            if (s.contains(county.getCountyName())) {
                cityName = s.split(SPLITOR)[0];
                break;
            }
        }

        for (String s : cityNames) {
            if (s.contains(cityName)) {
                provinceName = s.split(SPLITOR)[0];
                break;
            }
        }

        for (String s : cityNames) {
            if (s.contains(provinceName)) {
                String name = s.split(SPLITOR)[1];
                City city = new City();
                city.setCityName(name);
                cityList.add(city);
            }
        }
        return cityList;
    }

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

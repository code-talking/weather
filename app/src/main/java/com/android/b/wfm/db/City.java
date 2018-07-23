package com.android.b.wfm.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class City extends DataSupport {

    private int id;
    private String cityName;
    private int provinceId;
    private int cityCode;

    private List<County> allCounties;
    private final String SPLITOR = "@@";

    /**
     *  从一个地级市拿到对应的所有乡镇
     * @param city
     * @param countyNames
     * @return
     */
    public List<County> cityGetAllCounties(City city, String[] countyNames) {
        List<County> countyList = new ArrayList<>();
        for (String s : countyNames) {
            if (s.contains(city.getCityName())) {
                String name = s.split(SPLITOR)[1];
                County county = new County();
                county.setCountyName(name);
                countyList.add(county);
            }
        }

        return countyList;
    }

    /**
     *  从城市列表返回到省列表
     * @param provinceNames
     * @return
     */
    public List<Province> backToProvince(List<String> provinceNames) {
        List<Province> allProvicnes = new ArrayList<>();
        for (String s : provinceNames) {
            Province province = new Province();
            province.setProvinceName(s);
        }
        return allProvicnes;
    }

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

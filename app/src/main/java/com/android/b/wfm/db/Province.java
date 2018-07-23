package com.android.b.wfm.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class Province extends DataSupport {

    private int id;
    private String provinceName;
    private int provinceCode;
    private List<City> citiesInProvince;
    private final String SPLITOR = "@@";

    /**
     * 从省拿到其所有的地级市
     * @param province
     * @param cityNames
     * @return
     */
    private List<City> provinceGetAllCities(Province province, String[] cityNames) {
        List<City> cityList = new ArrayList<>();
        for (String s : cityNames) {
            if (s.contains(province.getProvinceName())) {
                String name = s.split(SPLITOR)[1];
                City city = new City();
                city.setCityName(name);
                cityList.add(city);
            }
        }
        return cityList;
    }


    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setProvinceName(String provinceName) {

        this.provinceName = provinceName;
    }

    public int getProvinceCode() {

        return provinceCode;
    }

    public String getProvinceName() {

        return provinceName;
    }

    public void setId(int id) {

        this.id = id;
    }

    public int getId() {

        return id;
    }
}

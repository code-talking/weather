package com.android.b.wfm.util;

import android.text.TextUtils;
import android.util.Log;

import com.android.b.wfm.db.City;
import com.android.b.wfm.db.County;
import com.android.b.wfm.db.Province;
import com.android.b.wfm.gson.WeatherFuture;
import com.android.b.wfm.gson.WeatherNow;
import com.android.b.wfm.gson.WeatherSuggestions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    public static final String TOP_KEY = "HeWeather6";

    /**
     *  解析实时天气
     * @param response
     * @return
     */
    public static WeatherNow handleWeatherNowResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray(TOP_KEY);
            String nowContent = jsonArray.getJSONObject(0).toString();
            WeatherNow weatherNow = new Gson().fromJson(nowContent, WeatherNow.class);
            return weatherNow;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *  解析未来7天天气
     * @param response
     * @return
     */
    public static WeatherFuture handleWeatherFeatureResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray(TOP_KEY);
            String featureContent = jsonArray.getJSONObject(0).toString();
            WeatherFuture weatherFuture = new Gson().fromJson(featureContent, WeatherFuture.class);
            return weatherFuture;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  解析生活建议
     * @param response
     * @return
     */
    public static WeatherSuggestions handleWeatherSuggestionsResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray(TOP_KEY);
            String suggestionContent = jsonArray.getJSONObject(0).toString();
            WeatherSuggestions weatherSuggestions = new Gson().fromJson(suggestionContent, WeatherSuggestions.class);
            return weatherSuggestions;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



    /**
     *  可能不需要
     * @param response
     * @return
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObj = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObj.getString("name"));
                    province.setProvinceCode(provinceObj.getInt("id"));
//                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     *  可能不需要
     * @param response
     * @param provinceId
     * @return
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObj = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObj.getString("name"));
                    city.setCityCode(cityObj.getInt("id"));
                    city.setProvinceId(provinceId);
//                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     *  可能不需要
     * @param response
     * @param cityId
     * @return
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObj = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObj.getString("name"));
                    county.setWeatherId(countyObj.getString("weather_id"));
                    county.setCityId(cityId);
//                    county.save();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

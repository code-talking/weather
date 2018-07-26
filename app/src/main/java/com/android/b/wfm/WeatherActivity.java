package com.android.b.wfm;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.b.wfm.gson.DailyForecast;
import com.android.b.wfm.gson.Suggestion;
import com.android.b.wfm.gson.WeatherFuture;
import com.android.b.wfm.gson.WeatherNow;
import com.android.b.wfm.gson.WeatherSuggestions;
import com.android.b.wfm.util.HttpUtil;
import com.android.b.wfm.util.Utility;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private static final String WEATHER_NOW = "weather";

    private static final String COUNTY_NAME = "countyName";

    private static final String WEATHER_SUGGESTION_BASE = "https://free-api.heweather.com/s6/weather/lifestyle?location=";

    private static final String WEATHER_FUTURE_BASE = "https://free-api.heweather.com/s6/weather/forecast?location=";

    private static final String WEATHER_NOW_BASE = "https://free-api.heweather.com/s6/weather/now?location=";

    private static final String HEWEATHER_KEY = "&key=f71cf2991a6f4acda8b96f0cb41d407c";

    private static final int DEFAULT_DAILY_FORECAST_NUMBER = 3;
    private static final int REFRESH = 1;
    protected static final int LOAD = 0;

    private static final String OK = "ok";

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView feelingTemperature;

    private TextView humidity;

    private TextView comfortText;

    private TextView clothText; //  对应R.id.drsg_text这个标签

    private TextView exerciseText;

    private TextView carWastText;

    private TextView fluText;

    private Map<String, String> suggestionMap;

    private ImageView bingPicImg;

    public SwipeRefreshLayout swipeRefresh;

    public DrawerLayout drawerLayout;

    private Button navButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_weather);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipte_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);


        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);

        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);

        /**
         *  详情布局
         */
        feelingTemperature = (TextView) findViewById(R.id.feeling_temp);
        humidity = (TextView) findViewById(R.id.hum_text);

        /**
         *  生活建议布局
         */
        comfortText = (TextView) findViewById(R.id.comfort_text);
        clothText = (TextView) findViewById(R.id.drsg_text);
        exerciseText = (TextView) findViewById(R.id.exercise_text);
        carWastText = (TextView) findViewById(R.id.car_wash_text);
        fluText = (TextView) findViewById(R.id.flu_text);

        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String weatherNowString = sharedPreferences.getString(WEATHER_NOW, null);
//

//
//        if (weatherNowString != null) {
//            WeatherNow weatherNow = Utility.handleWeatherNowResponse(weatherNowString);
//
//            showWeatherNowInfo(weatherNow);
//        } else {
//            String countyName = getIntent().getStringExtra(COUNTY_NAME);
//            weatherLayout.setVisibility(View.INVISIBLE);
//            requestWeather(countyName);
//        }

        loadBingPic();

        final String countyName = getIntent().getStringExtra(COUNTY_NAME);
        weatherLayout.setVisibility(View.INVISIBLE);
        requestWeather(countyName, LOAD);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(countyName, REFRESH);

            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void requestWeather(String countyName, int source) {
        requestWeatherNow(countyName);
        requestWeatherFuture(countyName);
        requestweatherSuggestions(countyName);
        if (source == REFRESH) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(WeatherActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     *  目前只尝试了请求weatherNow这个类
     *      成功
     * @param countyName
     */
    public void requestWeatherNow(String countyName) {
        String url = WEATHER_NOW_BASE + countyName + HEWEATHER_KEY;
        HttpUtil.sendOkHttpRequest(url, new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "天气加载失败...", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String weatherNowResponseText = response.body().string();
                final WeatherNow weatherNow = Utility.handleWeatherNowResponse(weatherNowResponseText);



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherNow != null && OK.equals(weatherNow.status)) {
//                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
//                            editor.apply();
//                            showWeatherNowInfo(weatherNow);
                        }
                        showWeatherNowInfo(weatherNow);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    /**
     * @param countyName
     */
    public void requestWeatherFuture(String countyName) {
        String url = WEATHER_FUTURE_BASE + countyName + HEWEATHER_KEY;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "未来天气加载失败...", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String weatherFutureResponseText = response.body().string();
                final WeatherFuture weatherFuture = Utility.handleWeatherFeatureResponse(weatherFutureResponseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherFuture != null && OK.equals(weatherFuture.status)) {
                            showWeatherFutureInfo(weatherFuture);
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    /**
     *  bug fixed
     * @param countyName
     */
    public void requestweatherSuggestions(String countyName) {
        String url = WEATHER_SUGGESTION_BASE + countyName + HEWEATHER_KEY;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "生活建议加载失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String weatherSuggestionResponseText = response.body().string();
                final WeatherSuggestions weatherSuggetion = Utility.handleWeatherSuggestionsResponse(weatherSuggestionResponseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherSuggetion != null && OK.equals(weatherSuggetion.status)) {
//                            showWeatherSuggestionInfo(weatherSuggetion);
                        }
                        showWeatherSuggestionInfo(weatherSuggetion);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void showWeatherFutureInfo(WeatherFuture weatherFuture) {
        List<DailyForecast> lst = weatherFuture.futures;
        forecastLayout.removeAllViews();

        for (int i = lst.size() - DEFAULT_DAILY_FORECAST_NUMBER; i < lst.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxTemp = (TextView) view.findViewById(R.id.max_text);
            TextView minTemp = (TextView) view.findViewById(R.id.min_text);

            dateText.setText(lst.get(i).date);
            infoText.setText(lst.get(i).condDay);
            maxTemp.setText(lst.get(i).temperatureMax + "°C");
            minTemp.setText(lst.get(i).temperatureMin + "°C");

            forecastLayout.addView(view);
        }

    }

    private void showWeatherSuggestionInfo(WeatherSuggestions weatherSuggestions) {
        String cityName = weatherSuggestions.basic.cityName;
        String str = "";
        suggestionMap = new HashMap<>();
        suggestionMap.put("comf", "舒适度");
        suggestionMap.put("drsg", "穿衣建议");
        suggestionMap.put("sport", "运动建议");
        suggestionMap.put("cw", "洗车建议");
        suggestionMap.put("flu", "感冒指数");
        for (int i = 0; i < weatherSuggestions.suggestions.size(); i++) {
            Suggestion suggestion = weatherSuggestions.suggestions.get(i);
            String type = weatherSuggestions.suggestions.get(i).type;
            str = suggestion.txt;
            String value = suggestionMap.get(type);
            if (type.equals("comf")) {
                comfortText.setText(value + "\n\t\t\t\t\t" + str);
            } else if (type.equals("drsg")) {
                clothText.setText(value + "\n\t\t\t\t\t" + str);
            } else if (type.equals("sport")) {
                exerciseText.setText(value + "\n\t\t\t\t\t" + str);
            } else if (type.equals("cw")) {
                carWastText.setText(value + "\n\t\t\t\t\t" + str);
            } else if (type.equals("flu")) {
                fluText.setText(value + "\n\t\t\t\t\t" + str);
            }
        }



        weatherLayout.setVisibility(View.VISIBLE);
    }

    private void showWeatherNowInfo(WeatherNow weatherNow) {
        String cityName = weatherNow.basic.cityName;
        String updateTime = weatherNow.update.loc.split(" ")[1];
        String degree = weatherNow.now.temperature + "°C";
        String feelingTemp = weatherNow.now.feelingTemperature;
        String humid = weatherNow.now.humidity;
        String weatherInfo = weatherNow.now.contTxt;

        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        feelingTemperature.setText(feelingTemp);
        humidity.setText(humid);
        weatherInfoText.setText(weatherInfo);

        weatherLayout.setVisibility(View.VISIBLE);

    }

    private void loadBingPic() {
        String base = "http://7xr4g8.com1.z0.glb.clouddn.com/";
        Random random = new Random();
        int num = random.nextInt(965);
        final String url = base + String.valueOf(num);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(WeatherActivity.this).load(url).into(bingPicImg);
//                Toast.makeText(WeatherActivity.this, "picture loaded", Toast.LENGTH_LONG).show();
            }
        });
    }



}

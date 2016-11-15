package com.sunshineapp.model;

import com.sunshineapp.BuildConfig;

/**
 * Created by hendrysetiadi on 12/11/2016.
 */

public class SunshineURL {
    public static final String
        BASE_URL= "http://api.openweathermap.org/data/2.5/";

    // http://api.openweathermap.org/data/2.5/weather/?
    // q=Jakarta
    // &
    // appid=bae2e88a50774eff50692ce8af26c060
    public static String getCuacaSekarang(String namaKota) {
        return BASE_URL + "weather?q=" + namaKota
                + "&appid=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY
                + "&units=metric";
    }

    // http://api.openweathermap.org/data/2.5/forecast/daily?
    // q=jakarta
    // &
    // cnt=16
    // &
    // units=metric
    // &
    // appid=bae2e88a50774eff50692ce8af26c060
    public static String getCuacaRamalan(String namaKota) {
        return BASE_URL + "forecast/daily?q=" + namaKota
                + "&appid=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY
                + "&cnt=16&units=metric";
    }
}

package com.sunshineapp.model;

/**
 * Created by hendrysetiadi on 19/11/2016.
 */

public class SunshineURL {
    public static final String BASE_URL
            = "http://api.openweathermap.org/data/2.5/";

    public static String getCuacaSekarang(String namaKota) {
        return BASE_URL + "weather?q=" + namaKota
                + "&appid=bae2e88a50774eff50692ce8af26c060"
                + "&units=metric";
    }

    // http://api.openweathermap.org/data/2.5/forecast/daily?
    // q=jakarta&
    // cnt=16&
    // units=metric&
    // appid=bae2e88a50774eff50692ce8af26c060
    public static String getCuacaRamalan (String namakota) {
        return BASE_URL + "forecast/daily?q=" + namakota
                + "&appid=bae2e88a50774eff50692ce8af26c060"
                + "&cnt=16"
                + "&units=metric";
    }
}

package com.sunshineapp.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;

import com.sunshineapp.data.CuacaDBHelper;
import com.sunshineapp.model.SunshineURL;
import com.sunshineapp.pojo.CuacaRamalan;
import com.sunshineapp.pojo.List;
import com.sunshineapp.pojo.Weather;
import com.sunshineapp.singleton.GsonSingleton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hendrysetiadi on 15/11/2016.
 */

public class SunshineService extends IntentService{

    public SunshineService(){
        super("Sunshine");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpURLConnection conn= null;
        try {
            URL url = new URL(SunshineURL.getCuacaRamalan("Jakarta"));
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.connect();

            InputStream is = conn.getInputStream();
            StringBuilder hasilStringBuilder = new StringBuilder();

            BufferedReader bReader =
                    new BufferedReader(
                            new InputStreamReader(is));

            String strLine;
            /** Reading the contents of the file , line by line */
            while ((strLine = bReader.readLine()) != null) {
                hasilStringBuilder.append(strLine);
            }
            CuacaRamalan cuacaRamalan =
                    GsonSingleton.getGson().fromJson(hasilStringBuilder.toString()
                            , CuacaRamalan.class);
            java.util.List<List> lists = cuacaRamalan.getList();
            ContentValues[] contentValues = new ContentValues[lists.size()];
            for (int i =0; i<lists.size(); i++) {
                int cityID= 1642911;
                List listObj = lists.get(i);
                int date = lists.get(i).getDt();
                ContentValues cv = new ContentValues();
                cv.put(CuacaDBHelper.CITY_ID, 1642911);
                cv.put(CuacaDBHelper.COLUMN_DT, date);
                cv.put(CuacaDBHelper.COLUMN_MINTEMP, listObj.getTemp().getMin());
                cv.put(CuacaDBHelper.COLUMN_MAXTEMP, listObj.getTemp().getMax());
                cv.put(CuacaDBHelper.COLUMN_PRESSURE, listObj.getPressure());
                cv.put(CuacaDBHelper.COLUMN_HUMIDITY, listObj.getHumidity());

                Weather weather = listObj.getWeather().get(0);
                cv.put(CuacaDBHelper.COLUMN_W_ID, weather.getId());
                cv.put(CuacaDBHelper.COLUMN_W_DESC, weather.getDescription());
                cv.put(CuacaDBHelper.COLUMN_W_ICON, weather.getIcon());
                cv.put(CuacaDBHelper.COLUMN_W_MAIN, weather.getMain());

                cv.put(CuacaDBHelper.COLUMN_WINDSPEED, listObj.getSpeed());
                cv.put(CuacaDBHelper.COLUMN_WINDDEGREE, listObj.getDeg());
                contentValues[i] = cv;
            }
            Uri uri = Uri.parse("content://com.sunshineapp/ramalan");
            getContentResolver().delete(
                    uri,
                    null,
                    null
            );
            getContentResolver().bulkInsert(
                    uri,
                    contentValues);
            getContentResolver().notifyChange(uri, null);
        }
        catch (Exception e) {

        }
        finally {
            if (null!= conn){
                conn.disconnect();
            }
        }
    }
}

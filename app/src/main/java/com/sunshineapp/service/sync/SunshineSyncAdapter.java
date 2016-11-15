package com.sunshineapp.service.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

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
public class SunshineSyncAdapter extends AbstractThreadedSyncAdapter {
    public SunshineSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.i("SSA", "Saya perform synch sekarang");
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
            getContext().getContentResolver().delete(
                    uri,
                    null,
                    null
            );
            getContext().getContentResolver().bulkInsert(
                    uri,
                    contentValues);
            getContext().getContentResolver().notifyChange(uri, null);
        }
        catch (Exception e) {

        }
        finally {
            if (null!= conn){
                conn.disconnect();
            }
        }
    }

    public static void memulaiSync(Context context){
        // check akun sunshine.com
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(
                "Lala"
                , "sunshine.com");
        String password = accountManager.getPassword(newAccount);
        if (null == password) {
            boolean sukses = accountManager.addAccountExplicitly(newAccount,"",null);
            if (sukses) {
                // konfigurasi sync
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    // we can enable inexact timers in our periodic sync
                    SyncRequest request = new SyncRequest.Builder().
                            syncPeriodic(60 * 180, (60 * 180) / 3).
                            setSyncAdapter(newAccount, "com.sunshineapp").
                            setExtras(new Bundle()).build();
                    ContentResolver.requestSync(request);
                } else {
                    ContentResolver.addPeriodicSync(newAccount,
                            "com.sunshineapp", new Bundle(), 60 * 180);
                }

                // start periodik
                ContentResolver.setSyncAutomatically(
                        newAccount, "com.sunshineapp", true);

                // synch sekarang
                Bundle bundle = new Bundle();
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                ContentResolver.requestSync(newAccount, "com.sunshineapp", bundle);
            }
        }
    }
}

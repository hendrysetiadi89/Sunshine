package com.sunshineapp.activity;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.sunshineapp.R;
import com.sunshineapp.adapter.CuacaRVAdapter;
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

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int RAMALAN_LOADER = 100;
    RecyclerView mRecyclerView;
    CuacaRVAdapter mCuacaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(llm);
        mCuacaAdapter = new CuacaRVAdapter(null);
        mRecyclerView.setAdapter(mCuacaAdapter);

        getLoaderManager().initLoader(RAMALAN_LOADER,null, this);

        new AmbilCuacaRamalanTask().execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (i == RAMALAN_LOADER) {
            return new CursorLoader(
                    MainActivity.this,
                    Uri.parse("content://com.sunshineapp/ramalan"),
                    new String[]{
                            CuacaDBHelper.COLUMN_DT,
                            CuacaDBHelper.COLUMN_MINTEMP,
                            CuacaDBHelper.COLUMN_MAXTEMP,
                            CuacaDBHelper.COLUMN_W_DESC,
                            CuacaDBHelper.COLUMN_W_ICON
                    },
                    "ramalan.city_id = ?",
                    new String[] {"1642911"},
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCuacaAdapter.updateList(cursor);
        mCuacaAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCuacaAdapter.updateList(null);
    }


    class AmbilCuacaRamalanTask extends AsyncTask<Void, Void, CuacaRamalan> {
        public AmbilCuacaRamalanTask(){

        }

        @Override
        protected CuacaRamalan doInBackground(Void... voids) {
            HttpURLConnection conn= null;
            try {
                URL url = new URL(SunshineURL.getCuacaRamalan("Jakarta"));
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                conn.connect();

                int responseCode = conn.getResponseCode();
                if (responseCode!= HttpURLConnection.HTTP_OK) {
                    return null;
                }
                InputStream is = conn.getInputStream();
                if (null == is) {
                    return null;
                }
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
                return cuacaRamalan;
            }
            catch (Exception e) {
                return null;
            }
            finally {
                if (null!= conn){
                    conn.disconnect();
                }
            }
        }

    }



}

package com.sunshineapp.activity;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
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
import com.sunshineapp.listener.OnCuacaClickListener;
import com.sunshineapp.model.SunshineURL;
import com.sunshineapp.pojo.CuacaRamalan;
import com.sunshineapp.pojo.List;
import com.sunshineapp.pojo.Weather;
import com.sunshineapp.service.SunshineService;
import com.sunshineapp.singleton.GsonSingleton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> ,
                    OnCuacaClickListener{

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
        mCuacaAdapter = new CuacaRVAdapter(null, this);
        mRecyclerView.setAdapter(mCuacaAdapter);

        getLoaderManager().initLoader(RAMALAN_LOADER,null, this);

        Intent bukaRamalanService = new Intent(this, SunshineService.class);
        startService(bukaRamalanService);
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

    @Override
    public void onCuacaClick(int date) {
        Intent intentBukaDetail = new Intent(MainActivity.this, DetailActivity.class);
        intentBukaDetail.putExtra(DetailActivity.EXTRA_DATE, date);
        this.startActivity(intentBukaDetail);
    }



}

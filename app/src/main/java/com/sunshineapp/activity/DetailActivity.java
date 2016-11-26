package com.sunshineapp.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunshineapp.R;
import com.sunshineapp.data.CuacaDBHelper;

import java.util.Date;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_DATE = "date";
    public static final int LOADER_RAMALAN_DETAIL = 200;
    private int mDate;
    private TextView tvDate;
    private TextView tvHigh;
    private TextView tvLow;
    private ImageView ivIcon;
    private TextView tvIconDesc;
    private TextView tvHumidity;
    private TextView tvPressure;
    private TextView tvWind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDate = getIntent().getIntExtra(EXTRA_DATE, 0);

        setContentView(R.layout.activity_detail);

        tvDate = (TextView) findViewById(R.id.tv_date);
        tvHigh = (TextView) findViewById(R.id.tv_high);
        tvLow = (TextView) findViewById(R.id.tv_low);

        ivIcon = (ImageView) findViewById(R.id.iv_icon);

        tvIconDesc = (TextView) findViewById(R.id.tv_icon_desc);
        tvHumidity = (TextView) findViewById(R.id.tv_humidity);
        tvPressure = (TextView) findViewById(R.id.tv_pressure);
        tvWind = (TextView) findViewById(R.id.tv_wind);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detail Cuaca");
        actionBar.setDisplayHomeAsUpEnabled(true);

        getLoaderManager().initLoader(LOADER_RAMALAN_DETAIL, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (i == LOADER_RAMALAN_DETAIL) {
            return new CursorLoader(
                    DetailActivity.this,
                    Uri.parse("content://com.sunshineapp/ramalan/" + mDate),
                    new String[]{
                            CuacaDBHelper.COLUMN_DT,
                            CuacaDBHelper.COLUMN_MINTEMP,
                            CuacaDBHelper.COLUMN_MAXTEMP,
                            CuacaDBHelper.COLUMN_W_DESC,
                            CuacaDBHelper.COLUMN_W_ICON,
                            CuacaDBHelper.COLUMN_HUMIDITY,
                            CuacaDBHelper.COLUMN_PRESSURE,
                            CuacaDBHelper.COLUMN_WINDSPEED,
                            CuacaDBHelper.COLUMN_WINDDEGREE
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
        if (cursor == null) return;

        //cursor is always 1
        cursor.moveToPosition(0);

        Date date = new Date(Long.parseLong(cursor.getLong(0) +"000"));
        tvDate.setText(date.toString());
        tvLow.setText(String.format(
                this.getString(R.string.xx_percent),
                cursor.getDouble(1) ) );
        tvHigh.setText(String.format(
                this.getString(R.string.xx_percent),
                cursor.getDouble(2) ) );
        tvIconDesc.setText(cursor.getString(3));

        // http://openweathermap.org/img/w/10d.png
        Picasso.with(this).load("http://openweathermap.org/img/w/" +
                cursor.getString(4)  + ".png").into(ivIcon);

        tvHumidity.setText(cursor.getString(5)+"%");
        tvPressure.setText(cursor.getString(6));
        tvWind.setText(cursor.getString(7)+"/" +cursor.getString(8));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

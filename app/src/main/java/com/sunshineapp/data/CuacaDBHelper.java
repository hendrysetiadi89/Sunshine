package com.sunshineapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hendrysetiadi on 14/11/2016.
 */

public class CuacaDBHelper extends SQLiteOpenHelper {

    public static final String CUACA_DB = "cuaca.db";
    public static final int VERSION = 1;

    public static final String TABLE_RAMALAN = "ramalan";
    public static final String COLUMN_DT = "dt";
    public static final String COLUMN_MINTEMP = "mintemp";
    public static final String COLUMN_MAXTEMP = "maxtemp";
    public static final String COLUMN_PRESSURE = "pressure";
    public static final String CITY_ID = "city_id";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_WINDSPEED = "windspeed";
    public static final String COLUMN_WINDDEGREE = "winddegree";
    public static final String COLUMN_W_ID = "w_id";
    public static final String COLUMN_W_MAIN = "w_main";
    public static final String COLUMN_W_DESC = "w_desc";
    public static final String COLUMN_W_ICON = "w_icon";

    public CuacaDBHelper(Context context) {
        super(context, CUACA_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_RAMALAN + " (" +
                CITY_ID + " int, " +
                COLUMN_DT + " int, " +
                COLUMN_MINTEMP + " real, " +
                COLUMN_MAXTEMP + " real, " +
                COLUMN_PRESSURE + " real, " +
                COLUMN_HUMIDITY + " real, " +
                COLUMN_WINDSPEED + " real, " +
                COLUMN_WINDDEGREE + " real, " +
                COLUMN_W_ID + " int, " +
                COLUMN_W_MAIN + " text, " +
                COLUMN_W_DESC + " text, " +
                COLUMN_W_ICON + " text, " +
                "unique ("+ CITY_ID +", "+ COLUMN_DT +") on conflict replace);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        buatUlangDB(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        buatUlangDB(db);
    }

    private void buatUlangDB(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_RAMALAN);
        onCreate(sqLiteDatabase);
    }
}

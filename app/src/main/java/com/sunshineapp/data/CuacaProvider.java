package com.sunshineapp.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by hendrysetiadi on 14/11/2016.
 */

public class CuacaProvider extends ContentProvider {

    public static final int RAMALAN = 100;
    public static final int RAMALAN_PER_DATE = 200;
    CuacaDBHelper cuacaDBHelper;
    UriMatcher mUriMatcher;

    @Override
    public boolean onCreate() {
        cuacaDBHelper = new CuacaDBHelper(getContext());
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI("com.sunshineapp", "ramalan", RAMALAN);
        mUriMatcher.addURI("com.sunshineapp", "ramalan/#", RAMALAN_PER_DATE);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionargs,
                        String sortOrder) {
        Cursor retCursor = null;
        switch (mUriMatcher.match(uri)) {
            // uri = "ramalan"
            case RAMALAN:
                retCursor = cuacaDBHelper.getReadableDatabase().query(
                        CuacaDBHelper.TABLE_RAMALAN,
                        projection,
                        selection,
                        selectionargs,
                        null,
                        null,
                        sortOrder
                );
                retCursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            // uri = "ramalan/234593000"
            case RAMALAN_PER_DATE:
                retCursor = cuacaDBHelper.getReadableDatabase().query(
                        CuacaDBHelper.TABLE_RAMALAN,
                        projection,
                        "ramalan.dt = ?",
                        new String[] { uri.getPathSegments().get(1) },
                        null,
                        null,
                        sortOrder
                );
                retCursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case RAMALAN:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.sunshineapp/ramalan";
            case RAMALAN_PER_DATE:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.sunshineapp/ramalan";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (mUriMatcher.match(uri)) {
            // uri = "ramalan"
            case RAMALAN:
                long id = cuacaDBHelper.getWritableDatabase().insert(
                        CuacaDBHelper.TABLE_RAMALAN,
                        null,
                        contentValues
                );
                return Uri.parse ("content://com.sunshineapp/ramalan/row/"+id );
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            // uri = "ramalan"
            case RAMALAN:
                int jmlBarisDelete = cuacaDBHelper.getWritableDatabase().delete(
                        CuacaDBHelper.TABLE_RAMALAN,
                        selection,
                        selectionArgs
                );
                return jmlBarisDelete;

        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues,
                      String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)) {
            // uri = "ramalan"
            case RAMALAN:
                int jmlBarisUpdate = cuacaDBHelper.getWritableDatabase().update(
                        CuacaDBHelper.TABLE_RAMALAN,
                        contentValues,
                        selection,
                        selectionArgs
                );
                return jmlBarisUpdate;

        }
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        switch (mUriMatcher.match(uri)) {
            // uri = "ramalan"
            case RAMALAN:
                int returnCount = 0;

                SQLiteDatabase writableDatabase= cuacaDBHelper.getWritableDatabase();
                writableDatabase.beginTransaction();
                try {
                    for (ContentValues cv : values) {
                        writableDatabase.insert(
                                CuacaDBHelper.TABLE_RAMALAN,
                                null,
                                cv
                        );
                        returnCount++;
                    }
                    writableDatabase.setTransactionSuccessful();
                }
                catch (Exception e) {
                    returnCount = 0;
                }
                finally {
                    writableDatabase.endTransaction();
                }
                return returnCount;
        }
        return 0;
    }
}

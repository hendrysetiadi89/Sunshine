package com.sunshineapp.service.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by hendrysetiadi on 15/11/2016.
 */
public class SunshineSyncService extends Service {
    private static SunshineSyncAdapter mSyncAdapter = null;

    public SunshineSyncService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mSyncAdapter == null) {
            mSyncAdapter = new SunshineSyncAdapter(getApplicationContext(), true);
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mSyncAdapter.getSyncAdapterBinder();
    }
}

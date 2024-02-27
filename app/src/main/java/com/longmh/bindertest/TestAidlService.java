package com.longmh.bindertest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TestAidlService extends Service {

    String tag = "TestAidlService";

    private int mData;

    @Override
    public IBinder onBind(Intent intent) {
        mData = 321;
        Log.d(tag, "success onBind,mData = " + mData);
        return iBinder;
    }

    private final IBinder iBinder = new IMyAidlInterface.Stub() {
        @Override
        public void setData(int data) {
            mData = data;
        }

        @Override
        public int getData() {
            return mData;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(tag, "onCreate: success");
    }
}




package com.heartbeat.myplugin;

import android.app.Application;

import com.heartbeat.myplugin.utils.ContextProvider;


/**
 * Created by zhouyuan on 2016/5/3.
 */
public class ZApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextProvider.init(getApplicationContext());
    }
}

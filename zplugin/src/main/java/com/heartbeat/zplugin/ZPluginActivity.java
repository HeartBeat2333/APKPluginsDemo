package com.heartbeat.zplugin;

import android.app.Activity;
import android.os.Bundle;

/**
 * 插件被调用activity基类
 * Created by zhouyuan on 2016/5/4.
 */
public class ZPluginActivity extends Activity implements IZPlugin {
    private static final String TAG = "ZPluginActivity";
    protected Activity mProxyActivity;
    protected Activity that;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProxyActivity = this;
        that = mProxyActivity;
    }

    @Override
    public void attach(Activity proxyActivity, PluginHolder pluginHolder) {
        mProxyActivity = proxyActivity;
        that = mProxyActivity;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

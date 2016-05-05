package com.heartbeat.zplugin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public void setContentView(View view) {
        mProxyActivity.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mProxyActivity.setContentView(view, params);
    }

    @Override
    public void setContentView(int layoutResID) {
        mProxyActivity.setContentView(layoutResID);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        mProxyActivity.addContentView(view, params);
    }

    @Override
    public View findViewById(int id) {
        return mProxyActivity.findViewById(id);
    }

    @Override
    public Intent getIntent() {
        return mProxyActivity.getIntent();
    }

    @Override
    public ClassLoader getClassLoader() {
        return mProxyActivity.getClassLoader();
    }

    @Override
    public Resources getResources() {
        return mProxyActivity.getResources();
    }
}

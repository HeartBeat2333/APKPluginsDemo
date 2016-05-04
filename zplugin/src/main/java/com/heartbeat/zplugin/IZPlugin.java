package com.heartbeat.zplugin;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by zhouyuan on 2016/5/4.
 */
public interface IZPlugin {
    public void onCreate(Bundle savedInstanceState);
    public void onStart();
    public void onRestart();
    public void onResume();
    public void onPause();
    public void onStop();
    public void onDestroy();
    public void attach(Activity proxyActivity, PluginHolder pluginHolder);
}

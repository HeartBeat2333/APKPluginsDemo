package com.heartbeat.apkplugins.Utils.ZPlugin;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

/**
 * Created by zhouyuan on 2016/5/3.
 */
public class PluginHolder {

    public String packageName;
    public DexClassLoader dexClassLoader;
    public AssetManager assetManager;
    public Resources resources;
    public PackageInfo packageInfo;

    public PluginHolder(DexClassLoader loader, Resources resources,
                           PackageInfo packageInfo) {
        this.packageName = packageInfo.packageName;
        this.dexClassLoader = loader;
        this.assetManager = resources.getAssets();
        this.resources = resources;
        this.packageInfo = packageInfo;
    }

}

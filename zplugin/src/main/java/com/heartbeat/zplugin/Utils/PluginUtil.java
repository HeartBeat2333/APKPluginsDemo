package com.heartbeat.zplugin.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by zhouyuan on 2016/5/6.
 */
public class PluginUtil {

    /**
     * 加载dex
     * @param pluginPath
     * @return
     */
    public static DexClassLoader loadDex(Context context, String pluginPath
            , String optimizedDirectory, String libraryPath) {
        try{
            DexClassLoader classloader = new DexClassLoader(
                    pluginPath, optimizedDirectory,
                    libraryPath,
                    context.getClassLoader());
            return classloader;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载Asset
     * 反射调用 addAssetPath 方法加载
     * @return
     */
    public static AssetManager loadAssetManager(String pluginPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, pluginPath);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载Resources
     * @param assetManager
     * @return
     */
    public static Resources loadResources(Context context, AssetManager assetManager) {
        Resources superRes = context.getResources();
        Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        return resources;
    }

    /**
     * 将apk从asset中拷贝到SD卡
     *
     * @param context
     * @param src
     * @param des
     */
    public static void copyAssetsApkToFile(Context context, String src, String des) {
        try {
            InputStream is = context.getAssets().open(src);
            FileOutputStream fos = new FileOutputStream(new File(des));
            byte[] buffer = new byte[1024];
            while (true) {
                int len = is.read(buffer);
                if (len == -1) {
                    break;
                }
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

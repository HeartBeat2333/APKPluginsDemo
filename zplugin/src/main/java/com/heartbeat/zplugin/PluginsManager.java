package com.heartbeat.zplugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.heartbeat.zplugin.Utils.PluginUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import dalvik.system.DexClassLoader;

/**
 * 插件加载管理类
 * 默认APK插件在Asset的plugins目录下
 * <p/>
 * TODO : 异步加载管理, KEY管理, Intent启动，与Rxjava结合，插件和宿主之间的通信
 * Created by zhouyuan on 2016/5/3.
 */
public class PluginsManager {
    private static final String TAG = "PluginsManager";
    private static final String OPT_LIB_DIR = "pluginlib";
    private static final String OPT_DEX_DIR = "dex";
    private static final String OPT_APK_DIR = "APK";

    private static PluginsManager mInstance;
    private Context mContext;
    private ConcurrentHashMap<String, PluginHolder> mPluginHolders;
    private String mNativeLibDir;
    private File mPluginAPKDir;
    private File mDexDir;

    public PluginsManager(Context context) {
        mContext = context;
        mPluginHolders = new ConcurrentHashMap<>();

        // 解压到app缓存目录，防止注入
        mNativeLibDir = mContext.getDir(OPT_LIB_DIR, Context.MODE_PRIVATE).getAbsolutePath();
        mDexDir = mContext.getDir(OPT_DEX_DIR, Context.MODE_PRIVATE);
        mPluginAPKDir = mContext.getDir(OPT_APK_DIR, Context.MODE_PRIVATE);
        mPluginAPKDir.mkdirs();
    }

    public static PluginsManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PluginsManager(context);
        }
        return mInstance;
    }

    /**
     * 加载asset下文件夹里的插件
     * @param assetName asset插件文件夹名字
     */
    public void loadAssetPlugins(String assetName) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            return;

        String paths[] = null;
        try {
            paths = mContext.getAssets().list(assetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (paths == null) {
            Log.v(TAG, "找不到插件plugins " + paths);
            return;
        }

        for (String file : paths) {
            String desDir = mPluginAPKDir.getAbsolutePath();
            String des = desDir + "/" + file;
            File desFile = new File(des);

            // TODO 目前是直接删除覆盖， 后续添加插件版本检查
            if (desFile.exists()) {
                desFile.delete();
            }
            PluginUtil.copyAssetsApkToFile(mContext, "plugins/" + file, des);

            loadPlugin(des);
        }
    }

    /**
     * 添加插件
     * @param pluginPath
     */
    private void loadPlugin(String pluginPath) {
        PackageInfo packageInfo = mContext.getPackageManager().getPackageArchiveInfo(pluginPath,
                PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        if (packageInfo == null) {
            return;
        }

        DexClassLoader dexClassLoader = PluginUtil.loadDex(mContext, pluginPath, mDexDir.getAbsolutePath(), mNativeLibDir);
        AssetManager assetManager = PluginUtil.loadAssetManager(pluginPath);
        Resources resources = PluginUtil.loadResources(mContext, assetManager);

        PluginHolder pluginHolder = new PluginHolder(dexClassLoader, resources, packageInfo);
        mPluginHolders.put(packageInfo.packageName, pluginHolder);
    }




    public PluginHolder getPluginHolder(String packageName) {
        return mPluginHolders.get(packageName);
    }

    public void startPluginActivity(Context context, String packageName, String activityName) {
        startPluginActivityForResult(context, packageName, activityName, -1);
    }

    public void startPluginActivityForResult(Context context, String packageName, String activityName, int requestCode) {
        if (TextUtils.isEmpty(packageName)) {
            throw new NullPointerException("packageName is null.");
        }

        PluginHolder holder = mPluginHolders.get(packageName);

        Class<?> clazz = loadPluginClass(holder.dexClassLoader, activityName);
        if(clazz == null) {
            throw new NullPointerException("can not found activity.");
        }

        Class<? extends Activity> activityClass = getProxyActivityClass(clazz);

        if (activityClass == null) {
            throw new NullPointerException("can not found activityClass.");
        }

        Intent intent = new Intent();
        intent.putExtra(ZConstants.EXTRA_CLASS, clazz.getName());
        intent.putExtra(ZConstants.EXTRA_PACKAGE, packageName);
        intent.setClass(context, activityClass);
        performStartActivityForResult(context, intent, requestCode);
    }


    /**
     * 获取classloader里的class
     * @param classLoader
     * @param className
     * @return
     */
    private Class<?> loadPluginClass(ClassLoader classLoader, String className) {
        Class<?> clazz = null;
        try {
//            clazz = Class.forName(className, true, classLoader);
            clazz = classLoader.loadClass(className);
            clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    private Class<? extends Activity> getProxyActivityClass(Class<?> clazz) {
        Class<? extends Activity> activityClass = null;
        if (ZPluginActivity.class.isAssignableFrom(clazz)) {
            activityClass = ZProxyActivity.class;
        }
        return activityClass;
    }

    private void performStartActivityForResult(Context context, Intent intent, int requestCode) {
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            context.startActivity(intent);
        }
    }
}

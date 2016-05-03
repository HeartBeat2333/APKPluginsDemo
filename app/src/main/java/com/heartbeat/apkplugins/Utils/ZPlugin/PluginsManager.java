package com.heartbeat.apkplugins.Utils.ZPlugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import dalvik.system.DexClassLoader;

/**
 * 插件加载管理类
 * 默认APK插件在Asset的plugins目录下
 * <p/>
 * TODO : 异步加载管理, KEY管理
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
            copyAssetsApkToFile(mContext, "plugins/" + file, des);

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

        DexClassLoader dexClassLoader = loadDex(pluginPath);
        AssetManager assetManager = loadAssetManager(pluginPath);
        Resources resources = loadResources(assetManager);

        PluginHolder pluginHolder = new PluginHolder(dexClassLoader, resources, packageInfo);
        mPluginHolders.put(packageInfo.packageName, pluginHolder);
    }

    /**
     * 加载dex
     * @param pluginPath
     * @return
     */
    public DexClassLoader loadDex(String pluginPath) {
        /*
        dexPath:被解压的apk路径，不能为空。
        optimizedDirectory：解压后的.dex文件的存储路径，不能为空。这个路径建议使用应用程序的私有路径，不要放到sdcard上，否则代码容易被注入攻击。
        libraryPath：os库的存放路径，可以为空，若有os库，必须填写。
        parent：父亲加载器，一般为context.getClassLoader(),使用当前上下文的类加载器。
         */
        DexClassLoader classloader = new DexClassLoader(
                pluginPath, mDexDir.getAbsolutePath(),
                mNativeLibDir,
                ClassLoader.getSystemClassLoader());
        return classloader;
    }

    /**
     * 加载Asset
     * 反射调用 addAssetPath 方法加载
     * @return
     */
    private AssetManager loadAssetManager(String pluginPath) {
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
    private Resources loadResources(AssetManager assetManager) {
        Resources superRes = mContext.getResources();
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
    public void copyAssetsApkToFile(Context context, String src, String des) {
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

    public PluginHolder getPluginHolder(String packageName) {
        return mPluginHolders.get(packageName);
    }
}

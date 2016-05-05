package com.heartbeat.apkplugins;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.heartbeat.zplugin.PluginsManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;

/**
 * 加载DEX文件示例
 */
public class LoadDexActivity extends Activity {
    private static final String TAG = "LoadDexActivity";
    private static final String OPT_DIR = "opt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_dex);
        PluginsManager
                .getInstance(this).loadAssetPlugins("plugins");
        final DexClassLoader classloader = PluginsManager
                .getInstance(this).getPluginHolder("com.heartbeat.myplugin").dexClassLoader;

        if(classloader != null) {
            try {
                Class<?> classToLoad = (Class<?>) classloader.loadClass("com.heartbeat.myplugin.Registry");

                Field classesField = classToLoad.getDeclaredField("_classes");

                ArrayList<Class<?>> classes = null;

                classes = (ArrayList<Class<?>>) classesField.get(null);
                for (Class<?> cls : classes) {
                    Log.v(TAG, "Class loaded " + cls.getName());
                    if (cls.getName().contains("ClassImpl")) {
                        Method m = cls.getMethod("method");
                        ClassLoader xb = (ClassLoader) m.invoke(null);
                        if (xb.equals(ClassLoader.getSystemClassLoader()))
                            Log.v(TAG, "Same ClassLoader");
                        else
                            Log.v(TAG, "Different ClassLoader");
                        Log.v("loadDexClasses", xb.toString());
                    }

                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}

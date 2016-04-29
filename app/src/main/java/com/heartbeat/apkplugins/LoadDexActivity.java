package com.heartbeat.apkplugins;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;

public class LoadDexActivity extends Activity {
    private static final String TAG = "LoadDexActivity";
    private static final String OPT_DIR = "opt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_dex);
        loadDexClassses();
    }

    private void loadDexClassses() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) return;
        String paths[] = null;
        try {
            paths = getAssets().list("plugins");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (paths == null) {
            Log.v(TAG, "找不到插件plugins " + paths);
            return;
        }

        Log.v(TAG, "插件准备完毕，开始加载！");

        for (String file : paths) {
            File pluginDir  = Environment.getExternalStorageDirectory();
            pluginDir.mkdirs();
            String desDir = pluginDir.getAbsolutePath();
            String des = desDir + "/" + "apkbeloaded-debug.apk";
            File desFile = new File(des);
            File optimizedDirectory = this.getDir(OPT_DIR, Context.MODE_PRIVATE);

            // 将apk从asset中拷贝到SD卡
            if (!desFile.exists()){
                copyAssetsApkToFile(this, "plugins/"+file, des);
            }

            /*
            dexPath:被解压的apk路径，不能为空。
            optimizedDirectory：解压后的.dex文件的存储路径，不能为空。这个路径建议使用应用程序的私有路径，不要放到sdcard上，否则代码容易被注入攻击。
            libraryPath：os库的存放路径，可以为空，若有os库，必须填写。
            parent：父亲加载器，一般为context.getClassLoader(),使用当前上下文的类加载器。
             */
            final DexClassLoader classloader = new DexClassLoader(
                    des, optimizedDirectory.getAbsolutePath(),
                    "data/local/tmp/natives/",
                    ClassLoader.getSystemClassLoader());

            Log.v(TAG, "扫描获取Registry");
             /*
              Apk中有Registry和ClassToBeImported两个类
              Registry维护了一个Class的集合, 其中有ClassToBeImported.class
              ClassToBeImported类中有个method方法， 返回ClassLoader
             */
            try {
                Class<?> classToLoad = (Class<?>) classloader.loadClass("com.dexclassdemo.liuguangli.apkbeloaded.Registry");

                Field classesField = classToLoad.getDeclaredField("_classes");

                ArrayList<Class<?>> classes = null;

                classes = (ArrayList<Class<?>>) classesField.get(null);
                for (Class<?> cls : classes) {
                    Log.v(TAG, "Class loaded " + cls.getName());
                    if (cls.getName().contains("ClassToBeImported")) {
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

}

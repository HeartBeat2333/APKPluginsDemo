package com.heartbeat.zplugin;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;

import java.lang.reflect.Constructor;

public class ZProxyActivity extends Activity {
    private static final String TAG = "ZProxyActivity";

    private String mClass;
    private String mPackageName;
    private PluginsManager mPluginManager;
    private PluginHolder mPluginHolder;

    private AssetManager mAssetManager;
    private Resources mResources;
    private Resources.Theme mTheme;

    private ActivityInfo mActivityInfo;
    protected IZPlugin mPluginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mPackageName = intent.getStringExtra(ZConstants.EXTRA_PACKAGE);
        mClass = intent.getStringExtra(ZConstants.EXTRA_CLASS);

        mPluginManager = PluginsManager.getInstance(this);
        mPluginHolder = mPluginManager.getPluginHolder(mPackageName);
        mAssetManager = mPluginHolder.assetManager;
        mResources = mPluginHolder.resources;

        initializeActivityInfo();
        handleActivityInfo();
        launchTargetActivity();
    }

    private void initializeActivityInfo() {
        PackageInfo packageInfo = mPluginHolder.packageInfo;
        if ((packageInfo.activities != null) && (packageInfo.activities.length > 0)) {
            if (mClass == null) { // 防止class为空， 打开第一个默认activity
                mClass = packageInfo.activities[0].name;
            }

            int defaultTheme = packageInfo.applicationInfo.theme;
            for (ActivityInfo a : packageInfo.activities) {
                if (a.name.equals(mClass)) {
                    mActivityInfo = a;
                    if (mActivityInfo.theme == 0) {
                        if (defaultTheme != 0) {
                            mActivityInfo.theme = defaultTheme;
                        } else {
                            if (Build.VERSION.SDK_INT >= 14) {
                                mActivityInfo.theme = android.R.style.Theme_DeviceDefault;
                            } else {
                                mActivityInfo.theme = android.R.style.Theme;
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * 主题
     */
    private void handleActivityInfo() {
        if (mActivityInfo.theme > 0) {
            setTheme(mActivityInfo.theme);
        }
        Resources.Theme superTheme = getTheme();
        mTheme = mResources.newTheme();
        mTheme.setTo(superTheme);
        try {
            mTheme.applyStyle(mActivityInfo.theme, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected void launchTargetActivity() {
        try {
            Class<?> localClass = getClassLoader().loadClass(mClass);
            Constructor<?> localConstructor = localClass.getConstructor(new Class[] {});
            Object instance = localConstructor.newInstance(new Object[] {});
            mPluginActivity = (IZPlugin) instance;
            Log.d(TAG, "instance = " + instance);
            mPluginActivity.attach(this, mPluginHolder);

            Bundle bundle = new Bundle();
            bundle.putInt(ZConstants.FROM, ZConstants.FROM_EXTERNAL);
            mPluginActivity.onCreate(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ClassLoader getClassLoader() {
        return mPluginHolder.dexClassLoader;
    }

    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    public Resources.Theme getTheme() {
        return mTheme == null ? super.getTheme() : mTheme;
    }

    @Override
    protected void onStart() {
        mPluginActivity.onStart();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        mPluginActivity.onRestart();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        mPluginActivity.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mPluginActivity.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mPluginActivity.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mPluginActivity.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mPluginActivity.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mPluginActivity.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mPluginActivity.onNewIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        mPluginActivity.onBackPressed();
        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return mPluginActivity.onTouchEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        super.onKeyUp(keyCode, event);
        return mPluginActivity.onKeyUp(keyCode, event);
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        mPluginActivity.onWindowAttributesChanged(params);
        super.onWindowAttributesChanged(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mPluginActivity.onWindowFocusChanged(hasFocus);
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mPluginActivity.onCreateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPluginActivity.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

}

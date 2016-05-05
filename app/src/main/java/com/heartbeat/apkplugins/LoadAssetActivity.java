package com.heartbeat.apkplugins;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.heartbeat.zplugin.PluginsManager;

/**
 * 加载Asset资源示例
 */
public class LoadAssetActivity extends Activity {

    private android.widget.Button btnActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_asset);
        this.btnActivity = (Button) findViewById(R.id.btn_activity);
        PluginsManager
                .getInstance(this).loadAssetPlugins("plugins");
        Resources resources =PluginsManager
                .getInstance(this).getPluginHolder("com.heartbeat.myplugin").resources;
        /**
         *获取图片资源
         */
//        Drawable drawable = resources.getDrawable(resources.getIdentifier("ic_launcher", "drawable",
//                "com.zhouyuan.plugin"));

        final String appName = resources.getString(resources.getIdentifier("app_name", "string", "com.heartbeat.myplugin"));

        btnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoadAssetActivity.this, appName, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

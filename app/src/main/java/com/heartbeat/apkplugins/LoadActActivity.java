package com.heartbeat.apkplugins;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.heartbeat.zplugin.PluginsManager;

/**
 * 加载Activity示例
 */
public class LoadActActivity extends Activity {

    private Button btnactivity;
    private PackageInfo mPluginPackgeInfo;
    private PluginsManager mPluginsManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_act);
        initView();
        // 会有内存泄漏的问题, 这只是个例子所以简单做
        mPluginsManger = PluginsManager.getInstance(this);
        mPluginsManger.loadAssetPlugins("plugins");
        mPluginPackgeInfo = mPluginsManger.getPluginHolder("com.heartbeat.myplugin").packageInfo;
    }

    private void initView() {
        this.btnactivity = (Button) findViewById(R.id.btn_activity);
        btnactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPluginsManger.startPluginActivity(LoadActActivity.this
                        , mPluginPackgeInfo.packageName, mPluginPackgeInfo.activities[0].name);
            }
        });
    }

}

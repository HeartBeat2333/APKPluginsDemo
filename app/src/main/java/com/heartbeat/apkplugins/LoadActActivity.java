package com.heartbeat.apkplugins;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 加载Activity示例
 */
public class LoadActActivity extends Activity {

    private Button btnactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_act);
        this.btnactivity = (Button) findViewById(R.id.btn_activity);

        btnactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}

package com.heartbeat.apkplugins;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private android.widget.Button btnDexclass;
    private android.widget.Button btnAsset;
    private android.widget.Button btnActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnActivity = (Button) findViewById(R.id.btn_activity);
        this.btnAsset = (Button) findViewById(R.id.btn_asset);
        this.btnDexclass = (Button) findViewById(R.id.btn_dex_class);

        setOnClickListener(btnActivity, btnAsset, btnDexclass);
    }

    private void setOnClickListener(View... views) {
        for (View view :
                views) {
            view.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_dex_class:
                intent = new Intent(MainActivity.this, LoadDexActivity.class);
                break;
            case R.id.btn_asset:
                intent = new Intent(MainActivity.this, LoadAssetActivity.class);
                break;
            case R.id.btn_activity:
                intent = new Intent(MainActivity.this, LoadActActivity.class);
                break;
            default:
                break;
        }
        if(intent != null) {
            startActivity(intent);
        }
    }
}

package com.heartbeat.myplugin;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.heartbeat.zplugin.ZPluginActivity;

public class MainTestActivity extends ZPluginActivity {

    private Button btntry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that.setContentView(R.layout.activity_main);
        initView(that);
//        that.setContentView(creatContentView(that));
    }

    private View creatContentView(final Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        Button button = new Button(context);
        button.setText("Invoke host method");
        layout.addView(button, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "测试", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView = new TextView(context);
        textView.setText("我是一个萌萌的插件");
        textView.setTextSize(30);
        layout.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        return layout;
    }

    /**
     * 插件需要用that
     * @param context
     */
    private void initView(final Context context) {
        btntry = (Button) findViewById(R.id.btn_try);
        btntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "试试就试试", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

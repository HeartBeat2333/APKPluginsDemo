package com.heartbeat.apkplugins.Utils;

import android.widget.Toast;

/**
 * 吐司工具类
 * @author zhudeshuai
 * @date 2015/12/2
 */
public class ToastUtil {
    public static Toast toast;

    public static void show(String text) {
        if (toast == null) {
            toast = Toast.makeText(ContextProvider.getContext(), text, Toast.LENGTH_SHORT);
        }
        toast.setText(text);
        toast.show();
    }

    public static void show(int resStr) {
        if (toast == null) {
            toast = Toast.makeText(ContextProvider.getContext(), resStr, Toast.LENGTH_SHORT);
        }
        toast.setText(resStr);
        toast.show();
    }
}

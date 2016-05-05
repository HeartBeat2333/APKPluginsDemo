package com.heartbeat.myplugin;

import android.util.Log;

/**
 * Created by zhouyuan on 2016/5/3.
 */
public class ClassImpl {
    public static ClassLoader method(){
        Log.i("ClassImpl", "调用 method 成功！");
        return ClassImpl.class.getClassLoader();
    }
}

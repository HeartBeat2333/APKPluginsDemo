package com.heartbeat.myplugin;

import java.util.ArrayList;

/**
 * class 注册集合
 *
 * Created by zhouyuan on 2016/5/3.
 */
public class Registry {
    public static ArrayList<Class<?>> _classes = new ArrayList<Class<?>>();
    static{
        _classes.add(ClassImpl.class);
        // 可添加更多Class
    }
}

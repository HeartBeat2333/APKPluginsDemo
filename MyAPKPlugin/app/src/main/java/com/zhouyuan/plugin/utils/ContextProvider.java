package com.zhouyuan.plugin.utils;

import android.app.Application;
import android.content.Context;

/**
 * 上下文提供者
 * @author zhudeshuai
 * @date 2015/11/17
 */
public class ContextProvider {
    private static Context mContext;
    public static void init(Context context){
        if(!(context instanceof Application)){
            throw new IllegalArgumentException("the parameter must be application");
        }
        mContext = context;
    }
    public static Context getContext(){
        return mContext;
    }
}

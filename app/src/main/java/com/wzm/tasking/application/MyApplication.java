package com.wzm.tasking.application;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by yyj on 2017/8/6 0006.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.init(this);
        JMessageClient.init(this);
    }
}

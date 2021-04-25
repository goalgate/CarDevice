package com.example.cardevice.app;

import android.content.Context;
import com.example.cardevice.BuildConfig;
import com.example.cardevice.R;
import com.example.cardevice.ui.login.LoginActivity;
import com.squareup.leakcanary.LeakCanary;

import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.crash.CaocConfig;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * @author Created by WZW on 2021-04-19 9:09.
 * @description
 */
public class AppInit extends BaseApplication {

    protected static AppInit instance;

    public static AppInit getInstance() {
        return instance;
    }

    public static Context getContext() {
        return getInstance().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        //是否开启打印日志
        KLog.init(BuildConfig.DEBUG);

        //初始化全局异常崩溃
        initCrash();
        //内存泄漏检测
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }
    }

    private void initCrash() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(LoginActivity.class) //重新启动后的activity
//                .errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
//                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }
}

package com.nsu.edu.androidmvpdemo.login;

import android.app.Application;
import android.content.Context;

import com.nsu.edu.androidmvpdemo.login.config.MyConstants;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.RxRetrofitApp;


/**
 * Created by WZG on 2016/10/25.
 */

public class MyApplication extends Application {
    public static Context app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = getApplicationContext();
        RxRetrofitApp.init(this, (MyConstants.AppRunModel != MyConstants.RunModel.PRO));
    }
}

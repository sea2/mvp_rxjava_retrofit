package com.wzgiceman.rxretrofitlibrary.retrofit_rx;

import android.app.Application;
import android.content.Context;

/**
 * 全局app
 * Created by WZG on 2016/12/12.
 */

public class RxRetrofitApp {
    private static Application application;
    private static boolean isDebug;
    private static String baseUrl;

    public static void init(Application app, boolean debug, String baseUrlFrom) {
        setApplication(app);
        isDebug = debug;
        baseUrl = baseUrlFrom;
    }

    public static Application getApplication() {
        return application;
    }

    public static Context getContext() {
        return application.getApplicationContext();
    }

    private static void setApplication(Application application) {
        RxRetrofitApp.application = application;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }


}

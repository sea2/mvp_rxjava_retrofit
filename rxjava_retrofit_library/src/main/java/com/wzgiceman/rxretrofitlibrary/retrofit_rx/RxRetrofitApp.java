package com.wzgiceman.rxretrofitlibrary.retrofit_rx;

import android.app.Application;

/**
 * 全局app
 * Created by WZG on 2016/12/12.
 */

public class RxRetrofitApp {
    private static Application application;
    private static boolean isDebug;

    public static void init(Application app, boolean debug) {
        setApplication(app);
        isDebug = debug;
    }

    public static Application getApplication() {
        return application;
    }

    private static void setApplication(Application application) {
        RxRetrofitApp.application = application;
    }

    public static boolean isDebug() {
        return isDebug;
    }


}

package com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils;

import android.util.Log;

import com.wzgiceman.rxretrofitlibrary.retrofit_rx.RxRetrofitApp;


public class LogManager {

    public static boolean isLogOpen = RxRetrofitApp.isDebug();
    private static String TAG = "com.log";
    private static String TAG_http = "http_json_out";

    public static void i(String msg) {
        if (isLogOpen && null != msg) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String TagMsg, String msg) {
        if (isLogOpen && null != msg) {
            if (TagMsg == null)
                Log.i(TAG, msg);
            else Log.i(TagMsg, msg);
        }
    }

    public static void http_i(String TagMsg, String msg) {
        if (isLogOpen && null != msg) {
            if (TagMsg == null)
                Log.i(TAG_http, msg);
            else Log.i(TagMsg, msg);
        }
    }

    public static void http_i(String msg) {
        if (isLogOpen && null != msg) {
            Log.i(TAG_http, msg);
        }
    }

    public static void d(String msg) {
        if (isLogOpen && null != msg) {
            Log.d(TAG, msg);
        }
    }

    public static void d(Class clazz, String msg) {
        if (isLogOpen && null != msg) {
            Log.d(clazz.getSimpleName(), msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isLogOpen && null != msg && tag != null) {
            Log.d(tag, msg);
        }
    }

    public static void e(String msg) {
        if (isLogOpen && null != msg) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isLogOpen && null != msg && tag != null) {
            Log.e(tag, msg);
        }
    }

}

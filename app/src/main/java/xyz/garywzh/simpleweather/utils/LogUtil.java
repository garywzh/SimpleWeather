package xyz.garywzh.simpleweather.utils;

import android.util.Log;

import xyz.garywzh.simpleweather.BuildConfig;


public class LogUtil {
    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void wtf(String tag, String msg) {
        Log.wtf(tag, msg);
    }
}

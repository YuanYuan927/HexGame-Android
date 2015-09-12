package com.yuan.hexgame.util;

import android.util.Log;

import com.yuan.hexgame.BuildConfig;

/**
 * Created by Yuan Sun on 2015/9/12.
 */
public final class LogUtil {

    private LogUtil() {

    }

    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, buildMessage(msg));
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, buildMessage(msg), tr);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, buildMessage(msg));
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, buildMessage(msg), tr);
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, buildMessage(msg));
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, buildMessage(msg), tr);
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, buildMessage(msg));
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, buildMessage(msg), tr);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, buildMessage(msg));
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, buildMessage(msg), tr);
        }
    }

    private static String buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];

        return new StringBuilder().append(caller.getMethodName()).append("(): ")
                .append(msg).toString();
    }
}

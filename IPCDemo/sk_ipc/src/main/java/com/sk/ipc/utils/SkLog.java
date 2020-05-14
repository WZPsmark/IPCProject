package com.sk.ipc.utils;

import android.util.Log;

import com.sk.ipc.BuildConfig;

/**
 * Created by smark on 2020/5/14.
 * 邮箱：smarkwzp@163.com
 */
public class SkLog {
    private static final String TAG = "SkLog";

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, msg);
        }
    }
    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }
}

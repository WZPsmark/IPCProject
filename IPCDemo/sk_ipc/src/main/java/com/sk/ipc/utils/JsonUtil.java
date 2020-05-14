package com.sk.ipc.utils;

import com.google.gson.Gson;

/**
 * Created by smark on 2020/5/14.
 * 邮箱：smarkwzp@163.com
 * Gson使用工具类
 */
public class JsonUtil {
    private static Gson mGson = new Gson();

    public static String toJson(Object src) {
        return mGson.toJson(src);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return mGson.fromJson(json, classOfT);
    }
}

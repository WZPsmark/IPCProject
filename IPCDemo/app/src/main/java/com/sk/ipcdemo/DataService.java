package com.sk.ipcdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.sk.ipc.SkIPC;

/**
 * Created by smark on 2020/5/14.
 * 邮箱：smarkwzp@163.com
 */
public class DataService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DataProvider.getInstance().setDataBean(new DataBean(1, "小华", 32));
        DataProvider.getInstance().setDataBean(new DataBean(2, "小红", 25));
        DataProvider.getInstance().setDataBean(new DataBean(3, "小黄", 35));

        /**
         * 服务注册
         */
        SkIPC.register(DataProvider.class);
    }
}

package com.sk.ipc;

import android.util.Log;

import com.sk.ipc.model.Request;
import com.sk.ipc.model.Response;
import com.sk.ipc.utils.JsonUtil;
import com.sk.ipc.utils.SkLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by smark on 2020/5/14.
 * 邮箱：smarkwzp@163.com
 * 数据提供实现类实际获取数据动态代理实现
 */
public class IPCInvocationHandler implements InvocationHandler {
    private final Class<? extends IPCService> service;
    private final Class<?> interfaceClass;

    public IPCInvocationHandler(Class<? extends IPCService> service, Class<?> interfaceClass) {
        this.service = service;
        this.interfaceClass = interfaceClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Response response = DataChannel.getInstance()
                .sendRequest(Request.GET_METHOD, service, interfaceClass, method.getName(), args);
        if (response.isSuccess()) {
            Class<?> returnType = method.getReturnType();
            if (returnType != Void.class && returnType != void.class) {
                String source = response.getSource();
                return JsonUtil.fromJson(source, returnType);
            }
        }else{
            SkLog.e(response.getSource());
        }
        return null;
    }
}

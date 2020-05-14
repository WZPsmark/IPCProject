package com.sk.ipc;

import android.content.Context;

import com.sk.ipc.model.Request;
import com.sk.ipc.model.Response;
import com.sk.ipc.utils.SkLog;

import java.lang.reflect.Proxy;

import static com.sk.ipc.utils.StringConstant.*;

/**
 * Created by smark on 2020/5/14.
 * 邮箱：smarkwzp@163.com
 * 对外提供使用服务注册与通讯类
 */
public class SkIPC {

    /**
     * 对外提供的服务端需要暴露的服务注册接口（服务端使用）
     *
     * @param service
     */
    public static void register(Class<?> service) {
        ServiceRegistry.getInstance().register(service);
    }


    /**
     * 连接本APP下其他进程服务
     *
     * @param context
     * @param service
     */
    public static void connect(Context context, Class<? extends IPCService> service) {
        connect(context, null, service);
    }


    /**
     * 连接其他APP下进程服务
     *
     * @param context
     * @param packageName
     * @param service
     */
    public static void connect(Context context, String packageName, Class<? extends IPCService> service) {
        DataChannel.getInstance().bind(context.getApplicationContext(), packageName, service);
    }


    /**
     * 取消服务连接
     *
     * @param context
     * @param service
     */
    public static void disConnect(Context context, Class<? extends IPCService> service) {
        DataChannel.getInstance().unBind(context, service);
    }


    /**
     * 通过数据提供实现类提供默认单例方法名getInstance获取其实例对象
     *
     * @param service
     * @param interfaceClass
     * @param parameters
     * @param <T>
     * @return
     */
    public static <T> T getInstanceWithNormal(Class<? extends IPCService> service, Class<T> interfaceClass, Object... parameters) {

        return getInstanceWithName(service, interfaceClass, "getInstance", parameters);
    }


    /**
     * 通过数据提供实现类提供具体单例方法名获取其实例对象
     *
     * @param service
     * @param interfaceClass
     * @param methodName
     * @param parameters
     * @param <T>
     * @return
     */
    public static <T> T getInstanceWithName(Class<? extends IPCService> service, Class<T> interfaceClass, String methodName, Object...
            parameters) {
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException(INTERFACE_ERROR);
        }
        Response response = DataChannel.getInstance().sendRequest(Request.GET_INSTANCE, service,
                interfaceClass, methodName, parameters);
        if (response.isSuccess()) {
            return getProxy(interfaceClass, service);
        } else {
            SkLog.e(response.getSource());
        }
        return null;
    }


    private static <T> T getProxy(Class<T> interfaceClass, Class<? extends IPCService> service) {
        ClassLoader classLoader = interfaceClass.getClassLoader();
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{interfaceClass}, new IPCInvocationHandler(service, interfaceClass));
    }

}

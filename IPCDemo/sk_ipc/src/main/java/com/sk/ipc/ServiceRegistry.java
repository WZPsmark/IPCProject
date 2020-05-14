package com.sk.ipc;

import com.sk.ipc.annotation.ServiceId;
import com.sk.ipc.utils.SkLog;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by smark on 2020/5/14.
 * 邮箱：smarkwzp@163.com
 * 记录服务端注册信息
 */
public class ServiceRegistry {

    private static volatile ServiceRegistry mInstance;
    /**
     * 注册服务记录表
     */
    private ConcurrentHashMap<String, Class<?>> mServices = new ConcurrentHashMap<>();

    /**
     * 所注册服务对应的方法表
     */
    private ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>> mMethods =
            new ConcurrentHashMap<>();

    /**
     * 数据提供接口实现类所对应的单例对象
     */
    private ConcurrentHashMap<String, Object> objects = new ConcurrentHashMap<>();


    public static ServiceRegistry getInstance() {
        if (null == mInstance) {
            synchronized (ServiceRegistry.class) {
                if (null == mInstance) {
                    mInstance = new ServiceRegistry();
                }
            }
        }
        return mInstance;
    }


    /**
     * 解析与存储服务信息
     *
     * @param service
     */
    public void register(Class<?> service) {

        ServiceId serviceId = service.getAnnotation(ServiceId.class);
        if (null == serviceId) {
            throw new RuntimeException("需要注册的服务请使用ServiceId进行注解!");
        }
        String serviceIdStr = serviceId.value();
        //注册服务保存
        mServices.put(serviceIdStr, service);

        ConcurrentHashMap<String, Method> methods = mMethods.get(service);
        if (null == methods) {
            methods = new ConcurrentHashMap<>();
            mMethods.put(service, methods);
        }
        StringBuilder stringBuilder = new StringBuilder(25);
        //根据方法名+参数的方式记录所有方法，防止方法重载
        for (Method method : service.getMethods()) {
            stringBuilder.delete(0, stringBuilder.length());
            stringBuilder.append(method.getName());
            stringBuilder.append("(");
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 0) {
                stringBuilder.append(encasementFundamentalType(parameterTypes[0].getName()));
            }
            for (int i = 1; i < parameterTypes.length; i++) {
                stringBuilder.append(",").append(encasementFundamentalType(parameterTypes[i].getName()));
            }
            stringBuilder.append(")");
            methods.put(stringBuilder.toString(), method);
        }
    }


    /**
     * 对8种基本数据类型参数进行单独处理
     *
     * @param type
     * @return
     */
    private String encasementFundamentalType(String type) {
        String typeStr = type;
        switch (type) {
            case "int":
                typeStr = Integer.class.getName();
                break;
            case "boolean":
                typeStr = Boolean.class.getName();
                break;
            case "long":
                typeStr = Long.class.getName();
                break;
            case "byte":
                typeStr = Byte.class.getName();
                break;
            case "char":
                typeStr = Character.class.getName();
                break;
            case "float":
                typeStr = Float.class.getName();
                break;
            case "double":
                typeStr = Double.class.getName();
                break;
            case "short":
                typeStr = Short.class.getName();
                break;
        }
        return typeStr;
    }

    /**
     * 通过serviceId 方法名，参数列表找出执行的方法
     *
     * @param serviceId
     * @param methodName
     * @param objects
     * @return
     */
    public Method findMethod(String serviceId, String methodName, Object[] objects) {

        Class<?> service = mServices.get(serviceId);
        ConcurrentHashMap<String, Method> methods = mMethods.get(service);
        StringBuilder builder = new StringBuilder(methodName);
        builder.append("(");
        if (objects.length != 0) {
            builder.append(objects[0].getClass().getName());
        }

        for (int i = 1; i < objects.length; i++) {
            builder.append(",").append(objects[i].getClass().getName());
        }
        builder.append(")");
        SkLog.e(builder.toString());

        return methods.get(builder.toString());
    }


    /**
     * 存储服务实现方法的单例对象
     *
     * @param serviceId
     * @param object
     */
    public void putInstanceObject(String serviceId, Object object) {
        objects.put(serviceId, object);
    }

    /**
     * 获取服务实现方法的单例对象
     *
     * @param serviceId
     * @return
     */
    public Object getInstanceObject(String serviceId) {
        return objects.get(serviceId);
    }
}

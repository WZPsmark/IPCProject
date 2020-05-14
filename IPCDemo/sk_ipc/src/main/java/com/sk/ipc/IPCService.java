package com.sk.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.sk.ipc.model.Parameters;
import com.sk.ipc.model.Request;
import com.sk.ipc.model.Response;
import com.sk.ipc.utils.JsonUtil;
import com.sk.ipc.utils.SkLog;

import java.lang.reflect.Method;

import static com.sk.ipc.utils.StringConstant.*;

/**
 * Created by smark on 2020/5/14.
 * 邮箱：smarkwzp@163.com
 * 通讯服务实现
 */
public abstract class IPCService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IIPCService.Stub() {
            @Override
            public Response send(Request request) throws RemoteException {
                String methodName = request.getMethodName();
                Parameters[] parameters = request.getParameters();
                String serviceId = request.getServiceId();
                Object[] objects = restoreParameters(parameters);
                Method method = ServiceRegistry.getInstance().findMethod(serviceId, methodName, objects);
                if(null==method){
                    return new Response(NO_METHOD_ERROR, false);
                }
                switch (request.getType()) {
                    case Request.GET_INSTANCE:
                        try {
                            Object result = method.invoke(null, objects);
                            ServiceRegistry.getInstance().putInstanceObject(serviceId, result);
                            return new Response(INSTANCE_METHOD_CALL_SUCCESS, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new Response(INSTANCE_METHOD_CALL_FAIL, false);
                        }
                    case Request.GET_METHOD:
                        Object instanceObject = ServiceRegistry.getInstance().getInstanceObject(serviceId);
                        try {
                            Object result = method.invoke(instanceObject, objects);
                            return new Response(JsonUtil.toJson(result), true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new Response(GENERAL_METHOD_CALL_FAIL, false);
                        }

                }
                return new Response(NO_METHOD_TYPE_ERROR,false);
            }
        };
    }

    /**
     * 还原参数
     *
     * @param parameters
     * @return
     */
    private Object[] restoreParameters(Parameters[] parameters) {
        Object[] objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameters parameter = parameters[i];
            try {
                objects[i] = JsonUtil.fromJson(parameter.getValue(), Class.forName(parameter.getType()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return objects;
    }


    /***************提供给不同业务服务使用（超过10个的话只能自己实现此服务进行拓展）****************/

    public static class IPCService0 extends IPCService {
    }

    public static class IPCService1 extends IPCService {
    }

    public static class IPCService2 extends IPCService {
    }

    public static class IPCService3 extends IPCService {
    }

    public static class IPCService4 extends IPCService {
    }

    public static class IPCService5 extends IPCService {
    }

    public static class IPCService6 extends IPCService {
    }

    public static class IPCService7 extends IPCService {
    }

    public static class IPCService8 extends IPCService {
    }

    public static class IPCService9 extends IPCService {
    }
}

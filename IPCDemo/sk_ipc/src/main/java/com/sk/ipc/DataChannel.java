package com.sk.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.sk.ipc.annotation.ServiceId;
import com.sk.ipc.model.Parameters;
import com.sk.ipc.model.Request;
import com.sk.ipc.model.Response;
import com.sk.ipc.utils.JsonUtil;

import java.util.concurrent.ConcurrentHashMap;

import static com.sk.ipc.utils.StringConstant.*;

/**
 * Created by smark on 2020/5/14.
 * 邮箱：smarkwzp@163.com
 * 服务绑定与数据传输
 */
public class DataChannel {

    /**
     * 记录此服务是否已经绑定
     */
    private ConcurrentHashMap<Class<? extends IPCService>, Boolean> mBinds =
            new ConcurrentHashMap<>();
    /**
     * 记录此服务是否正在绑定
     */
    private ConcurrentHashMap<Class<? extends IPCService>, Boolean> mBinding =
            new ConcurrentHashMap<>();
    /**
     * 已经绑定的服务对应的ServiceConnect
     */
    private final ConcurrentHashMap<Class<? extends IPCService>, IPCServiceConnection> mServiceConnections =
            new ConcurrentHashMap<>();

    /**
     * 记录已经绑定过的服务
     */
    private final ConcurrentHashMap<Class<? extends IPCService>, IIPCService> mBinders =
            new ConcurrentHashMap<>();

    private static volatile DataChannel mInstance;

    public static DataChannel getInstance() {
        if (null == mInstance) {
            synchronized (DataChannel.class) {
                if (null == mInstance) {
                    mInstance = new DataChannel();
                }
            }
        }
        return mInstance;
    }


    /**
     * 服务绑定
     *
     * @param context
     * @param packageName
     * @param service
     */
    public void bind(Context context, String packageName, Class<? extends IPCService> service) {
        IPCServiceConnection ipcServiceConnection;
        Boolean isBind = mBinds.get(service);
        if (null != isBind && isBind) {
            return;
        }
        Boolean isBinding = mBinding.get(service);
        if (null != isBinding && isBinding) {
            return;
        }
        mBinding.put(service, true);
        ipcServiceConnection = new IPCServiceConnection(service);
        mServiceConnections.put(service, ipcServiceConnection);

        Intent intent;
        if (TextUtils.isEmpty(packageName)) {
            intent = new Intent(context, service);
        } else {
            intent = new Intent();
            intent.setClassName(packageName, service.getName());
        }
        context.bindService(intent, ipcServiceConnection, Context.BIND_AUTO_CREATE);
    }


    /**
     * 服务解绑
     *
     * @param context
     * @param service
     */
    public void unBind(Context context, Class<? extends IPCService> service) {
        Boolean isBind = mBinds.get(service);
        if (isBind != null && isBind) {
            IPCServiceConnection connection = mServiceConnections.get(service);
            if (connection != null) {
                context.unbindService(connection);
                mServiceConnections.remove(service);
            }
            mBinds.put(service, false);

        }

    }

    /**
     * 服务绑定回调
     */
    private class IPCServiceConnection implements ServiceConnection {


        private final Class<? extends IPCService> mService;

        public IPCServiceConnection(Class<? extends IPCService> service) {
            mService = service;
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IIPCService ipcService = IIPCService.Stub.asInterface(iBinder);
            mBinders.put(mService, ipcService);
            mBinds.put(mService, true);
            mBinding.remove(mService);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBinders.remove(mService);
            mBinds.remove(mService);
        }
    }


    /**
     * 向服务提供者发送请求
     *
     * @param type
     * @param service
     * @param classType
     * @param methodName
     * @param parameters
     * @return
     */
    public Response sendRequest(int type, Class<? extends IPCService> service, Class<?> classType,
                                String methodName, Object[] parameters) {

        IIPCService iipcService = mBinders.get(service);
        if (null == iipcService) {
            return new Response(SERVICE_NOT_BIND, false);
        }

        ServiceId serviceIdAnnotation = classType.getAnnotation(ServiceId.class);
        String serviceId = serviceIdAnnotation.value();
        Request request = new Request(type, serviceId, methodName, makeParameters(parameters));
        try {
            return iipcService.send(request);
        } catch (RemoteException e) {
            e.printStackTrace();
            return new Response(REQUEST_SEND_FAIL, false);
        }

    }


    /**
     * 将参数制作为Parameters
     *
     * @param parameters
     * @return
     */
    private Parameters[] makeParameters(Object[] parameters) {
        Parameters[] p;
        if (null != parameters) {
            p = new Parameters[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object object = parameters[i];
                p[i] = new Parameters(object.getClass().getName(), JsonUtil.toJson(object));
            }
        } else {
            p = new Parameters[0];
        }
        return p;
    }
}

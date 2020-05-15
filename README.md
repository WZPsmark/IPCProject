# IPCProject
一个基于aidl实现的IPC进程间通讯框架，可以帮你从繁琐复杂的进程间通讯和aidl代码中解脱！

框架使用：



```
implementation project(":sk_ipc")
```



使用步骤：

1、创建自己的数据提供服务并注册，同时注册框架提供的IPCService及其是使用的服务子类用于通讯，并确保和你自己的服务处于同一个进程，如下：

```
<service
    android:name=".DataService"
    android:exported="true"
    android:process=":data"></service>

<service
    android:name="com.sk.ipc.IPCService$IPCService0"
    android:exported="true"
    android:process=":data" />
```

DataService为你所需要提供数据的服务进程。



2、创建你提供的数据接口和其实现类，并使用框架提供的@serviceId注解进行标识，同时传入相同的serviceId，注意：数据提供接口与实现类serviceId需要相同。并确保方法名相同，如下所示：

数据提供接口：

```
@ServiceId("DataProvider")
public interface IDataProvider {

    DataBean getData();

    DataBean getDataByUserId(int userId);

}
```

数据提供实现类：

```
@ServiceId("DataProvider")
public class DataProvider {
    private List<DataBean> mList;
    private static DataProvider instance;

    DataProvider() {
        mList = new ArrayList<>();
    }

    public static DataProvider getInstance() {
        if (null == instance) {
            synchronized (DataProvider.class) {
                if (null == instance) {
                    instance = new DataProvider();
                }
            }
        }
        return instance;
    }

    public void setDataBean(DataBean dataBean) {
        mList.add(dataBean);
    }

    public DataBean getData() {
        DataBean dataBean = null;
        if (mList.size() > 0) {
            dataBean = mList.get(0);
        } else {
            dataBean = new DataBean(1, "小华", 25);
        }
        return dataBean;
    }


    public DataBean getDataByUserId(int userId) {
        DataBean dataBean = null;
        if (mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).userId == userId) {
                    return mList.get(i);
                }
            }
            return new DataBean(userId, "晓龙", 32);
        }
        return new DataBean(userId, "晓龙", 32);

    }


}
```

3、在自己的数据提供服务内进行注册并启动你的服务进程：

```
SkIPC.register(DataProvider.class);

```

```
  startService(new Intent(this, DataService.class));
```

至此，服务端便搞定了，接下来看客户端：

同一APP内多进程通讯：

首先进行服务绑定：

```
SkIPC.connect(this, IPCService.IPCService0.class);
```

接下来就是与服务端进行通讯了：

```
IDataProvider provider =SkIPC.getInstanceWithNormal(IPCService.IPCService0.class,IDataProvider.class);
Toast.makeText(this,provider.getDataByUserId(1).toString(),Toast.LENGTH_SHORT).show();
```



不同app内通讯（传参获取数据）：

绑定需要填入服务端包名：

```
SkIPC.connect(this, "com.sk.ipcdemo",IPCService.IPCService0.class);
```

数据获取：

```
IDataProvider provider =SkIPC.getInstanceWithNormal(IPCService.IPCService0.class,IDataProvider.class);
Toast.makeText(this,provider.getData().toString(),Toast.LENGTH_SHORT).show();
```

注意：在不同app内通讯需要保证两边的数据提供接口与数据bean类保持一致就可以了，PS：不用在于aidl中蛋疼的包名问题啦！



想要使用的同学可以下载demo自己跑跑看，测试下，如果觉得好用的话，随手点个star吧！！！
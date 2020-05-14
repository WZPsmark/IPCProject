package com.sk.client;

import com.sk.ipc.annotation.ServiceId;

/**
 * Created by smark on 2020/5/14.
 * 邮箱：smarkwzp@163.com
 */
@ServiceId("DataProvider")
public interface IDataProvider {

    DataBean getData();

    DataBean getDataByUserId(int userId);
}

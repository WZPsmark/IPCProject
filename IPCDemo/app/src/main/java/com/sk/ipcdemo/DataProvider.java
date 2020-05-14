package com.sk.ipcdemo;

import com.sk.ipc.annotation.ServiceId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smark on 2020/5/14.
 * 邮箱：smarkwzp@163.com
 */
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

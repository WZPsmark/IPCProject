package com.sk.client;

import androidx.annotation.NonNull;

/**
 * Created by smark on 2020/5/14.
 * 邮箱：smarkwzp@163.com
 */
public class DataBean {
    int userId;
    String name;
    int age;

    public DataBean() {

    }

    public DataBean(int userId, String name, int age) {
        this.userId = userId;
        this.name = name;
        this.age = age;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @NonNull
    @Override
    public String toString() {
        return "数据为：name=" + name + ",age=" + age + ",userId=" + userId;
    }
}

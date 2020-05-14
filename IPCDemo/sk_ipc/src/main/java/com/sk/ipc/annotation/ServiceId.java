package com.sk.ipc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by smark on 2020/5/14.
 * 邮箱：smarkwzp@163.com
 * 用于服务标识的注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceId {
    String value();
}

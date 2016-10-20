package com.kz.android.bean;

/**
 * Created by 柯壮 on 2016/7/18.
 * Httpbean的基类
 */
public abstract class HttpBean {
    public String key;
    public String value;

    public HttpBean(String key, String value) {
        this.key = key;
        this.value = value;
    }
}

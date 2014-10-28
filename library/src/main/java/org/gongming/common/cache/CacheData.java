package org.gongming.common.cache;

import java.io.Serializable;

public class CacheData<T> implements Serializable {

    private long lastUpdated;
    private String key;
    private T data;
    private long expiration;

    public CacheData(String key, T data) {
        this(key, data, -1);
    }
    public CacheData(String key, T data, long expiration) {
        this.key = key;
        this.expiration = expiration;
        setData(data);
    }

    public void setData(T data) {
        this.data = data;
        this.lastUpdated = System.currentTimeMillis();
    }

    public boolean isValid() {
        return expiration == -1 || lastUpdated + expiration > System.currentTimeMillis();
    }

    public String getKey() {
        return key;
    }

    public T getData() {
        return data;
    }
}

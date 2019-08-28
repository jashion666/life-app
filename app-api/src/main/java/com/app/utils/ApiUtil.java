package com.app.utils;

import com.app.enums.HttpEnums;
import com.app.redis.RedisClient;
import com.app.utils.http.ProxyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author :wkh.
 * @date :2019/8/28.
 */
public class ApiUtil {

    /**
     * 获取redis 代理ip集合
     */
    @SuppressWarnings("unchecked")
    public static List<ProxyInfo> getProxyInfoList(RedisClient redisClient) {
        Object proxyInfoObject = Optional.ofNullable(redisClient.get(HttpEnums.PROXY_KEY.getValue())).orElse(new ArrayList<>());
        return (List<ProxyInfo>) proxyInfoObject;
    }
}

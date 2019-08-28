package com.app.redis;


import java.util.Map;

/**
 * jedis的接口.
 *
 * @author 会写代码的厨师.
 * * @date : 2018-08-04
 */
public interface RedisClient {

    /**
     * 批量删除对应的value
     *
     * @param keys keys
     */
    void remove(final String... keys);

    /**
     * 批量删除key
     *
     * @param pattern pattern
     */
    void removePattern(final String pattern);


    /**
     * 删除对应的value
     *
     * @param key key
     */
    void remove(final String key);

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key key
     * @return 是否存在key
     */
    Boolean exists(final String key);

    /**
     * 读取缓存
     *
     * @param key key
     * @return Object
     */
    Object get(final String key);

    /**
     * 写入缓存
     *
     * @param key   k
     * @param value v
     * @return boolean
     */
    boolean set(final String key, Object value);

    /**
     * 写入缓存 并设置过期时间
     *
     * @param key        k
     * @param value      v
     * @param expireTime 过期时间
     * @return boolean
     */
    boolean set(final String key, Object value, Long expireTime);

    /**
     * 获取自增的值
     *
     * @param key   k
     * @param delta delta
     * @return long
     */
    Long increment(final String key, long delta);

    /**
     * 开启事物
     *
     * @return boolean
     */
    Boolean multi();

    /**
     * 执行事务后关闭
     *
     * @return boolean
     */
    Boolean exec();

    Boolean hmset(String key, Map<String, Object> value);

    boolean hmset(String key, Map<String, Object> map, long time);

}

package app.miniprogram.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具实现类
 *
 * @author ：wkh.
 */
@Component
public class RedisClientImpl implements RedisClient {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 批量删除对应的value
     *
     * @param keys keys
     */
    @Override
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern pattern
     */
    @Override
    public void removePattern(final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key key
     */
    @Override
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key key
     * @return 是否存在key
     */
    @Override
    public Boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key key
     * @return Object
     */
    @Override
    public Object get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 写入缓存
     *
     * @param key   k
     * @param value v
     * @return boolean
     */
    @Override
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存 并设置过期时间
     *
     * @param key        k
     * @param value      v
     * @param expireTime 过期时间
     * @return boolean
     */
    @Override
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取自增的值
     *
     * @param key   k
     * @param delta delta
     * @return long
     */
    @Override
    public Long increment(final String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public Boolean multi() {
        try {
            redisTemplate.multi();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean exec() {
        try {
            redisTemplate.exec();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean hmset(String key, Map<String, Object> value) {
        try {
            redisTemplate.opsForHash().putAll(key, value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    @Override
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

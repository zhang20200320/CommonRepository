package com.zhang.demo.common.utils;

import com.alibaba.fastjson.JSON;
import com.google.gson.annotations.JsonAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Redis 分布式锁实现
 */
@Component
public class RedisUtils {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final Long RELEASE_SUCCESS = 1L;
    private static final String LOCK_SUCCESS = "OK";
    private static final Long LOCK_SUCCESS1 = 1L;
    private static final String SET_IF_NOT_EXIST = "NX";
    // 设置过期时间单位, EX = seconds; PX = milliseconds
    private static final String SET_WITH_EXPIRE_TIME = "EX";
    // if get(key) == value return del(key)
    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    private static final long NOT_EXPIRE = 0;
    private static final long DEFAULT_EXPIRE = 10;



    public void set(String key, Object value, long expire){
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(value));
//        valueOperations.set(key, JSON.toJSONString(value));
        if(expire != NOT_EXPIRE){
            stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if(expire != NOT_EXPIRE){
            stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : JSON.parseObject(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if(expire != NOT_EXPIRE){
            stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }


    /**
     * 该加锁方法仅针对单实例 Redis 可实现分布式加锁
     * 对于 Redis 集群则无法使用
     *
     * 支持重复，线程安全
     *
     * @param lockKey   加锁键
     * @param clientId  加锁客户端唯一标识(采用UUID)
     * @param seconds   锁过期时间
     * @return
     */
    public boolean tryLock(String lockKey, String clientId, int seconds) {
        return stringRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
//            String result = jedis.set(lockKey, clientId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, seconds);
//            SetParams setParams = new SetParams();// 设置key的过期时间单位
//            setParams.px(seconds); // （EX = seconds; PX = milliseconds）
//            String result = jedis.set(lockKey, clientId, setParams);

//            SetParams setParams = new SetParams();// 设置key的过期时间单位
//            setParams.px(seconds); // （EX = seconds; PX = milliseconds）
//            String result = jedis.set(lockKey, clientId, setParams);

            long result = jedis.setnx(lockKey, clientId);
            if (LOCK_SUCCESS1 == result) {
                return true;
            }

            return false;
        });
    }

    /**
     * 与 tryLock 相对应，用作释放锁
     *
     * @param lockKey
     * @param clientId
     * @return
     */
    public boolean releaseLock(String lockKey, String clientId) {
        return stringRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            Object result = jedis.eval(RELEASE_LOCK_SCRIPT, Collections.singletonList(lockKey),
                    Collections.singletonList(clientId));
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        });
    }
}

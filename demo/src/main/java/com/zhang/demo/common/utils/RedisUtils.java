package com.zhang.demo.common.utils;

import com.alibaba.fastjson.JSON;
import com.zhang.demo.service.impl.ZUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 * @author zhang
 * @date 2020-04-24 10:28:40
 */
@Component
public class RedisUtils {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final Long RELEASE_SUCCESS = 1L;
    private static final String LOCK_SUCCESS = "OK";
    private static final Long LOCK_SUCCESS1 = 1L;
    private static final String SET_IF_NOT_EXIST = "NX";
    /** 设置过期时间单位, EX = seconds; PX = milliseconds */
    private static final String SET_WITH_EXPIRE_TIME = "EX";
    // if get(key) == value return del(key)   仅当key存在且存储在key上的值恰好是我期望的值时才删除密钥.这是通过以下Lua脚本完成的
    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;
    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;

    /**
     * 是否锁定标志
     */
    private volatile boolean locked = false;

    public void set(String key, Object value, long expire){
        stringRedisTemplate.opsForValue().set(key, FormatConversionUtils.toJson(value));
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
     *  通过 setnx给redis赋值key-value(在value中加入过期时间)
     *      1.如果key不存在则正常通过 setnx给redis赋值key-value；
     *      2.如果key存在，则获取value值截取过期时间，和当前时间做判断
     *          1.如果过期时间小于当前时间则表示已经过期，
     *              则通过getSet命令给该key重新设置锁（value）并返回旧值。
     *          2.如果过期时间大于当前时间则表示还未过期，
     *              则返回false（表示当前key还没有过期，无法获取到锁（不能再加锁了，要等待））。
     *  【以上步骤说明 解决jedis.setnx方法没有给key设置过期时间的问题】
     *
     * @param lockKey   加锁键——key 【token拼接请求接口地址】
     * @param clientId_expireDate 加锁值——value  【加锁客户端唯一标识(采用UUID)_过期时间(System.currentTimeMillis() + lockSeconds * 1000)】
     * @param seconds   锁过期时间
     * @return
     */
    public boolean tryLock(String lockKey, String clientId_expireDate, int seconds) {
        return stringRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            //此方法报错，查看源码jedis.set方法不能如此传参
//            String result = jedis.set(lockKey, clientId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, seconds);

//            SetParams setParams = new SetParams();// 设置key的过期时间单位
//            setParams.px(seconds); // （EX = seconds; PX = milliseconds）
//            String result = jedis.set(lockKey, clientId, setParams);


            //redis里key的时间
            if (jedis.exists(lockKey)) {
                String currentValue = jedis.get(lockKey);
                System.out.println("currentValue : " + currentValue);
                String expireDate = currentValue.substring(currentValue.lastIndexOf("_") + 1);
                System.out.println("value : " + expireDate);
                //判断锁是否已经过期，过期则重新设置锁并获取旧值
                if (expireDate != null && Long.parseLong(expireDate) < System.currentTimeMillis()) {
                    //设置锁并返回旧值
                    String oldValue = jedis.getSet(lockKey, clientId_expireDate);
                    //比较锁的时间，如果不一致则可能是其他锁已经修改了值并获取
                    if (oldValue != null && oldValue.equals(currentValue)) {
                        locked = true;
                        return true;
                    }
                } else {// 表示锁还没有过期
                    logger.info("当前key还没有过期,过期时间为expireDate, lockKey = [{}], expireDate = [{}] ", lockKey, expireDate);
                    return false;
                }
            }
            // 表示缓存中lockKey不存在
            long result = jedis.setnx(lockKey, clientId_expireDate);// jedis.setnx该方式赋值————如果key不存在时设置值（成功返回1，否则返回0）
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

package com.zhang.demo.common.Aspect;

import com.zhang.demo.common.Annotation.NoRepeatSubmit;
import com.zhang.demo.common.CommonResult;
import com.zhang.demo.common.ResultCode;
import com.zhang.demo.common.utils.R1;
import com.zhang.demo.common.utils.RedisUtils;
import com.zhang.demo.common.utils.RequestUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.UUID;

@Aspect
@Component
public class RepeatSubmitAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepeatSubmitAspect.class);

    @Autowired
    private RedisUtils redisUtils;


    @Pointcut("@annotation(noRepeatSubmit)")
    public void pointCut(NoRepeatSubmit noRepeatSubmit) {
    }

    @Around("pointCut(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        int lockSeconds = noRepeatSubmit.lockTime();
        HttpServletRequest request = RequestUtils.getRequest();
        Assert.notNull(request, "request can not null");

        // 此处可以用token或者JSessionId
        String token = request.getHeader("Authorization");
//        Enumeration<String> token1 = request.getHeaders("Authorization");
        String path = request.getServletPath();
        LOGGER.info("获取token值, token = [{}], path = [{}]", token, path);
        String key = getKey(token, path);
        String clientId_expireDate = getClientId(lockSeconds);

        // 添加锁
        boolean isSuccess = redisUtils.tryLock(key, clientId_expireDate, lockSeconds);
        LOGGER.info("tryLock, isSuccess = [{}] key = [{}], clientId = [{}]", isSuccess, key, clientId_expireDate);
        if (isSuccess) {
            LOGGER.info("tryLock success, key = [{}], clientId = [{}]", key, clientId_expireDate);
            // 获取锁成功
            Object result;
            try {
                // 执行进程
                result = pjp.proceed();
            } finally {
                String value = redisUtils.get(key);// 获取key对应的
                if (clientId_expireDate.equals(value)) {
                    LOGGER.info("releaseLock start, key = [{}], value = [{}], clientId = [{}]", key, value, clientId_expireDate);
                    // 解锁
                    redisUtils.releaseLock(key, clientId_expireDate);
                    LOGGER.info("releaseLock success, key = [{}], clientId = [{}]", key, clientId_expireDate);
                }
            }
            return result;
        } else {
            // 获取锁失败，认为是重复提交的请求
            LOGGER.info("tryLock fail重复请求请稍后再试, key = [{}]", key);
            // (1221, "重复请求，请稍后再试", null);
            return CommonResult.repeatSubmitFailed(1221, "已提交，不可重复提交，请稍等..");
//            return ResultCode.REPEAT_SUBMIT;
//            return R1.error(1221, "重复请求，请稍后再试");
        }


    }

    private String getKey(String token, String path) {
        return token + path;
    }

    /**
     * 给clientId 拼接过期时间，以value保存在缓存中 clientId_expireDate
     * @param lockSeconds 过期时间（单位：秒）
     * @return clientId_expireDate 唯一标识_当前时间 + 过期时间 * 1000
     */
    private String getClientId(int lockSeconds) {
        // 给clientId 拼接过期时间，以value保存在缓存中 clientId_expireDate
        long expires = System.currentTimeMillis() + lockSeconds * 1000;
        // _1588841042818 ———— 表示当前时间加过期时间
        String expireTime = "_".concat(String.valueOf(expires));
        String clientId_expireDate = UUID.randomUUID().toString().concat(expireTime);
        return clientId_expireDate;
    }

}

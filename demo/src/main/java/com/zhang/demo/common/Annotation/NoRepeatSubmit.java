package com.zhang.demo.common.Annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防止重复提交自定义注解
 * @author zhang
 *
 */
@Target(ElementType.METHOD) // 说明了Annotation所修饰的对象范围
@Retention(RetentionPolicy.RUNTIME) // 用于描述注解的生命周期（即：被描述的注解在什么范围内有效）
@Documented // 是一个标记注解，没有成员 （文档化）
public @interface NoRepeatSubmit {
    /**
     * 设置请求锁定时间
     *
     * @return
     */
    int lockTime() default 10;
}

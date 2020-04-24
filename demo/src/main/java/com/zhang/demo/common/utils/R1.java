package com.zhang.demo.common.utils;

import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpStatus;
/**
 * 接口统一返回格式
 * @author zhang
 * @date 2020-04-24 10:28:40
 *
 */
public class R1<T> {

    private static final long serialVersionUID = 1L;

    private int code;

    private T data;

    private String message;

    /**
     * 无参构造
     */
    public R1() {
        this.code = 0;
        this.message = "success";
        this.data = (T) new ArrayList<Object>();
    }

    /**
     * 有参构造
     * @param code
     * @param data
     * @param message
     */
    public R1(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    /**
     * ok————无参返回
     * @return
     */
    public static <T> R1<T> ok() {
        return new <T> R1<T>();
    }

    /**
     * ok————指定提示语
     * @param message
     * @return
     */
    public static <T> R1<T> ok(String message) {
        R1<T> r = new R1<T>();
        r.message = message;
        r.data = (T) new ArrayList<Object>();
        return r;
    }

    /**
     * ok————指定data（承载数据）
     * @param map
     * @return
     */
    public static <T> R1<T> ok(Map<String, Object> map) {
        R1<T> r = new R1<T>();
        r.data = (T) map;
        return r;
    }

    /**
     * error————无参返回
     * @return
     */
    public static <T> R1<T> error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    /**
     * error————指定message
     * @param message
     * @return
     */
    public static <T> R1<T> error(String message) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, message);
    }

    /**
     * error————指定code, message
     * @param code
     * @param message
     * @return
     */
    public static <T> R1<T> error(int code, String message) {
        R1<T> r = new R1<T>();
        r.code = code;
        r.message = message;
        r.data = (T) new ArrayList<Object>();
        return r;
    }

    /**
     * ok————指定data（承载数据）
     * @param data
     * @return
     */
    public R1<T> ok(T data) {
        R1<T> r = new R1<T>();
        r.data = data;
        return r;
    }

    /**
     * 指定data（承载数据）
     * @param data
     * @return
     */
    public static <T> R1<T> data(T data) {
        return data(data, "success");
    }

    public static <T> R1<T> data(T data, String message) {
        return data(HttpStatus.SC_OK, data, message);
    }

    public static <T> R1<T> data(int code, T data, String message) {
        return new R1(code, data, data == null ? "暂无承载数据" : message);
    }

    public int getCode() {
        return this.code;
    }

    public T getData() {
        return this.data;
    }

    public String getmessage() {
        return this.message;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public void setmessage(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "R1 [code=" + code + ", data=" + data + ", message=" + message + "]";
    }

}

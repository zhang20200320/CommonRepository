package com.zhang.demo.common.utils;

import com.google.gson.Gson;

/**
 * 公共格式转换
 * @author zhang
 * @date 2020-04-24 10:28:40
 */
public class FormatConversionUtils {


    /**
     * 创建Gson对象
     */
    private static Gson createGson(){
        return new Gson();
    }

    /**
     * Object转成JSON数据(Gson方式)
     */
    public static String toJson(Object object){
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return createGson().toJson(object);
    }

    /**
     * JSON数据转成Object(Gson方式)
     */
    private <T> T fromJson(String json, Class<T> clazz){
        return createGson().fromJson(json, clazz);
    }

}

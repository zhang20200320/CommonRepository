package com.zhang.demo.common.utils;

import com.zhang.demo.common.CommonResult;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * jasypt加密/解密
 * 针对applicaion.yml/application.properties配置文件私密信息进行加密
 *      1.pom.xml文件中添加依赖jasypt-spring-boot-starter。
 *      2.applicaion.yml/application.properties配置文件配置文件中添加jasypt加密/解密的密匙（公钥）jasypt.encryptor.password
 *      3.导入本工具类，传值应用测试
 *          目前两种方式传参：
 *              1.字符串类型传参加密
 *              2.map集合类型传参加密
 *      4.将加密后的值替换applicaion.yml/application.properties中你加密的值，
 *          格式为 ENC(加密值)，【是固定写法，（）里面是加密后的信息】
 *
 * @author zhang
 * @date 2020-05-19 14:11:50
 */
public class jasyptUtils {

    /**
     * 加密的算法，这个算法是默认的
     */
    private static final String ALGORITHM = "PBEWithMD5AndDES";
    /**
     * jasypt加密的密匙
     */
    private static final String KEY = "zp";

    /**
     * 添加加密配置
     * @return
     */
    public static StandardPBEStringEncryptor getStandardPBEStringEncryptor () {
        // 加密工具
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        // 加密配置
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm(ALGORITHM); // 加密的算法，这个算法是默认的
        config.setPassword(KEY); // 加密的密钥（公钥）
        standardPBEStringEncryptor.setConfig(config);
        return standardPBEStringEncryptor;
    }

    /**
     * 加密（单字符串加密）
     * @param value
     * @return
     */
    public static String jasyptEncryptStr(String value) {
        StandardPBEStringEncryptor standardPBEStringEncryptor = getStandardPBEStringEncryptor();
        String encryptResult = standardPBEStringEncryptor.encrypt(value);
        System.out.println("加密完成，结果为resultStr: " + encryptResult);
        return encryptResult;
    }

    /**
     * 加密（map集合批量加密）
     * @param paramMap
     * @return
     */
    public static CommonResult<Map<String, String>> jasyptEncrypt(Map<String, String> paramMap) {
        StandardPBEStringEncryptor standardPBEStringEncryptor = getStandardPBEStringEncryptor();

        Map<String, String> resultMap = new HashMap<String, String>();
        // java8新特性遍历Map集合
        paramMap.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String key, String value) {
                String encryptResult = standardPBEStringEncryptor.encrypt(value);
                resultMap.put("decrypt_" + key, encryptResult);
                System.out.println("key: " + key + "; value: " + value);
                System.out.println("key: " + key + "; 加密完成，结果为result: " + encryptResult);
            }
        });
        return CommonResult.success(resultMap);
    }

    /**
     * 解密（单字符串解密）
     * @param value
     * @return
     */
    public static String jasyptDecryptStr(String value) {
        StandardPBEStringEncryptor standardPBEStringEncryptor = getStandardPBEStringEncryptor();
        String decryptResult = standardPBEStringEncryptor.decrypt(value);
        System.out.println("加密完成，结果为resultStr: " + decryptResult);
        return decryptResult;
    }

    /**
     * 解密（map集合批量解密）
     * @param paramMap
     * @return
     */
    public static CommonResult<Map<String, String>> jasyptDecrypt(Map<String, String> paramMap) {
        StandardPBEStringEncryptor standardPBEStringEncryptor = getStandardPBEStringEncryptor();

        Map<String, String> resultMap = new HashMap<String, String>();
        // java8新特性遍历Map集合
        paramMap.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String key, String value) {
                String decryptResult = standardPBEStringEncryptor.decrypt(value);
                resultMap.put("decrypt_" + key, decryptResult);
                System.out.println("key: " + key + "; value: " + value);
                System.out.println("key: " + key + "; 解密完成，结果为result: " + decryptResult);
            }
        });
        return CommonResult.success(resultMap);
    }



    public static void main(String[] args) {
        // jasypt加密
        String test = "test";
        String encryptResult = jasyptEncryptStr(test);
        // 解密
        jasyptDecryptStr(encryptResult);


    }
}

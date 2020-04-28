package com.zhang.demo.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * DES加密/解密工具类
 * <a href = "https://www.cnblogs.com/Lawson/archive/2012/05/20/2510781.html">JAVA和.NET使用DES对称加密的区别</a>
 *
 * @author zhang
 * @date 2020-04-28 10:09:10
 */
public class DESUtils {

    /**
     * 密钥算法
     * JAVA封装的DES算法的默认模式————ECB模式（电子密本方式）
     */
    private static final String KEY_ALGORITHM = "DES";
    /**
     * 加密方式(算法/模式/补码方式)
     * DES/CBC/PKCS5Padding————CBC模式（密文分组链接方式）
     */
    private static final String ENCRYPTION  = "DES/CBC/PKCS5Padding";
    /**
     * 编码格式
     */
    private static final String CHARSET  = "UTF-8";
    /**
     * 此类指定一个初始化向量zeroIv（使用 byteIV 中的字节作为 IV 来构造一个 IvParameterSpec 对象）(指定数组大小为8)
     * // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
     * <a href="https://download.oracle.com/technetwork/java/javase/6/docs/zh/api/javax/crypto/spec/IvParameterSpec.html">javax.crypto.spec.IvParameterSpec</a>
     * <a href= "https://blog.csdn.net/qq_25816185/article/details/81626499">java使用AES算法的CBC模式加密</a>
     */
    private static final IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);

    /**
     * 设置指定初始化向量值(指定值)
     */
    private static byte[] IV  = { 0x13, 0x24, 0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };

    /**
     * DES加密
     *
     * @param strIn -- 待加密明文
     * @param secretkey -- 密钥
     * @return
     * @throws Exception
     * IvParameterSpec对象————初始化向量，使用DES进行加/解密时字节长度必须为8的倍数，
     * 否则会报错（java.security.InvalidAlgorithmParameterException: Wrong IV length: must be 8 bytes long）
     *
     * 实例化Cipher加密对象，参数为加密方式，使用IvParameterSpec时，Cipher参数要使用CBC模式。
     * 加密方式为【DES】加密（JAVA封装的DES算法的默认模式————ECB模式）
     * 加密方式为【DES/CBC/PKCS5Padding】加密（密文分组链接方式————CBC模式）
     */
    public static String encrypt(String strIn, String secretkey) throws Exception {

//        IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
//        IvParameterSpec zeroIv = new IvParameterSpec(IV);
        Key key = getKey(secretkey.getBytes(CHARSET)); // 不足8的倍数位，则填充/截取（密钥）
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);

//        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv); // 初始化（第一个参数是工作模式，第二个是密钥key，第三个是初始化向量）
        cipher.init(Cipher.ENCRYPT_MODE, key); // ENCRYPT_MODE = 1; (加密模式)

        byte[] strInByte =  strIn.getBytes(CHARSET); // 待加密明文字符串转字节数组
        byte[] arrB = cipher.doFinal(strInByte); // 加密明文字节数组，并返回加密后的字节数组
        System.out.println("待加密明文字符串转字节数组strInByte: " + strInByte);
        System.out.println("返回加密后的字节数组arrB: " + arrB);
        return byteArr2HexStr(arrB); // 字节数组转16进制字符串
    }

    /**
     * DES解密
     * @param strIn -- 待解密密文
     * @param secretkey -- 密钥
     * @return
     * @throws Exception
     */
    public static String decrypt(String strIn, String secretkey) throws Exception {
//        IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
//        IvParameterSpec zeroIv = new IvParameterSpec(IV);
        Key key = getKey(secretkey.getBytes(CHARSET));
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key); // DECRYPT_MODE = 2 (解密模式)
        byte[] param = cipher.doFinal(hexStr2ByteArr(strIn));
        return new String(param, CHARSET);
    }

    /**
     * 填充盐值（密钥），使用DES进行加/解密时字节长度必须为8的倍数，
     * 否则会报错（java.security.InvalidAlgorithmParameterException: Wrong IV length: must be 8 bytes long）
     * @param arrBTmp
     * @return
     */
    private static Key getKey(byte[] arrBTmp) {
        byte[] arrB = new byte[8];
        for(int i = 0; i < arrBTmp.length && i < arrB.length; ++i) {
            arrB[i] = arrBTmp[i];
        }
        Key key = new SecretKeySpec(arrB, KEY_ALGORITHM);
        return key;
    }

    /**
     * 字节数组转换16进制字符串
     * @param arrB
     * @return
     * @throws Exception
     */
    public static String byteArr2HexStr(byte[] arrB) {
        int iLen = arrB.length;
        StringBuffer sb = new StringBuffer(iLen * 2);
        for(int i = 0; i < iLen; ++i) {
            int intTmp;
            for(intTmp = arrB[i]; intTmp < 0; intTmp += 256) {
            }
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 16进制字符串转换字节数组
     * @param strIn
     * @return
     * @throws Exception
     */
    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[iLen / 2];

        for(int i = 0; i < iLen; i += 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte)Integer.parseInt(strTmp, 16);
        }

        return arrOut;
    }

    private static String initReturnData(String key, String data) {
        String str = "";
        try {
            if (!data.equals("")) {
                str = DESUtils.decrypt(replaceBlank(data), key);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            str = "{\"status\":false,\"msg\":\"请检查密钥或加密方式是否正确！\",\"data\":\"\"}";
        }

        return str;
    }

    /**
     * 检查字符串中是否包含特殊字符（换行符等），并去除
     * @param str
     * @return
     */
    private static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }

        return dest;
    }


    /**
     * testZhang
     * @param args
     */
    public static void main(String[] args) {
        String strIn = "123456";
        String saltValue = "ZHANG";
        String encryptResult = ""; // 加密结果
        String decryptResult = ""; // 解密结果

        /**加密*/
        try {
            encryptResult = encrypt(strIn, saltValue);
        } catch (Exception e) {
            System.out.println("加密异常");
        }
        System.out.println("密钥: " + saltValue);
        System.out.println("加密result: " + encryptResult);

        /**解密*/
        try {
            decryptResult = decrypt(encryptResult, saltValue);
        } catch (Exception e) {
            System.out.println("解密异常");
        }
        System.out.println("密钥: " + saltValue);
        System.out.println("解密result1: " + decryptResult);


    }

}

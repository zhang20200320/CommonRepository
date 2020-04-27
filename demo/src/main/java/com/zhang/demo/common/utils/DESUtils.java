package com.zhang.demo.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * DES加密/解密工具类
 * @author zhang
 */
public class DESUtils {

    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "DES";
    /**
     * 加密方式
     */
    private static final String ENCRYPTION  = "DES/CBC/PKCS5Padding";
    /**
     * 编码格式
     */
    private static final String CHARSET  = "UTF-8";
    /**
     * 此类指定一个初始化向量zeroIv（使用 byteIV 中的字节作为 IV 来构造一个 IvParameterSpec 对象）
     */
    private static final IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);

    /**
     * DES加密
     * @param strIn -- 待加密明文
     * @param secretkey -- 密钥
     * @return
     * @throws Exception
     */
    public static String encrypt(String strIn, String secretkey) throws Exception {
//        IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
        Key key = getKey(secretkey.getBytes(CHARSET)); // 填充盐值（密钥），使用DES进行加/解密时字节长度必须为8的倍数，否则会报错
        Cipher cipher = Cipher.getInstance(ENCRYPTION); // 实例化加密对象，参数为加密方式，使用IvParameterSpec时，Cipher要写全
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv); // 初始化（第一个参数是工作模式，第二个是密钥key，第三个是初始化向量）

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
        Key key = getKey(secretkey.getBytes(CHARSET));
        Cipher cipher = Cipher.getInstance(ENCRYPTION);
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte[] param = cipher.doFinal(hexStr2ByteArr(strIn));
        return new String(param, CHARSET);
    }

    /**
     * 填充盐值（密钥），使用DES进行加/解密时字节长度必须为8的倍数，否则会报错
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


    public static void main(String[] args) {
        String strIn = "123456";
        String saltValue = "zhang";
        String encryptResult = "";
        String decryptResult1 = "";

        /**加密*/
        try {
            encryptResult = encrypt(strIn, saltValue);
        } catch (Exception e) {
            System.out.println("加密异常");
        }
        System.out.println("盐值: " + saltValue);
        System.out.println("加密result: " + encryptResult);

        /**解密*/
        try {
            decryptResult1 = decrypt(encryptResult, saltValue);
        } catch (Exception e) {
            System.out.println("解密异常");
        }
        System.out.println("盐值: " + saltValue);
        System.out.println("解密result1: " + decryptResult1);





    }

}

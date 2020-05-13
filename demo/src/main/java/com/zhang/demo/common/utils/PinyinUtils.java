package com.zhang.demo.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 汉字转拼音工具类
 * 文档链接地址(http://pinyin4j.sourceforge.net/pinyin4j-doc)
 */
public class PinyinUtils {


    public static String[] test() {
        String test = "张三".substring(0, 1);
        String convert = "";
        String[] pinyinArray = null;

        // 用于格式化返回值
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

        try {
            pinyinArray = PinyinHelper.toHanyuPinyinStringArray(test.charAt(0), format);

//            for (int j = 0; j < test.length(); j++) {
//                char word = test.charAt(j);
//                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
//
//                if (pinyinArray != null) {
//                    convert += pinyinArray[0].charAt(0);
//                } else {
//                    convert += word;
//                }
//            }
        }catch (BadHanyuPinyinOutputFormatCombination hanyuPinyinOutputFormatCombination) {
            System.out.println(hanyuPinyinOutputFormatCombination);
        }
        for(int i = 0; i < pinyinArray.length; ++i) {
            System.out.println(pinyinArray[i]);
            if (pinyinArray != null) {
                    convert += pinyinArray[0].charAt(0);
                } else {
                    convert += test.charAt(0);
                }
        }
        System.out.println(convert);
        return pinyinArray;
    }

    public static void main(String[] args) {
        // 测试
        test();
    }

}

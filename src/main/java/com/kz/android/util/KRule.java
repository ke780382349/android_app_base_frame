package com.kz.android.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * 柯壮
 * 正则校验
 */
public class KRule {

    /**
     * 校验手机号
     */
    public static boolean phone(String phone) {
        String regex = "^[1][3578][0-9]{9}$";
        return Pattern.compile(regex).matcher(phone).matches();
    }

    /**
     * 判断是否包含汉字
     */
    public static boolean isChineseChar(String text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        int len = text.length();
        boolean flag;
        String regex = "[\u4e00-\u9fa5]+";
        for (int i = 0; i < len; i++) {
            flag = Pattern.compile(regex).matcher(String.valueOf(text.charAt(i))).matches();
            if (!flag) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断身份证号是否合法
     *
     * @param id 身份证号
     */
    public static boolean idcard(String id) {
        String regex = "(^\\d{15}$)|(^\\d{17}([0-9]|X|x)$)";
        return Pattern.compile(regex).matcher(id).matches();
    }

    /**
     * 银行卡号
     */
    public static boolean bankNum(String num) {
        String regex = "^(\\d{16}$)|(\\d{19}$)";
        return Pattern.compile(regex).matcher(num).matches();
    }
}

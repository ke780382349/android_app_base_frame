package com.kz.android.util;

import java.util.regex.Pattern;

/**
 *  柯壮
 *  正则校验
 */
public class KRule {
    public static final String OK = "校验通过";
    /** 校验手机号*/
    public static String phone(String phone){
        String regex = "^[1][3578][0-9]{9}$";
        boolean isOK = Pattern.compile(regex).matcher(phone).matches();
        String message = isOK ? OK : "请输入正确的手机号";
        return message;
    }
}

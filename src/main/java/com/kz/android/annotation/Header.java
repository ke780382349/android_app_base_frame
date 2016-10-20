package com.kz.android.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 柯壮 2016/05/27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Header {
    //是否使用模版
    boolean usable() default true;

    //标题文字
    String color() default "";

    //左图片
    int lImg() default 0;

    //中文字
    String cText() default "";

    //中间layout
    int cLayout() default 0;

    //右边文字
    String rText() default "";

    //右图片
    int rImg() default 0;

    //右Layout文字
    String rLayoutText() default "";

    //右Layout图片
    int rLayoutImg() default 0;

}

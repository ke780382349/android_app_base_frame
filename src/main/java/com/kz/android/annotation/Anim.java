package com.kz.android.annotation;

import com.kz.android.base.KBaseActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解动画
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Anim {
    KBaseActivity.AnimType value() default KBaseActivity.AnimType.RIGHT;
}

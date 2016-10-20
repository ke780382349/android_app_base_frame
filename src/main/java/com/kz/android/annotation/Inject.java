package com.kz.android.annotation;

import android.view.View;

import com.kz.android.base.KBaseActivity;
import com.kz.android.util.KLog;

import java.lang.reflect.Field;

/**
 * 柯壮
 * 注解帮助类
 */
public class Inject {
    /**
     * 解注ID
     *
     * @param obj    用来取控件的值,判断下如果为空才会复制属性
     * @param view   从这个view下找控件
     * @param fields 字段集合
     */
    public static void idForObject(Object obj, View view, Field[] fields) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                Id idAnnomation = field.getAnnotation(Id.class);
                if (idAnnomation.value() > 0) {
                    try {
                        field.setAccessible(true);
                        Object object = field.get(obj);
                        if (object == null) {
                            field.set(obj, view.findViewById(idAnnomation.value()));
                        }
                    } catch (IllegalAccessException e) {
                        KLog.e("error field:" + field.getName());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 注解动画
     */
    public static void anim(KBaseActivity activity) {
        Class<?> clz = activity.getClass();
        if (clz.isAnnotationPresent(Anim.class)) {
            Anim anim = clz.getAnnotation(Anim.class);
            activity.mAnimType = anim.value();
        }
    }

    /**
     * 注解标题
     */
    public static void header(KBaseActivity activity) {
        Class<?> clz = activity.getClass();
        if (clz.isAnnotationPresent(Header.class)) {
            Header header = clz.getAnnotation(Header.class);
            new TitleBuilder(activity)
                    .usable(header.usable())
                    .color(header.color())
                    .lImg(header.lImg())
                    .cText(header.cText())
                    .cLayout(header.cLayout())
                    .rImg(header.rImg())
                    .rText(header.rText())
                    .rLayout(header.rLayoutImg(), header.rLayoutText())
                    .build();
        }
    }

}

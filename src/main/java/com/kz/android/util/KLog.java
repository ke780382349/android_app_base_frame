package com.kz.android.util;


import android.util.Log;

/**
 * Log统一管理类
 *
 * @author KeZhuang
 * @version v1.0.0
 * @since 2016年2月24日 11:12:45
 */
public final class KLog {
    /**
     * Log打印开关
     */
    public static final boolean LOG = true;
    //inner field
    private static final String CLASS_NAME = KLog.class.getSimpleName();
    private static final String NULL_OBJ = "null";
    private static final String NULL_METHOD = "method name is null!!";
    private static final String NULL_TAG = "tag is null,default tag : KLog";
    private static final String INFO = "i";
    private static final String ERROR = "e";
    private static final String WARN = "w";
    private static final String VERBOSE = "v";
    private static final String DEBUG = "d";

    public static void i(String tag, Object obj) {
        if (LOG) {
            Log.i(tag, obj.toString());
        }
    }

    public static void i(Object object) {
        if (LOG) {
            inner_log(object);
        }
    }

    public static void e(String message) {
        if (LOG) {
            inner_log(message);
        }
    }

    public static void w(Object object) {
        if (LOG) {
            inner_log(object);
        }
    }

    public static void v(Object object) {
        if (LOG) {
            inner_log(object);
        }
    }

    public static void d(String tag, Object object) {
        if(LOG){

        }
    }

    public static void d(Object object) {
        if (LOG) {
            inner_log(object);
        }
    }

    static void inner_log(Object object) {
        if (KJavaStack.check_null_obj(object)) {
            StringBuffer sb = new StringBuffer();
            sb.append(NULL_OBJ).append("  ");
            sb.append("class:" + KJavaStack.getCaller(CLASS_NAME)).append("  ");
            sb.append("line number:");
            sb.append(KJavaStack.getCallerLine(KJavaStack.getCallerMethod(3)));
            e(sb.toString());
            return;
        }
        check_caller(object);
    }

    static void check_caller(Object object) {
        String method = KJavaStack.getCallerMethod(3);
        String caller = KJavaStack.getCaller(CLASS_NAME);
        if (method == null) {
            e(NULL_METHOD);
            return;
        }
        if (caller == null) {
            w(NULL_TAG);
            caller = "KLog";
        }
        if (method.equals(INFO)) {
            Log.i(caller, object.toString());
        } else if (method.equals(DEBUG)) {
            Log.d(caller, object.toString());
        } else if (method.equals(VERBOSE)) {
            Log.v(caller, object.toString());
        } else if (method.equals(ERROR)) {
            Log.e(caller, object.toString());
        } else if (method.equals(WARN)) {
            Log.w(caller, object.toString());
        }
    }
}

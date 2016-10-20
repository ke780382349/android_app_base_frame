package com.kz.android.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.kz.android.app.FrameContext;
import com.kz.android.core.SecurityServer;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * sharepreference工具类
 *
 * @author KeZhuang
 * @version v1.0.0
 * @since 2016年2月24日 11:12:45
 */
public class KSPreference {

    /**
     * copy sp 到另外一个文件中
     */
    public static void copy(Context context, String oldName, String newName) {
        Map<String, ?> oldMap = getAll(context, oldName);
        if (oldMap == null || oldMap.isEmpty()) {
            removeSp(context, oldName);
            return;
        }
        Set<String> keySet = oldMap.keySet();
        for (String key : keySet) {
            Object obj = oldMap.get(key);
            if (obj instanceof Boolean) {
                putValue(context, newName, key, (Boolean) obj);
            } else if (obj instanceof Integer) {
                putValue(context, newName, key, (Integer) obj);
            } else if (obj instanceof String) {
                putValue(context, newName, key, (String) obj);
            } else if (obj instanceof Float) {
                putValue(context, newName, key, (float) obj);
            } else if (obj instanceof Long) {
                putValue(context, newName, key, (long) obj);
            } else if (obj instanceof Set) {
                putValue(context, newName, key, (Set<String>) obj);
            }
        }
        removeSp(context, oldName);
    }

    /**
     * 删除sp文件
     */
    public static void removeSp(Context context, String name) {
        File file = new File("/data/data/" + context.getPackageName() + "/shared_prefs/" + name + ".xml");
        if (file.exists()) {
            file.delete();
        }
    }

    public static void putValue(Context context, String name, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static void putValue(Context context, String name, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    public static void putValue(Context context, String name, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SecurityServer server = (SecurityServer) FrameContext.getServer(context, FrameContext.APP_SECURITY_SERVER);
        sp.edit().putString(key, server.base64encode(value)).apply();
    }

    public static void putValue(Context context, String name, String key, Float value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putFloat(key, value).apply();
    }

    public static void putValue(Context context, String name, String key, Long value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).apply();
    }

    public static void putValue(Context context, String name, String key, Set<String> value) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().putStringSet(key, value).apply();
    }

    public static boolean getValue(Context context, String name, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static int getValue(Context context, String name, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static String getValue(Context context, String name, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SecurityServer server = (SecurityServer) FrameContext.getServer(context, FrameContext.APP_SECURITY_SERVER);
        return new String(server.base64decode(sp.getString(key, defValue)));
    }

    public static float getValue(Context context, String name, String key, Float defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getFloat(key, defValue);
    }

    public static long getValue(Context context, String name, String key, Long defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getLong(key, defValue);
    }

    public static Set<String> getValue(Context context, String name, String key, Set<String> defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getStringSet(key, defValue);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context 上下文
     * @param key     存数据时候的key
     */
    public static void remove(Context context, String name, String key) {
        SharedPreferences sp = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context 上下文
     * @param key     存数据时候的key
     * @return
     */
    public static boolean contains(Context context, String name, String key) {
        SharedPreferences sp = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        boolean result = sp.contains(key);
        return result;
    }

    /**
     * 返回所有的键值对
     *
     * @param context 上下文
     * @return 返回sp所有的配置
     */
    public static Map<String, ?> getAll(Context context, String name) {
        SecurityServer server = (SecurityServer) FrameContext.getServer(context, FrameContext.APP_SECURITY_SERVER);
        SharedPreferences sp = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        Map<String, Object> map = (Map<String, Object>) sp.getAll();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            Object value = map.get(map);
            if (value instanceof String) {
                Object str = new String(server.base64decode((String) value));
                map.put(key, str);
            }

        }
        return map;
    }

    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }

        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

}
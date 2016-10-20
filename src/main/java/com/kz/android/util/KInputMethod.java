package com.kz.android.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by 柯壮 on 16-10-19.
 * 键盘工具类
 */

public class KInputMethod {
    /**
     * 唤起键盘
     *
     * @param context 上下文
     * @param view    需要唤起的焦点view
     */
    public static void up(Context context, View view) {
        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
    }

    /**
     * 隐藏键盘
     *
     * @param context 上下文
     * @param view    需要隐藏键盘的view
     */
    public static void dowm(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

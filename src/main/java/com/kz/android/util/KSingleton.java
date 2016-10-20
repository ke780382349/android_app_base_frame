/**
 * 
 */
package com.kz.android.util;

/**
 * @author KeZhuang
 *
 */
public abstract class KSingleton<T> {
    private T mInstance;

    protected abstract T create();

    public final T get() {
        synchronized (this) {
            if (mInstance == null) {
                mInstance = create();
            }
            return mInstance;
        }
    }
}
package com.kz.android.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 柯壮
 * 数据库操作类
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private OnSQLite onSQLite;

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (onSQLite != null) {
            onSQLite.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (onSQLite != null) {
            onSQLite.onUpgrade(db, oldVersion, newVersion);
        }
    }
    /**
     *  设置监听
     * */
    public void setOnSQLite(OnSQLite onSQLite) {
        this.onSQLite = onSQLite;
    }

    public interface OnSQLite {
        /**
         * SQLite的生命周期
         */
        void onCreate(SQLiteDatabase db);

        /**
         * SQLite的生命周期
         */
        void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    }

}

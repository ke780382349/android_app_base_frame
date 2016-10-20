/**
 *
 */
package com.kz.android.core;

import android.content.Context;
import android.database.Cursor;

import com.kz.android.special.AppServer;
import com.kz.android.util.SQLiteHelper;

/**
 * 数据库服务类
 *
 * @author 作者 E-mail:柯壮  15011033790
 * @since 创建时间：2016年3月28日 上午10:02:14
 */
public class SQLiteServer implements AppServer {
    private Context mContext;
    private SQLiteHelper mSQL;

    SQLiteServer() {

    }

    @Override
    public void initServer(Context context) {
        mContext = context;
    }

    /**
     * 初始化sqlite数据库
     */
    public void init(String name, int version, SQLiteHelper.OnSQLite onSQLite) {
        if (mSQL == null) {
            mSQL = new SQLiteHelper(mContext, name, null, version);
            mSQL.setOnSQLite(onSQLite);
            mSQL.getWritableDatabase();
        }
    }

    /**
     * 开启事务
     */
    public void beginTransaction() {
        mSQL.getWritableDatabase().beginTransaction();
    }

    /**
     * 结束事务
     * */
    public void endTransaction() {
        mSQL.getWritableDatabase().setTransactionSuccessful();
        mSQL.getWritableDatabase().endTransaction();
    }

    /**
     * 执行sql语句
     */
    public void exec(String sql) {
        mSQL.getWritableDatabase().execSQL(sql);
    }

    /**
     * 查询数据
     */
    public Cursor execQuery(String sql) {
        return mSQL.getWritableDatabase().rawQuery(sql, null);
    }

    /**
     * 关闭数据库连接,在退出应用的时候关闭
     */
    public void closeSQLite() {
        mSQL.close();
    }

}

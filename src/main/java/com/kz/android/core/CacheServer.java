/**
 *
 */
package com.kz.android.core;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.kz.android.app.FrameContext;
import com.kz.android.special.AppServer;
import com.kz.android.util.DiskLruCache;
import com.kz.android.util.DiskLruCache.Editor;
import com.kz.android.util.DiskLruCache.Snapshot;
import com.kz.android.util.KLog;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * 缓存服务类,采用DiskLruCache技术
 *
 * @author 作者 E-mail:柯壮  15011033790
 * @since 创建时间：2016年3月28日 上午10:02:14
 */
public class CacheServer implements AppServer {
    public static final String DATA_CACHE = "dataCache";
    public static final String BITMAP_CACHE = "bitmapCache";
    public static final String FILE_NAME = "journal";
    public static final String NO_CACHE_INFO = "无缓存信息";
    private DiskLruCache mFileCache = null;
    private DiskLruCache mBitmapCache = null;
    private SecurityServer mSecurityServer = null;
    private FileServer mFileServer;
    private int maxSize = 1024 * 1024 * 5;

    CacheServer() {
    }

    @Override
    public void initServer(Context context) {
        try {
            mSecurityServer = (SecurityServer) FrameContext.getServer(context, FrameContext.APP_SECURITY_SERVER);
            mFileServer = (FileServer) FrameContext.getServer(context, FrameContext.APP_FILE_SERVER);
            mFileCache = DiskLruCache.open(mFileServer.getDataCache(context), getVersionCode(context), 1, maxSize);
            mBitmapCache = DiskLruCache.open(mFileServer.getBitmapCache(context), getVersionCode(context), 1, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 存入json数据
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午2:50:26
     */
    public void putJson(String url, String json) {
        putString(url, json);
    }

    /**
     * 获取json数据
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午3:09:30
     */
    public String getJson(String url) {
        return getString(url);
    }

    /**
     * 存入字符串
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午2:49:33
     */
    public void putString(final String key, final String value) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                BufferedWriter bufr = null;
                Editor editor = null;
                String md5Key = mSecurityServer.md5(key);
                try {
                    editor = mFileCache.edit(md5Key);
                    OutputStream out = editor.newOutputStream(0);
                    bufr = new BufferedWriter(new OutputStreamWriter(out));
                    bufr.write(value);
                    bufr.flush();
                    editor.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bufr != null) {
                        DiskLruCache.closeQuietly(bufr);
                    }
                }
            }
        }).start();
    }

    /**
     * 获取字符串
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午2:49:58
     */
    public String getString(String url) {
        BufferedReader bufr = null;
        String str = null;
        StringBuffer sb = new StringBuffer();
        try {
            Snapshot snapshot = mFileCache.get(mSecurityServer.md5(url));
            //如果因为写入进程没有操作完成,会导导致缓存快照为null,解决操作:尝试着在获取
            int i = 0;
            while (snapshot == null && i <= 5) {
                snapshot = mFileCache.get(mSecurityServer.md5(url));
                i++;
            }
            if (snapshot == null) {
                return null;
            }
            bufr = new BufferedReader(new InputStreamReader(snapshot.getInputStream(0)));
            while ((str = bufr.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufr != null) {
                DiskLruCache.closeQuietly(bufr);
            }
        }
        return sb.toString();
    }

    /**
     * 存入bitmap图片
     *
     * @author 作者:柯壮
     * @date 创建时间：2016年3月28日 下午2:50:36
     */
    public void putBitmap(final String url, final Bitmap bitmap) {
        Editor editor = null;
        BufferedOutputStream os = null;
        try {
            editor = mBitmapCache.edit(mSecurityServer.md5(url));
            os = new BufferedOutputStream(editor.newOutputStream(0));
            byte[] bitmapByte = getByteFromBitmap(bitmap);
            os.write(bitmapByte);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            DiskLruCache.closeQuietly(os);
        }
    }

    /**
     * 获取bitmap图片
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午3:19:40
     */
    public Bitmap getBitmap(String url) {
        String md5Key = mSecurityServer.md5(url);
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            Snapshot snapshot = mBitmapCache.get(md5Key);
            if (snapshot == null) {
                KLog.e("缓存不存在");
                return null;
            }
            is = snapshot.getInputStream(0);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                DiskLruCache.closeQuietly(is);
            }
        }
        return bitmap;
    }

    /**
     * 移除字符串缓存
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午3:24:26
     */
    public void removeDataCache(String url) {
        try {
            mFileCache.remove(mSecurityServer.md5(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除bitmap缓存
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午3:25:05
     */
    public void removeBitmapCache(String url) {
        try {
            mBitmapCache.remove(mSecurityServer.md5(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除字符串内的所有缓存
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午3:39:57
     */
    public boolean clearDataCache() {
        return mFileServer.deleteCache(mFileCache);
    }

    /**
     * 清除字符串内所有缓存
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午3:40:14
     */
    public boolean clearBitmapCache() {
        return mFileServer.deleteCache(mBitmapCache);
    }

    private byte[] getByteFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

    private int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

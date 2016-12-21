/**
 *
 */
package com.kz.android.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import com.kz.android.special.AppServer;
import com.kz.android.util.DiskLruCache;
import com.kz.android.util.KLog;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * 文件服务类 默认操作目录:存储目录
 *
 * @author 作者 E-mail:柯壮 15011033790
 * @since 创建时间：2016年3月28日 上午10:02:14
 */
public class FileServer implements AppServer {
    private static final String EXIST_FILE = "文件已经存在";
    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值
    private Context mContext;

    FileServer() {
    }

    @Override
    public void initServer(Context context) {
        mContext = context;
        mkdirRoot();
    }

    /**
     * 追加字符串
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年4月1日 下午3:32:55
     */
    public void appendString(String path, String appendContent) {
        File file = new File(path);
        if (checkFile(file)) {
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file, true);
                write(outputStream, appendContent.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 追加字符串
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年4月1日 下午3:32:55
     */
    public void appendString(File dir, String appendContent) {
        if (checkFile(dir)) {
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(dir, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            write(outputStream, appendContent.getBytes());
        }
    }

    /**
     * 更新文件内容
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月29日 下午1:34:18
     */
    public void updateString(String path, String updateContent) {
        write(path, updateContent);
    }

    /**
     * 更新文件内容
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月29日 下午1:34:18
     */
    public void updateString(OutputStream os, String updateContent) {
        write(os, updateContent);
    }

    /**
     * 更新文件内容
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月29日 下午1:34:18
     */
    public void updateString(File file, String updateContent) {
        if (file == null) {
            KLog.e("file对象为空");
            return;
        }
        String path = file.getPath();
        if (path == null) {
            KLog.e("无路径:" + file);
            return;
        }
        int pkgIndex = path.indexOf(getAppDir());
        pkgIndex += (getAppDir().length() + 1);
        String targetPath = path.substring(pkgIndex, path.length());
        write(targetPath, updateContent);
    }

    /**
     * 写入文件
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年4月1日 下午2:50:36
     */
    public void write(File dir, String content) {
        write(dir.getPath(), content);
    }

    /**
     * 读取string字符串
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月29日 上午11:53:09
     */
    public String readString(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            KLog.e("此目录非文件:" + file.getPath());
            return null;
        }
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));
            String str = null;
            if ((str = reader.readLine()) != null) {
                sb.append(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(reader);
        }
        return sb.toString();
    }

    /**
     * 读取一个bitmap对象
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月29日 上午11:50:00
     */
    public Bitmap readBitmap(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            KLog.e("此目录非文件:" + file.getPath());
            return null;
        }
        return BitmapFactory.decodeFile(path);
    }

    /**
     * 打开文件目录
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月29日 上午11:18:43
     */
    public File openDir(String path) {
        return new File(path);
    }

    /**
     * 打开某文件
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月29日 上午10:21:08
     */
    public InputStream open(String path) {
        File file = new File(getStorage(), path);
        if (!file.exists()) {
            KLog.e("未找到文件:" + file.getPath());
            return null;
        }
        if (file.isDirectory()) {
            KLog.e("目录不可被打开:" + file.getPath());
            return null;
        }
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return is;
    }

    /**
     * 写入文件内容
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午6:11:26
     */
    public void write(String file, String writeContent) {
        File f = new File(getStorage(), file);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            KLog.d("文件不存在,新建文件:" + f.getPath());
        }
        try {
            write(new FileOutputStream(f), writeContent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入文件内容
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午6:12:16
     */
    public void write(OutputStream os, String writeContent) {
        write(os, writeContent.getBytes());
    }

    /**
     * 写入byte数据
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月29日 下午12:08:46
     */
    public void write(OutputStream os, byte[] writeContent) {
        try {
            os.write(writeContent);
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(os);
        }
    }

    /**
     * 写入图片
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月29日 下午12:10:48
     */
    public void write(File dir, Bitmap bitmap) {
        File file = new File(dir, getBitmapName());
        BufferedOutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, arrayOutputStream);
            write(os, arrayOutputStream.toByteArray());
            KLog.d("写入图片:" + file.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeStream(os);
        }
    }

    /**
     * 创建文件
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午6:05:21
     */
    public OutputStream mkfile(File dir, String fileName) {
        if (!dir.isDirectory()) {
            KLog.e("文件目录错误:" + dir.getPath());
            return null;
        }
        File file = new File(dir, fileName);
        if (file.exists()) {
            KLog.e(EXIST_FILE + ":" + dir.getPath());
            try {
                return new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            if (file.createNewFile()) {
                KLog.d("文件目录创建成功:" + file.getPath());
            } else {
                KLog.e("文件目录创建失败:" + file.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除目录
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午5:34:34
     */
    public void delete(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            KLog.e("目录不存在:" + file.getPath());
            return;
        }
        if (file.delete()) {
            KLog.d("删除成功:" + file.getPath());
        } else {
            deleteDir(file);
        }
    }

    /**
     * 创建目录,创建在
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午4:44:50
     */
    public File mkdirs(String filePath) {
        if (filePath == null) {
            if (!getStorage().exists()) {
                getStorage().mkdirs();
                KLog.d("创建root目录:" + getStorage().getPath());
            }
            return getStorage();
        }
        File last = new File(getStorage(), filePath);
        if (last.exists()) {
            KLog.e("目录已经存在:" + last.getPath());
            return last;
        }
        if (last.mkdirs()) {
            KLog.d("创建成功:" + last.getPath());
            return last;
        } else {
            KLog.d("创建失败:" + last.getPath());
            return null;
        }
    }

    /**
     * 获取sd卡路径
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午4:47:59
     */
    public File getSDCard() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 判断外部存储是否有效
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午4:45:51
     */
    public boolean isExternStorage() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable();
    }

    /**
     * 获取data缓存路径
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午4:37:38
     */
    public File getDataCache(Context context) {
        return getCacheDir(context, CacheServer.DATA_CACHE);
    }

    /**
     * 获取存储路径
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午5:33:14
     */
    public File getStorage() {
        File file = null;
        if (isExternStorage()) {
            file = getSDCard();
        } else {
            file = mContext.getCacheDir();
        }
        return new File(file, File.separator + getAppDir());
    }

    /**
     * 获取bitmap缓存路径
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月28日 下午4:37:01
     */
    public File getBitmapCache(Context context) {
        return getCacheDir(context, CacheServer.BITMAP_CACHE);
    }

    private File getCacheDir(Context context, String name) {
        File dir = null;
        if (isExternStorage()) {
            dir = context.getExternalCacheDir();
        } else {
            dir = context.getCacheDir();
        }
        return new File(dir, name);
    }

    private void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    boolean deleteCache(DiskLruCache lruCache) {
        File file = lruCache.getDirectory();
        File[] caches = file.listFiles();
        int i = 0;
        while (caches.length <= 1 && i <= 5) {
            caches = file.listFiles();
            i++;
        }
        if (caches.length <= 1) {
            KLog.e(CacheServer.NO_CACHE_INFO);
            return false;
        }
        for (File cache : caches) {
            String name = cache.getName();
            if (!name.equals(CacheServer.FILE_NAME)) {
                cache.delete();
            }
        }
        return true;
    }

    private boolean hasDirectory(File path) {
        return path.exists() && path.isDirectory();
    }

    public String getBitmapName() {
        StringBuffer sb = new StringBuffer();
        sb.append(Calendar.getInstance().get(Calendar.YEAR))
                .append(Calendar.getInstance().get(Calendar.MONTH) + 1)
                .append(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                .append(Calendar.getInstance().get(Calendar.HOUR))
                .append(Calendar.getInstance().get(Calendar.MINUTE))
                .append(Calendar.getInstance().get(Calendar.SECOND))
                .append(".png");
        return sb.toString();
    }

    private boolean checkFile(File file) {
        if (!file.exists()) {
            KLog.e("文件不存在:" + file.getPath());
            return false;
        }
        if (file.isDirectory()) {
            KLog.e("只能修改文件内容:" + file.getPath());
            return false;
        }
        return true;
    }


    /**
     * 获取文件夹大小
     */
    public String getPathSizeUseShell(File path) {

        if (path == null) {
            KLog.e("文件路径为空");
            return null;
        }
        String result = "";
        try {
            InputStream in = Runtime.getRuntime().exec("du " + path.getPath() + " -sh").getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                if (TextUtils.equals(str.substring(str.indexOf("/"), str.length()), path.toString())) {
                    String tmpSize = str.replaceAll(path.toString(), "").trim();
                    if (tmpSize.lastIndexOf("K") == -1 && tmpSize.lastIndexOf("M") == -1
                            && tmpSize.lastIndexOf("G") == -1) {
                        try {
                            //K
                            int size = Integer.parseInt(tmpSize);
                            //M
                            if (size >= 1024) {
                                size /= 1024;
                                //G
                                if (size >= 1024) {
                                    size /= 1024;
                                    result = size + "GB";
                                } else {
                                    result = size + "MB";
                                }
                            } else {
                                result = size + "KB";
                            }

                        } catch (Exception e) {
                            return null;
                        }
                    } else {
                        result = tmpSize;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        KLog.i(path.toString() + " -> " + result);
        return result;
    }

    /**
     * 删除目录
     */
    public void deleteDir(File path) {
        if (path == null) {
            KLog.e("文件路径为空");
            return;
        }
        try {
            Runtime.getRuntime().exec("rm -r " + path.toString());
            KLog.d("删除目录：" + path.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getAppDir() {
        return mContext.getApplicationInfo().processName;
    }

    private void mkdirRoot() {
        mkdirs(null);
    }
}

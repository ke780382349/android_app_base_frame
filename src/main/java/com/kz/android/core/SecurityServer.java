package com.kz.android.core;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.kz.android.special.AppServer;

import android.content.Context;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 类描述
 *
 * @author 作者 E-mail:
 * @since 创建时间：2016年3月28日 下午4:23:51
 */
public class SecurityServer implements AppServer {
    SecurityServer() {
    }

    @Override
    public void initServer(Context context) {

    }

    /**
     * MD5加密
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年3月30日 下午1:53:38
     */
    public String md5(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }
    public String base64encode(String key){
        return Base64.encodeToString(key.getBytes(),Base64.DEFAULT);
    }
    public byte[] base64decode(String key){
        return Base64.decode(key.getBytes(),Base64.DEFAULT);
    }


    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}

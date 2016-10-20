package com.kz.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/** 
 * 判断网络状态
 * @author 作者 E-mail: 柯壮
 * @since 创建时间：2016年4月1日 下午2:53:23
 */
public class KNetwork {
	public static final int NETWORK_NO = 1;
	public static final int NETWORK_WIFI = 2;
	public static final int NETWORK_MOBILE = 3;
	public static final int NETWORK_OTHER = 4;
	/**
	 * 判断网络是否有效
	 * @author 作者:柯壮
	 * @since 创建时间：2016年4月1日 下午3:00:43
	 */
	public static boolean isAvailable(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if(info==null){
			return false;
		}
		return info.isConnectedOrConnecting();
	}
	/**
	 * 获取网络状态
	 * @author 作者:柯壮
	 * @since 创建时间：2016年4月1日 下午3:00:51
	 * @see  {@link #NETWORK_NO} {@link #NETWORK_WIFI} {@link #NETWORK_MOBILE}
	 */
	public static int getNetworkState(Context context){
		int type = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if(info==null){
			type = NETWORK_NO;
		}else if(info.getType()==ConnectivityManager.TYPE_MOBILE){
			type = NETWORK_MOBILE;
		}else if(info.getType()==ConnectivityManager.TYPE_WIFI){
			type = NETWORK_WIFI;
		}else{
			type = NETWORK_OTHER;
		}
		return type;
	}
}

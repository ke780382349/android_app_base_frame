/**
 * 
 */
package com.kz.android.app;

import android.content.Context;

import com.kz.android.core.RegisterServer;
import com.kz.android.special.AppServer;
import com.kz.android.util.KLog;

/**
 * 核心包 上下文
 */
public class FrameContext {
	/** 获取Activity的服务 */
	public static final String APP_ACTIVITY_SERVER = "app_activity_server";
	/** 获取管理组件Service的服务 */
	public static final String APP_SERVICE_SERVER = "app_service_server";
	 /**获取数据库服务 */
	 public static final String APP_SQLITE_SERVER = "app_sqlite_server";
	/** 获取http请求服务 */
	public static final String APP_HTTP_SERVER = "app_http_server";
	/** 获取文件服务 */
	public static final String APP_FILE_SERVER = "app_file_server";
	/** 获取缓存服务 */
	public static final String APP_CACHE_SERVER = "app_cache_server";
	/** 获取加密服务 */
	public static final String APP_SECURITY_SERVER = "app_security_server";

	private static FrameContext sSinoContext = null;
	private RegisterServer serverFetch;

	private FrameContext(Context context) {
		serverFetch = new RegisterServer();
	}

	/**
	 * 获取一个SinoServer对象 同时也初始化所有的Server服务,建议在base中触发
	 * 
	 * @author 作者:柯壮
	 * @since 创建时间：2016年3月28日 下午5:01:14
	 */
	public static AppServer getServer(Context context, String name) {
		if (sSinoContext == null) {
			sSinoContext = new FrameContext(context);
			sSinoContext.serverFetch.initServer(context);
		}
		return sSinoContext.getServerFetch().getServer(name);
	}

	private RegisterServer getServerFetch() {
		return serverFetch;
	}
}

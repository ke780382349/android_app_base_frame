package com.kz.android.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.Context;

import com.kz.android.app.FrameContext;
import com.kz.android.special.AppServer;
import com.kz.android.util.KSingleton;

/**
 * 服务注册类
 * 
 * @author 作者 E-mail:柯壮 15011033790
 * @since 创建时间：2016年3月28日 上午10:02:14
 */
public class RegisterServer {
	private static final Map<String, KSingleton<AppServer>> ALL_SERVER = new HashMap<String, KSingleton<AppServer>>();
	static {
		registerServer(FrameContext.APP_ACTIVITY_SERVER,
				new KSingleton<AppServer>() {

					@Override
					protected AppServer create() {
						return new ActivityServer();
					}
				});
		registerServer(FrameContext.APP_SERVICE_SERVER,
				new KSingleton<AppServer>() {
					@Override
					protected AppServer create() {
						return new ServiceServer();
					}
				});
		registerServer(FrameContext.APP_HTTP_SERVER,
				new KSingleton<AppServer>() {

					@Override
					protected AppServer create() {
						return new HttpServer();
					}
				});
		registerServer(FrameContext.APP_FILE_SERVER,
				new KSingleton<AppServer>() {
					@Override
					protected AppServer create() {
						return new FileServer();
					}
				});
		registerServer(FrameContext.APP_SQLITE_SERVER,
				new KSingleton<AppServer>() {

					@Override
					protected AppServer create() {
						return new SQLiteServer();
					}
				});
		registerServer(FrameContext.APP_CACHE_SERVER,
				new KSingleton<AppServer>() {

					@Override
					protected AppServer create() {
						return new CacheServer();
					}
				});
		registerServer(FrameContext.APP_SECURITY_SERVER,
				new KSingleton<AppServer>() {

					@Override
					protected AppServer create() {
						return new SecurityServer();
					}
				});
	}

	private static <T> void registerServer(String name,
			KSingleton<AppServer> server) {
		ALL_SERVER.put(name, server);
	}

	public void initServer(Context context) {
		Set<String> keys = ALL_SERVER.keySet();
		for (String str : keys) {
			KSingleton<AppServer> server = ALL_SERVER.get(str);
			server.get().initServer(context);
		}
	}

	public AppServer getServer(String name) {
		KSingleton<AppServer> server = null;
		if (ALL_SERVER.containsKey(name)) {
			server = ALL_SERVER.get(name);
			return server.get();
		}
		return null;
	}
}

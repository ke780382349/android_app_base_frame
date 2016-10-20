/**
 * 
 */
package com.kz.android.core;

import java.util.ArrayList;
import java.util.List;

import com.kz.android.special.AppServer;

import android.app.Service;
import android.content.Context;

/** 
 * 服务组件的服务类
 * @author 作者 E-mail:柯壮  15011033790
 * @since 创建时间：2016年3月28日 上午10:02:14
 */
public class ServiceServer implements AppServer{
	private static final List<Service> ALL_SERVICE = new ArrayList<Service>();
	ServiceServer() {
	}
	@Override
	public void initServer(Context context) {
	}
	/**
	 * 注册一个activity
	 * @author 作者:柯壮
	 * @since 创建时间：2016年3月28日 下午4:09:58
	 */
	public void addService(Service service){
		ALL_SERVICE.add(service);
	}
	/**
	 * 移除一个activity
	 * @author 作者:柯壮
	 * @since 创建时间：2016年3月28日 下午4:10:15
	 */
	public void removeService(Service service){
		ALL_SERVICE.remove(service);
	}
	/**
	 * 清除activity
	 * @author 作者:柯壮
	 * @since 创建时间：2016年3月28日 下午4:10:26
	 */
	public void clearService(){
		ALL_SERVICE.clear();
	}
	/**
	 * 获取一个activity
	 * @author 作者:柯壮
	 * @since 创建时间：2016年3月28日 下午4:10:43
	 */
	public Service getService(int index){
		return ALL_SERVICE.get(index);
	}
	/**
	 * 设置一个activity
	 * @author 作者:柯壮
	 * @since 创建时间：2016年3月28日 下午4:10:53
	 */
	public void updateService(int index,Service updateService){
		Service service = ALL_SERVICE.get(index);
		if(service==null){
			ALL_SERVICE.set(index, updateService);
			return ;
		}
		if(service==updateService){
			return ;
		}
		if(service!=updateService){
			ALL_SERVICE.set(index, updateService);
		}
	}
	/**
	 * 关闭所有activity
	 * @author 作者:柯壮
	 * @since 创建时间：2016年3月28日 下午4:11:03
	 */
	public void shutdowmAllService(){
		for(Service service : ALL_SERVICE){
			if(service==null){
				continue ;
			}
			service.stopSelf();
		}
	}
}

/**
 * 
 */
package com.kz.android.core;

import android.app.Activity;
import android.content.Context;

import com.kz.android.base.KBaseActivity;
import com.kz.android.special.AppServer;

import java.util.ArrayList;
import java.util.List;

/** 
 * activity组件的服务类
 * @author 作者 E-mail:柯壮  15011033790
 * @since 创建时间：2016年3月28日 上午10:02:14
 */
public class ActivityServer implements AppServer{
	private static final List<KBaseActivity> ALL_ACTIVITY = new ArrayList<KBaseActivity>();

	ActivityServer() {
	}
	@Override
	public void initServer(Context context) {
	}
	/**
	 * 注册一个activity
	 * @since 创建时间：2016年3月28日 下午4:09:58
	 */
	public void addActivity(KBaseActivity activity){
		ALL_ACTIVITY.add(activity);
	}
	/**
	 * 移除一个activity
	 * @since 创建时间：2016年3月28日 下午4:10:15
	 */
	public void removeActivity(KBaseActivity activity){
		ALL_ACTIVITY.remove(activity);
	}
	/**
	 * 获取一个activity
	 * @since 创建时间：2016年3月28日 下午4:10:43
	 */
	public KBaseActivity getActivity(int index){
		return ALL_ACTIVITY.get(index);
	}
	/**
	 * 设置一个activity
	 * @since 创建时间：2016年3月28日 下午4:10:53
	 */
	public void updateActivity(int index,KBaseActivity activity){
		Activity acitivity2 = ALL_ACTIVITY.get(index);
		if(acitivity2==null){
			ALL_ACTIVITY.set(index, activity);
			return ;
		}
		if(acitivity2==activity){
			return ;
		}
		if(acitivity2!=activity){
			ALL_ACTIVITY.set(index, activity);
		}
	}
	/**
	 * 关闭所有activity
	 * @since 创建时间：2016年3月28日 下午4:11:03
	 */
	public void finishAllActivity(){
		for(Activity activity : ALL_ACTIVITY){
			if(activity==null){
				continue ;
			}
			activity.finish();
		}
	}
	/**
	 * 获取activity
	 * @since 创建时间：2016年6月3日 下午2:21:11
	 * */
	public KBaseActivity getActivity(Class clz){
		for(int i=0;i<ALL_ACTIVITY.size();i++){
			if(getActivity(i).getClass().equals(clz)){
				return getActivity(i);
			}
		}
		return null;
	}
	/**
	 *  关闭activity
	 * */
	public void finish(Class clz){
		KBaseActivity activity  = getActivity(clz);
		if(activity!=null){
			activity.finish();
			removeActivity(activity);
		}
	}
}

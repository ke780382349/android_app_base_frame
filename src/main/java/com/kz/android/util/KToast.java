package com.kz.android.util;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * toast统一管理类
 * @since 2016年2月24日 11:28:07
 * @version v1.0.0
 * @author KeZhuang
 */
@SuppressLint("ShowToast")
public class KToast {
	public static final boolean TOAST = true;
	private static final String CLASS_NAME = KToast.class.getSimpleName();
	private static KToast mKToast = new KToast();
	private Toast mDebugToast;
	private Toast mReleaseToast;
	private Toast getDebugToast(Context context,String content){
		if(mDebugToast==null){
			mDebugToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		}else{
			mDebugToast.setText(content);
		}
		return mDebugToast;
	}
	private Toast getReleaseToast(Context context,String content){
		if(mReleaseToast==null){
			mReleaseToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		}else{
			mReleaseToast.setText(content);
		}
		return mReleaseToast;
	}
	public static void debugToast(final Context context, final String content){
		if(TOAST){
			mKToast.getDebugToast(context,content).show();
			if(context instanceof Activity){
				((Activity)context).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						StringBuffer sb  = new StringBuffer();
						sb.append("debugToast").append("  ");
						sb.append("class:").append(KJavaStack.getCaller(CLASS_NAME)).append("  ");
						sb.append("line number:").append(KJavaStack.getCallerLine(KJavaStack.getCallerMethod(2)));
						KLog.e(sb.toString());
					}
				});
			}else{
				if(Looper.myLooper()!=Looper.getMainLooper()){
					KLog.e("非主线程Toast");
					return ;
				}
				StringBuffer sb  = new StringBuffer();
				sb.append("debugToast").append("  ");
				sb.append("class:").append(KJavaStack.getCaller(CLASS_NAME)).append("  ");
				sb.append("line number:").append(KJavaStack.getCallerLine(KJavaStack.getCallerMethod(2)));
				KLog.e(sb.toString());
			}
		}
	}
	public static void releaseToast(final Context context, final String content){
		if(context instanceof Activity){
			((Activity)context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mKToast.getReleaseToast(context, content).show();
				}
			});
		}else{
			if(Looper.myLooper()!=Looper.getMainLooper()){
				KLog.e("非主线程Toast");
				return ;
			}
			mKToast.getReleaseToast(context, content).show();
		}
	}
}

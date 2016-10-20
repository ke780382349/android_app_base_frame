/**
 * 
 */
package com.kz.android.util;

/**
 * java stack 工具类
 * @author KeZhuang
 *
 */
public class KJavaStack {
	private static final String GET_CALLER_METHOD = "getCallerMethod";
	/**检测对象是否为null*/
	public static boolean check_null_obj(Object object){
		return object == null;
	}
	public static String getCaller(String currentClzName){
		if(currentClzName==null){
			throw new NullPointerException();
		}
		StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
		String currentName = currentClzName;
		String tag = null;
		int index = 0;
		for(int i=traceElements.length-1;i>=0;i--){
			String stackName = traceElements[i].getClassName();
			if(stackName.contains(currentName)){
				index = i+=1;
				break;
			}
		}
		tag = traceElements[index].getClassName();
		String[] splitCaller = tag.split("\\.");
		if(splitCaller!=null&&splitCaller.length>0){
			tag = splitCaller[splitCaller.length-1];
		}
		return tag;
	}
	public static int getCallerLine(String methodName){
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		int lineNum = 0;
		for(int i=stackTraceElements.length-1;i>=0;i--){
			String method = stackTraceElements[i].getMethodName();
			if(method==null){
				return -1;
			}
			if(method.equals(methodName)){
				return stackTraceElements[i].getLineNumber();
			}
		}
		return lineNum;
	}
	public static String getCallerMethod(int callPlyNum){
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		int index = 0;
		for(int i=stackTraceElements.length-1;i>=0;i--){
			if(stackTraceElements[i].getMethodName()!=null&&
					stackTraceElements[i].getMethodName().equals(GET_CALLER_METHOD)){
				index = i+=callPlyNum;
				break;
			}
		}
		return stackTraceElements[index].getMethodName();
	}
}

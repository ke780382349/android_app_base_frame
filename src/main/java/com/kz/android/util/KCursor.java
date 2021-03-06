package com.kz.android.util;
import static com.kz.android.util.KLog.i;

import android.database.Cursor;
/**
 * @author KeZhuang
 *
 */
public class KCursor {
	/**遍历cursor*/
	public static void printCursor(String tag,Cursor cursor){
		checkCursor(cursor);
		while(cursor.moveToNext()){
			for(int i=0;i<cursor.getColumnCount();i++){
				printColumnData(tag,cursor, i);
			}
		}
		cursor.moveToFirst();
	}
	/**打印cursor内容*/
	public static void printColumnData(String tag,Cursor cursor,String columnName){
		checkCursor(cursor);
		int index = getColumnIndex(cursor,columnName);
		printColumnData(tag,cursor, index);
	}
	/**打印cursor内容*/
	public static void printColumnData(String tag,Cursor cursor,int columnIndex){
		checkCursor(cursor);
		String data = getColumnData(cursor, columnIndex);
		String name = getColumnName(cursor, columnIndex);
		i(tag,name+"\t"+data);
	}
	/**获取cursor的column数据*/
	public static String getColumnData(Cursor cursor,String columnName){
		int index = getColumnIndex(cursor, columnName);
		return getColumnData(cursor, index);
	}
	/**获取cursor的column数据*/
	public static String getColumnData(Cursor cursor,int columnIndex){
		checkCursor(cursor);
		return cursor.getString(columnIndex);
	}
	/**获取cursor的column名字*/
	public static String getColumnName(Cursor cursor,int columnIndex){
		checkCursor(cursor);
		return cursor.getColumnName(columnIndex);
	}
	/**获取cursor的column索引*/
	public static int getColumnIndex(Cursor cursor,String columnName){
		checkCursor(cursor);
		return cursor.getColumnIndex(columnName);
	}
	/**检查cursor是否为null*/
	private static void checkCursor(Cursor cursor){
		if(cursor==null)
			throw new NullPointerException("cursor 不能为空");
	}
}

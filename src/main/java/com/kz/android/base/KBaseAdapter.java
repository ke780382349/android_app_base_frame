package com.kz.android.base;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;

import com.kz.android.annotation.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * 采用适配器模式,增加一些必要方法
 */
public abstract class KBaseAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected View parentView;
    protected int mCurrentPosition;
    protected List<T> mData = new ArrayList<>();

    public KBaseAdapter(Context context) {
        mContext = context;
    }

    public void setCurrentPosition(int position) {
        mCurrentPosition = position;
    }

    /**
     * 抽象方法：设置adapter的数据
     */
    public abstract void setList(int page, List<T> list);

    /**
     * 得到数据集合
     */
    public List<T> getList() {
        if (mData != null) {
            return mData;
        }
        return new ArrayList<>();
    }

    /**
     * 获得实体类,有些数据会是不同的集合,所以封装成实体类放adapter中
     */
    public T getEntity() {
        return null;
    }

    /**
     * 清空数据
     */
    public void clear() {
        if (mData != null) {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        if (mData != null) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 抽象ViewHolder，继承这个类后ViewHolder就可以用注解来完成
     */
    public class KViewHodler {
        public KViewHodler(View view) {
            parentView = view;
            Inject.idForObject(this, view, getClass().getDeclaredFields());
        }
    }
}

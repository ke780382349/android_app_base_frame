package com.kz.android.annotation;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.kz.android.base.KBaseActivity;

/**
 * Created by 柯壮 on 2016/7/2.
 * 标题的建造者
 */
public class TitleBuilder {
    //基类Activity
    private KBaseActivity mActivity;

    public TitleBuilder(KBaseActivity activity) {
        mActivity = activity;
    }

    /**
     * 是否使用标题
     */
    public TitleBuilder usable(boolean bool) {
        if (bool) {
            mActivity.showTitle();
        } else {
            mActivity.hideTitle();
        }
        return this;
    }

    /**
     * 标题颜色
     */
    public TitleBuilder color(String color) {
        if (!TextUtils.isEmpty(color)) {
            mActivity.frameTitle.setBackgroundColor(Color.parseColor(color));
            mActivity.titleLine.setBackgroundColor(Color.parseColor(color));
        }
        return this;
    }

    /**
     * 左边图片
     * 左边有可能是图片,非文字
     */
    public TitleBuilder lImg(int idRes) {
        mActivity.showView(mActivity.titleLeftImg);
        if(idRes > 0){
            mActivity.titleLeftImg.setImageResource(idRes);
        }
        return this;
    }

    /**
     * c代表中间
     * 标题内容
     */
    public TitleBuilder cText(String title) {
        if (TextUtils.isEmpty(title)) {
            mActivity.hideGone(mActivity.titleCenterText);
        } else {
            mActivity.showView(mActivity.titleCenterText);
            mActivity.titleCenterText.setText(title);
        }
        return this;
    }


    /**
     * 右边图片
     * 右边可能是图片,非文字
     */
    public TitleBuilder rImg(int idRes) {
        if (idRes == 0) {
            mActivity.hideGone(mActivity.titleRightImg);
        } else {
            mActivity.showView(mActivity.titleRightImg);
            mActivity.titleRightImg.setImageResource(idRes);
        }
        return this;
    }

    /**
     * 右边文字
     * 右边可以是图片,可以是文字
     */
    public TitleBuilder rText(String text) {
        if (TextUtils.isEmpty(text)) {
            mActivity.hideGone(mActivity.titleRightText);
        } else {
            mActivity.showView(mActivity.titleRightText);
            mActivity.titleRightText.setText(text);
        }
        return this;
    }

    /**
     * 右边layout
     * 如果是layout的话就要隐藏文字和图片。只显示layout
     * 格式固定,一个图片,一个文字
     */
    public TitleBuilder rLayout(int idRes, String text) {
        mActivity.showView(mActivity.titleRightLayout);
        if (idRes > 0) {
            mActivity.titleRightLayoutImg.setImageResource(idRes);
        }
        if (!TextUtils.isEmpty(text)) {
            mActivity.titleRightLayoutText.setText(text);
        }
        return this;
    }

    /**
     * 中间layout
     * 格式不固定
     * 中间layout
     */
    public TitleBuilder cLayout(int idRes) {
        if (idRes == 0) {
            mActivity.hideGone(mActivity.titleCenterLayout);
        }
        if (idRes != 0) {
            mActivity.showView(mActivity.titleCenterLayout);
            View centerLayout = View.inflate(mActivity, idRes, null);
            mActivity.titleCenterLayout.removeAllViews();
            mActivity.titleCenterLayout.addView(centerLayout);
        }
        return this;
    }

    /**
     * 只是满足一下格式
     * 并不做任何操作
     */
    public void build() {

    }
}

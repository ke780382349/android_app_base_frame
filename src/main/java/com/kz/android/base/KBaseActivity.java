package com.kz.android.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kz.android.annotation.Inject;
import com.kz.android.app.FrameContext;
import com.kz.android.core.ActivityServer;
import com.kz.android.core.HttpServer;
import com.kz.android.util.KToast;
import com.kz.core.R;

/**
 * 柯壮
 * Activity基类
 */
public class KBaseActivity extends FragmentActivity {
    public AnimType mAnimType;
    public RelativeLayout frameTitle;                                   //框架标题
    public FrameLayout frameContent;                                    //框架内容
    public ImageView titleLeftImg;                                      //左图片
    public TextView titleLeftText;                                     //左文字
    public ImageView titleRightImg;                                     //右图片
    public TextView titleCenterText;                                    //中间文字
    public LinearLayout titleCenterLayout;                              //中间的Layout
    public TextView titleRightText;                                     //右文字
    public View titleLine;                                              //标题上的线
    public LinearLayout titleRightLayout;                               //右边的layout
    public ImageView titleRightLayoutImg;                               //右边layout里的img
    public TextView titleRightLayoutText;                               //右边layout里的text
    public ActivityServer mActivityServer;                              //用来管理所有的activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getActivityTheme());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        //由于改成依赖库后R文件编译出来的就不是常量了,所以只能用findViewById的方式去寻找了
        frameTitle = (RelativeLayout) findViewById(R.id.frame_title);
        frameContent = (FrameLayout) findViewById(R.id.frame_content);
        titleLeftImg = (ImageView) findViewById(R.id.title_left_img);
        titleRightImg = (ImageView) findViewById(R.id.title_right_img);
        titleCenterText = (TextView) findViewById(R.id.title_center_text);
        titleLeftText = (TextView) findViewById(R.id.title_left_text);
        titleCenterLayout = (LinearLayout) findViewById(R.id.title_center_container);
        titleLine = findViewById(R.id.frame_title_line);
        titleRightText = (TextView) findViewById(R.id.title_right_text);
        titleRightLayout = (LinearLayout) findViewById(R.id.title_right_layout);
        titleRightLayoutImg = (ImageView) findViewById(R.id.title_right_layout_img);
        titleRightLayoutText = (TextView) findViewById(R.id.title_right_layout_text);
        titleLeftImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mActivityServer = (ActivityServer) FrameContext.getServer(this, FrameContext.APP_ACTIVITY_SERVER);
        mActivityServer.addActivity(this);
        Inject.header(this);
        Inject.anim(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityServer.removeActivity(this);
        ((HttpServer)FrameContext.getServer(this,FrameContext.APP_HTTP_SERVER)).clearDialog(this);
    }

    public int getActivityTheme() {
        return android.R.style.Theme_Light_NoTitleBar;
    }

    @Override
    public void setContentView(int layoutResID) {
        if (layoutResID == R.layout.base_activity) {
            super.setContentView(layoutResID);
        } else {
            frameContent.removeAllViews();
            frameContent.addView(getLayoutInflater().inflate(layoutResID, null));
            Inject.idForObject(this, getWindow().getDecorView(), getClass().getDeclaredFields());
        }
    }

    /**
     * 隐藏标题
     */
    public void hideTitle() {
        hideGone(titleLine, frameTitle);
    }

    /**
     * 显示标题
     */
    public void showTitle() {
        showView(titleLine, frameTitle);
    }

    /**
     * 显示view
     */
    public void showView(View... view) {
        dealView(View.VISIBLE, view);
    }

    /**
     * 隐藏view
     */
    public void hideInVisiable(View... view) {
        dealView(View.INVISIBLE, view);
    }

    /**
     * 隐藏view
     */
    public void hideGone(View... view) {
        dealView(View.GONE, view);
    }

    /**
     * 显示吐司
     */
    public void showToast(String content) {
        KToast.releaseToast(this, content);
    }

    /**
     * 这个方法不提供容器,主要用来缓存数据
     */
    public void addFragment(Fragment fragment) {
        addFragment(-1, fragment, fragment.getClass().getSimpleName(), false);
    }

    /**
     * 添加fragment,tag默认为fragment的类名
     */
    public void addFragment(int container, Fragment fragment, boolean isBackStack) {
        addFragment(container, fragment, fragment.getClass().getSimpleName(), isBackStack);
    }

    /**
     * 添加fragment
     */
    public void addFragment(int container, Fragment fragment, String tag, boolean isBackStack) {
        FragmentTransaction transaction = beginTransaction();
        if (isBackStack) {
            transaction.addToBackStack(null);
        }
        if (container == -1) {
            transaction.add(fragment, tag).commit();
            return;
        }
        transaction.add(container, fragment, tag).commit();
    }

    /**
     * 替换fragment
     */
    public void replaceFragment(int container, Fragment fragment) {
        replaceFragment(container, fragment, fragment.getClass().getSimpleName(), false);
    }


    /**
     * 替换fragment
     */
    public void replaceFragment(int container, Fragment fragment, String tag, boolean isBackStack) {
        FragmentTransaction transaction = beginTransaction();
        if (isBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(container, fragment, tag).commit();
    }

    /**
     * 移除fragment
     */
    public void removeFragment(Fragment fragment) {
        beginTransaction().remove(fragment).commit();
    }

    /**
     * 替换fragment的另外一种,此种方法对应hideFragment,不走生命周期
     * setUserVisibleHint方法,这个方法会在onCreate之前调用,要注意!!
     * onHiddenChange方法
     */
    public void showFragment(Fragment showFragment, Fragment hideFragment) {
        beginTransaction().show(showFragment).hide(hideFragment).commit();
    }

    /**
     * 查找fragment
     */
    public KBaseFragment findFragmentByTag(String tag) {
        return (KBaseFragment) getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (mAnimType != null) {
            switch (mAnimType) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
                case NONE:
                    overridePendingTransition(0, 0);
                    break;
            }
        }
    }

    /**
     * 跳转activity
     *
     * @param clz 跳转到的类上
     */
    public void goIntent(Class<?> clz) {
        goIntent(clz, null);
    }

    /**
     * 跳转activity
     *
     * @param clz    跳转的类
     * @param bundle 传递参数
     */
    public void goIntent(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 开启一组activity
     */
    public void goIntent(Intent[] clzs) {
        startActivities(clzs);
    }

    /**
     * 带有Fragment动画的事务
     */
    public FragmentTransaction beginTransaction() {
        return beginTransaction(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * 带有Fragment动画的事务,可以自定义动画
     */
    public FragmentTransaction beginTransaction(int enter, int out) {
        return getSupportFragmentManager().beginTransaction().setCustomAnimations(enter, out);
    }

    @Override
    public void finish() {
        super.finish();
        if (mAnimType != null) {
            switch (mAnimType) {
                case LEFT:
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
                case NONE:
                    overridePendingTransition(0, 0);
                    break;
            }
        }
    }

    /**
     * 没有动画的finish
     */
    public void noAnimFinish() {
        mAnimType = null;
        finish();
    }

    private void dealView(int flag, View... views) {
        for (View item : views) {
            item.setVisibility(flag);
        }
    }

    public enum AnimType {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE, Gravity, NONE
    }

}

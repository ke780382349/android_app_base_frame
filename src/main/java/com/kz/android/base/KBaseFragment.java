package com.kz.android.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.kz.android.annotation.Inject;

/**
 * Fragment基类
 */
public class KBaseFragment extends Fragment {

    protected KBaseActivity mActivity;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Inject.idForObject(this,getView(),getClass().getDeclaredFields());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (KBaseActivity) getActivity();
    }

    /**
     * 显示view
     */
    public void showView(View... view) {
        mActivity.showView(view);
    }

    /**
     * 隐藏view
     */
    public void hideInVisiable(View... view) {
        mActivity.hideInVisiable(view);
    }

    /**
     * 隐藏view
     */
    public void hideGone(View... view) {
        mActivity.hideGone(view);
    }

    /**
     * 显示吐司
     */
    public void showToast(String content) {
        mActivity.showToast(content);
    }

    /**
     * 隐藏标题
     */
    public void hideTitle() {
        mActivity.hideTitle();
    }
    /**
     * 显示标题
     * */
    public void showTitle(){
        mActivity.showTitle();
    }

    /**
     * 查找fragment
     */
    public KBaseFragment findFragmentByTag(String tag) {
        return mActivity.findFragmentByTag(tag);
    }
    /**
     *  跳转activity
     *  @param clz 跳转到的类上
     * */
    public void goIntent(Class<?> clz){
        mActivity.goIntent(clz);
    }
    /**
     * 跳转activity
     *
     * @param clz    跳转的类
     * @param bundle 传递参数
     */
    public void goIntent(Class<?> clz,Bundle bundle){
        mActivity.goIntent(clz,bundle);
    }
    /**
     * 开启一组activity
     * */
    public void goIntent(Intent[] clzs){
        mActivity.goIntent(clzs);
    }

    /**
     * 这个方法不提供容器,主要用来缓存数据
     */
    public void addFragment(Fragment fragment) {
        addFragment(-1, fragment,false);
    }

    /**
     * 添加fragment,tag默认为fragment的类名
     */
    public void addFragment(int container, Fragment fragment,boolean isBackStack) {
        addFragment(container, fragment, fragment.getClass().getSimpleName(),isBackStack);
    }

    /**
     * 添加fragment
     */
    public void addFragment(int container, Fragment fragment, String tag,boolean isBackStack) {
        mActivity.addFragment(container,fragment,tag,isBackStack);
    }

    /**
     * 替换fragment
     */
    public void replaceFragment(int container, Fragment fragment) {
        replaceFragment(container, fragment, fragment.getClass().getSimpleName(),false);
    }

    /**
     * 替换fragment
     */
    public void replaceFragment(int container, Fragment fragment, String tag,boolean isBackStack) {
        mActivity.replaceFragment(container,fragment,tag,isBackStack);
    }

    /**
     * 移除fragment
     */
    public void removeFragment(Fragment fragment) {
        mActivity.removeFragment(fragment);
    }

    /**
     * 替换fragment的另外一种,此种方法对应hideFragment,不走生命周期
     * setUserVisibleHint方法,这个方法会在onCreate之前调用,要注意!!
     * onHiddenChange方法
     */
    public void showFragment(Fragment showFragment,Fragment hideFragment) {
        mActivity.showFragment(showFragment,hideFragment);
    }

}

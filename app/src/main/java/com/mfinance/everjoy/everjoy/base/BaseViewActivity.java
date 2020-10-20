package com.mfinance.everjoy.everjoy.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.ColorUtils;

import com.google.android.material.appbar.AppBarLayout;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.LoadingDialog;
import com.mfinance.everjoy.everjoy.utils.FullStatusUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 主要用于view的基类
 */
public abstract class BaseViewActivity extends BaseEverjoyActivity {

    // toolbar的父view
    protected AppBarLayout mAppBarLayout;
    // 声明toolbar：标题居左
    protected Toolbar mBaseToolbar;
    // 标题居中显示
    protected TextView mBaseTitle;

    // 顶层布局
    protected RelativeLayout mRootRelativeLayout;

    // ButterKnife
    private Unbinder mUnbinder;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置状态栏
        setStatusBarText(this, getResources().getColor(setStatusBarColor()));
        initData();
        @SuppressLint("InflateParams") View baseView = LayoutInflater.from(this)
                .inflate(R.layout.activity_base, null, false);
        initBaseView(baseView);
        setContentView(baseView);
    }

    /**
     * Android 6.0 以上设置状态栏颜色
     * 状态栏设置白色，则字体设置黑色
     * 状态栏设置黑色，则字体设置白色
     */
    private void setStatusBarText(Activity activity, int color) {
        Window window = activity.getWindow();
        // 设置状态栏底色颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(color);
        // 如果亮色，设置状态栏文字为黑色
        if (isLightColor(color)) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    /**
     * 判断颜色是不是亮色
     * <p>
     * https://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
     */
    private boolean isLightColor(int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }

    /**
     * 子类实现各自的状态栏颜色
     */
    protected int setStatusBarColor() {
        return R.color.whiteff;
    }

    private void initBaseView(View mBaseView) {
        // appbar包含toolbar
        mAppBarLayout = mBaseView.findViewById(R.id.base_appbar);
        mBaseToolbar = mBaseView.findViewById(R.id.base_toolbar);
        // 子类的根布局
        mRootRelativeLayout = mBaseView.findViewById(R.id.base_rl);

        // 线条
        View viewLine = mBaseView.findViewById(R.id.base_view_line);
        boolean visibleLine = isVisibleToolbarLine();
        if (visibleLine) {
            viewLine.setVisibility(View.VISIBLE);
        } else {
            viewLine.setVisibility(View.GONE);
        }

        boolean removeAppBar = isRemoveAppBar();
        Log.e("base", "-----------removeAppBar = " + removeAppBar);
        if (removeAppBar) {
            mRootRelativeLayout.removeView(mAppBarLayout);
            mRootRelativeLayout.removeView(viewLine);
        } else {
            // 不要标题，不设置toolbar的导航图标
            mBaseToolbar.setTitle("");
            setSupportActionBar(mBaseToolbar);
            // 中间标题
            mBaseTitle = mBaseView.findViewById(R.id.tv_base_title);
            // 返回按钮
            ImageView mBaseIvBack = mBaseView.findViewById(R.id.iv_base_back);
            mBaseIvBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean onShowToolbarBack = onToolbarBackAndFinish();
                    if (onShowToolbarBack) {
                        onBackPressed();
                    }
                }
            });

            // 中间标题
            mBaseTitle = mBaseToolbar.findViewById(R.id.tv_base_title);
        }

        // 设置沉浸式view
        View viewFullStatus = mBaseView.findViewById(R.id.view_full_status);
        boolean showStatus = isFullStatusByView();
        Log.e("base", "showStatus = " + showStatus);
        if (showStatus) {
            FullStatusUtils.setStatusBarTan(this, viewFullStatus);
        } else {
            mRootRelativeLayout.removeView(viewFullStatus);
            // 白底黑字
            FullStatusUtils.setStatusBarText(this, Color.parseColor("#FFFFFF"));
        }

        // 添加view（子类的activity）布局
        mRootRelativeLayout.addView(addCurrentView(setLayoutResId()));
    }

    /**
     * 子类activity布局
     *
     * @param layoutResId xml布局文件id
     * @return view
     */
    private View addCurrentView(int layoutResId) {
        View currentView = LayoutInflater.from(this).inflate(layoutResId, mRootRelativeLayout, false);
        // 添加在appbar、线条的下面
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) currentView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.base_view_line);
        // 初始化ButterKnife
        mUnbinder = ButterKnife.bind(this, currentView);
        // 初始化控件
        initView(currentView);
        return currentView;
    }

    /**
     * 子类实现设置布局layout
     */
    protected abstract int setLayoutResId();

    /**
     * 子类实现初始化自己的视图控件View，
     * currentView.findViewById(R.id.xxx),否则拿不到控件view
     */
    protected abstract void initView(View currentView);

    /**
     * 子类实现初始化数据，
     * 不要在这里做view的初始化，否则报view的空指针
     */
    protected void initData() {

    }

    /**
     * 是否需要删除Appbar
     *
     * @return 子类传true表示删除
     */
    protected boolean isRemoveAppBar() {
        return false;
    }

    /**
     * 是否使用沉浸式布局
     */
    protected boolean isFullStatusByView() {
        return false;
    }

    /**
     * 返回之前处理一些事情，子类传false则表示base不finish当前页面
     */
    protected boolean onToolbarBackAndFinish() {
        return true;
    }

    /**
     * 是否显示toolbar下面一条线
     *
     * @return 子类传false表示不显示
     */
    protected boolean isVisibleToolbarLine() {
        return true;
    }

    /**
     * toolbar居左设置标题，并且中间TextView消失
     *
     * @param title 标题
     */
    protected void setTitleCenter(String title) {
        if (mBaseTitle != null) {
            mBaseTitle.setVisibility(View.VISIBLE);
            mBaseTitle.setText(title);
        }
        if (mBaseToolbar != null) {
            mBaseToolbar.setTitle("");
        }
    }

    /**
     * toolbar居左设置标题，并且中间TextView消失
     *
     * @param title 标题
     */
    protected void setTitleCenter(int title) {
        if (mBaseTitle != null) {
            mBaseTitle.setVisibility(View.VISIBLE);
            mBaseTitle.setText(title);
        }
        if (mBaseToolbar != null) {
            mBaseToolbar.setTitle("");
        }
    }

    /**
     * toolbar居左设置标题，并且中间TextView消失
     *
     * @param title 标题
     */
    protected void setTitleLeft(String title) {
        if (mBaseTitle != null) {
            mBaseTitle.setVisibility(View.GONE);
        }
        if (mBaseToolbar != null) {
            mBaseToolbar.setTitle(title);
        }
    }

    /**
     * 隐藏加载进度
     */
    protected void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    /**
     * 设置加载框的标题
     */
    protected void setLoadingMessage(String message) {
        if (loadingDialog != null) {
            loadingDialog.setMessage(message);
        }
    }

    /**
     * 显示加载进度，当view加载完成时显示加载进度条
     */
    protected void showLoading() {
        showLoading("");
    }

    /**
     * 显示加载进度，当view加载完成时显示加载进度条
     */
    protected void showLoading(int message) {
        loadingDialog = new LoadingDialog(this, message);
        loadingDialog.show();
    }

    /**
     * 显示加载进度，当view加载完成时显示加载进度条
     *
     * @param message 提示信息
     */
    protected void showLoading(String message) {
        loadingDialog = new LoadingDialog(this, message);
        loadingDialog.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        hideLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}

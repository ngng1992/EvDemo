package com.mfinance.everjoy.everjoy.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.LoadingDialog;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 所有fragment的基类
 */
public abstract class BaseViewFragment extends Fragment {

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    private FrameLayout mBaseFrameLayout;
    // ButterKnife
    private Unbinder mUnbinder;
    // 加载动画
    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 避免发送fragment重叠 {https://www.jianshu.com/p/c12a98a36b2b}
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (isSupportHidden) {
                    fragmentTransaction.hide(this);
                } else {
                    fragmentTransaction.show(this);
                }
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_base, null, false);
        initBaseView(view);
        return view;
    }

    private void initBaseView(View view) {
        mBaseFrameLayout = view.findViewById(R.id.base_frame);
        // 子类实现添加布局
        mBaseFrameLayout.addView(addCurrentView(setLayoutResId()));
    }

    private View addCurrentView(int layoutResId) {
        View currentView = LayoutInflater.from(getActivity()).inflate(layoutResId, mBaseFrameLayout, false);
        // 初始化ButterKnife
        mUnbinder = ButterKnife.bind(this, currentView);
        initView(currentView);
        return currentView;
    }

    /**
     * 子类实现设置布局layout
     */
    protected abstract int setLayoutResId();

    /**
     * 子类实现初始化视图控件View
     */
    protected abstract void initView(View currentView);

    /**
     * 子类实现初始化数据
     */
    protected void initData() {
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
        if (loadingDialog != null && loadingDialog.isShowing()) {
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
        loadingDialog = new LoadingDialog(getActivity(), message);
        loadingDialog.show();
    }

    /**
     * 显示加载进度，当view加载完成时显示加载进度条
     *
     * @param message 提示信息
     */
    protected void showLoading(String message) {
        loadingDialog = new LoadingDialog(getActivity(), message);
        loadingDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideLoading();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

}

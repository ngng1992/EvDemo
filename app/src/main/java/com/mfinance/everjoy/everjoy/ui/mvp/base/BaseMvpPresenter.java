package com.mfinance.everjoy.everjoy.ui.mvp.base;

import android.util.Log;

import com.lzy.okgo.OkGo;

import java.lang.ref.WeakReference;

/**
 * 抽象类Presenter添加要实现view的泛型类
 */
public abstract class BaseMvpPresenter<V> {

    protected String mTAG;

    // 弱引用
    private WeakReference<V> mViewRef;

    /**
     * 与目标view建立关联
     *
     * @param view 绑定的View
     */
    public void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
        mTAG = view.getClass().getSimpleName();
        Log.e("mvp", "attachView：mTAG=" + mTAG);
    }

    /**
     * 获取当前弱引用对象
     *
     * @return V
     */
    protected V getView() {
        return mViewRef.get();
    }

    /**
     * 判断当前view是否关联
     *
     * @return boolean
     */
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    /**
     * 解除关联
     */
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
        Log.e("mvp", "取消网络mTAG=" + mTAG);
        cancelTag();
    }

    public void cancelTag() {
        OkGo.getInstance().cancelTag(mTAG);
    }
}

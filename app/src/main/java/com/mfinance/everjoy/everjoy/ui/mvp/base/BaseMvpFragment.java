package com.mfinance.everjoy.everjoy.ui.mvp.base;


import com.mfinance.everjoy.everjoy.base.BaseViewFragment;

/**
 * MVP模式所有fragment基类
 *
 */
public abstract class BaseMvpFragment<V, T extends BaseMvpPresenter<V>> extends BaseViewFragment {

    // Presenter对象
    protected T mPresenter;

    @Override
    protected void initData() {
        // 创建Presenter
        mPresenter = createPresenter();
        // View与Presenter建立关联
        mPresenter.attachView((V) this);
    }

    protected abstract T createPresenter();

    /**
     * 解除关联
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter.isViewAttached()) {
            mPresenter.detachView();
        }
    }
}

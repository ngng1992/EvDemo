package com.mfinance.everjoy.everjoy.ui.mvp.base;


import com.mfinance.everjoy.everjoy.base.BaseViewActivity;

/**
 * MVP模式抽象类Activity
 *
 * @author xiao 2017-05-24
 */
public abstract class BaseMvpViewActivity<V, T extends BaseMvpPresenter<V>> extends BaseViewActivity {

    // Presenter对象
    protected T mPresenter;

    @Override
    protected void initData() {
        super.initData();
        // 创建Presenter
        mPresenter = createPresenter();
        // View与Presenter建立关联
        mPresenter.attachView((V) this);
    }

    /**
     * 创建泛型的Presenter
     *
     * @return t 对应子类的Presenter
     */
    protected abstract T createPresenter();

    /**
     * 解除关联
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter.isViewAttached()) {
            mPresenter.detachView();
        }
    }
}

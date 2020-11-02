package com.mfinance.everjoy.everjoy.ui.mvp.base;

/**
 * mvp模式
 *
 */
public interface BaseRefreshMvpView<T> extends BaseMvpView<T> {

    /**
     * 显示刷新
     *
     */
    void onShowRefresh();

    /**
     * 隐藏刷新
     *
     */
    void onHideRefresh();
}

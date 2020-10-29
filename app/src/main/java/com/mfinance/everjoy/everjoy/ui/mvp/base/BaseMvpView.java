package com.mfinance.everjoy.everjoy.ui.mvp.base;

/**
 * mvp模式
 *
 */
public interface BaseMvpView<T> {

    /**
     * 显示数据
     *
     * @param data 对应bean数据
     */
    void onShowData(T data);

    /**
     * 显示错误
     * @param msg 错误信息
     */
    void onShowError(String msg);

    /**
     * 显示进度条
     *
     */
    void onShowLoading();

    /**
     * 隐藏进度条
     *
     */
    void onHideLoading();

}

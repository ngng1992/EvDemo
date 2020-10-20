package com.mfinance.everjoy.everjoy.dialog.impl;

import android.view.View;

/**
 * 点击view控件
 * 用法：
 *     @Override
 *     public void onAttach(Context context) {
 *         super.onAttach(context);
 *         try {
 *             onClickDialogOrFragmentViewListener = (OnClickDialogOrFragmentViewListener) context;
 *         } catch (RuntimeException e) {
 *             e.printStackTrace();
 *             throw new RuntimeException("activity no implements OnClickDialogOrFragmentViewListener");
 *         }
 *     }
 *
 *     @Override
 *     public void onDestroy() {
 *         super.onDestroy();
 *         if (onClickDialogOrFragmentViewListener != null) {
 *             onClickDialogOrFragmentViewListener = null;
 *         }
 *     }
 *     fragment重处理点击事件传给OnClickDialogOrFragmentViewListener接口处理
 *     activity实现OnClickLiveFragmentListener接口处理结果
 */
public interface OnClickDialogOrFragmentViewListener<T> {

    /**
     * 点击子控件
     *
     * @param view 子控件
     * @param object 数据
     */
    void onClickView(View view, T object);

}

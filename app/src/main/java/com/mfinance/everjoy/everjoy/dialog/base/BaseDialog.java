package com.mfinance.everjoy.everjoy.dialog.base;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;

/**
 * 所有dialog基类
 */
public abstract class BaseDialog extends Dialog {

    /**
     * 有黑色背景
     */
    protected static final int STYLE_DARK = R.style.BaseDialogDark;
    /**
     * 白色背景
     */
    protected static final int STYLE_BRIGHT = R.style.BaseDialog;

    protected Context context;
    protected OnClickDialogOrFragmentViewListener onClickDialogOrFragmentViewListener;

    public void setOnClickDialogOrFragmentViewListener(OnClickDialogOrFragmentViewListener onClickDialogOrFragmentViewListener) {
        this.onClickDialogOrFragmentViewListener = onClickDialogOrFragmentViewListener;
    }

    /**
     * 默认有黑色背景
     */
    public BaseDialog(Context context) {
        this(context, STYLE_DARK);
        this.context = context;
    }

    /**
     * 添加style
     */
    public BaseDialog(Context context, int style) {
        super(context, style);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在dialog外是否可以点击
        setCanceledOnTouchOutside(canceledOnTouchOutside());
        setCancelable(cancelable());
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.ScaleAnimationStyle);
            window.setBackgroundDrawable(new ColorDrawable());
            boolean transparent = isTransparent();
            if (transparent) {
                // 因为状态栏底色已经设置了白色，这里设置取消半透明色，会覆盖掉状态栏，看不到状态栏的时间显示等
                window.setDimAmount(0f);
            }
        }
    }

    /**
     * 是否可以外部点击，默认true，dialog消失，子类实现返回false，dialog不消失
     */
    protected boolean canceledOnTouchOutside() {
        return true;
    }

    /**
     * 是否屏蔽返回键按钮，默认true，dialog消失，子类实现返回false，dialog不消失
     */
    protected boolean cancelable() {
        return true;
    }

    /**
     * 是否显示半透明遮罩，子类传true不显示半透明遮罩
     */
    protected boolean isTransparent() {
        return false;
    }
}

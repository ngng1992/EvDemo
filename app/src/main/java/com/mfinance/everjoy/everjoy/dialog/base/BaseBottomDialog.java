package com.mfinance.everjoy.everjoy.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;

/**
 * 底部出现dialog
 */
public abstract class BaseBottomDialog extends Dialog {

    protected Context mContext;
    protected OnClickDialogOrFragmentViewListener onClickDialogOrFragmentViewListener;

    public void setOnClickDialogOrFragmentViewListener(OnClickDialogOrFragmentViewListener onClickDialogOrFragmentViewListener) {
        this.onClickDialogOrFragmentViewListener = onClickDialogOrFragmentViewListener;
    }

    public BaseBottomDialog(@NonNull Context context) {
        super(context, R.style.BaseDialogDark);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在dialog外是否可以点击
        setCanceledOnTouchOutside(true);
        setContentView(setLayout());
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            // x轴与y轴位置
            layoutParams.x = 0;
            layoutParams.y = window.getWindowManager().getDefaultDisplay().getHeight();
            // 设置布局参数
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            // dialog自适应窗口
            onWindowAttributesChanged(layoutParams);
            // 设置动画
            window.setWindowAnimations(R.style.BottomViewAnimStyle);
        }
        initView();
    }

    protected abstract void initView();

    protected abstract int setLayout();
}

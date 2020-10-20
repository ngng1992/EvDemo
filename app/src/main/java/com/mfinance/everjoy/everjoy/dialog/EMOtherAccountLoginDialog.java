package com.mfinance.everjoy.everjoy.dialog;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.base.BaseDialog;


/**
 * 环信账号在别的设备上登录提示
 */
public class EMOtherAccountLoginDialog extends BaseDialog {

    public EMOtherAccountLoginDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_em_other_account_login);
        TextView tvOk = findViewById(R.id.tv_define);
        tvOk.setOnClickListener(v -> {
            dismiss();
            if (onClickDialogOrFragmentViewListener != null) {
                onClickDialogOrFragmentViewListener.onClickView(tvOk, "");
            }
        });
    }

    @Override
    protected boolean cancelable() {
        return false;
    }

    @Override
    protected boolean canceledOnTouchOutside() {
        return false;
    }
}

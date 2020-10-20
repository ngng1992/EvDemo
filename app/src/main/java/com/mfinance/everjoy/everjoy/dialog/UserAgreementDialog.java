package com.mfinance.everjoy.everjoy.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.base.BaseDialog;

/**
 * 用户协议
 */
public class UserAgreementDialog extends BaseDialog {

    public UserAgreementDialog(Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_open_account_file);
        ImageView ivClose = findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}

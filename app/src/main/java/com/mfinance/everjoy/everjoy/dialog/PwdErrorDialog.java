package com.mfinance.everjoy.everjoy.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.base.BaseDialog;
import com.mfinance.everjoy.everjoy.ui.mine.ForgetPwdActivity;


/**
 * 密码登录错误5次提示警告
 */
public class PwdErrorDialog extends BaseDialog {

    public PwdErrorDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pwd_error);
        TextView tv_know = findViewById(R.id.tv_know);
        TextView tv_forget_pwd = findViewById(R.id.tv_forget_pwd);
        tv_know.setOnClickListener(v -> {
            dismiss();
        });
        tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                context.startActivity(new Intent(context, ForgetPwdActivity.class));
            }
        });
    }
}

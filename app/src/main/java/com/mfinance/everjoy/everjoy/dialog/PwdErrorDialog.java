package com.mfinance.everjoy.everjoy.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.base.BaseDialog;
import com.mfinance.everjoy.everjoy.ui.mine.ContactActivity;
import com.mfinance.everjoy.everjoy.ui.mine.ForgetPwdActivity;
import com.mfinance.everjoy.everjoy.ui.mine.securities.SecuritiesAccountActivity;

import net.mfinance.commonlib.view.StringTextView;


/**
 * 密码登录错误大于3次小于5次提示警告
 */
public class PwdErrorDialog extends BaseDialog {

    private int errorCount = 0;

    public PwdErrorDialog(Context context, int errorCount) {
        super(context);
        this.errorCount = errorCount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pwd_error);
        TextView tv_login_point_title = findViewById(R.id.tv_login_point_title);
        TextView tv_know = findViewById(R.id.tv_know);
        tv_know.setOnClickListener(v -> {
            dismiss();
        });

        String verifMsg = String.format(context.getResources().getString(R.string.str_pwd_error), errorCount);
        String target = verifMsg.substring(verifMsg.length() - 7);
        new StringTextView(tv_login_point_title)
                .setStrText(verifMsg)
                .setColor(context.getResources().getColor(R.color.blue18))
                .setTextSize(1f)
                .setTargetText(target)
                .setUnderline(false)
                .setClick(false)
                .create();
    }
}

package com.mfinance.everjoy.everjoy.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.base.BaseBottomDialog;

import net.mfinance.commonlib.toast.ToastUtils;

/**
 * 风险纰漏
 */
public class RiskDisclosureDialog extends BaseBottomDialog {

    private String username;
    private String usernameCode;

    private boolean cbx1;
    private boolean cbx2;

    public RiskDisclosureDialog(@NonNull Context context, String username, String usernameCode) {
        super(context);
        this.username = username;
        this.usernameCode = usernameCode;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        ImageView ivClose = findViewById(R.id.iv_close);
        TextView tv_content = findViewById(R.id.tv_content);
        CheckBox cbx_desc1 = findViewById(R.id.cbx_desc1);
        CheckBox cbx_desc2 = findViewById(R.id.cbx_desc2);
        TextView tv_open = findViewById(R.id.tv_open);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_content.setText(username + "    " + usernameCode);
        cbx1 = cbx_desc1.isChecked();
        cbx2 = cbx_desc2.isChecked();
        cbx_desc1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbx1 = isChecked;
            }
        });
        cbx_desc2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbx2 = isChecked;
            }
        });
        tv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cbx1) {
                    ToastUtils.showToast(mContext, R.string.str_checked);
                    return;
                }
                if (!cbx2) {
                    ToastUtils.showToast(mContext, R.string.str_checked);
                    return;
                }

                if (onClickDialogOrFragmentViewListener != null) {
                    onClickDialogOrFragmentViewListener.onClickView(tv_open, "");
                }
                dismiss();
            }
        });
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_risk_disclosure;
    }
}

package com.mfinance.everjoy.everjoy.ui.mine;

import android.content.Intent;
import android.os.Message;
import android.os.RemoteException;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.config.Contants;
import com.mfinance.everjoy.everjoy.ui.mine.securities.SecuritiesAccountActivity;

import net.mfinance.commonlib.view.StringTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 重置密码
 */
public class ResetPwdActivity extends BaseViewActivity {
    private String type;

    @BindView(R.id.et_oldpwd)
    EditText etOldpwd;
    @BindView(R.id.et_newpwd)
    EditText etNewpwd;
    @BindView(R.id.iv_show_newpwd)
    ImageView ivShowNewpwd;
    @BindView(R.id.et_define_pwd)
    EditText etDefinePwd;
    @BindView(R.id.iv_show_define_pwd)
    ImageView ivShowDefinepwd;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_contact)
    TextView tvContact;

    @Override
    protected boolean isRemoveAppBar() {
        return true;
    }

    @Override
    protected boolean isFullStatusByView() {
        return true;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    protected void initView(View currentView) {
        Intent intent = getIntent();
        type = intent.getStringExtra(ServiceFunction.RESETPASSWORD_TYPE);
        // 在线咨询
        String verifMsg = getString(R.string.sec_acc_ui_contact);
        String target = verifMsg.substring(verifMsg.length() - 4);
        new StringTextView(tvContact)
                .setStrText(verifMsg)
                .setColor(getResources().getColor(R.color.blue18))
                .setTextSize(1f)
                .setTargetText(target)
                .setUnderline(false)
                .setClick(true)
                .setOnClickSpannableStringListener(new StringTextView.OnClickSpannableStringListener() {
                    @Override
                    public void onClickSpannableString(View view) {
                        ContactActivity.startContactActivity(ResetPwdActivity.this);
                    }
                })
                .create();
    }

    @OnClick({R.id.iv_show_newpwd, R.id.iv_show_define_pwd, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_show_newpwd:
                ivShowNewpwd.setSelected(!ivShowNewpwd.isSelected());
                if (ivShowNewpwd.isSelected()) {
                    // 明文
                    etNewpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // 密文
                    etNewpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.iv_show_define_pwd:
                ivShowDefinepwd.setSelected(!ivShowDefinepwd.isSelected());
                if (ivShowDefinepwd.isSelected()) {
                    etDefinePwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etDefinePwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.tv_submit:
                resetPassword(etOldpwd.getText().toString(), etNewpwd.getText().toString());
//                // 重置密码成功
//                startActivity(new Intent(this, SetPwdSuccessActivity.class));
                break;
            default:
                break;
        }
    }

    public void resetPassword(String oldPassword, String newPassword) {
        Message resetPwdMsg = Message.obtain(null, ServiceFunction.SRV_RESET_PASSWORD);
        resetPwdMsg.replyTo = mServiceMessengerHandler;

        resetPwdMsg.getData().putString(ServiceFunction.RESETPASSWORD_TYPE, type);
        resetPwdMsg.getData().putString(ServiceFunction.RESETPASSWORD_OLDPASSWORD, oldPassword);
        resetPwdMsg.getData().putString(ServiceFunction.RESETPASSWORD_NEWPASSWORD, newPassword);
        try {
            mService.send(resetPwdMsg);
        } catch (RemoteException e) {
            Log.e("login", "Unable to send login message", e.fillInStackTrace());
        }
    }
}

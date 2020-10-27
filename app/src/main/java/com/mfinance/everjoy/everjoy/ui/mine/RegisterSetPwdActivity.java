package com.mfinance.everjoy.everjoy.ui.mine;

import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 邮箱注册，设置密码
 */
public class RegisterSetPwdActivity extends BaseViewActivity {

    @BindView(R.id.et_newpwd)
    EditText etNewpwd;
    @BindView(R.id.iv_show_newpwd)
    ImageView ivShowNewpwd;
    @BindView(R.id.et_define_pwd)
    EditText etDefinePwd;
    @BindView(R.id.iv_show_define_pwd)
    ImageView ivShowDefinePwd;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_go_login)
    TextView tvGoLogin;
    @BindView(R.id.tv_user_agreement)
    TextView tvUserAgreement;

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
        return R.layout.activity_register_set_pwd;
    }

    @Override
    protected void initView(View currentView) {

    }

    @OnClick({R.id.iv_back, R.id.iv_show_newpwd, R.id.iv_show_define_pwd, R.id.tv_register, R.id.tv_go_login, R.id.tv_user_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
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
                ivShowDefinePwd.setSelected(!ivShowDefinePwd.isSelected());
                if (ivShowDefinePwd.isSelected()) {
                    // 明文
                    etDefinePwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // 密文
                    etDefinePwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.tv_register:
                // TODO 注册成功后，进入开户页面
                startActivity(new Intent(this, RegisterSuccessOpenAccountActivity.class));
                break;
            case R.id.tv_go_login:
                LoginActivity.loginActivity(this);
                break;
            case R.id.tv_user_agreement:
                startActivity(new Intent(this, UserAgreementActivity.class));
                break;
            default:
                break;
        }
    }
}

package com.mfinance.everjoy.everjoy.ui.mine;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 重置密码
 */
public class ResetPwdActivity extends BaseViewActivity {


    @BindView(R.id.et_newpwd)
    EditText etNewpwd;
    @BindView(R.id.iv_show_newpwd)
    ImageView ivShowNewpwd;
    @BindView(R.id.et_define_pwd)
    EditText etDefinePwd;
    @BindView(R.id.iv_show_definepwd)
    ImageView ivShowDefinepwd;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    protected boolean isVisibleToolbarLine() {
        return false;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    protected void initView(View currentView) {

    }

    @OnClick({R.id.iv_show_newpwd, R.id.iv_show_definepwd, R.id.tv_submit})
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
            case R.id.iv_show_definepwd:
                ivShowDefinepwd.setSelected(!ivShowDefinepwd.isSelected());
                if (ivShowDefinepwd.isSelected()) {
                    etDefinePwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etDefinePwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.tv_submit:
                break;
            default:
                break;
        }
    }


}

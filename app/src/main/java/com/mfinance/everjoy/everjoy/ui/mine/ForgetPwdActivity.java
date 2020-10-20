package com.mfinance.everjoy.everjoy.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 忘记密码
 */
public class ForgetPwdActivity extends BaseViewActivity {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    protected boolean isVisibleToolbarLine() {
        return false;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initView(View currentView) {

    }

    @OnClick({R.id.et_email, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_email:
                break;
            case R.id.tv_submit:
                break;
            default:
                break;
        }
    }

    /**
     * 验证成功，跳转至登录验证
     */
    private void showVerifSuccess() {
        String email = etEmail.getText().toString();
        LoginVerificationActivity.startLoginVerificationActivity(this, email);
    }
}

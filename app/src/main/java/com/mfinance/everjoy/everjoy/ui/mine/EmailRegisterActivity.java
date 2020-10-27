package com.mfinance.everjoy.everjoy.ui.mine;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;

import net.mfinance.commonlib.timer.CountDownHelper;
import net.mfinance.commonlib.timer.OnTimerCallBack;
import net.mfinance.commonlib.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 邮箱注册
 */
public class EmailRegisterActivity extends BaseViewActivity {


    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_user_agreement)
    TextView tvUserAgreement;

    private CountDownHelper countDownHelper;


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String string = s.toString();
            if (string.length() > 0) {
                tvGetCode.setClickable(true);
                tvGetCode.setTextColor(getResources().getColor(R.color.blue18));
            } else {
                tvGetCode.setClickable(false);
                tvGetCode.setTextColor(getResources().getColor(R.color.gray999));
            }
        }
    };

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
        return R.layout.activity_email_register;
    }

    @Override
    protected void initView(View currentView) {
        etEmail.addTextChangedListener(textWatcher);
    }

    @OnClick({R.id.iv_back, R.id.tv_get_code, R.id.tv_register, R.id.tv_go_login, R.id.tv_user_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_get_code:
                getCode();
                break;
            case R.id.tv_register:
                registerAccount();
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

    private void registerAccount() {
//        String email = etEmail.getText().toString();
//        if (TextUtils.isEmpty(email)) {
//            ToastUtils.showToast(this, R.string.toast_input_email);
//            return;
//        }
//
//        if (!RegexUtils.isEmail(email)) {
//            ToastUtils.showToast(this, R.string.str_email_rule_error);
//            return;
//        }
//
//        String code = etCode.getText().toString();
//        if (TextUtils.isEmpty(code)) {
//            ToastUtils.showToast(this, R.string.toast_input_code);
//            return;
//        }

        // TODO 校验验证码，注册成功后设置密码
        startActivity(new Intent(this, RegisterSetPwdActivity.class));
    }


    private void getCode() {
        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            ToastUtils.showToast(this, R.string.toast_input_email);
            return;
        }
        if (!RegexUtils.isEmail(email)) {
            ToastUtils.showToast(this, R.string.str_email_rule_error);
            return;
        }

        // TODO 获取验证码后计时
        startTimer();
    }

    private void startTimer() {
        tvGetCode.setClickable(false);
        countDownHelper = new CountDownHelper(this);
        countDownHelper.setTime(60);
        tvGetCode.setTextColor(getColor(R.color.gray999));
        countDownHelper.setOnTimerTaskCallBack(new OnTimerCallBack() {
            @Override
            public void onCallBack(long time) {
                if (time <= 0) {
                    tvGetCode.setClickable(true);
                    tvGetCode.setText(R.string.str_resend_code);
                    tvGetCode.setTextColor(getColor(R.color.blue18));
                } else {
                    String timeContent = String.format(getString(R.string.str_second), time);
                    tvGetCode.setText(timeContent);
                }
            }
        });
        countDownHelper.startTimer();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownHelper != null) {
            countDownHelper.stopTimer();
        }
    }
}

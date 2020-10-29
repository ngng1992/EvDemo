package com.mfinance.everjoy.everjoy.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.bean.base.BaseBean;
import com.mfinance.everjoy.everjoy.config.Contants;
import com.mfinance.everjoy.everjoy.ui.mvp.base.BaseMvpViewActivity;
import com.mfinance.everjoy.everjoy.ui.mvp.presenter.EmailRegisterPresenter;
import com.mfinance.everjoy.everjoy.ui.mvp.presenter.ForgotPasswordPresenter;
import com.mfinance.everjoy.everjoy.ui.mvp.view.EmailRegisterView;
import com.mfinance.everjoy.everjoy.ui.mvp.view.ForgetPwdView;

import net.mfinance.commonlib.timer.CountDownHelper;
import net.mfinance.commonlib.timer.OnTimerCallBack;
import net.mfinance.commonlib.toast.ToastUtils;
import net.mfinance.commonlib.view.StringTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 忘记密码
 */
public class ForgetPwdActivity extends BaseMvpViewActivity<ForgetPwdView, ForgotPasswordPresenter>
        implements ForgetPwdView {

    String type = null;
    String email = null;

    @BindView(R.id.et_acc)
    EditText etAcc;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_contact)
    TextView tvContact;
    @BindView(R.id.tv_tips)
    TextView tvTips;

    private CountDownHelper countDownHelper;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initView(View currentView) {
        // 在线咨询
        Intent intent = getIntent();
        type = intent.getStringExtra(ServiceFunction.FORGETPASSWORD_TYPE);

        if (type.equals("2")) {
            etAcc.setVisibility(View.GONE);
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
                            ContactActivity.startContactActivity(ForgetPwdActivity.this);
                        }
                    })
                    .create();
        }
        else if (type.equals("3")) {
            tvTips.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
                if (type.equals("2"))
                    getCode();
                else if (type.equals("3"))
                    forgotPassword();
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            ToastUtils.showToast(this, R.string.toast_input_email);
            return;
        }
        if (!RegexUtils.isEmail(email)) {
            ToastUtils.showToast(this, R.string.str_email_rule_error);
            return;
        }
        mPresenter.requestEmailCode(email);
    }

    private void forgotPassword() {
        String acc = etAcc.getText().toString();
        email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            ToastUtils.showToast(this, R.string.toast_input_email);
            return;
        }
        if (!RegexUtils.isEmail(email)) {
            ToastUtils.showToast(this, R.string.str_email_rule_error);
            return;
        }
        mPresenter.requestForgotPassword(acc, email);
    }

    @Override
    protected boolean isRemoveAppBar() {
        return true;
    }

    @Override
    protected boolean isFullStatusByView() {
        return true;
    }

    @Override
    protected ForgotPasswordPresenter createPresenter() {
        return new ForgotPasswordPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownHelper != null) {
            countDownHelper.stopTimer();
        }
    }

    @Override
    public void onShowData(BaseBean data) {
        startTimer();
    }

    private void startTimer() {
        countDownHelper = new CountDownHelper(this);
        countDownHelper.setTime(60);
        countDownHelper.setOnTimerTaskCallBack(new OnTimerCallBack() {
            @Override
            public void onCallBack(long time) {
                if (time <= 0) {
                } else {
                    String timeContent = String.format(getString(R.string.str_second), time);
                }
            }
        });
        countDownHelper.startTimer();
    }

    @Override
    public void onShowError(String msg) {
        ToastUtils.showToast(this, msg);
    }

    @Override
    public void onShowLoading() {
        showLoading();
    }

    @Override
    public void onHideLoading() {
        hideLoading();
    }

    @Override
    public void onShowEmailCheckCode(BaseBean baseBean) {
        String prefix = baseBean.getMessage();

        Bundle data = new Bundle();
        data.putString(ServiceFunction.FORGETPASSWORD_TYPE, "2");
        data.putString(ServiceFunction.FORGETPASSWORD_EMAIL, email);
        data.putString(ServiceFunction.FORGETPASSWORD_PREFIX, prefix);
        //data.putString()
        data.putBoolean(Contants.IS_FORGET_PWD, true);

        //data.putString(ServiceFunction.LOGIN_SEC_PREFIX, strPrefix);
        goTo(ServiceFunction.SRV_MOVE_TO_FORGOT_PASSWORD_OTP, data);
    }

    @Override
    public void onShowEmailCheckCodeError(String msg) {
    }
}

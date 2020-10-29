package com.mfinance.everjoy.everjoy.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.config.Constants;
import com.mfinance.everjoy.everjoy.sp.UserSharedPUtils;
import com.mfinance.everjoy.everjoy.ui.home.MainActivity;

import net.mfinance.commonlib.timer.CountDownHelper;
import net.mfinance.commonlib.timer.OnTimerCallBack;
import net.mfinance.commonlib.view.StringTextView;
import net.mfinance.commonlib.view.VerificationCodeInput;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 验证邮箱
 */
public class LoginVerificationActivity extends BaseViewActivity {


    @BindView(R.id.tv_email_verif_tip)
    TextView tvEmailVerifTip;
    @BindView(R.id.vci_code)
    VerificationCodeInput vciCode;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_resend_code)
    TextView tvResendCode;

    /**
     * 首次在手机上登录需要验证邮箱，提示是否需要开启指纹登录
     */
    public static void startLoginVerificationActivity(Activity activity, String email) {
        Intent intent = new Intent(activity, LoginVerificationActivity.class);
        intent.putExtra(Constants.EMAIL, email);
        activity.startActivity(intent);
    }

    public static void startLoginVerificationActivityWithForgetPwd(Activity activity, String email, boolean isForgetPwd) {
        Intent intent = new Intent(activity, LoginVerificationActivity.class);
        intent.putExtra(Constants.EMAIL, email);
        intent.putExtra(Constants.IS_FORGET_PWD, isForgetPwd);
        activity.startActivity(intent);
    }

    /**
     * 是否是忘记密码跳转过来的
     */
    private boolean isForgetPwd;
    private String email;
    private String code;

    /**
     * 倒计时
     */
    private CountDownHelper countDownHelper;

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
        return R.layout.activity_login_verfication;
    }

    @Override
    protected void initView(View currentView) {
        Intent intent = getIntent();
        email = intent.getStringExtra(Constants.EMAIL);
        isForgetPwd = intent.getBooleanExtra(Constants.IS_FORGET_PWD, false);
        if (isForgetPwd) {
            tvSubmit.setText(R.string.str_submit);
        }

        String verifMsg = String.format(getString(R.string.verif_ui_send_msg), email);
        new StringTextView(tvEmailVerifTip)
                .setStrText(verifMsg)
                .setColor(getResources().getColor(R.color.blue18))
                .setTextSize(1f)
                .setTargetText(email)
                .setUnderline(false)
                .setClick(false)
                .create();

        vciCode.setOnCompleteListener(new VerificationCodeInput.OnCompleteListener() {

            @Override
            public void onComplete(String content) {
                code = content;
                Log.e("view", "验证码 code = " + content);
            }
        });

        startTimer();
    }

    @OnClick({R.id.iv_back, R.id.tv_submit, R.id.tv_resend_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_submit:
//                if (TextUtils.isEmpty(code)) {
//                    ToastUtils.showToast(this, R.string.toast_input_code);
//                    return;
//                }

                // TODO 验证成功
                if (isForgetPwd) {
                    startActivity(new Intent(this, ResetPwdActivity.class));
                }else {
                    // 首次登录，询问是否启用指纹识别
                    boolean loginFirstByEmail = UserSharedPUtils.isLoginFirstByEmail(email);
                    if (loginFirstByEmail) {
                        // 验证邮箱成功后，设置sp为false
                        startActivity(new Intent(this, FingeridOpenActivity.class));
                        UserSharedPUtils.setIsLoginFirst(email, false);
                    } else {
                        MainActivity.startMainActivity2(this);
                        finish();
                    }
                }
                break;
            case R.id.tv_resend_code:
                if (countDownHelper != null) {
                    countDownHelper.stopTimer();
                    countDownHelper = null;
                }
                startTimer();
                break;
            default:
                break;
        }
    }

    /**
     * 开启定时器
     */
    private void startTimer() {
        tvResendCode.setClickable(false);
        tvResendCode.setVisibility(View.VISIBLE);
        countDownHelper = new CountDownHelper(this);
        countDownHelper.setTime(60);
        countDownHelper.setOnTimerTaskCallBack(new OnTimerCallBack() {
            @Override
            public void onCallBack(long time) {
                if (time <= 0) {
                    tvResendCode.setClickable(true);
                    tvResendCode.setText(R.string.verif_ui_resend_code);
                } else {
                    String timeContent = String.format(getString(R.string.verif_ui_resend_code_second), time);
                    tvResendCode.setText(timeContent);
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

package com.mfinance.everjoy.everjoy.ui.mine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.model.LoginSecurityProgress;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.config.Constants;
import com.mfinance.everjoy.everjoy.utils.ToolsUtils;

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
    private String prefix;
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
        prefix = intent.getStringExtra(Constants.PREFIX);
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
                    Runnable r = new moveToOTPResetPassword(code);
                    (new Thread(r)).start();
                } else {
//                    // 首次登录，询问是否启用指纹识别
//                    boolean loginFirstByEmail = UserSharedPUtils.isLoginFirstByEmail(email);
//                    if (loginFirstByEmail) {
//                        // 验证邮箱成功后，设置sp为false
//                        startActivity(new Intent(this, FingeridOpenActivity.class));
//                        UserSharedPUtils.setIsLoginFirst(email, false);
//                    } else {
//                        MainActivity.startMainActivity2(this);
//                        finish();
//                    }

                    //Send Level 3 Login request to server
                    LoginSecurityProgress.reset();
                    Runnable r = new moveToOTPLogin(code);
                    (new Thread(r)).start();
                }
                break;
            case R.id.tv_resend_code:
                if (countDownHelper != null) {
                    countDownHelper.stopTimer();
                    countDownHelper = null;
                }
                startTimer();

                if (isForgetPwd) {
                    Bundle data = new Bundle();
                    data.putString(ServiceFunction.FORGETPASSWORD_TYPE, "2"); //Reset login type level "2"/"3"
                    data.putString(ServiceFunction.FORGETPASSWORD_RESEND, "1");
                    Intent intent = new Intent(this, ForgetPwdActivity.class);
                    intent.putExtras(data);
                    startActivity(intent);
                }else {
                    Runnable r = new moveToLogin(app.getSecLoginID(), app.tempSecPwd, false);
                    (new Thread(r)).start();
                    break;
                }
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
        countDownHelper.setTime(10);
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

    public class moveToOTPResetPassword implements Runnable {
        private String otp;

        public moveToOTPResetPassword() {
        }

        public moveToOTPResetPassword(String otp) {
            this.otp = otp;
        }

        public void run() {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog == null)
                            dialog = ProgressDialog.show(LoginVerificationActivity.this, "", res.getString(R.string.please_wait), true);
                    }
                });

                DataRepository.getInstance().clear();
                if (ToolsUtils.checkNetwork(app)) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            reset(prefix + "-" + otp);
                        }
                    });
                    thread.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class moveToOTPLogin implements Runnable {
        private String otp;

        public moveToOTPLogin() {
        }

        public moveToOTPLogin(String otp) {
            this.otp = otp;
        }

        public void run() {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog == null)
                            dialog = ProgressDialog.show(LoginVerificationActivity.this, "", res.getString(R.string.please_wait), true);
                    }
                });

                DataRepository.getInstance().clear();
                if (ToolsUtils.checkNetwork(app)) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            login(prefix + otp);
                        }
                    });
                    thread.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void login(String strOTP) {
        Message loginMsg = Message.obtain(null, ServiceFunction.SRV_LOGIN);
        loginMsg.replyTo = mServiceMessengerHandler;

        loginMsg.getData().putString(ServiceFunction.LOGIN_LEVEL, "3.1");
        loginMsg.getData().putString(ServiceFunction.LOGIN_SEC_OTP, strOTP);
        try {
            mService.send(loginMsg);
        } catch (RemoteException e) {
            Log.e("login", "Unable to send login message", e.fillInStackTrace());
        }
    }

    public void reset(String strOTP) {
        Message loginMsg = Message.obtain(null, ServiceFunction.SRV_SEND_CHANGE_PASSWORD_OTP_REQUEST);
        loginMsg.replyTo = mServiceMessengerHandler;

        loginMsg.getData().putString(ServiceFunction.FORGETPASSWORD_EMAIL, email);
        loginMsg.getData().putString(ServiceFunction.FORGETPASSWORD_OTP, strOTP);
        try {
            mService.send(loginMsg);
        } catch (RemoteException e) {
            Log.e("login", "Unable to send login message", e.fillInStackTrace());
        }
    }
}

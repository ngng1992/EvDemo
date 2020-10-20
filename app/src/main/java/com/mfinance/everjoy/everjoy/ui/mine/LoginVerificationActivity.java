package com.mfinance.everjoy.everjoy.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.config.Contants;
import com.mfinance.everjoy.everjoy.sp.UserSharedPUtils;
import com.mfinance.everjoy.everjoy.ui.home.MainActivity;

import net.mfinance.commonlib.timer.CountDownHelper;
import net.mfinance.commonlib.timer.OnTimerCallBack;
import net.mfinance.commonlib.toast.ToastUtils;
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

    public static void startLoginVerificationActivity(Activity activity, String email) {
        Intent intent = new Intent(activity, LoginVerificationActivity.class);
        intent.putExtra(Contants.EMAIL, email);
        activity.startActivity(intent);
    }


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
        email = intent.getStringExtra(Contants.EMAIL);

        // 登录有空格
        String login = tvSubmit.getText().toString();
        char[] chars = login.toCharArray();
        login = chars[0] + "  " + chars[1];
        tvSubmit.setText(login);

        // TODO 测试邮箱变色
        String verifMsg = String.format(getString(R.string.verif_ui_send_msg), "169@163.com");
        new StringTextView(tvEmailVerifTip)
                .setStrText(verifMsg)
                .setColor(getResources().getColor(R.color.blue18))
                .setTextSize(1f)
                .setTargetText("169@163.com")
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

    }

    /**
     * 验证成功
     */
    private void verifSuccess() {
        UserSharedPUtils.setIsLoginFirst(email, false);
        MainActivity.startMainActivity2(this);
        finish();
    }

    @OnClick({R.id.tv_submit, R.id.tv_resend_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.showToast(this, R.string.toast_input_code);
                    return;
                }
                startTimer();
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

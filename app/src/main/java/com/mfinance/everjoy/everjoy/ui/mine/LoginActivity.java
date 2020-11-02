package com.mfinance.everjoy.everjoy.ui.mine;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.RegexUtils;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.model.LoginProgress;
import com.mfinance.everjoy.app.pojo.ConnectionStatus;
import com.mfinance.everjoy.app.service.internal.PriceAgentConnectionProcessor;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.dialog.PwdErrorDialog;
import com.mfinance.everjoy.everjoy.dialog.PwdErrorFiveDialog;
import com.mfinance.everjoy.everjoy.sp.UserSharedPUtils;
import com.mfinance.everjoy.everjoy.ui.home.MainActivity;
import com.mfinance.everjoy.everjoy.utils.FingerprintUtils;
import com.mfinance.everjoy.everjoy.utils.ToolsUtils;
import com.mfinance.everjoy.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.modelbase.BaseResp;

import net.mfinance.commonlib.share.LoginUtils;
import net.mfinance.commonlib.share.bean.LoginBean;
import net.mfinance.commonlib.share.impl.OnLoginListener;
import net.mfinance.commonlib.share.plat.Platform;
import net.mfinance.commonlib.toast.ToastUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 */
public class LoginActivity extends BaseViewActivity {

    Timer loginTimer;
    boolean tokenLogin = false;

    public static void loginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.vp_social_login)
    ViewPager vpSocialLogin;
    @BindView(R.id.vpage_social_one)
    View vpageSocialOne;
    @BindView(R.id.vpage_social_two)
    View vpageSocialTwo;
    @BindView(R.id.tv_fingerprint_login)
    TextView tv_fingerprint_login;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    /**
     * 密码错误次数
     */
    private int pwdErrorCount = 0;

    @Override
    protected boolean isRemoveAppBar() {
        return true;
    }

    @Override
    protected boolean isFullStatusByView() {
        return true;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(View currentView) {
        LoginUtils.initLogin(this);
        // 登录有空格
        String login = tvLogin.getText().toString();
        char[] chars = login.toCharArray();
        login = chars[0] + "  " + chars[1];
        tvLogin.setText(login);
        setViewPagerView();

        boolean checkFingerprint = FingerprintUtils.checkFingerprint(this);
        tv_fingerprint_login.setVisibility(checkFingerprint ? View.VISIBLE : View.GONE);

        if (app.getPasswordToken() != null && app.getLoginID() != null) { //Autologin for level 2 users
            tokenLogin = true;
            LoginProgress.reset();
            if (loginTimer != null) {
                loginTimer.cancel();
            }
            loginTimer = new Timer();
            loginTimer.schedule(new Task(), 600 * 1000);

            if (app.getLoginType() == -1) {
                Runnable r = new moveToLogin(true, null, null);
                (new Thread(r)).start();
            } else {
                Runnable r = new moveToLogin(app.getLoginType(), app.getLoginID(), app.getOpenID());
                (new Thread(r)).start();
            }
        }
    }

    private void setViewPagerView() {
        List<View> viewList = new ArrayList<>();
        viewList.add(getPagerView(0));
        viewList.add(getPagerView(1));
        vpSocialLogin.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
        vpSocialLogin.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    vpageSocialOne.setAlpha(1f);
                    vpageSocialTwo.setAlpha(0.3f);
                } else {
                    vpageSocialOne.setAlpha(0.3f);
                    vpageSocialTwo.setAlpha(1f);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @SuppressLint("InflateParams")
    private View getPagerView(int i) {
        View view;
        if (i == 0) {
            view = LayoutInflater.from(this).inflate(R.layout.vp_social_one, null);
            ImageView ivSina = view.findViewById(R.id.iv_sina);
            ImageView ivTwitter = view.findViewById(R.id.iv_twitter);
            ImageView ivQQ = view.findViewById(R.id.iv_qq);
            ivSina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSocialLogin(Platform.SINA);
                }
            });
            ivTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSocialLogin(Platform.TWITTER);
                }
            });
            ivQQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSocialLogin(Platform.QQ);
                }
            });
        } else {
            view = LayoutInflater.from(this).inflate(R.layout.vp_social_two, null);
            ImageView iv_wechat = view.findViewById(R.id.iv_wechat);
            ImageView iv_facebook = view.findViewById(R.id.iv_facebook);
            ImageView iv_ig = view.findViewById(R.id.iv_ig);
            iv_wechat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSocialLogin(Platform.WECAHT);
                }
            });
            iv_facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSocialLogin(Platform.FACEBOOK);
                }
            });
            iv_ig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSocialLogin(Platform.INSTAGRAM);
                }
            });
        }
        return view;
    }

    /**
     * 第三方登录
     *
     * @param platform 哪个平台
     */
    private void startSocialLogin(Platform platform) {
        LoginUtils.startLogin(this, platform, new OnLoginListener() {
            @Override
            public void onLogin(LoginBean loginBean) {
                // 登录成功
                app.setOpenID(loginBean.getOpenId());

                LoginProgress.reset();
                if (loginTimer != null) {
                    loginTimer.cancel();
                }
                loginTimer = new Timer();
                loginTimer.schedule(new Task(), 600 * 1000);
                Runnable r = new moveToLogin(loginBean.getOAuthType(), loginBean.getNickname(), loginBean.getOpenId());
                (new Thread(r)).start();
            }

            @Override
            public void onCancel() {
                System.out.println("startSocialLogin onCancel");
            }

            @Override
            public void onError(String msg) {
                System.out.println("startSocialLogin onError");
                Toast.makeText(LoginActivity.this.getBaseContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 微信登录发送过来的消息
     * {@link WXEntryActivity#onResp(BaseResp)}
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginBean loginBean) {
        // 微信登录
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LoginUtils.onActivityResultForLogin(requestCode, resultCode, data);
    }

    @OnClick({R.id.tv_forget_pwd, R.id.tv_login, R.id.tv_fingerprint_login, R.id.tv_new_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forget_pwd:
                Bundle data = new Bundle();
                data.putString(ServiceFunction.FORGETPASSWORD_TYPE, "2"); //Reset login type level "2"/"3"
                Intent intent = new Intent(this, ForgetPwdActivity.class);
                intent.putExtras(data);
                startActivity(intent);
                break;
            case R.id.tv_login:
                View view1 = getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                String email = etEmail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    ToastUtils.showToast(this, R.string.toast_input_email);
                    return;
                }
                if (!RegexUtils.isEmail(email)) {
                    ToastUtils.showToast(this, R.string.toast_email_error);
                    return;
                }
                String pwd = etPwd.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showToast(this, R.string.toast_input_login_pwd);
                    return;
                }

                LoginProgress.reset();
                tokenLogin = false;
                app.setPasswordToken(null);
                if (loginTimer != null) {
                    loginTimer.cancel();
                }
                loginTimer = new Timer();
                loginTimer.schedule(new Task(), 600 * 1000);
                Runnable r = new moveToLogin(false, etEmail.getText().toString(), etPwd.getText().toString());
                (new Thread(r)).start();

                //login();
                break;
            case R.id.tv_fingerprint_login:
                // 指纹登录
                break;
            case R.id.tv_new_register:
                startActivity(new Intent(this, EmailRegisterActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 邮箱登录
     */
    private void login() {
//        String email = etEmail.getText().toString();
//        if (TextUtils.isEmpty(email)) {
//            ToastUtils.showToast(this, R.string.toast_input_email);
//            return;
//        }
//        if (!RegexUtils.isEmail(email)) {
//            ToastUtils.showToast(this, R.string.toast_email_error);
//            return;
//        }
//        String pwd = etPwd.getText().toString();
//        if (TextUtils.isEmpty(pwd)) {
//            ToastUtils.showToast(this, R.string.toast_input_login_pwd);
//            return;
//        }
        // 登录成功后，判断是否是首次登入，首次需要验证码验证
        showLoginForActivity();
    }


    /**
     * 显示密码输入错误次数提示
     */
    private void showPwdError() {
        if (pwdErrorCount == 5) {
            PwdErrorFiveDialog pwdErrorFiveDialog = new PwdErrorFiveDialog(this);
            pwdErrorFiveDialog.show();
            return;
        }
        pwdErrorCount--;
        if (pwdErrorCount >= 3) {
            PwdErrorDialog pwdErrorDialog = new PwdErrorDialog(this, 5 - pwdErrorCount);
            pwdErrorDialog.show();
        } else {
            ToastUtils.showToast(this, R.string.msg_306);
        }
    }

    /**
     * 是否是首次登录
     * 首次登录则跳转至第二重验证，否则跳转至首页
     */
    private void showLoginForActivity() {
        String email = etEmail.getText().toString();
        boolean loginFirstByEmail = UserSharedPUtils.isLoginFirstByEmail(email);
        if (loginFirstByEmail) {
            // 验证邮箱成功后，设置sp为false
            LoginVerificationActivity.startLoginVerificationActivity(LoginActivity.this, "1693538112@qq.com");
        } else {
            MainActivity.startMainActivity2(this);
            finish();
        }
    }

    public static String fetch(String address) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 10000);
        HttpConnectionParams.setSoTimeout(params, 10000);

        HttpGet request = new HttpGet(address);
        HttpResponse response = client.execute(request);

        //String html = "";
        InputStream in = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder str = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            str.append(line);
        }
        in.close();
        return str.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginUtils.release();
    }

    @Override
    public void updateUI() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}

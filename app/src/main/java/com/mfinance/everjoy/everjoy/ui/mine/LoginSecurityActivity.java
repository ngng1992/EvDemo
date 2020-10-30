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
import android.widget.LinearLayout;
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
public class LoginSecurityActivity extends BaseViewActivity {

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
    @BindView(R.id.v_social_login)
    LinearLayout v_social_login;

    class Task extends TimerTask {
        @Override
        public void run() {
            if (dialog != null && dialog.isShowing()) {
                boolean isFinished = false;

                // Timeout occur
                int RoundRobinIndex = 0;
                int iTrialIndex = 0;


                if (CompanySettings.checkProdServer() == 1) {
                    app.iTrialIndexProd = (app.iTrialIndexProd + 1) % app.alLoginInfoProd.size();
                    iTrialIndex = app.iTrialIndexProd;
                    RoundRobinIndex = app.RoundRobinIndexProd;
                }

                if (CompanySettings.ENABLE_FATCH_SERVER || CompanySettings.FOR_TEST || iTrialIndex == RoundRobinIndex) {
                    isFinished = true;
                    app.isLoading = false;
                    // If IP is fetch from Server or OTX Mode, or RR Trial has finished
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            dialog = null;
                            // 登录失败
                            showPwdError();
                        }
                    });
                }

                Message msg = Message.obtain(null, ServiceFunction.SRV_LOGOUT);
                msg.replyTo = mServiceMessengerHandler;
                try {
                    Bundle data = new Bundle();
                    data.putBoolean(Protocol.Logout.REDIRECT, false);
                    msg.setData(data);
                    mService.send(msg);
                } catch (RemoteException e) {
                    Log.e(TAG, "Unable to send login message", e.fillInStackTrace());
                }

                if (isFinished == false) {
                    // Wait 2 seconds to let the previous connection close
                    try {
                        Thread.sleep(2000);
                        Runnable r = new moveToLogin();
                        (new Thread(r)).start();
                        loginTimer.schedule(new Task(), 60 * 1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else {
                // The login progress is interrupted, change the isLoading to false
                app.isLoading = false;
            }

        }
    }

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

        boolean checkFingerprint = FingerprintUtils.checkFingerprint(this);
        tv_fingerprint_login.setVisibility(checkFingerprint ? View.VISIBLE : View.GONE);

        tvTitle.setText("Lv3");
        v_social_login.setVisibility(View.GONE);

        etEmail.setText("");
        etPwd.setText("");
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
                Toast.makeText(LoginSecurityActivity.this.getBaseContext(), msg, Toast.LENGTH_LONG).show();
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
                data.putString(ServiceFunction.FORGETPASSWORD_TYPE, "3"); //Reset login type level "2"/"3"
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

                String pwd = etPwd.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showToast(this, R.string.toast_input_login_pwd);
                    return;
                }

                Runnable r = new moveToLogin();
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

    public class moveToLogin implements Runnable {
        private int oType;
        private String userName;
        private String openID;

        public moveToLogin() {
            this.oType = -1;
        }

        public moveToLogin(int oType, String userName, String openID) {
            this.oType = oType;
            this.userName = userName;
            this.openID = openID;
        }

        public void run() {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog == null)
                            dialog = ProgressDialog.show(LoginSecurityActivity.this, "", res.getString(R.string.please_wait), true);
                    }
                });

                DataRepository.getInstance().clear();
                if (ToolsUtils.checkNetwork(app)) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String email = etEmail.getText().toString();
                            String pwd = etPwd.getText().toString();
                            login(oType, email, pwd);
                        }
                    });
                    thread.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void login(int oType, String strID, String strPassword) {

        Message loginMsg = Message.obtain(null, ServiceFunction.SRV_LOGIN);
        loginMsg.replyTo = mServiceMessengerHandler;

        loginMsg.getData().putString(ServiceFunction.LOGIN_TYPE, Integer.toString(oType));

        loginMsg.getData().putString(ServiceFunction.LOGIN_LEVEL, "3");

        loginMsg.getData().putString(ServiceFunction.LOGIN_USERNAME, strID);
        loginMsg.getData().putString(ServiceFunction.LOGIN_PASSWORD, strPassword);
        try {
            mService.send(loginMsg);
        } catch (RemoteException e) {
            Log.e("login", "Unable to send login message", e.fillInStackTrace());
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

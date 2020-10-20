package com.mfinance.everjoy.everjoy.ui.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.RegexUtils;
import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.pojo.ConnectionStatus;
import com.mfinance.everjoy.app.service.internal.PriceAgentConnectionProcessor;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.wxapi.WXEntryActivity;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.dialog.PwdErrorDialog;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;
import com.mfinance.everjoy.everjoy.sp.UserSharedPUtils;
import com.mfinance.everjoy.everjoy.ui.home.MainActivity;
import com.mfinance.everjoy.everjoy.utils.ToolsUtils;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 */
public class LoginActivity extends BaseViewActivity {


    public static void loginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private static String USERNAME = "johnnymf";
    private static String PASSWORD = "mF123456";

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
//        LoginUtils.initLogin(this);
        // 登录有空格
        String login = tvLogin.getText().toString();
        char[] chars = login.toCharArray();
        login = chars[0] + "  " + chars[1];
        tvLogin.setText(login);
        setViewPagerView();
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
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String msg) {

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
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_fingerprint_login:
                // 指纹登录
                break;
            case R.id.tv_new_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 邮箱登录
     */
    private void login() {
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
        startLogin();
    }

    /**
     * 显示密码输入错误次数提示
     */
    private void showPwdError() {
//        if (pwdErrorCount == 5) {
            PwdErrorDialog pwdErrorDialog = new PwdErrorDialog(this);
            pwdErrorDialog.show();
//            return;
//        }
//        pwdErrorCount--;
    }

    /**
     * 是否是首次登录
     * 首次登录则跳转至第二重验证，否则跳转至首页
     */
    private void showLoginForActivity(boolean isFirst) {
        String email = etEmail.getText().toString();
        boolean loginFirstByEmail = UserSharedPUtils.isLoginFirstByEmail(email);
        if (loginFirstByEmail) {
            // 验证邮箱成功后，设置sp为false
            LoginVerificationActivity.startLoginVerificationActivity(LoginActivity.this, email);
        } else {
            MainActivity.startMainActivity2(this);
        }
        finish();
    }


    public void startLogin() {
        try {
            DataRepository.getInstance().clear();
            DataRepository.getInstance().setStrUser(USERNAME);
            if (ToolsUtils.checkNetwork(mMobileTraderApplication)) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ConnectionStatus connectionStatus = mMobileTraderApplication.data.getGuestPriceAgentConnectionStatus();
                        switch (connectionStatus) {
                            case CONNECTING:
                            case CONNECTED:
                                Message message = Message.obtain(null, ServiceFunction.SRV_GUEST_PRICE_AGENT);
                                message.arg1 = PriceAgentConnectionProcessor.ActionType.DISCONNECT.getValue();
                                try {
                                    mService.send(message);
                                } catch (Exception ex) {

                                }
                                Message message1 = Message.obtain(null, ServiceFunction.SRV_GUEST_PRICE_AGENT);
                                message1.arg1 = PriceAgentConnectionProcessor.ActionType.RESET.getValue();
                                try {
                                    mService.send(message1);
                                } catch (Exception ex) {

                                }
                                break;
                            default:
                                break;
                        }
//                        String sID = etID.getEditableText().toString();
////                        String sRptgrp = etRptgrp.getEditableText().toString();
//                        String sPass = etPassword.getEditableText().toString();

//                        if (sRptgrp.length() > 0)
//                            CompanySettings.COMPANY_PREFIX = sRptgrp + "_";
//                        SharedPreferences.Editor editor = setting.edit();

                        //editor.putBoolean(FXConstants.SETTING_SAVE_LOGIN_INFO, tbSavePassword.isChecked());

//							    if(tbSavePassword.isChecked()){
//							    	editor.putString(FXConstants.SETTING_USER_ID, etID.getEditableText().toString());
//							    	CommonFunction cf = new CommonFunction();
//						            cf.setKey(Utility.getINIPasswdKey());
//							    	editor.putString(FXConstants.SETTING_USER_PASSWORD, cf.encryptText(sPass));
//							    	editor.putString("report_group", etRptgrp.getEditableText().toString());
//							    }

                        //editor.putBoolean(FXConstants.SETTING_PLATFORM_TYPE, tbPlatformType.isChecked());

//                        editor.commit();

                        if (CompanySettings.ENABLE_FATCH_REPORT_GROUP)
                            try {
                                boolean isDemo = false; //tbPlatformType.isChecked();
//                                if (CompanySettings.newinterface) {
//                                    if (label_demo.getTextColors().equals(res.getColor(R.color.theme_text)))
//                                        isDemo = true;
//                                } else
//                                    isDemo = tbPlatformType.isChecked();

                                if (CompanySettings.ENABLE_FATCH_PLATFORM_ID_FROM_MOBILE_SERVICE)
                                    CompanySettings.PLATFORM_ID_FROM_MOBILE_SERVICE = 1;

                                String URL = mMobileTraderApplication.getReportGroupURL(isDemo)
                                        + "id=" + USERNAME;
                                if (isDemo)
                                    URL += "&isDemo=1";
                                else
                                    URL += "&isDemo=0";
                                String result = fetch(URL);

                                if (BuildConfig.DEBUG)
                                    Log.e("REPORT_GROUP", result);

                                String[] results = result.split("\\|");
                                if (results.length > 1) {
                                    CompanySettings.PLATFORM_ID_FROM_MOBILE_SERVICE = Integer.parseInt(results[0]);
                                    result = results[1];
                                }

                                if (isDemo)
                                    CompanySettings.DEMO_REPORT_GROUP = result;
                                else
                                    CompanySettings.PRODUCTION_REPORT_GROUP = result;
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mMobileTraderApplication, R.string.msg_306,
                                                Toast.LENGTH_SHORT).show();
                                        mMobileTraderApplication.isLoading = false;
                                    }
                                });
                                return;
                            }

                        if (CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX)
                            try {
                                CommonFunction cf = new CommonFunction(false);
                                cf.setKey(Utility.getHttpKey());

                                String rptgrp = CompanySettings.COMPANY_PREFIX.replace("_", "");
                                String platform = "";
                                String result = "";
                                if (CompanySettings.newinterface) {
                                    platform = "production";//tbPlatformType.isChecked()?"demo":"production";
                                    result = fetch(mMobileTraderApplication.getReportGroupURL(false)
                                            + "param=" + cf.encryptText("rptgroup=" + CompanySettings.COMPANY_PREFIX.replace("_", "") + "&ios=false"));
                                } else {
                                    result = fetch(mMobileTraderApplication.getReportGroupURL(false))
                                            + "param=" + cf.encryptText("rptgroup=" + CompanySettings.COMPANY_PREFIX.replace("_", "") + "&ios=false");
                                }
                                result = cf.decryptText(result);
                                //Log.e("FATCH_REQ", "rptgroup=" + CompanySettings.COMPANY_PREFIX.replace("_", "")+ "&ios=false");
                                Log.i("FATCH_URL", mMobileTraderApplication.getReportGroupURL(false)
                                        + "param=" + cf.encryptText("rptgroup=" + CompanySettings.COMPANY_PREFIX.replace("_", "") + "&ios=false"));
                                Log.i("FATCH_REP", result);
                                JSONObject jo = new JSONObject(result);
                                if (platform.equals("demo")) {
                                    mMobileTraderApplication.loginInfoDemo.sURL = jo.getString("ip");
                                    mMobileTraderApplication.loginInfoDemo.sPort = jo.getString("port");
                                    CompanySettings.DEMO_REPORT_GROUP = rptgrp;
                                } else {
                                    mMobileTraderApplication.loginInfoProd.sURL = jo.getString("ip");
                                    mMobileTraderApplication.loginInfoProd.sPort = jo.getString("port");
                                    CompanySettings.PRODUCTION_REPORT_GROUP = rptgrp;
                                }
                                MessageObj.setKey(Integer.parseInt(jo.getString("keyCode")), jo.getString("messageKey"));
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mMobileTraderApplication, R.string.msg_306,
                                                Toast.LENGTH_SHORT).show();
                                        mMobileTraderApplication.isLoading = false;
                                    }
                                });
                                return;
                            }

                        if (CompanySettings.ENABLE_FATCH_SERVER)
                            try {
                                String platform = "";
                                if (CompanySettings.newinterface)
                                    platform = "production";//tbPlatformType.isChecked()?"demo":"production";
                                else
                                    platform = "demo";

                                String result = fetch(mMobileTraderApplication.getServerByIdURL()
                                        + "id=" + USERNAME + "&platform=" + platform);
                                //Log.e("FATCH_SERVER", result);
                                JSONObject jo = new JSONObject(result);
                                if (platform.equals("demo")) {
                                    mMobileTraderApplication.loginInfoDemo.sURL = jo.getString("url");
                                    mMobileTraderApplication.loginInfoDemo.sPort = jo.getString("port");
                                } else {
                                    mMobileTraderApplication.loginInfoProd.sURL = jo.getString("url");
                                    mMobileTraderApplication.loginInfoProd.sPort = jo.getString("port");
                                }
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mMobileTraderApplication, R.string.msg_306,
                                                Toast.LENGTH_SHORT).show();
                                        mMobileTraderApplication.isLoading = false;
                                    }
                                });
                                return;
                            }

                        login(USERNAME, PASSWORD);
                    }
                });
                thread.start();
            } else {
                if (CompanySettings.ENABLE_FATCH_SERVER || CompanySettings.FOR_TEST || CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void login(String strID, String strPassword) {

        Message loginMsg = Message.obtain(null, ServiceFunction.SRV_LOGIN);
        loginMsg.replyTo = mServiceMessengerHandler;

        boolean demo = false;

//        if (CompanySettings.newinterface) {
//            if (label_demo.getCurrentTextColor() == (res.getColor(R.color.theme_text)))
//                demo = true;
//        } else {
//            if (tbPlatformType.isChecked())
//                demo = true;
//        }

        if (demo) {
            loginMsg.getData().putString(ServiceFunction.LOGIN_PLATFORM_TYPE, FXConstants.DEMO);
        } else {
            loginMsg.getData().putString(ServiceFunction.LOGIN_PLATFORM_TYPE, FXConstants.PRODUCT);
        }

        if (CompanySettings.newinterface) {
//            if (IDLogin) {
//                CommonFunction cf = new CommonFunction();
//                cf.setKey(Utility.getINIPasswdKey());
//                strID = setting.getString(FXConstants.SETTING_USER_ID, "");
//                strPassword = cf.decryptText(setting.getString(FXConstants.SETTING_USER_PASSWORD, ""));
//            }
//            if (bSavePassword) {
//                SharedPreferences.Editor editor = setting.edit();
//                editor.putBoolean(FXConstants.SETTING_SAVE_LOGIN_INFO, bSavePassword);
//                editor.putString(FXConstants.SETTING_USER_ID, etID.getEditableText().toString());
//                CommonFunction cf = new CommonFunction();
//                cf.setKey(Utility.getINIPasswdKey());
//                if (!IDLogin && etPassword.getEditableText().toString() != null)
//                    editor.putString(FXConstants.SETTING_USER_PASSWORD, cf.encryptText(etPassword.getEditableText().toString()));
//                editor.putString("report_group", etRptgrp.getEditableText().toString());
//                editor.commit();
//            }
        }
        // williamto 08012020: [Tanrich] old and new layout uses the same flow for save password
        else {
//            if (tbSavePassword.isChecked()) {
//                SharedPreferences.Editor editor = setting.edit();
//                editor.putBoolean(FXConstants.SETTING_SAVE_LOGIN_INFO, tbSavePassword.isChecked());
//                editor.putString(FXConstants.SETTING_USER_ID, etID.getEditableText().toString());
//                CommonFunction cf = new CommonFunction();
//                cf.setKey(Utility.getINIPasswdKey());
//                if (!IDLogin && etPassword.getEditableText().toString() != null)
//                    editor.putString(FXConstants.SETTING_USER_PASSWORD, cf.encryptText(etPassword.getEditableText().toString()));
//                editor.putString("report_group", etRptgrp.getEditableText().toString());
//                editor.commit();
//            }
        }

        loginMsg.getData().putString(ServiceFunction.LOGIN_ID, strID);
        loginMsg.getData().putString(ServiceFunction.LOGIN_PASSWORD, strPassword);
//        if (!CompanySettings.ENABLE_FATCH_SERVER && !CompanySettings.FOR_TEST && !CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX) {
//            if (demo)
//                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, mMobileTraderApplication.iTrialIndexDemo);
//            else if (CompanySettings.checkProdServer() == 1)
//                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, mMobileTraderApplication.iTrialIndexProd);
//            else if (CompanySettings.checkProdServer() == 2)
//                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, mMobileTraderApplication.iTrialIndexProd2);
//            else if (CompanySettings.checkProdServer() == 3)
//                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, mMobileTraderApplication.iTrialIndexProd3);
//            else if (CompanySettings.checkProdServer() == 4)
//                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, mMobileTraderApplication.iTrialIndexProd4);
//            else if (CompanySettings.checkProdServer() == 5)
//                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, mMobileTraderApplication.iTrialIndexProd5);
//            else if (CompanySettings.checkProdServer() == 6)
//                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, mMobileTraderApplication.iTrialIndexProd6);
//            else if (CompanySettings.checkProdServer() == 7)
//                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, mMobileTraderApplication.iTrialIndexProd7);
//            else if (CompanySettings.checkProdServer() == 8)
//                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, mMobileTraderApplication.iTrialIndexProd8);
//            else if (CompanySettings.checkProdServer() == 9)
//                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, mMobileTraderApplication.iTrialIndexProd9);
//            else if (CompanySettings.checkProdServer() == 10)
//                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, mMobileTraderApplication.iTrialIndexProd10);
//        } else
            loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, -1);

        try {
            //Save id, pwd for 2FA
            mMobileTraderApplication.data.setTwoFAMessage(loginMsg);

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
//        LoginUtils.release();
    }
}

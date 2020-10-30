package net.mfinance.commonlib.share.instagram;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;

import net.mfinance.commonlib.share.Utils;
import net.mfinance.commonlib.share.bean.LoginBean;
import net.mfinance.commonlib.share.impl.OnLoginListener;
import net.mfinance.commonlib.share.wechat.Util;

import org.json.JSONException;
import org.json.JSONObject;

import net.mfinance.commonlib.share.instagram.InstagramApp.OAuthAuthenticationListener;

import android.os.Handler;
import android.os.Handler.Callback;

public class InstagramLoginUtils {
    /**
     Login step
     1. AUTHORIZE
     2. SHORT TERM
     3. LONG TERM
     4. ME DETAILS
     */
    private static int loginCount = 0;
    private static InstagramApp mApp;

    @SuppressLint("StaticFieldLeak")
    private static Activity mActivity;
    private static OnLoginListener mOnLoginListener;

    public static void initInstagram(Activity activity) {
        mActivity = activity;
    }

    private static Handler handler = new Handler(new Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
//                userInfoHashmap = mApp.getUserInfo();
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
//                Toast.makeText(MainActivity.this, "Check your network.",
//                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    public static void login(final OnLoginListener onLoginListener) {
        mOnLoginListener = onLoginListener;

        mApp = new InstagramApp(mActivity, Utils.INSTAGRAM_APP_ID,
                Utils.INSTAGRAM_APP_SECRET, Utils.INSTAGRAM_APP_REDIRECT_URL);
        mApp.setListener(new OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                mApp.fetchUserName(handler);
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(mActivity, error, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        if (mApp.hasAccessToken()) {
            // tvSummary.setText("Connected as " + mApp.getUserName());
//            btnConnect.setText("Disconnect");
//            llAfterLoginView.setVisibility(View.VISIBLE);
            mApp.fetchUserName(handler);

        }

        mApp.authorize();
    }

    /**
     * Activity结果回调 onActivityResult
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN) {
            //Tencent.onActivityResultData(requestCode, resultCode, data, iUiListener);
        }
    }

    public static void logout(Context context) {
        //tencent.logout(context);
    }

//    public static boolean isQQInstalled(Context context) {
//        if (tencent != null) {
//            return tencent.isQQInstalled(context);
//        }
//        return false;
//    }


//    private static IUiListener iUiListener = new IUiListener() {
//        @Override
//        public void onComplete(Object o) {
//            paresLoginUserInfo((JSONObject) o);
//        }
//
//        @Override
//        public void onError(UiError uiError) {
//            Log.e("qq", "login onError code = " + uiError.errorCode + "; msg = " + uiError.errorMessage);
//            mOnLoginListener.onError(uiError.errorMessage);
//        }
//
//        @Override
//        public void onCancel() {
//            Log.e("qq", "login cancel ======================");
//            mOnLoginListener.onCancel();
//        }
//    };

    /**
     * 用户登录信息
     */
    private static void paresLoginUserInfo(final JSONObject jsonObject) {
//        if (jsonObject == null || jsonObject.length() == 0) {
//            Log.e("qq", "login onComplete jsonObject == null 登录失败  loginCount = " + loginCount);
//            // 最多登录5次
//            if (loginCount > 5) {
//                mOnLoginListener.onError("");
//            } else {
//                loginCount++;
//                login(mOnLoginListener);
//            }
//            return;
//        }
//
//        Log.e("qq", "login onComplete jsonObject = " + jsonObject.toString());
//        int ret = jsonObject.optInt("ret", 0);
//        String msg = jsonObject.optString("msg");
//        if (ret == 0) {
//            loginCount = 0;
//            // 成功
//            String token = jsonObject.optString("access_token");
//            int expires = jsonObject.optInt("expires_in");
//            final String openid = jsonObject.optString("openid");
//            tencent.setAccessToken(token, String.valueOf(expires));
//            tencent.setOpenId(openid);
//            String access_token = jsonObject.optString("access_token");
//            String url = "https://graph.qq.com/user/get_user_info?" +
//                    "access_token=" + access_token +
//                    "&oauth_consumer_key=" + Utils.QQ_APP_ID +
//                    "&openid=" + openid;
//            OkGo.<String>get(url)
//                    .tag("qq_login")
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onSuccess(Response<String> response) {
//                            String body = response.body();
//                            try {
//                                JSONObject userJson = new JSONObject(body);
//                                int ret = jsonObject.optInt("ret", 0);
//                                String msg = jsonObject.optString("msg");
//                                if (ret == 0) {
//                                    String nickname = userJson.optString("nickname");
//                                    int gender_type = userJson.optInt("gender_type", 1);
//                                    String figureurl_qq = userJson.optString("figureurl_qq");
//                                    String gender = gender_type == 1 ? LoginBean.SEX_MALE : LoginBean.SEX_FEMALE;
//                                    LoginBean loginBean = new LoginBean(Utils.LoginOAuthType.QQ, openid, nickname, gender, figureurl_qq);
//                                    mOnLoginListener.onLogin(loginBean);
//                                } else {
//                                    mOnLoginListener.onError(msg);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                mOnLoginListener.onError(e.getMessage());
//                            }
//                        }
//
//                        @Override
//                        public void onError(Response<String> response) {
//                            super.onError(response);
//                            mOnLoginListener.onError(response.message());
//                        }
//                    });
//        } else {
//            mOnLoginListener.onError(msg);
//        }
    }
}

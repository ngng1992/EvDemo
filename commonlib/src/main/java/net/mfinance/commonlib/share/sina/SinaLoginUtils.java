package net.mfinance.commonlib.share.sina;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import net.mfinance.commonlib.share.Utils;
import net.mfinance.commonlib.share.bean.LoginBean;
import net.mfinance.commonlib.share.impl.OnLoginListener;

import org.json.JSONException;
import org.json.JSONObject;

public class SinaLoginUtils {

    @SuppressLint("StaticFieldLeak")
    private static SsoHandler mSsoHandler;

    public static void initSina(Context context) {
        AuthInfo mAuthInfo = new AuthInfo(context, Utils.SINA_APP_KEY, Utils.SINA_REDIRECT_URL, Utils.SINA_SCOPE);
        WbSdk.install(context, mAuthInfo);
    }

    /**
     * 登录
     *
     * @param onLoginListener 接口是在子线程中执行的，回调处理必须在ui线程
     */
    public static void login(final Activity activity, final OnLoginListener onLoginListener) {
        mSsoHandler = new SsoHandler(activity);
        mSsoHandler.authorizeWeb(new WbAuthListener() {
            @Override
            public void onSuccess(final Oauth2AccessToken oauth2AccessToken) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (oauth2AccessToken.isSessionValid()) {
                            // 保存 Token 到 SharedPreferences
                            AccessTokenKeeper.writeAccessToken(activity, oauth2AccessToken);

                            String token = oauth2AccessToken.getToken();
                            final String uid = oauth2AccessToken.getUid();
                            String url = Utils.SINA_USER_INFO + "?access_token=" + token + "&uid=" + uid;
                            OkGo.<String>get(url)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            String body = response.body();
                                            try {
                                                JSONObject jsonObject = new JSONObject(body);
                                                String avatar_large = jsonObject.optString("avatar_large", "");
                                                String screen_name = jsonObject.optString("screen_name", "");
                                                // 性别，m：男、f：女、n：未知
                                                String gender = jsonObject.optString("gender", "");
                                                if (gender.equals("n")) {
                                                    gender = LoginBean.SEX_MALE;
                                                }
                                                LoginBean loginBean = new LoginBean(Utils.LoginOAuthType.SINA, uid,
                                                        screen_name, gender.toUpperCase(), avatar_large);
                                                onLoginListener.onLogin(loginBean);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                onLoginListener.onError(e.getMessage());
                                            }
                                        }

                                        @Override
                                        public void onError(Response<String> response) {
                                            super.onError(response);
                                            String message = response.message();
                                            message = TextUtils.isEmpty(message) ? "http is error" : message;
                                            onLoginListener.onError(message);
                                        }
                                    });

                        } else {
                            onLoginListener.onError("token is null");
                        }
                    }
                });

            }

            @Override
            public void cancel() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onLoginListener.onCancel();
                    }
                });
            }

            @Override
            public void onFailure(final WbConnectErrorMessage wbConnectErrorMessage) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String errorMessage = wbConnectErrorMessage.getErrorMessage();
                        Log.e("sina", "login onFailure msg = " + errorMessage);
                        onLoginListener.onError(errorMessage);
                    }
                });
            }
        });
    }

    public void onActivityResultForLogin(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}

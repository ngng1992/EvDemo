package net.mfinance.commonlib.share.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import net.mfinance.commonlib.share.Utils;
import net.mfinance.commonlib.share.bean.LoginBean;
import net.mfinance.commonlib.share.impl.OnLoginListener;

import org.json.JSONObject;

import java.util.Collections;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FacebookLoginUtils {


    private static CallbackManager callbackManager;

    public static void initFB() {
        callbackManager = CallbackManager.Factory.create();
    }

    public static void login(Activity activity, final OnLoginListener onLoginListener) {
        // 设置监听
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                requestFBInfo(new FacebookLoginUtils.OnUserInfoCallBack() {
                    @Override
                    public void onUserInfo(String id, String name, String gender, String picUrl) {
                        if (onLoginListener != null) {
                            LoginBean loginBean = new LoginBean(Utils.LoginOAuthType.FACEBOOK, id, name, gender, picUrl);
                            onLoginListener.onLogin(loginBean);
                        }
                    }
                });
            }

            @Override
            public void onCancel() {
                if (onLoginListener != null) {
                    onLoginListener.onCancel();
                }
            }

            @Override
            public void onError(FacebookException error) {
                if (onLoginListener != null) {
                    onLoginListener.onError(error.getMessage());
                }
            }
        });
        // 发起登录, 第二个参数表示登录需要获取哪些facebook权限
        LoginManager.getInstance().logInWithReadPermissions(activity, Collections.singletonList("public_profile"));
    }

    /**
     * 获取不到信息，没有在用户权限里面申请
     */
    public static void requestFBInfo(final OnUserInfoCallBack onUserInfoCallBack) {
        Bundle param = new Bundle();
        param.putString("fields", "id,name,gender,picture");
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // {"id":"280522193350146","name":"Mftest Mftest",
                        // "picture":{"data":
                        // {"height":50,"is_silhouette":true,"url":"https:\/\/scontent-hkt1-1.xx.fbcdn.net\/v\/t1.30497-1\/cp0\/c15.0.50.50a\/p50x50\/84628273_176159830277856_972693363922829312_n.jpg?_nc_cat=1&_nc_sid=12b3be&_nc_ohc=OO0BRlU0LscAX8fz_pQ&_nc_ht=scontent-hkt1-1.xx&oh=c0192095614ff3800bd45337ff749463&oe=5F321E38","width":50}}}
                        String id = object.optString("id");
                        String name = object.optString("name");
                        // 没有参数
//                        String gender = object.optString("gender");
                        String picUrl = "";
                        JSONObject jsonObject = object.optJSONObject("picture");
                        if (jsonObject != null) {
                            JSONObject data = jsonObject.optJSONObject("data");
                            if (data != null) {
                                picUrl = data.optString("url");
                            }
                        }
                        onUserInfoCallBack.onUserInfo(id, name, "", picUrl);
                    }
                });
        graphRequest.setParameters(param);
        graphRequest.executeAsync();
    }

    /**
     * 在Activity里面回调
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static void release() {
        LoginManager.getInstance().unregisterCallback(callbackManager);
    }

    public interface OnUserInfoCallBack {
        void onUserInfo(String id, String name, String gender, String picUrl);
    }
}

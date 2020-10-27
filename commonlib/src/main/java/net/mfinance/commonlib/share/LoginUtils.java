package net.mfinance.commonlib.share;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import net.mfinance.commonlib.share.bean.LoginBean;
import net.mfinance.commonlib.share.facebook.FacebookLoginUtils;
import net.mfinance.commonlib.share.impl.OnLoginListener;
import net.mfinance.commonlib.share.plat.Platform;
import net.mfinance.commonlib.share.qq.QQLoginUtils;
import net.mfinance.commonlib.share.sina.SinaLoginUtils;
import net.mfinance.commonlib.share.wechat.WechatUtils;

public class LoginUtils {

    public static void initLogin(Activity activity) {
        WechatUtils.initWechat(activity);
        QQLoginUtils.initQQ(activity);
        FacebookLoginUtils.initFB();
        //SinaLoginUtils.initSina(activity);
    }

    public static void startLogin(Activity activity, Platform sharePlat, final OnLoginListener onLoginListener) {
        if (sharePlat == Platform.WECAHT) {
            WechatUtils.login();
        } else if (sharePlat == Platform.QQ) {
            QQLoginUtils.login(onLoginListener);
        } else if (sharePlat == Platform.SINA) {
            SinaLoginUtils.login(activity, onLoginListener);
        } else if (sharePlat == Platform.FACEBOOK) {
            FacebookLoginUtils.login(activity, onLoginListener);
        }
    }

    public static void onActivityResultForLogin(int requestCode, int resultCode, Intent data) {
        Log.e("login", "登录 requestCode = " + requestCode + ";resultCode = " + resultCode);
        QQLoginUtils.onActivityResult(requestCode, resultCode, data);
        FacebookLoginUtils.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 释放资源
     */
    public static void release() {
        FacebookLoginUtils.release();
    }
}

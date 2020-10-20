package net.mfinance.commonlib.share;

import android.app.Activity;
import android.content.Intent;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import net.mfinance.commonlib.share.bean.ShareBean;
import net.mfinance.commonlib.share.facebook.FacebookLoginUtils;
import net.mfinance.commonlib.share.facebook.FacebookShareUtils;
import net.mfinance.commonlib.share.impl.OnShareListener;
import net.mfinance.commonlib.share.plat.Platform;
import net.mfinance.commonlib.share.qq.QQShareUtils;
import net.mfinance.commonlib.share.sina.SinaShareUtils;
import net.mfinance.commonlib.share.wechat.WechatUtils;

public class ShareUtils {

    public static void initShare(Activity activity) {
        WechatUtils.initWechat(activity);
        QQShareUtils.initQQ(activity);
        FacebookShareUtils.initFB();
        SinaShareUtils.initSina(activity);
    }

    /**
     * 分享
     * <p>
     * 目前只做了weburl的分享
     * <p>
     * 新浪微博的分享如果有一张图片，则传一张图片bitmap
     */
    public static void startShare(Activity activity, Platform platform, ShareBean shareBean, OnShareListener onShareListener) {
        if (platform == Platform.WECAHT) {
            WechatUtils.shareWeb(activity, SendMessageToWX.Req.WXSceneSession, shareBean);
        } else if (platform == Platform.WECHAT_LINE) {
            WechatUtils.shareWeb(activity, SendMessageToWX.Req.WXSceneTimeline, shareBean);
        } else if (platform == Platform.QQ) {
            QQShareUtils.shareWeb(activity, shareBean, onShareListener);
        } else if (platform == Platform.SINA) {
            SinaShareUtils.shareWeb(activity, shareBean, onShareListener);
        } else if (platform == Platform.FACEBOOK) {
            FacebookShareUtils.shareWeb(activity, shareBean, onShareListener);
        }
    }

    /**
     * 在Activity里面回调
     */
    public static void onActivityResultForShare(int requestCode, int resultCode, Intent data) {
        QQShareUtils.onActivityResult(requestCode, resultCode, data);
        FacebookShareUtils.onActivityResult(requestCode, resultCode, data);
        SinaShareUtils.onActivityResult(data);
    }

    /**
     * 释放资源
     */
    public static void release() {
        FacebookLoginUtils.release();
    }
}

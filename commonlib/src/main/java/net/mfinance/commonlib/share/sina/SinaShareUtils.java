package net.mfinance.commonlib.share.sina;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

import net.mfinance.commonlib.share.bean.ShareBean;
import net.mfinance.commonlib.share.impl.OnShareListener;


/**
 * 新浪分享
 * 参考
 * https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/weibosdkdemo/app/src/main/java/com/sina/weibo/sdk/demo/WBShareActivity.java
 */
public class SinaShareUtils {

    @SuppressLint("StaticFieldLeak")
    private static WbShareHandler mWbShareHandler;
    private static OnShareListener mOnShareListener;

    public static void initSina(Activity activity) {
        mWbShareHandler = new WbShareHandler(activity);
        mWbShareHandler.registerApp();
        // 设置进度条的颜色
//        mWbShareHandler.setProgressColor(0xff33b5e5);
    }

    /**
     * 创建多媒体（网页）消息对象。
     */
    public static void shareWeb(Activity activity, ShareBean shareBean, OnShareListener onShareListener) {
        mOnShareListener = onShareListener;
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = shareBean.getTitle();
        mediaObject.description = shareBean.getDescription();
        Bitmap bitmap;
        if (shareBean.getBitmap() != null) {
            bitmap = shareBean.getBitmap();
        } else {
            bitmap = BitmapFactory.decodeResource(activity.getResources(), shareBean.getLocalThumb());
        }
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = shareBean.getUrl();
        mediaObject.defaultText = "Webpage 默认文案";

        weiboMessage.mediaObject = mediaObject;
        mWbShareHandler.shareMessage(weiboMessage, false);
    }


    public static void onActivityResult(Intent data) {
        mWbShareHandler.doResultIntent(data, new WbShareCallback() {
            @Override
            public void onWbShareSuccess() {
                mOnShareListener.onShare();
            }

            @Override
            public void onWbShareCancel() {
                mOnShareListener.onCancel();
            }

            @Override
            public void onWbShareFail() {
                mOnShareListener.onError("");
            }
        });
    }
}

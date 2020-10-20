package net.mfinance.commonlib.share.wechat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.blankj.utilcode.util.ImageUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import net.mfinance.commonlib.share.Utils;
import net.mfinance.commonlib.share.bean.ShareBean;

import java.util.UUID;

public class WechatUtils {


    private static IWXAPI api;

    /**
     * 在哪个activity调用，必须先初始化
     */
    public static IWXAPI initWechat(Context context) {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(context, Utils.WECHAT_APP_ID, true);
        // 将应用的appId注册到微信
        api.registerApp(Utils.WECHAT_APP_ID);
        return api;
    }

    /**
     * 判断微信是否安装
     *
     * @return true 已安装   false 未安装
     */
    public static boolean isWxAppInstalled() {
        return api.isWXAppInstalled();
    }

    public static void login() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = UUID.randomUUID().toString();
        api.sendReq(req);
    }


    /**
     * @param scene     分享到对话:SendMessageToWX.Req.WXSceneSession
     *                  分享到朋友圈:SendMessageToWX.Req.WXSceneTimeline;
     *                  分享到收藏:SendMessageToWX.Req.WXSceneFavorite
     * @param shareBean 分享的数据源
     */
    public static void shareWeb(Context context, final int scene, final ShareBean shareBean) {
        if (TextUtils.isEmpty(shareBean.getThumbUrl())) {
            //初始化一个WXWebpageObject，填写url
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = shareBean.getUrl();

            //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = shareBean.getTitle();
            String description = getDesc(shareBean.getDescription());
            if (!TextUtils.isEmpty(description)) {
                msg.description = description;
            }
            // 图片不能大于32k
            Bitmap thumbBmp = BitmapFactory.decodeResource(context.getResources(), shareBean.getLocalThumb());
            thumbBmp = getBitmap(thumbBmp);
            msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

            //构造一个Req
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            // 分享到哪个位置，收藏、朋友圈、朋友，SendMessageToWX.Req.WXSceneTimeline;SendMessageToWX.Req.WXSceneFavorite;
            req.scene = scene;

            //调用api接口，发送数据到微信
            api.sendReq(req);
        } else {
            OkGo.<Bitmap>get(shareBean.getThumbUrl())
                    .tag("bitmap")
                    .execute(new BitmapCallback() {
                        @Override
                        public void onSuccess(Response<Bitmap> response) {
                            Bitmap bitmap = response.body();

                            WXWebpageObject webpage = new WXWebpageObject();
                            webpage.webpageUrl = shareBean.getUrl();

                            WXMediaMessage msg = new WXMediaMessage(webpage);
                            msg.title = shareBean.getTitle();
                            String description = getDesc(shareBean.getDescription());
                            if (!TextUtils.isEmpty(description)) {
                                msg.description = description;
                            }
                            Bitmap thumbBmp = getBitmap(bitmap);
                            msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = buildTransaction("webpage");
                            req.message = msg;
                            req.scene = scene;

                            api.sendReq(req);
                        }
                    });
        }
    }

    /**
     * 限制长度不超过 1KB
     * 1K=1024字节=512个汉字
     */
    private static String getDesc(String desc) {
        if (TextUtils.isEmpty(desc)) {
            return "";
        }
        if (desc.length() > 100) {
            return desc.substring(0, 100);
        }
        return desc;
    }

    /**
     * 限制内容大小不超过 32KB
     */
    private static Bitmap getBitmap(Bitmap bitmap) {
        int byteCount = bitmap.getByteCount();
        if (byteCount < 32 * 1024) {
            return bitmap;
        }
        // 压缩大小，压缩后再判断一次
        bitmap = ImageUtils.compressBySampleSize(bitmap, 200, 200);
        if (bitmap.getByteCount() > 32 * 1024) {
            bitmap = ImageUtils.compressBySampleSize(bitmap, 100, 100);
        }
        return bitmap;
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}

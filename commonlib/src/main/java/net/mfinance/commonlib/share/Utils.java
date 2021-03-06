package net.mfinance.commonlib.share;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import net.mfinance.commonlib.share.plat.Platform;
import net.mfinance.commonlib.share.qq.QQLoginUtils;
import net.mfinance.commonlib.share.qq.QQShareUtils;
import net.mfinance.commonlib.share.wechat.WechatUtils;

public class Utils {

    /**
     * QQ
     */
    public static final String QQ_APP_ID = "";
    public static final String QQ_APP_KEY = "";

    /**
     * 微信
     */
    public static final String WECHAT_APP_ID = "wx12cc7dcb43d518c2";
    public static final String WECHAT_APP_KEY = "8e71d9196ee3e76990db99fa998cbad3";


    /**
     * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
     */
    public static final String SINA_APP_KEY = "";

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     */
    public static final String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    /**
     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    public static final String SINA_SCOPE = "";

    /**
     * 新浪授权后通过api获取用户的信息：https://open.weibo.com/wiki/2/users/show
     */
    public static final String SINA_USER_INFO = "https://api.weibo.com/2/users/show.json";


    /**
     * 检测微信、QQ是否安装，facebook可不用检测，web登录
     */
    public static boolean isAppInstalled(Context context, Platform platform) {
        if (platform == Platform.WECAHT) {
            return WechatUtils.isWxAppInstalled();
        } else if (platform == Platform.WECHAT_LINE) {
            return WechatUtils.isWxAppInstalled();
        } else if (platform == Platform.QQ) {
            return QQLoginUtils.isQQInstalled(context) || QQShareUtils.isQQInstalled(context);
        }
        return false;
    }


    /**
     * 获取App存储目录 logo
     */
    public static String getFilePathForAppIcon(Context context) {
        String path = Environment.getExternalStorageDirectory() + "/" + getAppName(context) + "/app_icon.png";
        Log.e("path", "保存icon的路径 path = " + path);
        return path;
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}

package net.mfinance.commonlib.share.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import net.mfinance.commonlib.share.Utils;
import net.mfinance.commonlib.share.bean.ShareBean;
import net.mfinance.commonlib.share.impl.OnShareListener;

import org.json.JSONObject;

public class QQShareUtils {

    private static Tencent tencent;
    private static OnShareListener mOnShareListener;

    public static void initQQ(Activity activity) {
        tencent = Tencent.createInstance(Utils.QQ_APP_ID, activity.getApplicationContext());
    }

    public static void shareWeb(Activity activity, ShareBean shareBean, OnShareListener onShareListener) {
        mOnShareListener = onShareListener;
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        // 最长30，且不能为空
        String title = shareBean.getTitle();
        // 最长40，可没有
        String description = shareBean.getDescription();
        params.putString(QQShare.SHARE_TO_QQ_TITLE, getTitle(title, description));
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareBean.getUrl());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, saveImage(activity, shareBean.getLocalThumb(), shareBean.getThumbUrl()));
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, Utils.getAppName(activity));
        tencent.shareToQQ(activity, params, iUiListener);
    }

    private static String getTitle(String title, String content) {
        if (TextUtils.isEmpty(title)) {
            title = content;
        }
        return title;
    }

    private static String saveImage(Activity activity, int resId, String thumbUrl) {
        String path = "";
        if (TextUtils.isEmpty(thumbUrl)) {
            String filePath = Utils.getFilePathForAppIcon(activity);
            boolean fileExists = FileUtils.isFileExists(filePath);
            if (fileExists) {
                path = filePath;
            }
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), resId);
            boolean save = ImageUtils.save(bitmap, filePath, Bitmap.CompressFormat.PNG);
            if (save) {
                path = filePath;
            }
        } else {
            path = thumbUrl;
        }
        Log.e("path", "分享icon的路径 url = " + path);
        return path;
    }


    /**
     * Activity结果回调 onActivityResult
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, iUiListener);
        }
    }

    public static boolean isQQInstalled(Context context) {
        if (tencent != null) {
            return tencent.isQQInstalled(context);
        }
        return false;
    }


    private static IUiListener iUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            JSONObject jsonObject = (JSONObject) o;
            Log.e("qq", "share json = " + jsonObject.toString());
            mOnShareListener.onShare();
        }

        @Override
        public void onError(UiError uiError) {
            Log.e("qq", "share onError code = " + uiError.errorCode + "; msg = " + uiError.errorMessage);
            mOnShareListener.onError(uiError.errorMessage);
        }

        @Override
        public void onCancel() {
            Log.e("qq", "share cancel ======================");
            mOnShareListener.onCancel();
        }
    };
}

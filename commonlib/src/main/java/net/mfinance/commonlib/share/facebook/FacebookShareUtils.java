package net.mfinance.commonlib.share.facebook;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import net.mfinance.commonlib.share.bean.ShareBean;
import net.mfinance.commonlib.share.impl.OnShareListener;

/**
 * facebook分享
 */
public class FacebookShareUtils {

    private static CallbackManager callbackManager;

    public static void initFB() {
        callbackManager = CallbackManager.Factory.create();
    }

    public static void shareWeb(Activity activity, ShareBean shareBean, final OnShareListener onShareListener) {
        ShareDialog shareDialog = new ShareDialog(activity);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                if (onShareListener != null) {
                    onShareListener.onShare();
                }
            }

            @Override
            public void onCancel() {
                if (onShareListener != null) {
                    onShareListener.onCancel();
                }
            }

            @Override
            public void onError(FacebookException error) {
                if (onShareListener != null) {
                    onShareListener.onError(error.getMessage());
                }
            }
        });
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            String url = shareBean.getUrl();
            String title = shareBean.getTitle();
            int localThumb = shareBean.getLocalThumb();
            String thumbUrl = shareBean.getThumbUrl();
            String description = shareBean.getDescription();

            ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
            builder.setQuote(description);
            // 话题
            builder.setShareHashtag(new ShareHashtag.Builder()
                    .setHashtag(title)
                    .build());
            builder.setContentUrl(Uri.parse(url));
            // 过时的方法不起作用
            builder.setContentTitle(title);
            builder.setContentDescription(description);
            if (!TextUtils.isEmpty(thumbUrl)) {
                builder.setImageUrl(Uri.parse(thumbUrl));
            }
            ShareLinkContent linkContent = builder.build();
            shareDialog.show(linkContent);
        }
    }

    /**
     * 在Activity里面回调
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}

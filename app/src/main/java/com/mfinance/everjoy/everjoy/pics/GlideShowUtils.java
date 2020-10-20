package com.mfinance.everjoy.everjoy.pics;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mfinance.everjoy.R;

/**
 * 图片显示
 */
public class GlideShowUtils {

    /**
     * 显示圆形图片
     */
    public static void showCircleImage(Context context, ImageView imageView, String imgUrl) {
        try {
            RequestOptions requestOptions = RequestOptions
                    .circleCropTransform()
                    .placeholder(R.mipmap.def_userimg)
                    .error(R.mipmap.def_userimg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context).load(imgUrl).apply(requestOptions).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示圆形图片
     */
    public static void showCircleImage(Fragment fragment, ImageView imageView, String imgUrl) {
        try {
            RequestOptions requestOptions = RequestOptions
                    .circleCropTransform()
                    .placeholder(R.mipmap.def_userimg)
                    .error(R.mipmap.def_userimg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(fragment).load(imgUrl).apply(requestOptions).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示图片
     */
    public static void showImage(Context context, ImageView imageView, String imgUrl) {
        try {
            RequestOptions requestOptions = new RequestOptions()
//                .placeholder(R.mipmap.def_userimg)
//                .error(R.mipmap.def_userimg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context).load(imgUrl).apply(requestOptions).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示图片
     */
    public static void showImageAsBitmap(Context context, ImageView imageView, Bitmap bitmap) {
        RequestOptions requestOptions = new RequestOptions()
//                .placeholder(R.mipmap.def_userimg)
//                .error(R.mipmap.def_userimg)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).asBitmap().load(bitmap).apply(requestOptions).into(imageView);
    }
}

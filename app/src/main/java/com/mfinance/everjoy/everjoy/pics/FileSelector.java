package com.mfinance.everjoy.everjoy.pics;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.ImageUtils;
import com.mfinance.everjoy.everjoy.utils.Contents;

import net.mfinance.chatlib.utils.ConfigUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * PictureSelector选择框架：图片/文件/视频/音频
 */
public class FileSelector {

    /**
     * 选择1张图片
     */
    public static void selectImage(Activity activity) {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intentToPickPic, Contents.REQUEST_CODE_SELECT_PHOTO);
    }

    /**
     * 选择相机拍照
     */
    public static String selectCamera(Activity activity){
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + ConfigUtils.NAME + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(new Date());
        File photoFile = new File(fileDir, "idcard_" + timeStamp + ".jpeg");
        String mTempPhotoPath = photoFile.getAbsolutePath();
        Uri imageUri = FileProvider7.getUriForFile(activity, photoFile);
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intentToTakePhoto, Contents.REQUEST_CODE_SELECT_CAMERA);
        return mTempPhotoPath;
    }

    /**
     * 清除缓存
     * <p>
     * 包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
     */
    public static void deleteCacheDirFile(Activity activity) {
        //包括裁剪和压缩后的缓存，要在上传成功后调用，type 指的是图片or视频缓存取决于你设置的ofImage或ofVideo 注意：需要系统sd卡权限
//        PictureFileUtils.deleteCacheDirFile(activity, type);
//        // 清除所有缓存 例如：压缩、裁剪、视频、音频所生成的临时文件
//        PictureFileUtils.deleteAllCacheDirFile(activity);
    }

    /**
     * 选择任意张数图片，最低1张，压缩，无相机
     */
//    public static void selectImage(Activity activity, OnResultCallbackListener<LocalMedia> onResultCallbackListener) {
//        PictureSelector.create(activity)
//                .openGallery(PictureMimeType.ofImage())
//                .imageEngine(GlideEngine.createGlideEngine())
//                .isCompress(true)
//                .isCamera(false)
//                .minSelectNum(1)
//                .forResult(onResultCallbackListener);
//    }

    /**
     * 选择单张，压缩，相机
     */
//    public static void selectImageSingle(Activity activity, OnResultCallbackListener<LocalMedia> onResultCallbackListener) {
//        PictureSelector.create(activity)
//                .openGallery(PictureMimeType.ofImage())
//                .imageEngine(GlideEngine.createGlideEngine())
//                .isCompress(true)
//                .isCamera(true)
//                .minSelectNum(1)
//                .maxSelectNum(1)
//                .forResult(onResultCallbackListener);
//    }

    /**
     * 选择相机拍照
     */
//    public static void selectCamera(Activity activity, OnResultCallbackListener<LocalMedia> onResultCallbackListener) {
//        PictureSelector.create(activity)
//                .openCamera(PictureMimeType.ofImage())
//                .imageEngine(GlideEngine.createGlideEngine())
//                .isCompress(true)
//                .forResult(onResultCallbackListener);
//    }

    /**
     * 选择视频
     */
//    public static void selectVideo(Activity activity, OnResultCallbackListener<LocalMedia> onResultCallbackListener) {
//        PictureSelector.create(activity)
//                .openGallery(PictureMimeType.ofVideo())
//                .imageEngine(GlideEngine.createGlideEngine())
//                .minSelectNum(1)
//                .maxSelectNum(1)
//                // 最大5分钟
//                .videoMaxSecond(300)
//                .videoMinSecond(1)
//                .forResult(onResultCallbackListener);
//    }
}

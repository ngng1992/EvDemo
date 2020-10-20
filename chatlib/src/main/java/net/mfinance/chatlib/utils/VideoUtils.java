package net.mfinance.chatlib.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.hyphenate.util.ImageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 视频工具类
 */
public class VideoUtils {

    /**
     * 视频的缩略图
     */
    private static String videoThumbPath;

    /**
     * 压缩视频的路径
     */
    private static String[] files;

    /**
     * 视频时长 媒体库查询比MediaMetadataRetriever要快
     *
     * @param contentPath 视频路径 content://media/external/file/2032861
     */
    public static int getVideoDuration(Context context, String contentPath) {
        int duration = 0;
        String[] filePathColumn = {MediaStore.Video.Media.DURATION};
        Cursor cursor = context.getContentResolver().query(Uri.parse(contentPath), filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            // 视频时长
            duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            Log.e("video", "媒体库获取视视频时长 duration = " + duration);
            cursor.close();
        }
        return duration;
    }


    /**
     * 获取视频第一帧图片，如果图片过大，无法发送，code=405;error=File too large，自定义宽高
     *
     * @param videoPath 视频路径 /storage/emulated/0/Pictures/Screenshots/SVID_20200715_094549_1.mp4
     * @return path 路径
     */
    public static String getVideoFrame(String videoPath) {
        String thumbPath = "";
        try {
            // 先获取视频缩略图，再利用环信保存
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(videoPath);
            // 获取第一帧
            Bitmap bitmap = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            if (bitmap == null) {
                Log.e("video", "获取本地视频第一帧图片 bitmap == null");
                thumbPath = "";
            }else {
                // /storage/emulated/0/Android/data/com.zhaochegou.car/1144191219107518#cardog/cardog202001091043233/video/thSVID_20200715_094549_1.mp4
                thumbPath = ImageUtils.saveVideoThumb(new File(videoPath), bitmap.getWidth(), bitmap.getHeight(), MediaStore.Images.Thumbnails.MINI_KIND);
                Log.e("video", "获取本地视频第一帧图片 thumbPath =" + thumbPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thumbPath;
    }

    /**
     * 设置bitmap的宽高
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 获取视频时长
     *
     * @return 视频秒数s
     */
    public static int getLocalVideoDuration(String videoPath) {
        int second;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(videoPath);
            // 转成秒
            second = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;
        } catch (Exception e) {
            Log.e("TAG", "MediaMetadataRetriever exception " + e);
            e.printStackTrace();
            return 0;
        } finally {
            mmr.release();
        }
        Log.e("TAG", "视频时长：second = " + second);
        return second;
    }

    /**
     * 判断视频是否大于10M，环信图片和视频最大是10m
     * {https://www.easemob.com/question/4548}
     */
    public static boolean getVideoGreaterThanTen(String videoPath) {
        File file = new File(videoPath);
        long length = file.length();
        return length > 10 * 1024 * 1024;
    }

    /**
     * 创建视频路径
     */
    public static String[] getVideoCompressorPathHasChinese(AppCompatActivity activity, String srcFilePath) {
        files = new String[2];
        String dirPath = ConfigUtils.getFileDir(activity) + "/";
        File fileDir = new File(dirPath);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String copyPath = dirPath + UUID.randomUUID().toString() + ".mp4";
        boolean containChinese = isContainChinese(srcFilePath);
        if (containChinese) {
            nioTransferCopy(new File(srcFilePath), new File(copyPath));
            Log.e("video", "复制后的路径：path = " + copyPath);
            String compressorPath = dirPath + UUID.randomUUID().toString() + ".mp4";
            Log.e("video", "压缩后的路径：compressorPath = " + compressorPath);
            files[0] = copyPath;
            files[1] = compressorPath;
            return files;
        } else {
            files[0] = srcFilePath;
            files[1] = copyPath;
        }
        return files;
    }

    /**
     * 如果路径有中文，则先复制文件，再压缩文件
     *
     * @param source 原文件
     * @param target 目标文件
     */
    private static void nioTransferCopy(File source, File target) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(target);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断是否包含中文，压缩命令不能识别中文
     */
    private static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 删除视频缩略图和压缩文件
     */
    public static void deleteVideoFiles(String srcPath) {
        if (files != null) {
            if (isContainChinese(srcPath)) {
                // 复制的文件地址
                File srcFile = new File(files[0]);
                srcFile.delete();
                // 压缩后的文件地址
                File compressFile = new File(files[1]);
                compressFile.delete();
            } else {
                // 复制的文件地址
                File compressFile = new File(files[1]);
                compressFile.delete();
            }
            files = null;
        }
        // 视频帧文件地址
        if (videoThumbPath != null) {
            File file = new File(videoThumbPath);
            file.delete();
            videoThumbPath = null;
        }
    }
}

package com.mfinance.everjoy.everjoy.config;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.mfinance.everjoy.everjoy.sp.SecuritiesSharedPUtils;

import net.mfinance.chatlib.utils.ConfigUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileConfig {

    /**
     * 默认文件目录名
     */
    public static final String NAME = "everjoy";

    /**
     * 目录
     */
    public static final String FILE_DIR = Environment.getExternalStorageDirectory() + "/" + NAME;

    /**
     * 保存拍摄身份证、证件等的目录
     */
    public static final String FILE_IDCARDS = Environment.getExternalStorageDirectory() + "/" + NAME +"/idcards";

    /**
     * 保存拍摄视频的目录
     */
    public static final String FILE_VIDEOS = Environment.getExternalStorageDirectory() + "/" + NAME +"/videos";

    /**
     * 文件目录
     */
    private static String mFileDirName;

    public static void initFileConfig() {
        File file = new File(FILE_DIR);
        if (!file.exists()) {
            file.mkdir();
        }
        File fileIdcards = new File(FILE_IDCARDS);
        if (!fileIdcards.exists()) {
            fileIdcards.mkdirs();
        }
        File filevideos = new File(FILE_VIDEOS);
        if (!filevideos.exists()) {
            filevideos.mkdirs();
        }
    }

    /**
     * 文件视频路径
     */
    public static String getVideoNamePath(String videoName) {
        String path = FILE_VIDEOS + "/" +  videoName;
        Log.e("video", "videopath = " + path);
        return path;
    }

    public static String getVideoName() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        String persNameEnglish = SecuritiesSharedPUtils.getPersNameEnglish();
        if (!TextUtils.isEmpty(persNameEnglish)) {
            if (persNameEnglish.contains(" ")) {
                persNameEnglish = persNameEnglish.replaceAll(" ", "_");
            }
            persNameEnglish = "video_" + persNameEnglish + "_" + timeStamp + ".mp4";
        }else {
            persNameEnglish = "video_" + timeStamp + ".mp4";
        }
        return persNameEnglish;
    }


    public static String getImageName() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        String persNameEnglish = SecuritiesSharedPUtils.getPersNameEnglish();
        if (!TextUtils.isEmpty(persNameEnglish)) {
            if (persNameEnglish.contains(" ")) {
                persNameEnglish = persNameEnglish.replaceAll(" ", "_");
            }
            persNameEnglish = "img_" + persNameEnglish + "_" + timeStamp + ".jpg";
        }else {
            persNameEnglish = "img_" + timeStamp + ".jpg";
        }
        return persNameEnglish;
    }

    /**
     * 证件路径
     */
    public static String getFileNamePath() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        String path = FILE_IDCARDS + "/" +  "cards_" + timeStamp + ".jpg";
        Log.e("video", "imagepath = " + path);
        return path;
    }
    
}

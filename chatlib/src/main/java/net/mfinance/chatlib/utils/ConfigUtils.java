package net.mfinance.chatlib.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * 配置项
 */
public class ConfigUtils {

    /**
     * 默认文件目录名
     */
    public static final String NAME = "everjoy";

    public static String mFileDirName = null;

    /**
     * 获取文件存储路径
     */
    public static String getFileDir(Context context) {
        // sd卡缓存路径，app卸载文件删除：/storage/emulated/0/Android/data/com.example.mfinance/cache/MFinance/003cae29-3b53-43de-b7a5-63a58fb70d82.amr
        File file = context.getExternalCacheDir();
        if (file == null) {
            Log.e("chat", " file == null");
            // 手机存储路径，app卸载文件删除：/data/user/0/com.example.mfinance/cache/MFinance/e54349e4-98bc-4133-b498-4872217e5f4b.amr
            file = context.getCacheDir();
        }
        String path = file.getPath();
        String fileDirPath = path + "/" + getFileDirName();
        // 没有文件夹目录，则创建文件夹
        File fileDir = new File(fileDirPath);
        if (!file.exists()) {
            fileDir.mkdir();
        }
        return fileDirPath;
    }

    public static String getFileDirName() {
        if (mFileDirName == null || mFileDirName.length() == 0) {
            mFileDirName = NAME;
        }
        return mFileDirName;
    }

    /**
     * 设置文件保存的目录
     *
     * @param fileDirName 目录名称，可设置为项目名称
     */
    public static void setFileDirName(String fileDirName) {
        mFileDirName = fileDirName;
    }
}

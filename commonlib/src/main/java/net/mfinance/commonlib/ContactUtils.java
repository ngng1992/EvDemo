package net.mfinance.commonlib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

public class ContactUtils {

    /**
     * 获取App存储目录
     */
    public static String getAppFileDir(Context context) {
        return Environment.getExternalStorageDirectory() + "/" + getAppName(context);
    }


    /**
     * 获取应用程序名称
     */
    private static String getAppName(Context context) {
        String appName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            appName = context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appName;
    }
}

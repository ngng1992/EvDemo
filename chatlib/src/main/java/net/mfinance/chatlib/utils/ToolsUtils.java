package net.mfinance.chatlib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.RequiresPermission;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

public class ToolsUtils {

    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isConnected(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isConnected();
    }

    @RequiresPermission(ACCESS_NETWORK_STATE)
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return null;
        return cm.getActiveNetworkInfo();
    }
}

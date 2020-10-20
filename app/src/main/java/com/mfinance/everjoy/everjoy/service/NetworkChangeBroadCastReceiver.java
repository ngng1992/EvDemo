package com.mfinance.everjoy.everjoy.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 监听网络状态变化
 */
public class NetworkChangeBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            // 获取联网状态的NetworkInfo对象
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                // 如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    //
//                    SocketUtils2.startWebSocket();
                    Log.e("network", getConnectionType(info.getType()) + "连接，启动socket");
                } else {
//                    SocketUtils2.closeWebSocket();
                    Log.e("network", getConnectionType(info.getType()) + "断开，关闭socket");
                }
            }
        }
    }

    private String getConnectionType(int type) {
        String connType = "no";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "3g";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "wifi";
        }
        return connType;
    }

}

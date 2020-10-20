package com.mfinance.everjoy.everjoy.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.ui.SplashActivity;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

/**
 * 用来清除主服务（前台服务的）的notif
 * 保持id一样
 */
public class StopFxService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("service", "StopFx:onCreate =============");
        startForeground(this);
        // stop self to clear the notification，执行onDestroy销毁服务
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("service", "StopFx:onDestroy =============");
        stopForeground(true);
    }

    public static void startForeground(Service context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent activityIntent = new Intent(context, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 要想使声音生效，id必须每次都不一样
            String channelId = (context.getPackageName() + (int) (Math.random() * 1000));
            String channelName = context.getPackageName() + "_NEW_MESSAGE";
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(true);
            // 不设置声音，默认是系统声音
            channel.setSound(null, null);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
            notification = new NotificationCompat.Builder(context, channelId)
                    // 设置显示的提示文字
                    .setTicker("Service")
                    // 设置小图标
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle(context.getResources().getText(R.string.noti_name))
                    .setContentText(context.getResources().getText(R.string.noti_desc))
                    .setWhen(System.currentTimeMillis())
                    // 滑动消失
//                    .setOngoing(false)
                    // 点击消失
                    .setAutoCancel(true)
                    // 点击跳转
                    .setContentIntent(pendingIntent)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(context)
                    // 设置显示的提示文字
                    .setTicker("Service")
                    // 设置小图标
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle(context.getResources().getText(R.string.noti_name))
                    .setContentText(context.getResources().getText(R.string.noti_desc))
                    .setWhen(System.currentTimeMillis())
                    // 滑动消失
//                    .setOngoing(false)
                    // 点击消失
                    .setAutoCancel(true)
                    // 点击跳转
                    .setContentIntent(pendingIntent)
                    .build();
        }

        context.startForeground(99999, notification);
        // TODO 开启服务，立马调用停止
//        context.stopForeground(true);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

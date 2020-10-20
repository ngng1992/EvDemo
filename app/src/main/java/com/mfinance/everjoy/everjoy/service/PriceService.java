package com.mfinance.everjoy.everjoy.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.model.LoginProgress;
import com.mfinance.everjoy.everjoy.service.event.base.LoginEvent;
import com.mfinance.everjoy.everjoy.service.event.base.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;

/**
 * 报价服务连接
 */
public class PriceService extends Service {

    private static final String TAG = PriceService.class.getSimpleName();

    private MobileTraderApplication application;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = (MobileTraderApplication) getApplication();
        Log.e(TAG, "onCreate =================== ");
        EventBus.getDefault().register(this);


        // TODO 用来清除notif
        StopFxService.startForeground(this);
//        Intent intent = new Intent(this, StopFxService.class);
//        startService(intent);
    }

    @Override
    public boolean stopService(Intent name) {
        Log.e(TAG, "stopService =========");
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy =================== ");
        EventBus.getDefault().unregister(this);
    }


    /**
     * eventbus处理activity或fragment中需要执行的动作
     *
     * @param event 消息实体
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event == null) {
            return;
        }
        int function = event.getFunction();
        LoginEvent loginEvent = event.getLoginFunction();
        Log.e(TAG, "function = " + function + ";loginEvent = " + loginEvent.toString());


//        LoginProgress.reset();


        LoginFunction loginFunction = new LoginFunction();
        loginFunction.login(loginEvent.getUsername(), loginEvent.getPassword(), application);

    }
}

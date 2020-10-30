package com.mfinance.everjoy.everjoy.base;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfinance.everjoy.app.LocaleUtility;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.everjoy.ui.home.MainActivity;
import com.mfinance.everjoy.everjoy.ui.mine.LoginActivity;
import com.mfinance.everjoy.everjoy.ui.mine.LoginVerificationActivity;
import com.mfinance.everjoy.everjoy.ui.mine.ResetPwdActivity;
import com.mfinance.everjoy.everjoy.utils.ToolsUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;
import java.util.function.Consumer;

/**
 * 基类，初始化第三方库等
 */
public class BaseEverjoyActivity extends AppCompatActivity implements ServiceConnection {

    protected static final String TAG = BaseEverjoyActivity.class.getSimpleName();

    protected MobileTraderApplication app;
    protected Resources res;
    protected Consumer<Locale> changeLocale = l -> {
    };

    /**
     * Preferences
     */
    protected SharedPreferences setting;
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (preferences, key) -> {
        if (key.equals("LANGUAGE")) {
            Locale locale = LocaleUtility.getLanguage(preferences);
            changeLocale.accept(locale);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MobileTraderApplication) getApplication();
        res = this.getResources();
        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this);
        }

        setting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        setting.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        // 启动服务
        if (!isServiceRunning()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 5s内调用startForeground
                startForegroundService(new Intent(this, FxMobileTraderService.class));
            } else {
                startService(new Intent(this, FxMobileTraderService.class));
            }
        } else {
            Log.e(TAG, "FxMobileTraderService服务正在运行");
        }


//        // 启动服务
//        if (!isServiceRunning2()) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                // 5s内调用startForeground
//                startForegroundService(new Intent(this, PriceService.class));
//            } else {
//                startService(new Intent(this, PriceService.class));
//            }
//        } else {
//            Log.e(TAG, "FxMobileTraderService服务正在运行");
//        }
    }

    /**
     * 判断FxMobileTraderService服务是否已经启动
     */
    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            String servicePath = getPackageName() + ".service.FxMobileTraderService";
            if (servicePath.equals(service.service.getClassName())
                    && service.service.getPackageName().equals(this.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断FxMobileTraderService服务是否已经启动
     */
    private boolean isServiceRunning2() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            String servicePath = "com.mfinance.everjoy.everjoy.service.PriceService";
            if (servicePath.equals(service.service.getClassName())
                    && service.service.getPackageName().equals(this.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否需要注册eventbus
     *
     * @return 子类实现
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟统计
        MobclickAgent.onResume(this);
        doBindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        doUnbindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * Message handler for service
     */
    protected MessengeHandler handler = new MessengeHandler();

    /**
     * A messenger for sending message to service
     */
    protected Messenger mService = null;

    /**
     * A receiver for receiving message from service
     */
    protected Messenger mServiceMessengerHandler = new Messenger(handler);

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mService = new Messenger(iBinder);
        mBound = true;

        try {
            Bundle data = new Bundle();
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                extras = new Bundle();
            }
            data.putBoolean(ServiceFunction.IS_LOGIN, true);
            data.putBoolean(ServiceFunction.REQUIRE_LOGIN, extras.getBoolean(ServiceFunction.REQUIRE_LOGIN, true));
            Message msg = Message.obtain(handler, ServiceFunction.SRV_REGISTER_ACTIVITY);
            msg.setData(data);
            msg.replyTo = mServiceMessengerHandler;
            mService.send(msg);
        } catch (Exception e) {
            Log.e(TAG, "Bind service fail", e.fillInStackTrace());
        }
        // 超时设置
//        if (app.bLogon) {
//            if (app.isTimeout()) {
//                this.goTo(ServiceFunction.SRV_LOGOUT);
//            }
//
//            app.stopTimeout();
//            long lTimeout = getTimeout();
//            long alTimeout = 0;
//            if (app.data.sessTimeoutDisconn != -1) {
//                lTimeout = app.data.sessTimeoutDisconn * 1000;
//                alTimeout = app.data.sessTimeoutAlert * 1000;
//            }
//            //System.out.println(lTimeout);
//            backgroundthread = false;
//            if (lTimeout > 0)
//                app.startTimeout(lTimeout, this);
//            if (alTimeout > 0)
//                app.startAlertTimeout(alTimeout, this);
//
//        } else {
//            app.stopTimeout();
//        }

//        if (CompanySettings.ENABLE_CONTENT) {
//            if (!isInitialActivity()) {
//                //goTo(ServiceFunction.SRV_START_XML_TRANSFER);
//                MobileTraderApplication.isBackground = false;
//            }
//        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mService = null;
    }


    public class MessengeHandler extends Handler {

        @Override
        /**
         * Handle message method
         * @param msg Message from service
         */
        public void handleMessage(Message msg) {
            Intent intent = null;
            //System.out.println("msg.what:"+msg.what);
            switch (msg.what) {
                case ServiceFunction.ACT_SYSTEM_ALERT_MESSAGE:
//                    showSystemAlertMessage();
                    break;
                case ServiceFunction.ACT_UPDATE_UI:
                    fireUIUpdate();
                    break;
                case ServiceFunction.ACT_VIBRATE:
//                    fireVibrate();
                    break;
                case ServiceFunction.ACT_DISCONNECT:
                    try {
                        boolean isLogin = msg.getData().getBoolean("login", false);
                        if (!isLogin) {
                            intent = new Intent(BaseEverjoyActivity.this, LoginActivity.class);
                            intent.putExtras(msg.getData());
                            intent.putExtra("disconnected", true);
                            startActivity(intent);
                            BaseEverjoyActivity.this.finish();
                        } else {
//                            if (dialog != null) {
//                                dialog.dismiss();
//                                dialog = null;
//                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;
                case ServiceFunction.ACT_GO_TO_LOGIN:
                    //System.out.println("-------------------------------------- finish 1");
                    BaseEverjoyActivity.this.finish();
                    //System.out.println("-------------------------------------- finish 2");
                    intent = new Intent(BaseEverjoyActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    //System.out.println("-------------------------------------- finish 3");
                    break;
                case ServiceFunction.ACT_GO_TO_OTP_LOGIN_PAGE:
                    BaseEverjoyActivity.this.finish();
                    intent = new Intent(BaseEverjoyActivity.this, LoginVerificationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_PRICE: {
//                    boolean guest = msg.getData().getBoolean("guest", false);
//                    intent = new Intent(BaseEverjoyActivity.this, guest ? ContractListGuestActivity.class : ContractListActivity.class);
//                    intent.putExtras(msg.getData());
//                    startActivity(intent);
//                    break;
                }
                case ServiceFunction.ACT_VISIBLE_POP_UP:
//                    if (dialog == null)
//                        dialog = ProgressDialog.show(BaseEverjoyActivity.this, "", res.getString(R.string.please_wait), true);
//                    else
//                        dialog.show();
                    break;
                case ServiceFunction.ACT_INVISIBLE_POP_UP:
                    dialog.dismiss();
                    dialog = null;
                    break;
                case ServiceFunction.ACT_GO_TO_ANDROID_MARKET:
                    ToolsUtils.openAndroidMarket(BaseEverjoyActivity.this);
                    break;
                case ServiceFunction.ACT_SHOW_TOAST:
                    String sMsg = msg.getData().getString(ServiceFunction.MESSAGE);
                    Toast.makeText(BaseEverjoyActivity.this.getBaseContext(), sMsg, Toast.LENGTH_LONG).show();
                    break;
                case ServiceFunction.ACT_SHOW_DIALOG:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String sFinish = msg.getData().getString(ServiceFunction.FINISH, "");
                            String sDialogMsg = msg.getData().getString(ServiceFunction.MESSAGE);
                            String sDialogTitle = msg.getData().getString(ServiceFunction.TITLE);
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(BaseEverjoyActivity.this);
                            builder1.setTitle(sDialogTitle);
                            builder1.setMessage(sDialogMsg);

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            if (sFinish.isEmpty())
                                                dialog.cancel();
                                            else {

                                            }
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    });
                    break;
                case ServiceFunction.ACT_UNBIND_SERVICE:
                    doUnbindService();
                    break;
                case ServiceFunction.ACT_FINISH_ACTIVITY:
                    // TODO 登录成功？
                    finish();
                    break;
                case ServiceFunction.ACT_SHOW_ACC_INFO:
//                    showAccountInformation();
                    break;
                case ServiceFunction.ACT_GO_TO_LOST_PASSWORD:
//                    intent = new Intent(BaseEverjoyActivity.this, LostPasswordActivity.class);
//                    intent.putExtras(msg.getData());
//                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_CHANGE_PASSWORD:
                    intent = new Intent(BaseEverjoyActivity.this, ResetPwdActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.SRV_WEB_VIEW:
//                    intent = new Intent(BaseEverjoyActivity.this, WebViewActivity.class);
//                    intent.putExtras(msg.getData());
//                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_DEFAULT_LOGIN_PAGE:
                    intent = new Intent(BaseEverjoyActivity.this, MainActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;

                case ServiceFunction.ACT_GO_TO_MAIN_PAGE:
                    // TODO 默认进入首页
//                    intent = new Intent(BaseEverjoyActivity.this, MainActivity.class);
                    intent = new Intent(BaseEverjoyActivity.this, LoginActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;

                case ServiceFunction.ACT_DISCONNECT_DUPLICATE:
                    app.setPasswordToken(null);
                    app.setLoginID(null);
                    break;

                case ServiceFunction.ACT_GO_TO_FORGOT_PASSWORD_OTP_PAGE:
                    intent = new Intent(BaseEverjoyActivity.this, LoginVerificationActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;

                default:
                    handleByChild(msg);
                    break;
            }
        }
    }

    /**
     * Let the sub class can handle the message
     *
     * @param msg Message received from service
     */
    public void handleByChild(Message msg) {

    }

    private boolean mBound;

    /**
     * Unbindle from service
     */
    public void doUnbindService() {
        if (mBound) {
            if (mService != null) {
                try {
                    Message msg = Message.obtain(handler, ServiceFunction.SRV_UNREGISTER_ACTIVITY);
                    msg.replyTo = mServiceMessengerHandler;
                    mService.send(msg);
                } catch (Exception e) {
                    Log.e(TAG, "[Unbind service fail]", e.fillInStackTrace());
                }
                //Log.i(SLEEP, this.getClass().getSimpleName() + "@doUnbindService |"+this+">>>>>>>>>>>>>>>>>>>>>>");
                unbindService(this);
                mBound = false;
            }
        }
    }

    /**
     * Bindle to service
     */
    public void doBindService() {
        if (!mBound) {
            //Log.i(SLEEP, this.getClass().getSimpleName() + "@doBindService |"+this+">>>>>>>>>>>>>>>>>>>>>>");
            bindService(new Intent(this, FxMobileTraderService.class), this, Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * Jump to other activity
     *
     * @param iService ref. "ServiceFunction"
     */
    public void goTo(int iService) {
        Message msg = Message.obtain(null, iService);
        msg.replyTo = mServiceMessengerHandler;
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to send login message", e.fillInStackTrace());
        }

    }

    /**
     * Jump to other activity
     *
     * @param iService ref. "ServiceFunction"
     * @param bundle
     */
    public void goTo(int iService, Bundle bundle) {
        Message msg = Message.obtain(null, iService);
        msg.replyTo = mServiceMessengerHandler;
        msg.setData(bundle);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to send login message", e.fillInStackTrace());
        }
    }

    protected Handler uiHandler = new Handler();

    public void fireUIUpdate() {
        Thread t = new Thread() {
            public void run() {
                if (uiHandler != null)
                    uiHandler.post(updateUI);
            }
        };
        t.start();
    }

    Runnable updateUI = new Runnable() {
        @Override
        public void run() {
            try {
                updateBaseUI();
                updateUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void updateUI() {
    }

    protected void updateBaseUI() {
    }

    protected static ProgressDialog dialog;

}

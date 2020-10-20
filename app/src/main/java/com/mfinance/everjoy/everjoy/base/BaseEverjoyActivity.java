package com.mfinance.everjoy.everjoy.base;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
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

import com.mfinance.everjoy.app.AnnouncementActivity;
import com.mfinance.everjoy.app.CancelledOrderListActivity;
import com.mfinance.everjoy.app.CashMovementActivity;
import com.mfinance.everjoy.app.CashMovementHistoryListActivity;
import com.mfinance.everjoy.app.ChangePasswordActivity;
import com.mfinance.everjoy.app.ChartActivity;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.ContactActivity;
import com.mfinance.everjoy.app.ContractDetailActivity;
import com.mfinance.everjoy.app.ContractListActivity;
import com.mfinance.everjoy.app.ContractListGuestActivity;
import com.mfinance.everjoy.app.ContractListSortActivity;
import com.mfinance.everjoy.app.CustomListActivity;
import com.mfinance.everjoy.app.DashboardActivity;
import com.mfinance.everjoy.app.DealActivity;
import com.mfinance.everjoy.app.DemoRegistrationActivity;
import com.mfinance.everjoy.app.EconomicActivity;
import com.mfinance.everjoy.app.EditOrderActivity;
import com.mfinance.everjoy.app.ExecutedOrderListActivity;
import com.mfinance.everjoy.app.HistoryListActivity;
import com.mfinance.everjoy.app.IdentityCheckActivity;
import com.mfinance.everjoy.app.LiquidateAllActivity;
import com.mfinance.everjoy.app.LiquidationActivity;
import com.mfinance.everjoy.app.LiquidationHistoryListActivity;
import com.mfinance.everjoy.app.LocaleUtility;
import com.mfinance.everjoy.app.LoginActivity;
import com.mfinance.everjoy.app.LostPasswordActivity;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.NewPriceAlertActivity;
import com.mfinance.everjoy.app.NewsActivity;
import com.mfinance.everjoy.app.OnLineStatementActivity;
import com.mfinance.everjoy.app.OpenPositionListActivity;
import com.mfinance.everjoy.app.OpenPositionSummaryActivity;
import com.mfinance.everjoy.app.OrderActivity;
import com.mfinance.everjoy.app.PriceAlertActivity;
import com.mfinance.everjoy.app.PriceAlertHistoryActivity;
import com.mfinance.everjoy.app.RunningOrderListActivity;
import com.mfinance.everjoy.app.SettingActivity;
import com.mfinance.everjoy.app.SettingIDActivity;
import com.mfinance.everjoy.app.TermsNConditionActivity;
import com.mfinance.everjoy.app.TransactionListActivity;
import com.mfinance.everjoy.app.TwoFAActivity;
import com.mfinance.everjoy.app.WebViewActivity;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.content.CompanyProfileActivity;
import com.mfinance.everjoy.app.content.ContactUsActivity;
import com.mfinance.everjoy.app.content.EconomicDataDetailActivity;
import com.mfinance.everjoy.app.content.EconomicDataListingActivitiy;
import com.mfinance.everjoy.app.content.HourProductDetailActivity;
import com.mfinance.everjoy.app.content.HourProductListingActivitiy;
import com.mfinance.everjoy.app.content.MasterDetailActivity;
import com.mfinance.everjoy.app.content.MasterListingActivitiy;
import com.mfinance.everjoy.app.content.NewsContentDetailActivity;
import com.mfinance.everjoy.app.content.NewsContentListingActivitiy;
import com.mfinance.everjoy.app.content.NewsDetailActivity;
import com.mfinance.everjoy.app.content.NewsListingActivitiy;
import com.mfinance.everjoy.app.content.StrategyDetailActivity;
import com.mfinance.everjoy.app.content.StrategyListingActivitiy;
import com.mfinance.everjoy.app.content.TermsActivity;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.everjoy.utils.ToolsUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;
import java.util.function.Consumer;

/**
 * 基类，初始化第三方库等
 */
public class BaseEverjoyActivity extends HuanxinChatActivity implements ServiceConnection {

    private static final String TAG = BaseEverjoyActivity.class.getSimpleName();

    protected MobileTraderApplication mMobileTraderApplication;
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
        mMobileTraderApplication = (MobileTraderApplication) getApplication();
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
//        if (mMobileTraderApplication.bLogon) {
//            if (mMobileTraderApplication.isTimeout()) {
//                this.goTo(ServiceFunction.SRV_LOGOUT);
//            }
//
//            mMobileTraderApplication.stopTimeout();
//            long lTimeout = getTimeout();
//            long alTimeout = 0;
//            if (mMobileTraderApplication.data.sessTimeoutDisconn != -1) {
//                lTimeout = mMobileTraderApplication.data.sessTimeoutDisconn * 1000;
//                alTimeout = mMobileTraderApplication.data.sessTimeoutAlert * 1000;
//            }
//            //System.out.println(lTimeout);
//            backgroundthread = false;
//            if (lTimeout > 0)
//                mMobileTraderApplication.startTimeout(lTimeout, this);
//            if (alTimeout > 0)
//                mMobileTraderApplication.startAlertTimeout(alTimeout, this);
//
//        } else {
//            mMobileTraderApplication.stopTimeout();
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
//                    fireUIUpdate();
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
                case ServiceFunction.ACT_GO_TO_DASHBOARD:
                    // TODO 跳转首页
                    intent = new Intent(BaseEverjoyActivity.this, DashboardActivity.class);
                    if (CompanySettings.newinterface)
                        intent = new Intent(BaseEverjoyActivity.this, ContractListActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    finish();
                    break;
                case ServiceFunction.ACT_GO_TO_PRICE: {
                    boolean guest = msg.getData().getBoolean("guest", false);
                    intent = new Intent(BaseEverjoyActivity.this, guest ? ContractListGuestActivity.class : ContractListActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                }
                case ServiceFunction.ACT_GO_TO_OPEN_POSITION:
                    intent = new Intent(BaseEverjoyActivity.this, OpenPositionListActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_CONTRACT_DETAIL: {
                    if (mMobileTraderApplication.getSelectedContract() != null) {
                        intent = new Intent(BaseEverjoyActivity.this, ContractDetailActivity.class);
                        intent.putExtras(msg.getData());
                        startActivity(intent);
                    }
                    break;
                }
                case ServiceFunction.ACT_GO_TO_TRANSACTION:
                    intent = new Intent(BaseEverjoyActivity.this, TransactionListActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_DEAL:
                    if (mMobileTraderApplication.getSelectedContract() != null) {
                        intent = new Intent(BaseEverjoyActivity.this, DealActivity.class);
                        intent.putExtras(msg.getData());
                        startActivity(intent);
                    }
                    break;
                case ServiceFunction.ACT_GO_TO_LIQUIDATE:
                    intent = new Intent(BaseEverjoyActivity.this, LiquidationActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_RUNNING_ORDER:
                    intent = new Intent(BaseEverjoyActivity.this, RunningOrderListActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_EXECUTED_ORDER:
                    intent = new Intent(BaseEverjoyActivity.this, ExecutedOrderListActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_CANCELLED_ORDER:
                    intent = new Intent(BaseEverjoyActivity.this, CancelledOrderListActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_LIQUIDATION_HISTORY:
                    intent = new Intent(BaseEverjoyActivity.this, LiquidationHistoryListActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_ORDER:
                    if (mMobileTraderApplication.getSelectedContract() != null) {
                        intent = new Intent(BaseEverjoyActivity.this, OrderActivity.class);
                        intent.putExtras(msg.getData());
                        startActivity(intent);
                    }
                    break;
                case ServiceFunction.ACT_GO_TO_EDIT_ORDER:
                    if (mMobileTraderApplication.getSelectedRunningOrder() != null) {
                        intent = new Intent(BaseEverjoyActivity.this, EditOrderActivity.class);
                        intent.putExtras(msg.getData());
                        startActivity(intent);
                    }
                    break;
                case ServiceFunction.ACT_GO_TO_CHART:
                    if (mMobileTraderApplication.getSelectedContract() != null) {
                        intent = new Intent(BaseEverjoyActivity.this, ChartActivity.class);
                        intent.putExtras(msg.getData());
                        startActivity(intent);
                    }
                    break;
                case ServiceFunction.ACT_GO_TO_OPEN_POSITION_SUMMARY:
                    intent = new Intent(BaseEverjoyActivity.this, OpenPositionSummaryActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_SETTING: {
                    ContractObj defaultContract = getDefaultContract();
                    if (defaultContract != null) {
                        intent = new Intent(BaseEverjoyActivity.this, SettingActivity.class);
                        intent.putExtras(msg.getData());
                        startActivity(intent);
                    } else if (defaultContract == null && (CompanySettings.ENABLE_CONTENT) && !mMobileTraderApplication.bLogon) {
                        intent = new Intent(BaseEverjoyActivity.this, SettingActivity.class);
                        intent.putExtras(msg.getData());
                        startActivity(intent);
                    }
                    break;
                }
                case ServiceFunction.ACT_GO_TO_SETTING_ID:
                    if (getDefaultContract() != null) {
                        intent = new Intent(BaseEverjoyActivity.this, SettingIDActivity.class);
                        intent.putExtras(msg.getData());
                        startActivity(intent);
                    }
                    break;
                case ServiceFunction.ACT_GO_TO_HISTORY:
                    intent = new Intent(BaseEverjoyActivity.this, HistoryListActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_VISIBLE_POP_UP:
//                    if (dialog == null)
//                        dialog = ProgressDialog.show(BaseEverjoyActivity.this, "", res.getString(R.string.please_wait), true);
//                    else
//                        dialog.show();
                    break;
                case ServiceFunction.ACT_INVISIBLE_POP_UP:
//                    dialog.dismiss();
//                    dialog = null;
                    break;
                case ServiceFunction.ACT_GO_TO_ANDROID_MARKET:
                    ToolsUtils.openAndroidMarket(BaseEverjoyActivity.this);
                    break;
                case ServiceFunction.ACT_GO_TO_ON_LINE_STATEMENT:
                    intent = new Intent(BaseEverjoyActivity.this, OnLineStatementActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_POST_FB:
                    //System.out.println(msg.getData().getString(ServiceFunction.FACEBOOK_MESSAGE));
					/* -- Facebook
					if (isPostFacebookEnable())
						app.postMessageOnWall(msg.getData().getString(ServiceFunction.FACEBOOK_MESSAGE));
					 */

                    break;
                case ServiceFunction.ACT_SHOW_TOAST:
                    String sMsg = msg.getData().getString(ServiceFunction.MESSAGE);
                    Toast.makeText(BaseEverjoyActivity.this.getBaseContext(), sMsg, Toast.LENGTH_LONG).show();
                    break;
                case ServiceFunction.ACT_UNBIND_SERVICE:
                    doUnbindService();
                    break;
                case ServiceFunction.ACT_GO_TO_COMPANY_PROFILE:
                    intent = new Intent(BaseEverjoyActivity.this, CompanyProfileActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_NEWS_DETAIL:
                    intent = new Intent(BaseEverjoyActivity.this, NewsDetailActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_CONTACT_US:
                    intent = new Intent(BaseEverjoyActivity.this, ContactUsActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_STRATEGY_DETAIL:
                    intent = new Intent(BaseEverjoyActivity.this,
                            StrategyDetailActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_ECONOMIC_DATA_DETAIL:
                    intent = new Intent(BaseEverjoyActivity.this,
                            EconomicDataDetailActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_MASTER_DETAIL:
                    intent = new Intent(BaseEverjoyActivity.this,
                            MasterDetailActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_NEWS_LIST:
                    intent = new Intent(BaseEverjoyActivity.this,
                            NewsListingActivitiy.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_DAY_PLAN_LIST:
                    intent = new Intent(BaseEverjoyActivity.this,
                            StrategyListingActivitiy.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_ECONOMIC_DATA_LIST:
                    intent = new Intent(BaseEverjoyActivity.this,
                            EconomicDataListingActivitiy.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_NEWS_CONTENT_LIST:
                    intent = new Intent(BaseEverjoyActivity.this,
                            NewsContentListingActivitiy.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_MASTER_LIST:
                    intent = new Intent(BaseEverjoyActivity.this,
                            MasterListingActivitiy.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_NEWS_CONTENT_DETAIL:
                    intent = new Intent(BaseEverjoyActivity.this,
                            NewsContentDetailActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_FINISH_ACTIVITY:
                    finish();
                    break;
                case ServiceFunction.ACT_SHOW_ACC_INFO:
//                    showAccountInformation();
                    break;
                case ServiceFunction.ACT_GO_TO_HOUR_PRODUCT:
                    intent = new Intent(BaseEverjoyActivity.this, HourProductListingActivitiy.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_HOUR_PRODUCT_DETAIL:
                    intent = new Intent(BaseEverjoyActivity.this, HourProductDetailActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_TERMS:
                    intent = new Intent(BaseEverjoyActivity.this, TermsActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_CASH_MOVEMENT:
                    intent = new Intent(BaseEverjoyActivity.this, CashMovementActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_CASH_MOVEMENT_HISTORY:
                    intent = new Intent(BaseEverjoyActivity.this, CashMovementHistoryListActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.SRV_REPORT_ERROR:
                    Message ReportErrorMsg = Message.obtain(null, ServiceFunction.SRV_REPORT_ERROR);
                    ReportErrorMsg.getData().putString(ServiceFunction.MESSAGE, msg.getData().getString(ServiceFunction.MESSAGE));
                    ReportErrorMsg.replyTo = mServiceMessengerHandler;

                    try {
                        mService.send(ReportErrorMsg);
                    } catch (RemoteException e) {
                        Log.e(CommonFunction.class.getSimpleName(), "Unable to sendBankInfoRequest message", e.fillInStackTrace());
                    }
                    break;
                case ServiceFunction.ACT_GO_TO_DEMO_REGISTRATION:
                    intent = new Intent(BaseEverjoyActivity.this, DemoRegistrationActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_LOST_PASSWORD:
                    intent = new Intent(BaseEverjoyActivity.this, LostPasswordActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_IDENTITY_CHECK:
                    intent = new Intent(BaseEverjoyActivity.this, IdentityCheckActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_CUST_LIST:
                    intent = new Intent(BaseEverjoyActivity.this, CustomListActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_TWO_FA:
                    intent = new Intent(BaseEverjoyActivity.this, TwoFAActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_CHANGE_PASSWORD:
                    intent = new Intent(BaseEverjoyActivity.this, ChangePasswordActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                case ServiceFunction.ACT_GO_TO_CONTRACT_SORT:
                    intent = new Intent(BaseEverjoyActivity.this, ContractListSortActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_ANNOUNCEMENT:
                    intent = new Intent(BaseEverjoyActivity.this, AnnouncementActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_NEWS:
                    intent = new Intent(BaseEverjoyActivity.this, NewsActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_ECONOMIC:
                    intent = new Intent(BaseEverjoyActivity.this, EconomicActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_CONTACTUS:
                    intent = new Intent(BaseEverjoyActivity.this, ContactActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_TERMSNCONDITION:
                    intent = new Intent(BaseEverjoyActivity.this, TermsNConditionActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                    break;
                case ServiceFunction.ACT_GO_TO_LIQUIDATE_ALL:
                    intent = new Intent(BaseEverjoyActivity.this, LiquidateAllActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                case ServiceFunction.SRV_WEB_VIEW:
                    intent = new Intent(BaseEverjoyActivity.this, WebViewActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                case ServiceFunction.ACT_GO_TO_PRICE_ALERT:
                    intent = new Intent(BaseEverjoyActivity.this, PriceAlertActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                case ServiceFunction.ACT_GO_TO_PRICE_ALERT_HISTORY:
                    intent = new Intent(BaseEverjoyActivity.this, PriceAlertHistoryActivity.class);
                    intent.putExtras(msg.getData());
                    startActivity(intent);
                case ServiceFunction.ACT_GO_TO_NEW_PRICE_ALERT:
                    intent = new Intent(BaseEverjoyActivity.this, NewPriceAlertActivity.class);
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


    /**
     * Get default Contract
     *
     * @return Default contract
     */
    public ContractObj getDefaultContract() {
        String sContract = setting.getString("DEFAULT_CONTRACT", CompanySettings.getDefaultDefaultContract(mMobileTraderApplication));
        if (sContract == null)
            return null;
        else {
            if (mMobileTraderApplication.bLogon) {
                // Restrict user set non-tradable contract as default
                ContractObj c = mMobileTraderApplication.data.getContract(sContract);
                if (c == null || c.isViewable() == false || DataRepository.getInstance().getTradableContract().contains(sContract) == false) {
                    for (int i = 0; i < DataRepository.getInstance().getTradableContract().size(); i++) {
                        sContract = DataRepository.getInstance().getTradableContract().get(i);
                        if (mMobileTraderApplication.data.getContract(sContract).isViewable())
                            break;
                    }

                }
            }
            mMobileTraderApplication.setDefaultContract(sContract);
            return mMobileTraderApplication.data.getContract(sContract);
        }
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
}

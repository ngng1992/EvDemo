package com.mfinance.everjoy.app.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.LocaleContextWrapper;
import com.mfinance.everjoy.app.LocaleUtility;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.bo.BalanceRecord;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.bo.SystemMessage;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.bo.TransactionObjBuilder;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.db.TransactionLogHelper;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.service.external.HeartBeatMessageHandler;
import com.mfinance.everjoy.app.service.external.LoginMessageHandler;
import com.mfinance.everjoy.app.service.external.PriceAgentContractUpdateMessageHandler;
import com.mfinance.everjoy.app.service.external.PriceAgentPushNotificationHandler;
import com.mfinance.everjoy.app.service.external.ResetPasswordMessageHandler;
import com.mfinance.everjoy.app.service.external.ServerMessageHandler;
import com.mfinance.everjoy.app.service.external.SystemMessageHandler;
import com.mfinance.everjoy.app.service.internal.ChangePasswordRequestProcessor;
import com.mfinance.everjoy.app.service.internal.DisconnectProcessor;
import com.mfinance.everjoy.app.service.internal.EchoProcessor;
import com.mfinance.everjoy.app.service.internal.FinishActivityProcessor;
import com.mfinance.everjoy.app.service.internal.LoginProcessor;
import com.mfinance.everjoy.app.service.internal.LogoutProcessor;
import com.mfinance.everjoy.app.service.internal.MessageProcessor;
import com.mfinance.everjoy.app.service.internal.MoveToAndroidMarketProcessor;
import com.mfinance.everjoy.app.service.internal.MoveToChangePasswordProcessor;
import com.mfinance.everjoy.app.service.internal.MoveToCompanyProfileActivityHandler;
import com.mfinance.everjoy.app.service.internal.MoveToDefaultLoginPageProcessor;
import com.mfinance.everjoy.app.service.internal.MoveToLoginActivityProcessor;
import com.mfinance.everjoy.app.service.internal.MoveToShowGoToAndroidMarketMessageProcessor;
import com.mfinance.everjoy.app.service.internal.PriceAgentConnectionProcessor;
import com.mfinance.everjoy.app.service.internal.ResetPasswordRequestProcessor;
import com.mfinance.everjoy.app.service.internal.ServiceCloseProcessor;
import com.mfinance.everjoy.app.service.internal.ServiceRegisterProcessor;
import com.mfinance.everjoy.app.service.internal.ServiceUnregisterProcessor;
import com.mfinance.everjoy.app.service.internal.ToastMessageProcessor;
import com.mfinance.everjoy.app.util.MainThreadExecutor;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.everjoy.service.StopFxService;
import com.mfinance.everjoy.hungkee.xml.dao.AbstractDao;
import com.mfinance.everjoy.hungkee.xml.dao.CompanyProfileDao;
import com.mfinance.everjoy.hungkee.xml.dao.ContactUsDao;
import com.mfinance.everjoy.hungkee.xml.dao.EconomicDataDao;
import com.mfinance.everjoy.hungkee.xml.dao.HourProductDao;
import com.mfinance.everjoy.hungkee.xml.dao.MasterDao;
import com.mfinance.everjoy.hungkee.xml.dao.NewsContentDao;
import com.mfinance.everjoy.hungkee.xml.dao.NewsDao;
import com.mfinance.everjoy.hungkee.xml.dao.OtherDao;
import com.mfinance.everjoy.hungkee.xml.dao.RealTimePriceDao;
import com.mfinance.everjoy.hungkee.xml.dao.StrategyDao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import androidx.annotation.RequiresApi;

/*-- Facebook
import com.mfinance.everjoy.MobileTraderApplication.PostRequestListener;
*/

/**
 * A background service for support Mobile trader
 *
 * @author : justin.lai
 * @version : v1.00
 * @ModificationHistory Who            When			Version			What<br>
 * -------------------------------------------------------------------------------<br>
 * justin.lai		yyyymmdd		v1.00			Creation<br>
 * <p>
 * <p>
 * *
 */

public class FxMobileTraderService extends Service implements ConnectionStatusListener {

    /**
     * Tag name for logging
     */
    private final String TAG = "FxMobileTraderService";
    /**
     * A receiver for receiving message from activity
     */
    public ServiceMessageHandler handler = new ServiceMessageHandler();
    /**
     * A messenger for sending message to activity
     */
    public Messenger mMessenger = new Messenger(handler);
    /**
     * Listener for Activity
     */
    protected ArrayList<Messenger> alListener = new ArrayList<Messenger>();
    /**
     * HashMap of handler for Activity
     */
    private HashMap<Integer, MessageProcessor> hmProcessor = new HashMap<Integer, MessageProcessor>();
    /**
     * HashMap of receiver for receiving message from activity
     */
    private HashMap<String, ServerMessageHandler> hmServerMessageHandler = new HashMap<String, ServerMessageHandler>();
    /**
     * Server Connection
     */
    public ServerConnection connection = null;
    /**
     * Application class for data exchange
     */
    public MobileTraderApplication app = null;
    /**
     * Is application quit
     */
    public boolean bQuit = false;
    /**
     * A thread for handling message, which received from server
     */
    protected ResponseHandler responseHandler = null;
    /**
     * Message Queue
     */
    protected LinkedBlockingQueue<MessageObj> lbqMessage = new LinkedBlockingQueue<MessageObj>();
    /**
     * Contract amount
     */
    protected HashMap<String, double[]> hmContractAmount = new HashMap<String, double[]>(20);

    public TransactionLogHelper helper;

    Date LastMarginTime = new Date();
    int MarginAlertState = 0;
    boolean isMarginTimerTimout = true;
    private ScheduledExecutorService scheduleTaskExecutor;

    protected Consumer<Locale> changeLocale = l -> {
    };

    final private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (preferences, key) -> {
        if (key.equals("LANGUAGE")) {
            Locale locale = LocaleUtility.getLanguage(preferences);
            changeLocale.accept(locale);
        }
    };

    public void onLogin() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferenceChangeListener.onSharedPreferenceChanged(preferences, "DEFAULT_CONTRACT_SETTING");
        sharedPreferenceChangeListener.onSharedPreferenceChanged(preferences, "CONTRACT_SEQUENCE_SORT");
    }

    public void onLogout() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferenceChangeListener.onSharedPreferenceChanged(preferences, "DEFAULT_CONTRACT_SETTING");
        sharedPreferenceChangeListener.onSharedPreferenceChanged(preferences, "CONTRACT_SEQUENCE_SORT");
    }
    //private NotificationManager notificationManager = getSystemService(NotificationManager.class);

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale locale = LocaleUtility.getLanguage(PreferenceManager.getDefaultSharedPreferences(newBase));
        Context newBase1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            newBase1 = LocaleUtility.updateResourcesLocale(newBase, locale);
        } else {
            newBase1 = LocaleUtility.updateResourcesLocaleLegacy(newBase, locale);
        }
        LocaleContextWrapper localeContextWrapper = new LocaleContextWrapper(newBase1);
        localeContextWrapper.changeLocale(LocaleUtility.getLanguage(PreferenceManager.getDefaultSharedPreferences(newBase)));
        changeLocale = localeContextWrapper::changeLocale;
        super.attachBaseContext(localeContextWrapper);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        //System.out.println("start Service~~");
        super.onCreate();
        Log.e("service", "Fx:onCreate =============");
        app = (MobileTraderApplication) getApplicationContext();
        scheduleTaskExecutor = Executors.newSingleThreadScheduledExecutor();
        hmProcessor.put(ServiceFunction.SRV_REGISTER_ACTIVITY, new ServiceRegisterProcessor());
        hmProcessor.put(ServiceFunction.SRV_UNREGISTER_ACTIVITY, new ServiceUnregisterProcessor());
        hmProcessor.put(ServiceFunction.SRV_LOGIN, new LoginProcessor());
        hmProcessor.put(ServiceFunction.SRV_LOGOUT, new LogoutProcessor());
        hmProcessor.put(ServiceFunction.SRV_CLOSE_SERVCIE, new ServiceCloseProcessor());
        hmProcessor.put(ServiceFunction.SRV_ANDROID_MARKET, new MoveToAndroidMarketProcessor());
        hmProcessor.put(ServiceFunction.SRV_DISCONNECT, new DisconnectProcessor());
        hmProcessor.put(ServiceFunction.SRV_SHOW_TOAST, new ToastMessageProcessor());
        hmProcessor.put(ServiceFunction.SRV_MOVE_TO_LOGIN, new MoveToLoginActivityProcessor());
        hmProcessor.put(ServiceFunction.SRV_TO_SHOW_ANDROID_MARKET_MSG, new MoveToShowGoToAndroidMarketMessageProcessor());
        hmProcessor.put(ServiceFunction.SRV_MOVE_TO_COMPANY_PROFILE, new MoveToCompanyProfileActivityHandler());
        hmProcessor.put(ServiceFunction.SRV_FINISH_ACTIVITY, new  FinishActivityProcessor());
        hmProcessor.put(ServiceFunction.SRV_CHANGE_PASSWORD, new MoveToChangePasswordProcessor());
        hmProcessor.put(ServiceFunction.SRV_SEND_CHANGE_PASSWORD_REQUEST, new ChangePasswordRequestProcessor());
        hmProcessor.put(ServiceFunction.SRV_WEB_VIEW, new EchoProcessor());
        hmProcessor.put(ServiceFunction.SRV_GUEST_PRICE_AGENT, new PriceAgentConnectionProcessor(new MainThreadExecutor(), true));
        hmProcessor.put(ServiceFunction.SRV_RESET_PASSWORD, new ResetPasswordRequestProcessor());

        hmProcessor.put(ServiceFunction.SRV_DEFAULT_LOGIN_PAGE, new MoveToDefaultLoginPageProcessor());

        hmServerMessageHandler.put(IDDictionary.TRADER_SYSTEM_TYPE + "|" + IDDictionary.TRADER_SHOW_SYSTEM_MSG, new SystemMessageHandler(this));
//		hmServerMessageHandler.put(IDDictionary.TRADER_SYSTEM_TYPE +"|"+IDDictionary.TRADER_DISCONNECT_ACCOUNT, new LogoutMessageHandler(this));
        hmServerMessageHandler.put(IDDictionary.TRADER_LOGIN_SERVICE_TYPE + "|" + IDDictionary.TRADER_REQUEST_LOGIN_RETURN, new LoginMessageHandler(this));
        hmServerMessageHandler.put(IDDictionary.TRADER_ORDER_SERVICE_TYPE + "|" + IDDictionary.TRADER_REQUEST_ORDER_RETURN, hmServerMessageHandler.get(IDDictionary.TRADER_DEAL_SERVICE_TYPE + "|" + IDDictionary.TRADER_REQUEST_DEAL_RETURN));
        hmServerMessageHandler.put(IDDictionary.TRADER_ORDER_SERVICE_TYPE + "|" + IDDictionary.TRADER_RECEIVE_DEALER_ESTABLISH_ORDER, hmServerMessageHandler.get(IDDictionary.TRADER_DEAL_SERVICE_TYPE + "|" + IDDictionary.TRADER_RECEIVE_DEALER_ESTABLISH_DEAL));
        hmServerMessageHandler.put(IDDictionary.TRADER_ORDER_SERVICE_TYPE + "|" + IDDictionary.TRADER_RECEIVE_ORDER_MSG, hmServerMessageHandler.get(IDDictionary.TRADER_DEAL_SERVICE_TYPE + "|" + IDDictionary.TRADER_RECEIVE_DEAL_MSG));

        hmServerMessageHandler.put(IDDictionary.TRADER_LIVE_PRICE_TYPE + "|" + IDDictionary.TRADER_UPDATE_LIVE_PRICE_WITH_PRICE_AGENT_CONNECTION, new PriceAgentContractUpdateMessageHandler(this));

        hmServerMessageHandler.put(IDDictionary.TRADER_PRICE_ALERT_TYPE + "|" + IDDictionary.TRADER_PUSH_PRICE_ALERTS, new PriceAgentPushNotificationHandler(this));
        hmServerMessageHandler.put(IDDictionary.TRADER_IO_SERVICE_TYPE + "|" + IDDictionary.TRADER_REQUEST_CHANGE_PASSWORD_RETURN, new ResetPasswordMessageHandler(this));

        hmServerMessageHandler.put("9|0", new HeartBeatMessageHandler(this));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferenceChangeListener.onSharedPreferenceChanged(preferences, "DEFAULT_CONTRACT_SETTING");
        preferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        helper = new TransactionLogHelper(this);

        try {
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            BroadcastReceiver mReceiver = new ReceiverScreen();
            registerReceiver(mReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO 用来清除notif
        StopFxService.startForeground(this);
//        Intent intent = new Intent(this, StopFxService.class);
//        startService(intent);
    }


    @Override
    public boolean stopService(Intent name) {
        Log.e("Fx", "Fx stopService =========");
        return super.stopService(name);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mMessenger.getBinder();
    }

    /**
     * Activity's Messenger
     *
     * @param client Activity's Messenger
     */
    public void addClient(Messenger client) {
        alListener.add(client);
    }

    /**
     * Is Activity's Messenger exist
     *
     * @param client target Activity's Messenger
     * @return exist or not
     */
    public boolean isClientActivityExist(Messenger client) {
        return alListener.contains(client);
    }

    /**
     * Remove Activity's Messenger
     *
     * @param client Activity's Messenger
     */
    public void removeClient(Messenger client) {
        alListener.remove(client);
    }

    public int getNumberOfClient() {
        return alListener.size();
    }

    /**
     * Kick start ResponseHandler
     */
    public void startResponseHandler() {
        bQuit = false;
        if ((responseHandler == null) || (responseHandler != null && !responseHandler.isAlive())) {
            responseHandler = new ResponseHandler();
            responseHandler.start();
        }
    }

    /**
     * HashMap of receiver for receiving message from activity
     */
    public HashMap<String, ServerMessageHandler> getServerMessageHandler() {
        return hmServerMessageHandler;
    }

    public ScheduledExecutorService getScheduleTaskExecutor() {
        return scheduleTaskExecutor;
    }

    /**
     * Resource bundler
     */
    public Resources getRes() {
        return getResources();
    }

    /**
     * A receiver for receiving message from activity
     *
     * @author : justin.lai
     * @version : v1.00
     * @ModificationHistory Who            When			Version			What<br>
     * -------------------------------------------------------------------------------<br>
     * justin.lai	20110413		v1.00			Creation<br>
     * <p>
     * <p>
     * *
     */
    public class ServiceMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                MessageProcessor mp = hmProcessor.get(msg.what);

                if (mp != null) {
                    mp.processMessage(msg, FxMobileTraderService.this);
                } else {
                    if (BuildConfig.DEBUG)
                        Log.i(TAG, "No process found : " + msg.what);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "No process found : " + msg.what + " mp size : " + hmProcessor.size(), e.fillInStackTrace());
            }
        }
    }

    /**
     * A thread for handling message, which received from server
     *
     * @author : justin.lai
     * @version : v1.00
     * @ModificationHistory Who            When			Version			What<br>
     * -------------------------------------------------------------------------------<br>
     * justin.lai	20110413		v1.00			Creation<br>
     * <p>
     * <p>
     * *
     */
    class ResponseHandler extends Thread implements MessageListener {
        @Override
        public void run() {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "[Start ResponseHandler]");
            while (!bQuit) {
                MessageObj msgObj = null;
                try {
                    msgObj = lbqMessage.poll(500, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ex) {

                }
                if (msgObj != null) {
                    ServerMessageHandler messageHandler = null;
                    try {
                        messageHandler = hmServerMessageHandler.get(msgObj.getKey());
                        if (messageHandler != null) {
                            synchronized (messageHandler) {
                                try {
                                    messageHandler.assignMessageObj(msgObj);
                                    messageHandler.handleMessage(msgObj);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        } else {
                            if (BuildConfig.DEBUG)
                                Log.i(TAG, "No server message handler found : " + msgObj.getKey());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            lbqMessage.clear();
        }

        @Override
        public void notifyMessageReceived() {
            synchronized (this) {
                notify();
            }
        }
    }

    /**
     * Recalculate account balance
     */
    public boolean bReloadStatus = false;

    public void recalAll() {
        BalanceRecord balance = app.data.getBalanceRecord();
        if (balance == null)
            return;

        int iNetType = balance.iNetType;

        ArrayList<ContractObj> alContract = app.data.getContractList();

        if (!bReloadStatus && alContract.size() > 0) {
            bReloadStatus = true;
            ArrayList<TransactionObj> alT = reloadTransactionMessage();
            for (TransactionObj t : alT)
                app.data.addTransaction(t);
        }

        hmContractAmount.clear();

        double dFixedSettlementExRate = app.data.getBalanceRecord().dFixedSettlementExRate;
        String strFloatingCurr;

        if (dFixedSettlementExRate != 1.0)
            strFloatingCurr = "USD";
        else
            strFloatingCurr = app.data.getBalanceRecord().strSettleCurr;

        for (ContractObj contract : alContract) {
            if (!contract.isEmptyPosition()) {
                double dBuyLot = 0.0;
                double dSellLot = 0.0;
                double dNew = 0.0;
                double dHegde = 0.0;
                double dFloating = 0.0;
                double dInitialRatio = 0.0;
                double dCounterRatio = 0.0;
                double dContractSize = 0.0;
                double dInterest = 0.0;
                double dLatestMargin = 0.0;
                double dBid = contract.dBid;
                double dAsk = contract.dAsk;
                Iterator<OpenPositionRecord> itBuy = contract.toBuyIterator();
                double dOutput[] = {0, 1.0, 1.0, 1.0};

                OpenPositionRecord.getExchangeRate(contract.sCurr1, strFloatingCurr, dOutput);


                while (itBuy.hasNext()) {
                    OpenPositionRecord buyPosition = itBuy.next();
                    if (CompanySettings.USE_NEW_PnL_CALCULATION)
                        buyPosition.reCalculateFloating();
                    else
                        buyPosition.reCalculateFloatingOld();
                    dBuyLot += buyPosition.dAmount;
                    dFloating += buyPosition.dFloating;
                    dNew = buyPosition.dNewPosMargin;
                    dHegde = buyPosition.dHedgePosMargin;
                    dInitialRatio = buyPosition.dInitialRatio;
                    dContractSize = buyPosition.contract.iContractSize;
                    dInterest += buyPosition.dInterest;
                    if (CompanySettings.GET_LatestMargin_FROM_SERVER)
                        dLatestMargin += buyPosition.dLatestMargin;
                    else
                        dLatestMargin += buyPosition.dAmount * buyPosition.dDealRate * (buyPosition.dInitialRatio / 100);
                }

                Iterator<OpenPositionRecord> itSell = contract.toSellIterator();

                while (itSell.hasNext()) {
                    OpenPositionRecord sellPosition = itSell.next();
                    if (CompanySettings.USE_NEW_PnL_CALCULATION)
                        sellPosition.reCalculateFloating();
                    else
                        sellPosition.reCalculateFloatingOld();
                    dSellLot += sellPosition.dAmount;
                    dFloating += sellPosition.dFloating;
                    dNew = sellPosition.dNewPosMargin;
                    dHegde = sellPosition.dHedgePosMargin;
                    dInitialRatio = sellPosition.dInitialRatio;
                    dContractSize = sellPosition.contract.iContractSize;
                    dInterest += sellPosition.dInterest;
                    if (CompanySettings.GET_LatestMargin_FROM_SERVER)
                        dLatestMargin += sellPosition.dLatestMargin;
                    else
                        dLatestMargin += sellPosition.dAmount * sellPosition.dDealRate * (sellPosition.dInitialRatio / 100);
                }
                dCounterRatio = contract.dCounterRate;

                //System.out.println("put : " + contract.strContractCode +" | dLatestMargin:" + dLatestMargin);
                if (Math.abs(dOutput[0] - 1) < 0.01) {
                    dOutput[1] = 1 / dOutput[1];
                    dOutput[2] = 1 / dOutput[2];
                    dOutput[3] = 1 / dOutput[3];
                }
                hmContractAmount.put(contract.strContractCode,
                        new double[]{dBuyLot, dSellLot, dNew, dHegde, dFloating,
                                dCounterRatio, dInitialRatio, dContractSize, dInterest,
                                dLatestMargin, dBid, dAsk, contract.getBSD() == true ? 1 : 0,
                                dOutput[1], dOutput[2], dOutput[3]});
            }
        }

        //{dBuyLot, dSellLot, dNew, dHegde, dFloating, dCounterRatio, dInitialRatio, dContractSize, dInterest}
        double dTotalInitMargin = 0.0;
        double dTotalIMMContractValue = 0.0;
        double dTotalFloating = 0.0;
        double dTotalInterest = 0.0;

        synchronized (hmContractAmount) {
            for (double[] dValue : hmContractAmount.values()) {
                double dBuyAmount = dValue[0];
                double dSellAmount = dValue[1];
                double dNewPosMargin = !CompanySettings.USE_DEFAULT_NEW_HEDGE_POSITION_MARGIN ? dValue[2] : app.data.getBalanceRecord().dNewPosMargin;
                double dHedgePosMargin = !CompanySettings.USE_DEFAULT_NEW_HEDGE_POSITION_MARGIN ? dValue[3] : app.data.getBalanceRecord().dHedgePosMargin;
                double dFloating = dValue[4];
                double dCounterRatio = dValue[5];
                double dInitRatio = dValue[6];
                double dContractSize = dValue[7];
                double dInterest = dValue[8];
                double dLatestMargin = dValue[9];
                double dBid = dValue[10];
                double dAsk = dValue[11];
                boolean bBSD = Math.abs(dValue[12]) < 0.01 ? false : true;
                double dExchangeRateMean = dValue[13];
                double dExchangeRateBid = dValue[14];
                double dExchangeRateAsk = dValue[15];

                dTotalInterest += dInterest;
                dTotalFloating += dFloating;

                switch (iNetType) {
                    case 0: //Ratio net
                        //dTotalInitMargin += Math.abs(dBuyAmount + dSellAmount) * dCounterRatio * (dInitRatio / 100);
                        if (dBuyAmount > dSellAmount) {
                            dTotalInitMargin += (dBuyAmount - dSellAmount) * dAsk * (dInitRatio / 100);
                        } else if (dSellAmount > dBuyAmount) {
                            dTotalInitMargin += (dSellAmount - dBuyAmount) * dBid * (dInitRatio / 100);
                        }
                        break;
                    case 1: //Ratio no net
                        //dTotalInitMargin += Math.abs(dBuyAmount - dSellAmount) * dCounterRatio * (dInitRatio / 100);
                        if (CompanySettings.MARGIN_RATIO_NO_NET_CALCULATE_BY_MKT_PRICE)
                            dTotalInitMargin += dBuyAmount * dAsk * (dInitRatio / 100) + dSellAmount * dBid * (dInitRatio / 100);
                        else
                            dTotalInitMargin += dLatestMargin;
                        break;
                    case 2: //Fixed
                    {
                        double dNew = (Math.abs(dBuyAmount - dSellAmount) / dContractSize) * dNewPosMargin;
                        double dHedge = 0;

                        if (dBuyAmount <= dSellAmount)
                            dHedge = (dBuyAmount / dContractSize) * dHedgePosMargin;
                        else if (dSellAmount < dBuyAmount)
                            dHedge = (dSellAmount / dContractSize) * dHedgePosMargin;

                        dTotalInitMargin += dNew + dHedge;
                        break;
                    }
                    case 4:
                    case 5:
                        dTotalInitMargin = balance.dInitialMarginFromServer;
                        break;
                    case 6:
                        if (bBSD == false)    // BSD = 0
                        {
                            if (dBuyAmount > dSellAmount) // BuyAmount > SellAmount
                            {
                                dTotalInitMargin += dBuyAmount * dExchangeRateAsk * dInitRatio / 100 * dFixedSettlementExRate;
                                dTotalIMMContractValue += dBuyAmount * dExchangeRateAsk * dFixedSettlementExRate;
                            } else {
                                dTotalInitMargin += dSellAmount * dExchangeRateAsk * dInitRatio / 100 * dFixedSettlementExRate;
                                dTotalIMMContractValue += dSellAmount * dExchangeRateAsk * dFixedSettlementExRate;
                            }
                        } else {
                            if (dBuyAmount > dSellAmount) // BuyAmount > SellAmount
                            {
                                dTotalInitMargin += dBuyAmount * dExchangeRateBid * dInitRatio / 100 * dFixedSettlementExRate;
                                dTotalIMMContractValue += dBuyAmount * dExchangeRateBid * dFixedSettlementExRate;
                            } else {
                                dTotalInitMargin += dSellAmount * dExchangeRateBid * dInitRatio / 100 * dFixedSettlementExRate;
                                dTotalIMMContractValue += dSellAmount * dExchangeRateBid * dFixedSettlementExRate;
                            }
                        }
                        break;
                    case 7:
                        if (bBSD == false)    // BSD = 0
                        {
                            dTotalInitMargin += dBuyAmount * dExchangeRateAsk * dInitRatio / 100 * dFixedSettlementExRate;
                            dTotalInitMargin += dSellAmount * dExchangeRateAsk * dInitRatio / 100 * dFixedSettlementExRate;
                            dTotalIMMContractValue += dBuyAmount * dExchangeRateAsk * dFixedSettlementExRate;
                            dTotalIMMContractValue += dSellAmount * dExchangeRateAsk * dFixedSettlementExRate;
                        } else {
                            dTotalInitMargin += dBuyAmount * dExchangeRateBid * dInitRatio / 100 * dFixedSettlementExRate;
                            dTotalInitMargin += dSellAmount * dExchangeRateBid * dInitRatio / 100 * dFixedSettlementExRate;
                            dTotalIMMContractValue += dBuyAmount * dExchangeRateBid * dFixedSettlementExRate;
                            dTotalIMMContractValue += dSellAmount * dExchangeRateBid * dFixedSettlementExRate;
                        }
                        break;
                    case 8: //Fixed	* ratio
                    {
                        double dNew = (Math.abs(dBuyAmount - dSellAmount) / dContractSize) * dNewPosMargin;
                        double dHedge = 0;

                        if (dBuyAmount <= dSellAmount)
                            dHedge = (dBuyAmount / dContractSize) * dHedgePosMargin;
                        else if (dSellAmount < dBuyAmount)
                            dHedge = (dSellAmount / dContractSize) * dHedgePosMargin;

                        dTotalInitMargin += (dNew + dHedge) * CompanySettings.MAINTAINENCE_MARGIN_RATIO;
                        break;
                    }
                    case 9: //Fixed no net * ratio
                        double dNew = (Math.abs(dBuyAmount + dSellAmount) / dContractSize) * dNewPosMargin;

                        dTotalInitMargin += dNew * CompanySettings.MAINTAINENCE_MARGIN_RATIO;
                        break;
                }
            }
        }

        balance.dFloating = dTotalFloating;
        balance.dInitialMargin = dTotalInitMargin;
        balance.dInterest = dTotalInterest;
        balance.dIMMContractValue = dTotalIMMContractValue;
        balance.dFreeMargin = balance.dPartialEquity + balance.dFloating + balance.dInterest - balance.dInitialMargin + balance.tradableMargin.doubleValue();


        //Alert Margin and Cut-Loss messages on System Message dialog.
        String sLowerMarginMsg = null;

        Date currentTime = new Date();
        if (currentTime.getTime() - LastMarginTime.getTime() > 60 * 1000) {
            isMarginTimerTimout = true;
            LastMarginTime = new Date();
        }
        if (iNetType == 6 || iNetType == 7) {
            if (balance.dIMMContractValue > 0) {
                if (((balance.getCashValue() / balance.dIMMContractValue) <= balance.dFixedAmountMarginCut)) {
                    balance.bMarginAlertRange = false;
                    balance.bMarginCallRange = false;

                    if (balance.bMarginCutRange == false) {
                        balance.bMarginCutRange = true;
                    }

                    if (isMarginTimerTimout == true || MarginAlertState != 1) {
                        isMarginTimerTimout = false;
                        sLowerMarginMsg = MessageMapping.getMessageByCode(getResources(), String.valueOf("4003"), app.locale);
                        sLowerMarginMsg = sLowerMarginMsg.replace("#s", BigDecimal.valueOf(balance.dFixedAmountMarginCut).multiply(new BigDecimal("100")).toString());
                        app.data.addSystemMessage(new SystemMessage(-1, sLowerMarginMsg));
                    }

                    MarginAlertState = 1;
                } else if (((balance.getCashValue() / balance.dIMMContractValue) <= balance.dFixedAmountMarginCall)) {
                    balance.bMarginAlertRange = false;

                    if (balance.bMarginCallRange == false) {
                        balance.bMarginCallRange = true;
                    }

                    if (isMarginTimerTimout == true || MarginAlertState != 2) {
                        isMarginTimerTimout = false;
                        sLowerMarginMsg = MessageMapping.getMessageByCode(getResources(), String.valueOf("4003"), app.locale);
                        sLowerMarginMsg = sLowerMarginMsg.replace("#s", BigDecimal.valueOf(balance.dFixedAmountMarginCall).multiply(new BigDecimal("100")).toString());
                        app.data.addSystemMessage(new SystemMessage(-1, sLowerMarginMsg));
                    }
                    balance.bMarginCutRange = false;
                    MarginAlertState = 2;
                } else if (((balance.getCashValue() / balance.dIMMContractValue) <= balance.dFixedAmountMarginAlert)) {

                    if (balance.bMarginAlertRange == false) {
                        balance.bMarginAlertRange = true;
                        if (balance.bMarginCallRange == false) {
                            balance.bMarginCallRange = true;
                        }
                    }
                    if (isMarginTimerTimout == true || MarginAlertState != 3) {
                        isMarginTimerTimout = false;
                        sLowerMarginMsg = MessageMapping.getMessageByCode(getResources(), String.valueOf("4003"), app.locale);
                        sLowerMarginMsg = sLowerMarginMsg.replace("#s", BigDecimal.valueOf(balance.dFixedAmountMarginAlert).multiply(new BigDecimal("100")).toString());
                        app.data.addSystemMessage(new SystemMessage(-1, sLowerMarginMsg));
                    }
                    balance.bMarginCallRange = false;
                    balance.bMarginCutRange = false;
                    MarginAlertState = 3;
                } else {
                    MarginAlertState = 0;
                    balance.bMarginCutRange = false;
                    balance.bMarginCallRange = false;
                    balance.bMarginCutRange = false;
                }
            }

        } else {
            if (balance.dInitialMargin > 0) {
                if (((balance.getCashValue() / (balance.dInitialMargin * balance.dLeverage)) <= balance.dFixedAmountMarginCut)) {
                    balance.bMarginAlertRange = false;
                    balance.bMarginCallRange = false;

                    if (balance.bMarginCutRange == false) {
                        balance.bMarginCutRange = true;
                    }

                    if (isMarginTimerTimout == true || MarginAlertState != 1) {
                        isMarginTimerTimout = false;
                        sLowerMarginMsg = MessageMapping.getMessageByCode(getResources(), String.valueOf("4003"), app.locale);
                        sLowerMarginMsg = sLowerMarginMsg.replace("#s", BigDecimal.valueOf(balance.dFixedAmountMarginCut).multiply(new BigDecimal("100")).toString());
                        app.data.addSystemMessage(new SystemMessage(-1, sLowerMarginMsg));
                    }

                    MarginAlertState = 1;
                } else if (((balance.getCashValue() / (balance.dInitialMargin * balance.dLeverage)) <= balance.dFixedAmountMarginCall)) {
                    balance.bMarginAlertRange = false;

                    if (balance.bMarginCallRange == false) {
                        balance.bMarginCallRange = true;
                    }

                    if (isMarginTimerTimout == true || MarginAlertState != 2) {
                        isMarginTimerTimout = false;
                        sLowerMarginMsg = MessageMapping.getMessageByCode(getResources(), String.valueOf("4003"), app.locale);
                        sLowerMarginMsg = sLowerMarginMsg.replace("#s", BigDecimal.valueOf(balance.dFixedAmountMarginCall).multiply(new BigDecimal("100")).toString());
                        app.data.addSystemMessage(new SystemMessage(-1, sLowerMarginMsg));
                    }
                    balance.bMarginCutRange = false;
                    MarginAlertState = 2;
                } else if (((balance.getCashValue() / (balance.dInitialMargin * balance.dLeverage)) <= balance.dFixedAmountMarginAlert)) {

                    if (balance.bMarginAlertRange == false) {
                        balance.bMarginAlertRange = true;
                        if (balance.bMarginCallRange == false) {
                            balance.bMarginCallRange = true;
                        }
                    }
                    if (isMarginTimerTimout == true || MarginAlertState != 3) {
                        isMarginTimerTimout = false;
                        sLowerMarginMsg = MessageMapping.getMessageByCode(getResources(), String.valueOf("4003"), app.locale);
                        sLowerMarginMsg = sLowerMarginMsg.replace("#s", BigDecimal.valueOf(balance.dFixedAmountMarginAlert).multiply(new BigDecimal("100")).toString());
                        app.data.addSystemMessage(new SystemMessage(-1, sLowerMarginMsg));
                    }
                    balance.bMarginCallRange = false;
                    balance.bMarginCutRange = false;
                    MarginAlertState = 3;
                } else {
                    MarginAlertState = 0;
                    balance.bMarginCutRange = false;
                    balance.bMarginCallRange = false;
                    balance.bMarginCutRange = false;
                }
            }
        }
    }

    /**
     * Broadcast message to all activity
     *
     * @param what function code
     * @param data parameter
     */
    ArrayList<Messenger> alClone = new ArrayList<Messenger>();

    public synchronized void broadcast(int what, Bundle data) {
        alClone.addAll((ArrayList<Messenger>) alListener.clone());
        for (Messenger listener : alClone) {
            try {
                Message msg = handler.obtainMessage(what);
                msg.setData(data);
                listener.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Unable to broadcast to " + listener.getClass().getSimpleName(), e.fillInStackTrace());
            }
        }
        alClone.clear();
    }

    /**
     * Start connect to server
     *
     * @param strURL URL
     * @param iPort  port
     */
    public void startConnection(String strURL, int iPort) throws Exception {
        startResponseHandler();
        // Previous connection close, do not need to notify upward!
        if (connection != null) {
            connection.connectionStatusListener = null;
            connection.closeConnection();
        }
        connection = new ServerConnection(strURL, iPort, responseHandler, lbqMessage, this);
    }

    /**
     * Post message to server
     *
     * @param msgObj
     */
    public void postMessage(MessageObj msgObj) {
        lbqMessage.add(msgObj);
        synchronized (responseHandler) {
            responseHandler.notify();
        }
    }

    @Override
    public void disconnected() {
        //System.out.println("disconnected");
        if (app.bLogon) {
            Message msg = Message.obtain(handler, ServiceFunction.SRV_DISCONNECT);
            handler.handleMessage(msg);
        } else if (app.isLoading) {
            app.isLoading = false;
            Message msg = Message.obtain(handler, ServiceFunction.SRV_DISCONNECT);
            handler.handleMessage(msg);
        }
//        else {
//            if (!CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN)
//                broadcast(ServiceFunction.ACT_GO_TO_LOGIN, null);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
		Log.e("service", "Fx:onDestroy =============");

        // TODO 服务销毁不退出
        System.exit(0);
//        stopForeground(true);
    }

    public class ReceiverScreen extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //System.out.println("getCategories"+intent.getCategories());
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                //System.out.println("<!------------Screen ON-------------------->");

            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                //System.out.println("<!------------Screen OFF-------------------->");
                broadcast(ServiceFunction.ACT_UNBIND_SERVICE, null);
            }
        }
    }

	/*-- Facebook
	public void postMessageOnWall(String msg) {
		app.postMessageOnWall(msg);
	}
	*/

    public void addTransaction(TransactionObj t) {
        app.data.addTransaction(t);
        addTransactionToDB(t);
    }

    public TransactionObj removeTransaction(String sID) {
        deleteTransactionInDB(sID);
        return app.data.removeTransaction(sID);
    }

    public void addTransactionToDB(TransactionObj t) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("ID", t.sTransactionID);
            values.put("REF", t.sRef);
            values.put("CONTRACT", t.sContract);
            values.put("ACCOUNT", t.sAccount);
            values.put("BUYSELL", t.sBuySell);
            values.put("REQ_RATE", t.dRequestRate);
            values.put("STATUS", t.iStatus);
            values.put("STATUS_MSG", t.sStatusMsg);
            values.put("MSG_CODE", t.iMsg);
            values.put("MSG", t.sMsg);
            values.put("REMARK_CODE", t.iRemarkCode);
            values.put("TYPE", t.iType);
            values.put("AMOUNT", t.dAmount);
            values.put("CREATE_DATE", Utility.getDateToDB(t.dateCreate));
            values.put("LAST_UPDATE", Utility.getDateToDB(t.dateLastUpdate));
            values.put("TRADE_DATE", Utility.dateToString(app.dtTradeDate));

            values.put("I_STATUS_MSG", t.iStatusMsg);
            values.put("S_MSG_CODE", t.sMsgCode);
            values.put("ORDER_REF", t.sOrderRef);
            values.put("LIQ_REF", t.sLiqRef);
            values.put("L_REF", t.sLRef);
            values.put("S_REF", t.sSRef);
            values.put("REPLY", t.sReply);
            values.put("LIQ_METHOD", t.sLiqmethod);
            values.put("MSG_1", t.sMsg1);

            values.put("IS_DEMO", (app.isDemoPlatform ? 1 : 0));
            db.insertOrThrow(TransactionLogHelper.TABLE_NAME, null, values);
            db.close();
        }
    }

    public ArrayList<TransactionObj> reloadTransactionMessage() {
        SQLiteDatabase db = this.helper.getReadableDatabase();
        ArrayList<TransactionObj> alT = new ArrayList<TransactionObj>();
        Cursor cursor = null;
        try {
            String[] args = {app.data.getStrUser(), Integer.toString((app.isDemoPlatform ? 1 : 0))};
            cursor = db.rawQuery("SELECT * FROM transaction_log WHERE ACCOUNT=? AND IS_DEMO = ? ORDER BY LAST_UPDATE DESC ", args);
            while (cursor.moveToNext()) {
                TransactionObj t = new TransactionObjBuilder()
                        .setsTransactionID(cursor.getString(cursor.getColumnIndex("ID")))
                        .setsRef(cursor.getString(cursor.getColumnIndex("REF")))
                        .setsContract(cursor.getString(cursor.getColumnIndex("CONTRACT")))
                        .setsAccount(cursor.getString(cursor.getColumnIndex("ACCOUNT")))
                        .setsBuySell(cursor.getString(cursor.getColumnIndex("BUYSELL")))
                        .setdRequestRate(cursor.getDouble(cursor.getColumnIndex("REQ_RATE")))
                        .setiStatus(cursor.getInt(cursor.getColumnIndex("STATUS")))
                        .setsStatusMsg(cursor.getString(cursor.getColumnIndex("STATUS_MSG")))
                        .setiMsg(cursor.getInt(cursor.getColumnIndex("MSG_CODE")))
                        .setiRemarkCode(cursor.getInt(cursor.getColumnIndex("REMARK_CODE")))
                        .setiType(cursor.getInt(cursor.getColumnIndex("TYPE")))
                        .setdAmount(cursor.getInt(cursor.getColumnIndex("AMOUNT")))
                        .setContract(app.data.getContract(cursor.getString(cursor.getColumnIndex("CONTRACT"))))
                        .setiStatusMsg(cursor.getInt(cursor.getColumnIndex("I_STATUS_MSG")))
                        .setsMsgCode(cursor.getString(cursor.getColumnIndex("S_MSG_CODE")))
                        .setsOrderRef(cursor.getString(cursor.getColumnIndex("ORDER_REF")))
                        .setsLiqRef(cursor.getString(cursor.getColumnIndex("LIQ_REF")))
                        .setsLRef(cursor.getString(cursor.getColumnIndex("L_REF")))
                        .setsSRef(cursor.getString(cursor.getColumnIndex("S_REF")))
                        .setsReply(cursor.getString(cursor.getColumnIndex("REPLY")))
                        .setsLiqmethod(cursor.getString(cursor.getColumnIndex("LIQ_METHOD")))
                        .setsMsg1(cursor.getString(cursor.getColumnIndex("MSG_1")))
                        .setOverrideMsgCode(cursor.getInt(cursor.getColumnIndex("OVERRIDE_MSG_CODE")) > 0)
                        .setMsgEnglish(cursor.getString(cursor.getColumnIndex("MSG_ENGLISH")))
                        .setMsgTraditionalChinese(cursor.getString(cursor.getColumnIndex("MSG_TRADITIONAL_CHINESE")))
                        .setMsgSimplifiedChinese(cursor.getString(cursor.getColumnIndex("MSG_SIMPLIFIED_CHINESE")))
                        .setsMsg(cursor.getString(cursor.getColumnIndex("MSG")))
                        .setDateCreate(Utility.getDateFromDB(cursor.getString(cursor.getColumnIndex("CREATE_DATE"))))
                        .setDateLastUpdate(Utility.getDateFromDB(cursor.getString(cursor.getColumnIndex("LAST_UPDATE"))))
                        .createTransactionObj();
                //System.out.println("reload:");
                //System.out.println("cursor.getString(cursor.getColumnIndex('S_MSG_CODE')):" +
                //		cursor.getString(cursor.getColumnIndex("S_MSG_CODE")));
                alT.add(t);
            }
        } catch (Exception e) {
        } finally {
            try {
                if (cursor != null && !cursor.isClosed()) cursor.close();
                if (db != null && db.isOpen()) db.close();
            } catch (Exception e) {
            }
        }
        return alT;
    }

    public void deleteTransactionInDB(String sID) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        if (db.isOpen()) {
            db.execSQL("DELETE FROM transaction_log where ID=?", new Object[]{sID});
            db.close();
        }
    }

    public void deleteExpireTransactionInDB(Date date) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        if (db.isOpen()) {
            db.execSQL("DELETE FROM transaction_log WHERE NOT TRADE_DATE = ?", new Object[]{Utility.dateToString(date)});
            db.close();
        }
    }

    public void updateTransactionToDB(TransactionObj t) {
        SQLiteDatabase db = this.helper.getWritableDatabase();
        try {
            if (db.isOpen()) {
                //System.out.println("update- t.sMsgCode :" + t.sMsgCode );
                //System.out.println("update- t.sTransactionID :" + t.sTransactionID );
                db.execSQL(
                        "UPDATE transaction_log SET " +
                                "REF=?,CONTRACT=?,ACCOUNT=?,BUYSELL=?,REQ_RATE=?,STATUS=?,STATUS_MSG=?,MSG_CODE=?,MSG=?," +
                                "REMARK_CODE=?,TYPE=?,AMOUNT=?,CREATE_DATE=?,LAST_UPDATE=?,TRADE_DATE=?," +
                                "S_MSG_CODE=?,ORDER_REF=?,LIQ_REF=?,L_REF=?,S_REF=?,REPLY=?,LIQ_METHOD=?,MSG_1=?, I_STATUS_MSG=?,IS_DEMO=?," +
                                "OVERRIDE_MSG_CODE=?,MSG_ENGLISH=?,MSG_TRADITIONAL_CHINESE=?,MSG_SIMPLIFIED_CHINESE=?" +
                                " WHERE ID=?",
                        new Object[]{t.sRef, t.sContract, t.sAccount, t.sBuySell, t.dRequestRate,
                                t.iStatus, t.sStatusMsg, t.iMsg, t.sMsg, t.iRemarkCode, t.iType, t.dAmount,
                                Utility.getDateToDB(t.dateCreate), Utility.getDateToDB(t.dateLastUpdate), Utility.dateToString(app.dtTradeDate),
                                t.sMsgCode, t.sOrderRef, t.sLiqRef, t.sLRef, t.sSRef, t.sReply, t.sLiqmethod, t.sMsg1, t.iStatusMsg, (app.isDemoPlatform ? 1 : 0),
                                t.overrideMsgCode ? 1 : 0, t.msgEnglish, t.msgTraditionalChinese, t.msgSimplifiedChinese,
                                t.sTransactionID}
                );
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateProcessingMsgToTransactionFail() {

        SQLiteDatabase db = this.helper.getWritableDatabase();
        try {
            if (db.isOpen()) {
                //System.out.println("update- t.sMsgCode :" + t.sMsgCode );
                //System.out.println("update- t.sTransactionID :" + t.sTransactionID );
                db.execSQL(
                        "UPDATE transaction_log SET " +
                                "S_MSG_CODE=?, I_STATUS_MSG=? " +
                                "WHERE IS_DEMO=? AND S_MSG_CODE = ?",
                        new Object[]{
                                "4002", 4002, (app.isDemoPlatform ? 1 : 0), "917"
                        }
                );
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startXMLTransfer() {
        initDAO();
        bindThrowaWaysXML();
        restartRefreshGeneralXMLTimer();
    }

    public void refreshXMLTransfer() {
        if (xmlTimer != null) {
            xmlTimer.cancel();
            xmlTimer.purge();
            xmlTimer = null;
        }

        XMLTimerTask xmlTask = new XMLTimerTask(false);
//        for (ArrayList<DashboardItem> dal : DashboardItemRespository.alItem) {
//            for (DashboardItem item : dal) {
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_NEWS_CONTENT_LIST)
//                    xmlTask.addDAO(newsContentDAO);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_COMPANY_PROFILE)
//                    xmlTask.addDAO(companyProfileDAO);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_ECONOMIC_DATA_LIST)
//                    xmlTask.addDAO(ecoDataDAO);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_STRATEGY_LIST)
//                    xmlTask.addDAO(strategyAO);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_MASTER_LIST)
//                    xmlTask.addDAO(masterDAO);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_CONTACT_US)
//                    xmlTask.addDAO(contactUsDao);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_TERMS)
//                    xmlTask.addDAO(otherDAO);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_NEWS_LIST)
//                    xmlTask.addDAO(newsDAO);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_HOUR_PRODUCT)
//                    xmlTask.addDAO(hourProductDao);
//
//            }
//        }

        xmlTimer = new Timer();
        xmlTimer.schedule(xmlTask, 60 * 1000, app.getDefaultRefreshGeneralInfo() * 60 * 1000);
    }

    private void initDAO() {
        priceDAO = new RealTimePriceDao(app);
        companyProfileDAO = new CompanyProfileDao(app);
        strategyAO = new StrategyDao(app);
        masterDAO = new MasterDao(app);
        newsContentDAO = new NewsContentDao(app);
        newsDAO = new NewsDao(app);
        ecoDataDAO = new EconomicDataDao(app);
        contactUsDao = new ContactUsDao(app);
        otherDAO = new OtherDao(app);
        hourProductDao = new HourProductDao(app);
    }

    private void bindThrowaWaysXML() {

    }

    /**
     * Timer : For update the general XML from the content server;
     */
    Timer xmlTimer;
    Timer priceTimer;

    RealTimePriceDao priceDAO = null;
    StrategyDao strategyAO = null;
    MasterDao masterDAO = null;
    NewsContentDao newsContentDAO = null;
    NewsDao newsDAO = null;
    EconomicDataDao ecoDataDAO = null;
    CompanyProfileDao companyProfileDAO = null;
    ContactUsDao contactUsDao = null;
    OtherDao otherDAO = null;
    HourProductDao hourProductDao = null;

    public void restartRefreshGeneralXMLTimer() {
        if (CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN) {
            if (app.bLogon) {
                removePriceLoadingFromXML();
            } else {
                addPriceLoadingFromXML();
            }
        }

        if (CompanySettings.ENABLE_CONTENT && !app.bLogon) {
            addPriceLoadingFromXML();
        } else if (CompanySettings.ENABLE_CONTENT && app.bLogon) {
            removePriceLoadingFromXML();
        }

        if (xmlTimer != null) {
            xmlTimer.cancel();
            xmlTimer.purge();
            xmlTimer = null;
        }

        XMLTimerTask xmlTask = new XMLTimerTask(false);
//        for (ArrayList<DashboardItem> dal : DashboardItemRespository.alItem) {
//            for (DashboardItem item : dal) {
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_NEWS_CONTENT_LIST)
//                    xmlTask.addDAO(newsContentDAO);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_COMPANY_PROFILE)
//                    xmlTask.addDAO(companyProfileDAO);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_ECONOMIC_DATA_LIST)
//                    xmlTask.addDAO(ecoDataDAO);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_STRATEGY_LIST)
//                    xmlTask.addDAO(strategyAO);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_MASTER_LIST && CompanySettings.ENABLE_WEBVIEW_MASTER == false)
//                    xmlTask.addDAO(masterDAO);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_CONTACT_US && CompanySettings.ENABLE_WEBVIEW_CONTACT_US == false)
//                    xmlTask.addDAO(contactUsDao);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_TERMS)
//                    xmlTask.addDAO(otherDAO);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_NEWS_LIST)
//                    xmlTask.addDAO(newsDAO);
//                if (item.iService == ServiceFunction.SRV_MOVE_TO_HOUR_PRODUCT)
//                    xmlTask.addDAO(hourProductDao);
//            }
//        }


        if (CompanySettings.ENABLE_CONTENT && CompanySettings.newinterface) {
            xmlTask.addDAO(contactUsDao);
            //xmlTask.addDAO(newsDAO);
            //xmlTask.addDAO(ecoDataDAO);
            //xmlTask.addDAO(otherDAO);
            //xmlTask.addDAO(newsContentDAO);
        }

        xmlTimer = new Timer();
        xmlTimer.schedule(xmlTask, 0, app.getDefaultRefreshGeneralInfo() * 60 * 1000);
    }

    public void addPriceLoadingFromXML() {
        app.bPriceReloadInXML = true;

        priceTimer = new Timer();
        priceTimer.schedule((new XMLTimerTask(true)).addDAO(priceDAO), 0, 1000);
    }

    public void removePriceLoadingFromXML() {
        if (priceTimer != null) {
            priceTimer.cancel();
            priceTimer.purge();
            priceTimer = null;
        }
    }

    public void reloadTransactionHistory() {
        DataRepository data = app.data;
        ArrayList<TransactionObj> alT = reloadTransactionMessage();
        for (TransactionObj t : alT)
            data.addTransaction(t);
    }

    private Set<Runnable> clearServiceListener = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private ScheduledFuture<?> scheduledClearServices;

    public boolean addClearServiceListener(Runnable runnable) {
        return clearServiceListener.add(runnable);
    }

    public boolean removeClearServiceListener(Runnable runnable) {
        return clearServiceListener.remove(runnable);
    }

    public void stopClearServiceTimeout() {
        if (scheduledClearServices != null) {
            scheduledClearServices.cancel(false);
        }
    }

    public void startClearServiceTimeout() {
        stopClearServiceTimeout();
        scheduledClearServices = scheduleTaskExecutor.schedule(() -> clearServiceListener.forEach(r -> r.run()), 3, TimeUnit.MINUTES);
    }

    class XMLTimerTask extends TimerTask {
        protected ArrayList<AbstractDao<?>> alDao = new ArrayList<AbstractDao<?>>();
        boolean bUpdateUI = false;

        public XMLTimerTask(boolean bUpdateUI) {
            this.bUpdateUI = bUpdateUI;
        }

        public XMLTimerTask addDAO(AbstractDao<?> dao) {
            synchronized (alDao) {
                alDao.add(dao);
            }
            return this;
        }

        public void removeDAO(AbstractDao<?> dao) {
            synchronized (alDao) {
                this.alDao.remove(dao);
            }
        }

        @Override
        public void run() {
            if (app != null && !MobileTraderApplication.isBackground) {
                synchronized (alDao) {

                    for (AbstractDao<?> dao : alDao) {
                        dao.updateXML();
                    }

                    if (bUpdateUI && alDao.size() > 0) {
                        broadcast(ServiceFunction.ACT_UPDATE_UI, null);
                    }
                }
            }
        }
    }


}


package com.mfinance.everjoy.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lzy.okgo.OkGo;
import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.LiquidationRecord;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.bo.OrderRecord;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.util.ConnectionSelector;
import com.mfinance.everjoy.app.util.PRNGFixes;
import com.mfinance.everjoy.everjoy.network.okgo.OkGoInit;
import com.mfinance.everjoy.everjoy.sp.AppSharedPUtils;
import com.mfinance.everjoy.everjoy.sp.UserSharedPUtils;
import com.mfinance.everjoy.hungkee.xml.Economicdata;
import com.mfinance.everjoy.hungkee.xml.Hourproduct;
import com.mfinance.everjoy.hungkee.xml.Master;
import com.mfinance.everjoy.hungkee.xml.News;
import com.mfinance.everjoy.hungkee.xml.Newscontent;
import com.mfinance.everjoy.hungkee.xml.Strategy;

import net.mfinance.chatlib.init.ChatInit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import androidx.multidex.MultiDex;

/* -- Facebook
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
*/

/**
 * An Application class for store system data.
 *
 * @author : justin.lai
 * @version : v1.00
 * @ModificationHistory Who            When			Version			What<br>
 * -------------------------------------------------------------------------------<br>
 * justin.lai	20110413		v1.00			Creation<br>
 * tom.lo		20130417		v1.01			Modification for YTJY<br>
 */
public class MobileTraderApplication extends Application {
    public static boolean isBackground = false;
    public static final String APP_LAUNCHER_URL_DOMAIN_1 = "applauncher.mfcloud.net";
    public static final String APP_LAUNCHER_URL_DOMAIN_2 = "applauncher.tradingengine.net";

    public static final int APP_LAUNCHER_URL_PORT_1 = 2083;
    public static final int APP_LAUNCHER_URL_PORT_2 = 2083;

    public static final String CONTENT_HOST = "192.168.123.68";
    public static final int CONTENT_PORT = 80;
    public static final String CONTENT_URL = "http://" + CONTENT_HOST + ":" + CONTENT_PORT + "/" + CompanySettings.CONTENT_FOLDER_NAME + "/ContentServlet";
    public static final String CONTENT_URL_DIRECT = "http://" + CONTENT_HOST + ":" + CONTENT_PORT + "/" + CompanySettings.CONTENT_FOLDER_NAME + "/";

    //for real
    public static final String APP_LAUNCHER_URL_1 = "http://" + APP_LAUNCHER_URL_DOMAIN_1 + ":" + APP_LAUNCHER_URL_PORT_1 + "/metadata/applauncher.asp?key=";
    public static final String APP_LAUNCHER_URL_2 = "http://" + APP_LAUNCHER_URL_DOMAIN_2 + ":" + APP_LAUNCHER_URL_PORT_2 + "/metadata/applauncher.asp?key=";
    //public static final String APP_LAUNCHER_URL_1 = "http://192.168.123.233/uat/metadata/applauncher.asp?key=";
    //public static final String APP_LAUNCHER_URL_2 = "http://192.168.123.233/uat/metadata/applauncher.asp?key=";

    //for real
    //public static final String PROD_LICENSE_KEY = "342042009627191517238693015574503129432030310199";
    //public static final String DEMO_LICENSE_KEY = "-169310123387868284475088963731111910114993579184";

    public static String DEFAULT_REFRESH_GENERAL_INFO_KEY = "GENERAL_INFO";
	/*
	protected Facebook mFacebook;
	protected AsyncFacebookRunner mAsyncRunner;	
	*/
    /**
     * Data Repository
     */
    public DataRepository data = DataRepository.getInstance();
    /**
     * Current selected contract
     */
    public ContractObj selectedContract = null;
    /**
     * Current default contract, which stored in preference
     */
    public ContractObj defaultContract = null;

    public int iDefaultService = -1;
    /**
     * Current selected position
     */
    public OpenPositionRecord selectedPosition = null;
    /**
     * Current selected order
     */
    private OrderRecord selectedOrder = null;
    /**
     * Current selected Transaction
     */
    public TransactionObj selectedTransaction = null;
    /**
     * Current trade date
     */
    public Date dtTradeDate = null;
    /**
     * Current selected type of order
     */
    public int iSelectedLimitStop = -1;
    /**
     * Current selected buy sell
     */
    public String sSelectedBuySell = null;
    /**
     * Current selected Liquidation
     */
    public LiquidationRecord selectedLiquidation = null;
    /**
     * Production FxServer URL
     */
    //public static String PRODUCTION_URL = "192.168.123.43";
    /**
     * Demo server FxServer URL
     */
    public static final Boolean isNeedFontBold = Boolean.TRUE;
    public static String DEFAULT_CURR = "HKG";

    public static String WEB = null;
    public String chartDomain = null;
    public int CHART_PORT = CompanySettings.CHART_PORT;
    public int REALTIME_CHART_PORT = CompanySettings.REALTIME_CHART_PORT;

    public static class BuySellStrengthPriceAdjustSetting {
        public int openPosWaitSec;
        public int liqWaitSec;
    }

    // Buy Sell Strength Price Adjust Settings
    public static HashMap<String, BuySellStrengthPriceAdjustSetting> hmBuySellStrengthPriceAdjustSetting = new HashMap<String, BuySellStrengthPriceAdjustSetting>();

    public static double CONV_RATE = 1;
    public static String CONV_RATE_OP = "D";
    public Locale locale = null;

    public volatile boolean bPriceReload = false;
    public volatile boolean bPriceReloadInXML = false;

    public boolean bLogon = false;

    public boolean bQuit = false;
    public Activity firstActivity;

    public boolean bPostMsgToFacebook = false;

    public boolean isArrivedDashBoard = false;
    public boolean bTimeout = false;

    public boolean isLoading = Boolean.FALSE;

    public boolean usingPriceStreaming = false;

    public Date dServerDateTime = new Date();

    public static class LoginInfo {
        public String sURL = null;
        public String sPort = null;

        @Override
        public boolean equals(Object o) {
            LoginInfo compare = (LoginInfo) o;
            return this.sURL.equals(compare.sURL) && this.sPort.equals(compare.sPort);
        }

        public LoginInfo(String sURL, String sPort) {
            this.sURL = sURL;
            this.sPort = sPort;
            toString();
        }

        public LoginInfo(String sURL, int iPort) {
            this.sURL = sURL;
            this.sPort = String.valueOf(iPort);
            toString();
        }

        @Override
        public String toString() {
            return "LoginInfo{" +
                    "sURL='" + sURL + '\'' +
                    ", sPort='" + sPort + '\'' +
                    '}';
        }
    }

    public ArrayList<LoginInfo> alLoginInfoDemo = new ArrayList<LoginInfo>(20);
    public ArrayList<LoginInfo> alLoginInfoProd = new ArrayList<LoginInfo>(20);
    public ArrayList<LoginInfo> alLoginInfoProd2 = new ArrayList<LoginInfo>(20);
    public ArrayList<LoginInfo> alLoginInfoProd3 = new ArrayList<LoginInfo>(20);
    public ArrayList<LoginInfo> alLoginInfoProd4 = new ArrayList<LoginInfo>(20);
    public ArrayList<LoginInfo> alLoginInfoProd5 = new ArrayList<LoginInfo>(20);
    public ArrayList<LoginInfo> alLoginInfoProd6 = new ArrayList<LoginInfo>(20);
    public ArrayList<LoginInfo> alLoginInfoProd7 = new ArrayList<LoginInfo>(20);
    public ArrayList<LoginInfo> alLoginInfoProd8 = new ArrayList<LoginInfo>(20);
    public ArrayList<LoginInfo> alLoginInfoProd9 = new ArrayList<LoginInfo>(20);
    public ArrayList<LoginInfo> alLoginInfoProd10 = new ArrayList<LoginInfo>(20);
    public int RoundRobinIndexDemo = 0;
    public int RoundRobinIndexProd = 0;
    public int RoundRobinIndexProd2 = 0;
    public int RoundRobinIndexProd3 = 0;
    public int RoundRobinIndexProd4 = 0;
    public int RoundRobinIndexProd5 = 0;
    public int RoundRobinIndexProd6 = 0;
    public int RoundRobinIndexProd7 = 0;
    public int RoundRobinIndexProd8 = 0;
    public int RoundRobinIndexProd9 = 0;
    public int RoundRobinIndexProd10 = 0;
    public int iTrialIndexDemo = 0;
    public int iTrialIndexProd = 0;
    public int iTrialIndexProd2 = 0;
    public int iTrialIndexProd3 = 0;
    public int iTrialIndexProd4 = 0;
    public int iTrialIndexProd5 = 0;
    public int iTrialIndexProd6 = 0;
    public int iTrialIndexProd7 = 0;
    public int iTrialIndexProd8 = 0;
    public int iTrialIndexProd9 = 0;
    public int iTrialIndexProd10 = 0;
    public int AppLauncherMessageKeyIndexForDemo = -1;
    public int AppLauncherMessageKeyIndexForProd1 = -1;
    public int AppLauncherMessageKeyIndexForProd2 = -1;
    public int AppLauncherMessageKeyIndexForProd3 = -1;
    public int AppLauncherMessageKeyIndexForProd4 = -1;
    public int AppLauncherMessageKeyIndexForProd5 = -1;
    public int AppLauncherMessageKeyIndexForProd6 = -1;
    public int AppLauncherMessageKeyIndexForProd7 = -1;
    public int AppLauncherMessageKeyIndexForProd8 = -1;
    public int AppLauncherMessageKeyIndexForProd9 = -1;
    public int AppLauncherMessageKeyIndexForProd10 = -1;

    public Map<String, String> hmMasterUrls = Collections.EMPTY_MAP;
    public Map<String, String> hmContactUSUrls = Collections.EMPTY_MAP;
    public Map<String, String> hmDemoRegistrationUrls = Collections.EMPTY_MAP;
    public Map<String, String> hmLostPwdUrls = Collections.EMPTY_MAP;
    public Map<String, String> hmIdentityCheckUrls = Collections.EMPTY_MAP;

    public LoginInfo loginInfoDemoOrg = null;
    public LoginInfo loginInfoDemo = null;
    public LoginInfo loginInfoProd = null;
    public LoginInfo loginInfoProd2 = null;
    public LoginInfo loginInfoProd3 = null;
    public LoginInfo loginInfoProd4 = null;
    public LoginInfo loginInfoProd5 = null;
    public LoginInfo loginInfoProd6 = null;
    public LoginInfo loginInfoProd7 = null;
    public LoginInfo loginInfoProd8 = null;
    public LoginInfo loginInfoProd9 = null;
    public LoginInfo loginInfoProd10 = null;
    public LoginInfo loginInfoDemoServer_otx = null;
    public LoginInfo loginInfoProdServer_otx = null;
    public Uri economicUri = Uri.parse("");
    public Uri newsUri = Uri.parse("");
    public Uri economicUriSC = Uri.parse("");
    public Uri newsUriSC = Uri.parse("");
    public Uri economicUriTC = Uri.parse("");
    public Uri newsUriTC = Uri.parse("");
    public Uri contactUsUri = Uri.parse("");
    public Uri contactUsUriTC = Uri.parse("");
    public Uri contactUsUriSC = Uri.parse("");
    public Uri termsUri = Uri.parse("");
    public Uri termsUriTC = Uri.parse("");
    public Uri termsUriSC = Uri.parse("");
    public Uri announcementUri = Uri.parse("");
    public Uri announcementUriTC = Uri.parse("");
    public Uri announcementUriSC = Uri.parse("");

    class TimeoutThread extends Thread {
        public boolean bQuit = false;
        public long lRemain = 10000;
        TimeoutListener listener = null;

        public TimeoutThread(long lTimeout, TimeoutListener listener) {
            this.listener = listener;
            this.lRemain = lTimeout;
        }

        @Override
        public void run() {
            while (!bQuit) {
                lRemain -= 1000;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (lRemain == 0) {
                    listener.timeout();
                    bQuit = true;
                }
            }
        }
    }

    class AlertTimeoutThread extends Thread {
        public boolean bQuit = false;
        public long lRemain = 10000;
        TimeoutListener listener = null;

        public AlertTimeoutThread(long lTimeout, TimeoutListener listener) {
            this.listener = listener;
            this.lRemain = lTimeout;
        }

        @Override
        public void run() {
            while (!bQuit) {
                lRemain -= 1000;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (lRemain == 0) {
                    data.timeoutAlert = true;
                    listener.timeout();
                    bQuit = true;
                }
            }
        }
    }

    public TimeoutThread at = null;
    public AlertTimeoutThread aat = null;

    public boolean isDemoPlatform = false;

    public void startTimeout(long lTimeout, TimeoutListener listener) {
        if (at == null) {
            at = new TimeoutThread(lTimeout, listener);
            at.start();
        }
    }

    public void startAlertTimeout(long lTimeout, TimeoutListener listener) {
        if (aat == null) {
            aat = new AlertTimeoutThread(lTimeout, listener);
            aat.start();
        }
    }

    public void stopTimeout() {
        if (at != null) {
            at.bQuit = true;
            at = null;
        }

        if (aat != null) {
            aat.bQuit = true;
            aat = null;
        }
    }

    public boolean isTimeout() {
        if (at == null)
            return false;
        else
            return at.lRemain <= 0;
    }

    @SuppressLint("NewApi")
    private static void doEnableStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private static void enableStrictMode() {
        if (Build.VERSION.SDK_INT >= 9) {
            doEnableStrictMode();
        }

        if (Build.VERSION.SDK_INT >= 16) {
            //restore strict mode after onCreate() returns.
            new Handler().postAtFrontOfQueue(new Runnable() {
                @Override
                public void run() {
                    doEnableStrictMode();
                }
            });
        }
    }

    public static Context context;

    protected Consumer<Locale> changeLocale = l -> {
    };

    final private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (preferences, key) -> {
        if (key.equals("LANGUAGE")) {
            Locale locale = LocaleUtility.getLanguage(preferences);
            this.locale = locale;
            Locale.setDefault(locale);
            changeLocale.accept(locale);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        enableStrictMode();
        PRNGFixes.apply();

        CompanySettings.initValues(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        context = this;

        // ======================================

        // 网络
        OkGoInit.init(this, BuildConfig.DEBUG);
        // 环信
        ChatInit.init(this, BuildConfig.DEBUG);
        // 工具类
        Utils.init(this);
        // sp
        UserSharedPUtils.init();
        AppSharedPUtils.init();
    }

    /**
     * Set selected contract
     *
     * @param sContract contract code
     */
    public void setSelectedContract(String sContract) {
        if (sContract == null)
            selectedContract = null;
        else
            selectedContract = data.getContract(sContract);
    }

    /**
     * Set selected contract by contract name
     *
     * @param sContractName contract name
     */
    public void setSelectedContractByName(String sContractName) {
        if (sContractName == null)
            selectedContract = null;
        else
            selectedContract = data.getContractByName(sContractName);
    }

    /**
     * Set selected contract by contract object
     *
     * @param selectedContract contract object
     */
    public void setSelectedContract(ContractObj selectedContract) {
        this.selectedContract = selectedContract;
    }

    /**
     * Get selected contract
     *
     * @return selected contract
     */
    public ContractObj getSelectedContract() {
        return selectedContract;
    }

    /**
     * Set selected buy sell
     *
     * @param sBuySell buy/sell ("B" or "S")
     */
    public void setSelectedBuySell(String sBuySell) {
        this.sSelectedBuySell = sBuySell;
    }

    /**
     * Get selected buy sell
     *
     * @return buy/sell ("B" or "S")
     */
    public String getSelectedBuySell() {
        return sSelectedBuySell;
    }

    /**
     * Set selected position by reference number
     *
     * @param iRef open position reference number
     */
    public void setSelectedOpenPosition(int iRef) {
        if (iRef == -1)
            selectedPosition = null;
        else
            selectedPosition = data.getOpenPosition(iRef);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        locale = LocaleUtility.getLanguage(PreferenceManager.getDefaultSharedPreferences(newBase));
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

        // 分包工具
		MultiDex.install(this);
    }

    /**
     * Set selected position by reference number
     *
     * @param sID open position reference number
     */
    public void setSelectedTransaction(String sID) {
        if (sID == null && sID != "")
            selectedTransaction = null;
        else
            selectedTransaction = data.getTransaction(sID);
    }


    /**
     * Get selected transaction
     *
     * @return selected transaction
     */
    public TransactionObj getSelectedTransaction() {
        return selectedTransaction;
    }

    /**
     * Set selected Liquidation by key
     *
     * @param sKey
     */
    public void setSelectedLiquidation(String sKey) {
        if (sKey == null && sKey != "")
            selectedLiquidation = null;
        else
            selectedLiquidation = data.getLiquidationRecord(sKey);
    }


    /**
     * Get selected Liquidation
     *
     * @return selected Liquidation
     */
    public LiquidationRecord getSelectedLiquidation() {
        return selectedLiquidation;
    }

    /**
     * Get selected open position
     *
     * @return selected position
     */
    public OpenPositionRecord getSelectedOpenPosition() {
        return selectedPosition;
    }

    /**
     * Set selected limit or stop order
     *
     * @param iLimitStop 0 = Limit, 1 = Stop
     */
    public void setSelectedLimitStop(int iLimitStop) {
        this.iSelectedLimitStop = iLimitStop;
    }

    /**
     * Get selected limit or stop order
     *
     * @return 0 = Limit, 1 = Stop
     */
    public int getSelectedLimitStop() {
        return iSelectedLimitStop;
    }

    /**
     * Set selected running order
     *
     * @param iRef order reference
     */
    public void setSelectedRunningOrder(int iRef) {
        selectedOrder = data.getRunningOrder(iRef);

        if (selectedOrder != null && selectedOrder.sTargetPosition != null) {
            this.setSelectedOpenPosition(Integer.parseInt(selectedOrder.sTargetPosition));
        } else {
            this.setSelectedOpenPosition(-1);
        }
    }

    /**
     * Set selected cancelled order
     *
     * @param iRef order reference
     */
    public void setSelectedCancelledOrder(int iRef) {
        selectedOrder = data.getCancelledOrder(iRef);
    }

    /**
     * Set selected excuted order
     *
     * @param iRef order reference
     */
    public void setSelectedExcutedOrder(int iRef) {
        selectedOrder = data.getExecutedOrder(iRef);
    }

    /**
     * Get selected running order
     *
     * @return running order
     */
    public OrderRecord getSelectedRunningOrder() {
        return selectedOrder;
    }

    /**
     * Get selected cancelled order
     *
     * @return running order
     */
    public OrderRecord getSelectedCancelledOrder() {
        return selectedOrder;
    }

    /**
     * Get selected excuted order
     *
     * @return running order
     */
    public OrderRecord getSelectedExcutedOrder() {
        return selectedOrder;
    }


    /**
     * Get default contract object, which setted in preference
     *
     * @return default contract
     */
    public ContractObj getDefaultContract() {
        ContractObj c;
        if (defaultContract == null) {
            SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String sContract = setting.getString("DEFAULT_CONTRACT", (CompanySettings.getDefaultDefaultContract(this)));
            c = data.getContract(sContract);
        } else {
            c = defaultContract;
        }

        if (bLogon == true) {
            // Restrict user set non-tradable contract as default
            if (c.isViewable() == false || DataRepository.getInstance().getTradableContract().contains(c.strContractCode) == false) {
                for (int i = 0; i < DataRepository.getInstance().getTradableContract().size(); i++) {
                    String strContract = DataRepository.getInstance().getTradableContract().get(i);
                    if (DataRepository.getInstance().getContract(strContract).isViewable())
                        return c;
                }
            }
        }
        return c;
    }

    /**
     * Set default contract by contract code
     *
     * @param sContract contract code
     */
    public void setDefaultContract(String sContract) {
        defaultContract = data.getContract(sContract);
    }

    /**
     * Get default page int, which setted in preference
     *
     * @return default contract
     */

    public void reloadDefaultPageInMemory() {
        iDefaultService = -1;
    }

    public void setDefaultPage(int iService) {
        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = setting.edit();
        editor.putInt("DEFAULT_PAGE", iService);
        editor.commit();
    }

    public int getDefaultPage() {
        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        iDefaultService = setting.getInt("DEFAULT_PAGE", CompanySettings.DEFAULTDEFAULTPAGE);
        return iDefaultService;
    }

    public void setDefaultPage(String sSeq) {
        if (sSeq != null) {
            data.setContractSequence(sSeq);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Not maintained. Need to update locale on both application, service and activity.
     */
    @Deprecated
    public void changeLang(Context mContext, String sLocale, String variant) {
        Locale locale = null;
        if (variant == null) {
            locale = new Locale(sLocale);
        } else {
            locale = new Locale(sLocale, variant);
        }
        Locale.setDefault(locale);
        Configuration config = mContext.getResources().getConfiguration();
        config.locale = locale;
        mContext.getResources().updateConfiguration(config,
                mContext.getResources().getDisplayMetrics());
    }
		 
/* -- Facebook
	public void postMessageOnWall(String msg) {
		if(bPostMsgToFacebook){
		    Bundle parameters = new Bundle();
		    parameters.putString("message", msg);
		    try {
				//String response = mFacebook.request("me/feed", parameters,"POST");
				mAsyncRunner.request("me/feed", parameters, "POST", new PostRequestListener(), this);
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}
	
	public class PostRequestListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			System.out.println("Got response: " + response);
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
			Log.e("Facebook", e.getMessage());
	        e.printStackTrace();
		}
	}
	
	public class SampleAuthListener implements AuthListener {

		public void onAuthSucceed() {
			System.out.println(">>>>>onAuthSucceed");
			SessionStore.save(mFacebook, MobileTraderApplication.this);
		}

		public void onAuthFail(String error) {
			System.out.println(error);
		}
	}

	public class SampleLogoutListener implements LogoutListener {
		public void onLogoutBegin() {
			System.out.println(">>>>>onLogoutBegin");
		}

		public void onLogoutFinish() {
			System.out.println(">>>>>onLogoutFinish");
			SessionStore.clear(MobileTraderApplication.this);
		}
	}
	*/

    private ArrayList<LoginInfo> convertJsonToArray(String sJson) {
        ArrayList<LoginInfo> alList = new ArrayList<LoginInfo>(20);

        JSONObject json;
        JSONArray connInfo;
        try {
            json = new JSONObject(sJson);
            connInfo = json.getJSONArray("server_conn");

            for (int i = 0; i < connInfo.length(); i++) {
                JSONObject info = connInfo.getJSONObject(i);
                alList.add(new LoginInfo(info.getString("ip"), info.getString("port")));
            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return alList;
    }

    public void convertJsonToArrayDemo(String sJson) {
        ArrayList<LoginInfo> alList = new ArrayList<LoginInfo>(20);
        alList = convertJsonToArray(sJson);
        for (int i = 0; i < alList.size(); i++) {
            String[] sURL = alList.get(i).sURL.split(";");
            String[] sPort = alList.get(i).sPort.split(";");
            for (int j = 0; j < sURL.length; j++) {
                String curPort = "15000";
                if (sPort.length > j)
                    curPort = sPort[j];
                LoginInfo newInfo = new LoginInfo(sURL[j], curPort);
                if (alLoginInfoDemo.indexOf(newInfo) < 0)
                    alLoginInfoDemo.add(newInfo);
            }

        }
    }

    public void convertJsonToArrayProd(String sJson) {
        ArrayList<LoginInfo> alList = new ArrayList<LoginInfo>(20);
        alList = convertJsonToArray(sJson);
        for (int i = 0; i < alList.size(); i++) {
            String[] sURL = alList.get(i).sURL.split(";");
            String[] sPort = alList.get(i).sPort.split(";");
            for (int j = 0; j < sURL.length; j++) {
                String curPort = "15000";
                if (sPort.length > j)
                    curPort = sPort[j];
                LoginInfo newInfo = new LoginInfo(sURL[j], curPort);
                if (alLoginInfoProd.indexOf(newInfo) < 0)
                    alLoginInfoProd.add(newInfo);
            }

        }
    }

    public String getRegisterURL() {
        try {
            return "http://" + this.loginInfoDemo.sURL + CompanySettings.REGISTER_URL;
        } catch (Exception e) {
            return "";
        }
    }

    public String getForgotPasswordURL() {
        try {
            String returnUrl = "http://" + this.loginInfoDemo.sURL + CompanySettings.FORGOT_PASSWORD_URL;
            return returnUrl;
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                if (this.loginInfoDemo == null)
                    Log.e("loginInfoDemo", "null");
                if (this.loginInfoDemo.sURL == null)
                    Log.e("loginInfoDemo.sURL", "null");
            }
            return "";
        }
    }

    public String getReportGroupURL(boolean isDemo) {
        String url = null;
        if (CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX == true) {
            if (isDemo)
                url = this.loginInfoDemoServer_otx.sURL;
            else
                url = this.loginInfoProdServer_otx.sURL;
            return "http://" + url + CompanySettings.REPORTGROUP_URL;
        } else {
            if (isDemo) {
                url = this.loginInfoDemo.sURL;
                return "http://" + url + CompanySettings.REPORTGROUP_URL;
            } else {
                if (CompanySettings.ENABLE_FATCH_PLATFORM_ID_FROM_MOBILE_SERVICE) {
                    url = this.loginInfoProd.sURL;
                    return "http://" + url + CompanySettings.REPORTGROUP_URL;
                } else if (CompanySettings.checkProdServer() == 1) {
                    url = this.loginInfoProd.sURL;
                    return "http://" + url + CompanySettings.REPORTGROUP_URL;
                } else {
                    url = this.loginInfoProd2.sURL;
                    return "http://" + url + CompanySettings.REPORTGROUP_URL_PROD2;
                }
            }
        }
    }

    public String getServerByIdURL() {
        if (CompanySettings.SERVERBYID_URL.contains("http://"))
            return CompanySettings.SERVERBYID_URL;
        else
            return "http://" + this.loginInfoDemoOrg.sURL + CompanySettings.SERVERBYID_URL;
    }

    public String getContractListURL() {
        return "http://" + this.loginInfoDemo.sURL + CompanySettings.CONTRACTLIST_URL;
    }

    public String getStatmentURL() {
        if (WEB != null) {
            return WEB + "/accountbalance.asp?";
        }
        if (isDemoPlatform) {
            if (CompanySettings.STATEMENT_DEMO_URL.contains("http://"))
                return CompanySettings.STATEMENT_DEMO_URL;
            else
                return "http://" + this.loginInfoDemo.sURL + "/" + CompanySettings.STATEMENT_DEMO_URL;
        } else {
            if (CompanySettings.FOR_UAT) {
                if (CompanySettings.STATEMENT_DEMO_URL.contains("http://"))
                    return CompanySettings.STATEMENT_DEMO_URL;
                else
                    return "http://" + this.loginInfoDemo.sURL + "/" + CompanySettings.STATEMENT_DEMO_URL;
            } else {
                if (CompanySettings.checkProdServer() == 1)
                    if (CompanySettings.STATEMENT_PROD_URL.contains("http://"))
                        return CompanySettings.STATEMENT_PROD_URL;
                    else
                        return "http://" + this.loginInfoProd.sURL + "/" + CompanySettings.STATEMENT_PROD_URL;
                else if (CompanySettings.checkProdServer() == 2)
                    if (CompanySettings.STATEMENT_PROD_URL.contains("http://"))
                        return CompanySettings.STATEMENT_PROD_URL;
                    else
                        return "http://" + this.loginInfoProd2.sURL + "/" + CompanySettings.STATEMENT_PROD_URL;
                else if (CompanySettings.checkProdServer() == 3)
                    if (CompanySettings.STATEMENT_PROD_URL.contains("http://"))
                        return CompanySettings.STATEMENT_PROD_URL;
                    else
                        return "http://" + this.loginInfoProd3.sURL + "/" + CompanySettings.STATEMENT_PROD_URL;
                else if (CompanySettings.checkProdServer() == 4)
                    if (CompanySettings.STATEMENT_PROD_URL.contains("http://"))
                        return CompanySettings.STATEMENT_PROD_URL;
                    else
                        return "http://" + this.loginInfoProd4.sURL + "/" + CompanySettings.STATEMENT_PROD_URL;
                else if (CompanySettings.checkProdServer() == 5)
                    if (CompanySettings.STATEMENT_PROD_URL.contains("http://"))
                        return CompanySettings.STATEMENT_PROD_URL;
                    else
                        return "http://" + this.loginInfoProd5.sURL + "/" + CompanySettings.STATEMENT_PROD_URL;
                else if (CompanySettings.checkProdServer() == 6)
                    if (CompanySettings.STATEMENT_PROD_URL.contains("http://"))
                        return CompanySettings.STATEMENT_PROD_URL;
                    else
                        return "http://" + this.loginInfoProd6.sURL + "/" + CompanySettings.STATEMENT_PROD_URL;
                else if (CompanySettings.checkProdServer() == 7)
                    if (CompanySettings.STATEMENT_PROD_URL.contains("http://"))
                        return CompanySettings.STATEMENT_PROD_URL;
                    else
                        return "http://" + this.loginInfoProd7.sURL + "/" + CompanySettings.STATEMENT_PROD_URL;
                else if (CompanySettings.checkProdServer() == 8)
                    if (CompanySettings.STATEMENT_PROD_URL.contains("http://"))
                        return CompanySettings.STATEMENT_PROD_URL;
                    else
                        return "http://" + this.loginInfoProd8.sURL + "/" + CompanySettings.STATEMENT_PROD_URL;
                else if (CompanySettings.checkProdServer() == 9)
                    if (CompanySettings.STATEMENT_PROD_URL.contains("http://"))
                        return CompanySettings.STATEMENT_PROD_URL;
                    else
                        return "http://" + this.loginInfoProd9.sURL + "/" + CompanySettings.STATEMENT_PROD_URL;
                else if (CompanySettings.STATEMENT_PROD_URL.contains("http://"))
                    return CompanySettings.STATEMENT_PROD_URL;
                else
                    return "http://" + this.loginInfoProd10.sURL + "/" + CompanySettings.STATEMENT_PROD_URL;
            }

        }
    }

    public void convertJsonToArrayProd2(String sJson) {
        ArrayList<LoginInfo> alList = new ArrayList<LoginInfo>(20);
        alList = convertJsonToArray(sJson);
        for (int i = 0; i < alList.size(); i++) {
            String[] sURL = alList.get(i).sURL.split(";");
            String[] sPort = alList.get(i).sPort.split(";");
            for (int j = 0; j < sURL.length; j++) {
                String curPort = "15000";
                if (sPort.length > j)
                    curPort = sPort[j];
                LoginInfo newInfo = new LoginInfo(sURL[j], curPort);
                if (alLoginInfoProd2.indexOf(newInfo) < 0)
                    alLoginInfoProd2.add(newInfo);
            }

        }
    }

    public void convertJsonToArrayProd3(String sJson) {
        ArrayList<LoginInfo> alList = new ArrayList<LoginInfo>(20);
        alList = convertJsonToArray(sJson);
        for (int i = 0; i < alList.size(); i++) {
            String[] sURL = alList.get(i).sURL.split(";");
            String[] sPort = alList.get(i).sPort.split(";");
            for (int j = 0; j < sURL.length; j++) {
                String curPort = "15000";
                if (sPort.length > j)
                    curPort = sPort[j];
                LoginInfo newInfo = new LoginInfo(sURL[j], curPort);
                if (alLoginInfoProd3.indexOf(newInfo) < 0)
                    alLoginInfoProd3.add(newInfo);
            }

        }
    }

    public void convertJsonToArrayProd4(String sJson) {
        ArrayList<LoginInfo> alList = new ArrayList<LoginInfo>(20);
        alList = convertJsonToArray(sJson);
        for (int i = 0; i < alList.size(); i++) {
            String[] sURL = alList.get(i).sURL.split(";");
            String[] sPort = alList.get(i).sPort.split(";");
            for (int j = 0; j < sURL.length; j++) {
                String curPort = "15000";
                if (sPort.length > j)
                    curPort = sPort[j];
                LoginInfo newInfo = new LoginInfo(sURL[j], curPort);
                if (alLoginInfoProd4.indexOf(newInfo) < 0)
                    alLoginInfoProd4.add(newInfo);
            }

        }
    }

    public void convertJsonToArrayProd5(String sJson) {
        ArrayList<LoginInfo> alList = new ArrayList<LoginInfo>(20);
        alList = convertJsonToArray(sJson);
        for (int i = 0; i < alList.size(); i++) {
            String[] sURL = alList.get(i).sURL.split(";");
            String[] sPort = alList.get(i).sPort.split(";");
            for (int j = 0; j < sURL.length; j++) {
                String curPort = "15000";
                if (sPort.length > j)
                    curPort = sPort[j];
                LoginInfo newInfo = new LoginInfo(sURL[j], curPort);
                if (alLoginInfoProd5.indexOf(newInfo) < 0)
                    alLoginInfoProd5.add(newInfo);
            }

        }
    }

    public void convertJsonToArrayProd6(String sJson) {
        ArrayList<LoginInfo> alList = new ArrayList<LoginInfo>(20);
        alList = convertJsonToArray(sJson);
        for (int i = 0; i < alList.size(); i++) {
            String[] sURL = alList.get(i).sURL.split(";");
            String[] sPort = alList.get(i).sPort.split(";");
            for (int j = 0; j < sURL.length; j++) {
                String curPort = "15000";
                if (sPort.length > j)
                    curPort = sPort[j];
                LoginInfo newInfo = new LoginInfo(sURL[j], curPort);
                if (alLoginInfoProd6.indexOf(newInfo) < 0)
                    alLoginInfoProd6.add(newInfo);
            }

        }
    }

    public void convertJsonToArrayProd7(String sJson) {
        ArrayList<LoginInfo> alList = new ArrayList<LoginInfo>(20);
        alList = convertJsonToArray(sJson);
        for (int i = 0; i < alList.size(); i++) {
            String[] sURL = alList.get(i).sURL.split(";");
            String[] sPort = alList.get(i).sPort.split(";");
            for (int j = 0; j < sURL.length; j++) {
                String curPort = "15000";
                if (sPort.length > j)
                    curPort = sPort[j];
                LoginInfo newInfo = new LoginInfo(sURL[j], curPort);
                if (alLoginInfoProd7.indexOf(newInfo) < 0)
                    alLoginInfoProd7.add(newInfo);
            }

        }
    }

    public void convertJsonToArrayProd8(String sJson) {
        ArrayList<LoginInfo> alList = new ArrayList<LoginInfo>(20);
        alList = convertJsonToArray(sJson);
        for (int i = 0; i < alList.size(); i++) {
            String[] sURL = alList.get(i).sURL.split(";");
            String[] sPort = alList.get(i).sPort.split(";");
            for (int j = 0; j < sURL.length; j++) {
                String curPort = "15000";
                if (sPort.length > j)
                    curPort = sPort[j];
                LoginInfo newInfo = new LoginInfo(sURL[j], curPort);
                if (alLoginInfoProd8.indexOf(newInfo) < 0)
                    alLoginInfoProd8.add(newInfo);
            }

        }
    }

    public void convertJsonToArrayProd9(String sJson) {
        ArrayList<LoginInfo> alList = new ArrayList<LoginInfo>(20);
        alList = convertJsonToArray(sJson);
        for (int i = 0; i < alList.size(); i++) {
            String[] sURL = alList.get(i).sURL.split(";");
            String[] sPort = alList.get(i).sPort.split(";");
            for (int j = 0; j < sURL.length; j++) {
                String curPort = "15000";
                if (sPort.length > j)
                    curPort = sPort[j];
                LoginInfo newInfo = new LoginInfo(sURL[j], curPort);
                if (alLoginInfoProd9.indexOf(newInfo) < 0)
                    alLoginInfoProd9.add(newInfo);
            }

        }
    }

    public void convertJsonToArrayProd10(String sJson) {
        ArrayList<LoginInfo> alList = new ArrayList<LoginInfo>(20);
        alList = convertJsonToArray(sJson);
        for (int i = 0; i < alList.size(); i++) {
            String[] sURL = alList.get(i).sURL.split(";");
            String[] sPort = alList.get(i).sPort.split(";");
            for (int j = 0; j < sURL.length; j++) {
                String curPort = "15000";
                if (sPort.length > j)
                    curPort = sPort[j];
                LoginInfo newInfo = new LoginInfo(sURL[j], curPort);
                if (alLoginInfoProd10.indexOf(newInfo) < 0)
                    alLoginInfoProd10.add(newInfo);
            }

        }
    }

    public int getDefaultRefreshGeneralInfo() {
        return defaultRefreshGeneralInfo;
    }

    /**
     * Current default refresh general information time, which stored in
     * preference
     */
    public int defaultRefreshGeneralInfo = 5;

    public void setDefaultRefreshGeneralInfo(int defaultRefreshGeneralInfo) {
        this.defaultRefreshGeneralInfo = defaultRefreshGeneralInfo;
    }

    public Newscontent getSelectedNewscontent() {
        return selectedNewscontent;
    }

    public void setSelectedNewscontent(Integer iNewscontent) {
        if (iNewscontent == null)
            selectedNewscontent = null;
        else
            selectedNewscontent = data.getHmNewscontents().get(iNewscontent);
    }

    public Economicdata getSelectedEconomicdata() {
        return selectedEconomicdata;
    }

    public void setSelectedEconomicdata(Integer iEconomicdata) {
        if (iEconomicdata == null)
            selectedEconomicdata = null;
        else
            selectedEconomicdata = data.getHmEconomicdatas().get(iEconomicdata);
    }

    public News getSelectedNews() {
        return selectedNews;
    }

    public void setSelectedNews(Integer iNews) {
        if (iNews == null)
            selectedNews = null;
        else
            selectedNews = data.getHmNewses().get(iNews);
    }

    public Strategy getSelectedStrategy() {
        return selectedStrategy;
    }

    public void setSelectedStrategy(Integer iStrategy) {
        if (iStrategy == null)
            selectedStrategy = null;
        else
            selectedStrategy = data.getHmStrategies().get(iStrategy);
    }

    public void setSelectedStrategy(String sStrategy) {
        if (sStrategy == null)
            selectedStrategy = null;
        else
            selectedStrategy = data.getHmStrategiesWithStr().get(sStrategy);
    }

    public Master getSelectedMaster() {
        return selectedMaster;
    }

    /**
     * Current selected Date in Day Plan Listing
     */
    public int selectedStrategyIndex = 0;

    /**
     * Current selected index in Maste Listing
     */
    public int selectedMasterIndex = 0;

    public int getSelectedMasterIndex() {
        return selectedMasterIndex;
    }

    public void setSelectedMaster(Integer Integer) {
        if (Integer == null)
            selectedMaster = null;
        else {
            selectedMaster = data.getAlEmailMaster().get(getSelectedMasterIndex()).get(Integer);
            //selectedMaster = data.getHmMasters().get(Integer);
        }
    }


    /**
     * Current selected Newscontent
     */
    public Newscontent selectedNewscontent = null;

    /**
     * Current selected Economicdatas
     */
    public Economicdata selectedEconomicdata = null;

    /**
     * Current selected News
     */
    public News selectedNews = null;

    /**
     * Current selected master
     */
    public Master selectedMaster = null;

    /**
     * Current selected Strategy
     */
    public Strategy selectedStrategy = null;

    public void setSelectedMasterIndex(int index) {
        this.selectedMasterIndex = index;
    }

    public int getSelectedStrategyIndex() {
        return selectedStrategyIndex;
    }

    public void setSelectedStrategyIndex(int selectedStrategyIndex) {
        this.selectedStrategyIndex = selectedStrategyIndex;
    }

    public Hourproduct selectedHourproduct = null;

    public Hourproduct getSelectedHourproduct() {
        return selectedHourproduct;
    }

    public void setSelectedHourproduct(Hourproduct hourproduct) {
        selectedHourproduct = hourproduct;
    }

    public void setSelectedHourproduct(Integer iKey) {
        if (iKey == null)
            selectedHourproduct = null;
        else
            selectedHourproduct = data.getHmHourproducts().get(iKey);
    }

    //For Price Agent Use
    private final AtomicReference<Socket> priceAgentSocketRef = new AtomicReference<Socket>();
    private final AtomicReference<Iterator<ConnectionSelector.ConnectionEntry>> priceAgentConnectListRef = new AtomicReference<Iterator<ConnectionSelector.ConnectionEntry>>();
    private final AtomicBoolean priceAgentConnectionStatus = new AtomicBoolean();
    public ScheduledExecutorService executor = getNewExecutor();
    public int m_connectionTimeout = 60000;
    private String selfIP;

    public ScheduledExecutorService getNewExecutor() {
        return Executors.newSingleThreadScheduledExecutor(
                new ThreadFactoryBuilder().setNameFormat("sad-priceagent-thread-%d").build()
        );
    }

    public boolean setPriceAgentSocketRef(Socket lastClosedSocket, Socket priceAgentSocket) {
        return priceAgentSocketRef.compareAndSet(lastClosedSocket, priceAgentSocket);
    }

    public Socket getPriceAgentSocketRef() {
        return priceAgentSocketRef.get();
    }

    public void setPriceAgentConnectListRef(Iterator<ConnectionSelector.ConnectionEntry> a) {
        priceAgentConnectListRef.set(a);
    }

    public ConnectionSelector.ConnectionEntry getPriceAgentConnectListRef() {
        return priceAgentConnectListRef.get().next();
    }

    public void setPriceAgentConnectionStatus(boolean status) {
        priceAgentConnectionStatus.set(status);
    }

    public boolean getPriceAgentConnectionStatus() {
        return priceAgentConnectionStatus.get();
    }

    public void setSelfIP(String ip) {
        selfIP = ip;
    }

    public String getSelfIP() {
        return selfIP;
    }
}

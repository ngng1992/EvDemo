package com.mfinance.everjoy.app;


import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.BalanceRecord;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.MenuServiceID;
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
import com.mfinance.everjoy.app.model.Menu;
import com.mfinance.everjoy.app.pojo.ContractDefaultSetting;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.ColorController;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.wheel.WheelView;
import com.mfinance.everjoy.app.widget.zoom.ImageZoomView;
import com.mfinance.everjoy.hungkee.xml.Advertisement;
import com.mfinance.everjoy.hungkee.xml.dao.AdvertisementDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import me.pushy.sdk.Pushy;

/**
 * * 
 * It is a view controller, which implemented navigation behavior. 
 *
 *  @author        : justin.lai
 *  @version       : v1.00
 *
 *
 *  @ModificationHistory
 *  Who			When			Version			What<br>
 *  -------------------------------------------------------------------------------<br> 
 *  justin.lai	20110413		v1.00			Creation<br>
 *
 *
**
 */
public abstract class BaseActivity  extends Activity implements ServiceConnection, TimeoutListener {
	public final String mimetype = "text/html";
	public final String encoding = "utf-8";
	private Timer advTimer;

	private boolean backgroundthread = false;

	String SLEEP = "SLEEP";

	/**
	 * Preference name
	 */
	public static final String PREF_SETTING = "PREF_SETTING";
	/**
	 * Vibrating time usage
	 */
	public static final int MESSAGE_VIBRATE = 1000;

	protected Double release;

	/**
	 * * 
	 * A Handler for handling message from service 
	 *
	 *  @author        : justin.lai
	 *  @version       : v1.00
	 *
	 *
	 *  @ModificationHistory
	 *  Who			When			Version			What<br>
	 *  -------------------------------------------------------------------------------<br> 
	 *  justin.lai	20110413		v1.00			Creation<br>
	 *
	 *
	**
	 */
	class MessengeHandler extends Handler {

		@Override
		/**
		 * Handle message method
		 * @param msg Message from service
		 */
		public void handleMessage(Message msg) {
			Intent intent = null;
			//System.out.println("msg.what:"+msg.what);
			switch(msg.what){
				case ServiceFunction.ACT_SYSTEM_ALERT_MESSAGE:
					showSystemAlertMessage();
					break;
				case ServiceFunction.ACT_UPDATE_UI:
					fireUIUpdate();
					break;
				case ServiceFunction.ACT_VIBRATE:
					fireVibrate();
					break;
				case ServiceFunction.ACT_DISCONNECT:
					try{
						boolean isLogin = msg.getData().getBoolean("login", false);

						if(!isLogin){
							BaseActivity.this.finish();
							intent = new Intent(BaseActivity.this, LoginActivity.class);
							intent.putExtras(msg.getData());
							intent.putExtra("disconnected", true);
							startActivity(intent);
						}else{
							if(dialog != null)
							{
								dialog.dismiss();
								dialog = null;
							}
						}
					}catch(Exception e){
						e.printStackTrace();
						System.out.println("BaseActivity is "+ (BaseActivity.this== null) +" res is " + (res == null));
					}


					break;
				case ServiceFunction.ACT_GO_TO_LOGIN:
					//System.out.println("-------------------------------------- finish 1");
					BaseActivity.this.finish();
					//System.out.println("-------------------------------------- finish 2");
					intent = new Intent(BaseActivity.this, LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtras(msg.getData());
					startActivity(intent);
					//System.out.println("-------------------------------------- finish 3");
					break;
				case ServiceFunction.ACT_GO_TO_DASHBOARD:
					finish();
					intent = new Intent(BaseActivity.this, DashboardActivity.class);
					if (CompanySettings.newinterface)
						intent = new Intent(BaseActivity.this, ContractListActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_PRICE: {
					boolean guest = msg.getData().getBoolean("guest", false);
					intent = new Intent(BaseActivity.this, guest ? ContractListGuestActivity.class : ContractListActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				}
				case ServiceFunction.ACT_GO_TO_OPEN_POSITION:
					intent = new Intent(BaseActivity.this, OpenPositionListActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_CONTRACT_DETAIL: {
					if (app.getSelectedContract() != null) {
						intent = new Intent(BaseActivity.this, ContractDetailActivity.class);
						intent.putExtras(msg.getData());
						startActivity(intent);
					}
					break;
				}
				case ServiceFunction.ACT_GO_TO_TRANSACTION:
					intent = new Intent(BaseActivity.this, TransactionListActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_DEAL:
					if (app.getSelectedContract()!=null){
						intent = new Intent(BaseActivity.this, DealActivity.class);
						intent.putExtras(msg.getData());
						startActivity(intent);
					}
					break;
				case ServiceFunction.ACT_GO_TO_LIQUIDATE:
					intent = new Intent(BaseActivity.this, LiquidationActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_RUNNING_ORDER:
					intent = new Intent(BaseActivity.this, RunningOrderListActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_EXECUTED_ORDER:
					intent = new Intent(BaseActivity.this, ExecutedOrderListActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_CANCELLED_ORDER:
					intent = new Intent(BaseActivity.this, CancelledOrderListActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_LIQUIDATION_HISTORY:
					intent = new Intent(BaseActivity.this, LiquidationHistoryListActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_ORDER:
					if (app.getSelectedContract()!=null){
						intent = new Intent(BaseActivity.this, OrderActivity.class);
						intent.putExtras(msg.getData());
						startActivity(intent);
					}
					break;
				case ServiceFunction.ACT_GO_TO_EDIT_ORDER:
					if (app.getSelectedRunningOrder()!=null){
						intent = new Intent(BaseActivity.this, EditOrderActivity.class);
						intent.putExtras(msg.getData());
						startActivity(intent);
					}
					break;
				case ServiceFunction.ACT_GO_TO_CHART:
					if (app.getSelectedContract()!=null){
						intent = new Intent(BaseActivity.this, ChartActivity.class);
						intent.putExtras(msg.getData());
						startActivity(intent);
					}
					break;
				case ServiceFunction.ACT_GO_TO_OPEN_POSITION_SUMMARY:
					intent = new Intent(BaseActivity.this, OpenPositionSummaryActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_SETTING: {
                    ContractObj defaultContract = getDefaultContract();
                    if (defaultContract != null) {
                        intent = new Intent(BaseActivity.this, SettingActivity.class);
						intent.putExtras(msg.getData());
                        startActivity(intent);
                    } else if (defaultContract == null && (CompanySettings.ENABLE_CONTENT) && !app.bLogon) {
                        intent = new Intent(BaseActivity.this, SettingActivity.class);
						intent.putExtras(msg.getData());
                        startActivity(intent);
                    }
                    break;
                }
				case ServiceFunction.ACT_GO_TO_SETTING_ID:
					if (getDefaultContract()!=null){
						intent = new Intent(BaseActivity.this, SettingIDActivity.class);
						intent.putExtras(msg.getData());
						startActivity(intent);
					}
					break;
				case ServiceFunction.ACT_GO_TO_HISTORY:
					intent = new Intent(BaseActivity.this, HistoryListActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_VISIBLE_POP_UP:
					if(dialog == null)
						dialog = ProgressDialog.show(BaseActivity.this, "", res.getString(R.string.please_wait), true);
					else
						dialog.show();
					break;
				case ServiceFunction.ACT_INVISIBLE_POP_UP:
					dialog.dismiss();
					dialog = null;
					break;
				case ServiceFunction.ACT_GO_TO_ANDROID_MARKET:
					intent = new Intent(Intent.ACTION_VIEW);
					Uri UpdateUri;
					if( CompanySettings.APP_UPDATE_URL_TYPE != null && !CompanySettings.APP_UPDATE_URL_PROD1.equals("-"))
					{
						// since we cannot determine user login platform at this moment , hard code using prod1 now! 
						UpdateUri = Uri.parse(CompanySettings.APP_UPDATE_URL_PROD1);
					}
					else {
						UpdateUri = Uri.parse(CompanySettings.APP_UPDATE_URL!=null?CompanySettings.APP_UPDATE_URL:"market://details?id="+ getApplicationContext().getPackageName());
						PackageManager pm = getApplicationContext().getPackageManager();
						boolean isInstalled = isPackageInstalled("com.android.browser", pm);
						if (isInstalled)
							intent.setPackage("com.android.browser");
					}
					intent.setData(UpdateUri);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_ON_LINE_STATEMENT:
					intent = new Intent(BaseActivity.this, OnLineStatementActivity.class);
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
					Toast.makeText(BaseActivity.this.getBaseContext(), sMsg, Toast.LENGTH_LONG ).show();
					break;
				case ServiceFunction.ACT_UNBIND_SERVICE:
					doUnbindService();
					break;
				case ServiceFunction.ACT_GO_TO_COMPANY_PROFILE:
					intent = new Intent(BaseActivity.this, CompanyProfileActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_NEWS_DETAIL :
					intent = new Intent(BaseActivity.this,NewsDetailActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_CONTACT_US:
					intent = new Intent(BaseActivity.this, ContactUsActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_STRATEGY_DETAIL:
					intent = new Intent(BaseActivity.this,
							StrategyDetailActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_ECONOMIC_DATA_DETAIL:
					intent = new Intent(BaseActivity.this,
							EconomicDataDetailActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_MASTER_DETAIL:
					intent = new Intent(BaseActivity.this,
							MasterDetailActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_NEWS_LIST:
					intent = new Intent(BaseActivity.this,
							NewsListingActivitiy.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_DAY_PLAN_LIST:
					intent = new Intent(BaseActivity.this,
							StrategyListingActivitiy.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_ECONOMIC_DATA_LIST:
					intent = new Intent(BaseActivity.this,
							EconomicDataListingActivitiy.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_NEWS_CONTENT_LIST:
					intent = new Intent(BaseActivity.this,
							NewsContentListingActivitiy.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_MASTER_LIST:
					intent = new Intent(BaseActivity.this,
							MasterListingActivitiy.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_NEWS_CONTENT_DETAIL:
					intent = new Intent(BaseActivity.this,
							NewsContentDetailActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_FINISH_ACTIVITY:
					finish();
					break;
				case ServiceFunction.ACT_SHOW_ACC_INFO:
					showAccountInformation();
					break;
				case ServiceFunction.ACT_GO_TO_HOUR_PRODUCT:
					intent = new Intent(BaseActivity.this, HourProductListingActivitiy.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_HOUR_PRODUCT_DETAIL :
					intent = new Intent(BaseActivity.this,HourProductDetailActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_TERMS:
					intent = new Intent(BaseActivity.this, TermsActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_CASH_MOVEMENT:
					intent = new Intent(BaseActivity.this, CashMovementActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_CASH_MOVEMENT_HISTORY:
					intent = new Intent(BaseActivity.this, CashMovementHistoryListActivity.class);
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
					intent = new Intent(BaseActivity.this, DemoRegistrationActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_LOST_PASSWORD:
					intent = new Intent(BaseActivity.this, LostPasswordActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_IDENTITY_CHECK:
					intent = new Intent(BaseActivity.this, IdentityCheckActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_CUST_LIST:
					intent = new Intent(BaseActivity.this, CustomListActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_TWO_FA:
					intent = new Intent(BaseActivity.this, TwoFAActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_CHANGE_PASSWORD:
					intent = new Intent(BaseActivity.this, ChangePasswordActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
				case ServiceFunction.ACT_GO_TO_CONTRACT_SORT:
					intent = new Intent(BaseActivity.this, ContractListSortActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_ANNOUNCEMENT:
					intent = new Intent(BaseActivity.this, AnnouncementActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_NEWS:
					intent = new Intent(BaseActivity.this, NewsActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_ECONOMIC:
					intent = new Intent(BaseActivity.this, EconomicActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_CONTACTUS:
					intent = new Intent(BaseActivity.this, ContactActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_TERMSNCONDITION:
					intent = new Intent(BaseActivity.this, TermsNConditionActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
					break;
				case ServiceFunction.ACT_GO_TO_LIQUIDATE_ALL:
					intent = new Intent(BaseActivity.this, LiquidateAllActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
				case ServiceFunction.SRV_WEB_VIEW:
					intent = new Intent(BaseActivity.this, WebViewActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
				case ServiceFunction.ACT_GO_TO_PRICE_ALERT:
					intent = new Intent(BaseActivity.this, PriceAlertActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
				case ServiceFunction.ACT_GO_TO_PRICE_ALERT_HISTORY:
					intent = new Intent(BaseActivity.this, PriceAlertHistoryActivity.class);
					intent.putExtras(msg.getData());
					startActivity(intent);
				case ServiceFunction.ACT_GO_TO_NEW_PRICE_ALERT:
					intent = new Intent(BaseActivity.this, NewPriceAlertActivity.class);
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
	 * A handler for handle UI update.
	 */
	protected Handler uiHandler = new Handler();
	/**
	 * Tag name for logging
	 */
	protected static final String TAG = "BaseActivity";
	/**
	 * Application instance
	 */
	protected MobileTraderApplication app;
	/**
	 * Message handler for service
	 */
	protected MessengeHandler handler = new MessengeHandler();
	/**
	 * This class is used to instantiate layout XML file into its corresponding View objects
	 */
	protected LayoutInflater inflater;
	/**
	 * A flag for indicate that is the Activity bound to service or not.
	 */
	protected boolean mBound = false;
	/**
	 * A messenger for sending message to service
	 */
	protected Messenger mService = null;
	/**
	 * A receiver for receiving message from service 
	 */
	protected Messenger mServiceMessengerHandler = new Messenger(handler);
	/**
	 * Class for accessing an application's resources.
	 */
    protected Resources res;
	/**
	 * A flag for indicate the account bar is shown or not.
	 */
	protected boolean bAccShow = false;

    /**
     * A flag for indicate the message bar is shown or not.
     */
    protected boolean bMsgShow = false;
    /**
     * Account bar
     */
    protected RelativeLayout rlAccount = null;
	protected ImageView bg_account = null;
	protected ImageButton btn_statement = null;
	protected ImageButton btn_logout = null;

	/**
	 * Menu
	 */
	protected View rlMenu = null;
	protected RelativeLayout rlTop = null;
	protected ImageView bg_menu = null;
	protected View llMenu = null;


    /**
     * Message bar
     */
    protected ViewGroup rlMsg = null;
    /**
     * A adapter for message listing
     */
    protected SystemMessageListAdapter sysMsgAdapter;
    /**
     * Message listing
     */
    protected ListView lvSysMsg = null;
    /**
     * TextView for display message count
     */
    protected TextView tvSysMsgCount = null;
    /**
     *
     */
    //protected ViewGroupResizeHierarchyChangeListener resizeAnimation;
    /**
     * Original heigh of status bar
     */
    protected int iOrigStatusBarHeight = -1;
    /**
     * Original heigh of account bar
     */
    protected int iOrigAccountBarTopMargin = -1;

	protected int iOrigMenuLeftMargin = -1;
    /**
     * Button for trigger account bar down
     */
    protected View btnAccountDown = null;
    /**
     * Button for trigger account bar up
     */
    protected View btnAccountUp = null;
	protected ImageView btnAccount = null;
    /**
     * Preferences
     */
    protected SharedPreferences setting;
    /**
     * Vibrate controller
     */
    protected Vibrator vibrator;

	static ProgressDialog dialog;

    protected View myButton = null;

    protected Optional<TextView> tvRealizedPNL;
    protected Optional<TextView> tvEffectiveMargin;
    protected Optional<TextView> tvCallMarginAmount;
    protected Optional<TextView> tvTradableMargin;
    protected Optional<TextView> tvNonTradableMargin;
    protected Optional<TextView> tvCreditLimit;
    protected Optional<View> viewCredit;
    protected Optional<TextView> tvCreditLimitNonTradable;
    protected Optional<TextView> tvFreeLot;
    protected Optional<View> viewFreeLot;
    protected Optional<View> viewMarginLevel;
    protected Optional<View> viewCreditLimitNonTradable;

    /**
     * Task for update GUI
     */
	Runnable updateUI = new Runnable(){
		@Override
		public void run() {
			try{
				updateBaseUI();
				updateUI();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};

	/**
	 * Event binding for sub class
	 */
	public abstract void bindEvent();

	/**
	 * Trigger GUI update
	 */
	public void fireUIUpdate(){
        Thread t = new Thread() {
            public void run() {
            	if(uiHandler != null)
            		uiHandler.post(updateUI);
            }
        };
        t.start();
	}

	/**
	 * Trigger vibrate
	 */
	public void fireVibrate(){
		(new Thread(new Runnable(){
			public void run(){
				vibrator.vibrate(MESSAGE_VIBRATE);
			}
		})).start();
	}

	public boolean logonFlag = false;

	/**
	 * Update base GUI, which include tools bar, account bar, message bar.
	 */
	protected void updateBaseUI() {
		try{
			if (CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN&&logonFlag!=app.bLogon){
				logonFlag = app.bLogon;
				initTopBar();
				initBottonBar();
				loginOrLogoutScreenUpdate();
			}

			if(rlAccount != null && app.bLogon){
				BalanceRecord balanceRecord = app.data.getBalanceRecord();
				Consumer<View> setGone = v -> v.setVisibility(View.GONE);
				Consumer<View> setVisible = v -> v.setVisibility(View.VISIBLE);
				switch (balanceRecord.getCreditType()) {
					default:
					case MARGIN:
						viewFreeLot.ifPresent(setGone);
						viewCredit.ifPresent(setGone);
						viewMarginLevel.ifPresent(setVisible);
						viewCreditLimitNonTradable.ifPresent(setGone);
						break;
					case FREE_LOT:
						viewFreeLot.ifPresent(setVisible);
						viewCredit.ifPresent(setGone);
						viewMarginLevel.ifPresent(setGone);
						viewCreditLimitNonTradable.ifPresent(setGone);
						break;
					case CREDIT_LIMIT:
						viewFreeLot.ifPresent(setGone);
						viewCredit.ifPresent(setVisible);
						viewMarginLevel.ifPresent(setVisible);
						viewCreditLimitNonTradable.ifPresent(setVisible);
						break;
				}
				ContractObj contract =  getDefaultContract();
				double[] dBidAsk = contract.getBidAsk();

				StringBuilder sb = new StringBuilder();
				sb.append(Utility.round(dBidAsk[0], contract.iRateDecPt, contract.iRateDecPt));
				sb.append("/");
				sb.append(Utility.round(dBidAsk[1], contract.iRateDecPt, contract.iRateDecPt));

				if (!CompanySettings.newinterface) {
					((TextView) rlAccount.findViewById(R.id.tvUserCurr)).setText(contract.getContractName(app.locale));
					((TextView) rlAccount.findViewById(R.id.tvUserCurrRate)).setText(sb.toString());

					ColorStateList csl = ((TextView) rlAccount.findViewById(R.id.tvUserCurr)).getTextColors();
					if (contract.bChangeBidAsk) {
						ColorController.setPriceColor(res, contract.iBidUpDown, rlAccount.findViewById(R.id.tvUserCurrRate), csl);
					} else {
						ColorController.setPriceColor(res, 0, rlAccount.findViewById(R.id.tvUserCurrRate), csl);
					}
				}


				String sAcc = balanceRecord.strAccount;
				if(CompanySettings.COMPANY_PREFIX!=null&&CompanySettings.COMPANY_PREFIX.length()>0)
					sAcc = sAcc.replace(CompanySettings.COMPANY_PREFIX, "");

				((TextView)rlAccount.findViewById(R.id.tvAcc)).setText(sAcc);
                Optional<TextView> tvAccName = Optional.ofNullable(rlAccount.findViewById(R.id.tvAccName));
				tvAccName.ifPresent(v -> v.setText(balanceRecord.getAccountName()));

				((TextView)rlAccount.findViewById(R.id.tvBalance)).setText(Utility.formatValue(balanceRecord.dBalance));
				ColorController.setNumberColor(res, app.data.getBalanceRecord().dBalance == 0.0 ? null : app.data.getBalanceRecord().dBalance >= 0, rlAccount.findViewById(R.id.tvBalance));

				((TextView)rlAccount.findViewById(R.id.tvFloating)).setText(Utility.formatValue(balanceRecord.dFloating));
				ColorController.setNumberColor(res, balanceRecord.dFloating == 0.0 ? null : balanceRecord.dFloating >= 0, rlAccount.findViewById(R.id.tvFloating));



				//自有總資產值
				double dEquity = balanceRecord.dPartialEquity + balanceRecord.dFloating + balanceRecord.dInterest;
				double dDisplayEquity;
				if (!CompanySettings.DISPLAY_EQUITY_WITH_CREDIT) {
					dDisplayEquity = dEquity - balanceRecord.dCreditLimit;
				} else {
					dDisplayEquity = dEquity;
				}

				((TextView)rlAccount.findViewById(R.id.tvOwnEquity)).setText(Utility.formatValue(dDisplayEquity));
				ColorController.setNumberColor(res, dEquity == 0.0 ? null : dEquity >= 0, rlAccount.findViewById(R.id.tvOwnEquity));

				String sMarginRequire=null;

				if(CompanySettings.ENABLE_CREDIT_RATIO)
				{
					rlAccount.findViewById(R.id.LinearLayout03).setVisibility(View.VISIBLE);
					//可支配金额
					double grantedEquity = dEquity * balanceRecord.creditRatio;
					((TextView)rlAccount.findViewById(R.id.tvEquity)).setText(Utility.formatValue(grantedEquity));
					ColorController.setNumberColor(res, grantedEquity == 0.0 ? null : grantedEquity >= 0, rlAccount.findViewById(R.id.tvEquity));

					//信用額
					double credit = grantedEquity - dEquity;
					((TextView)rlAccount.findViewById(R.id.tvCredit)).setText(Utility.formatValue(credit));
					ColorController.setNumberColor(res, credit == 0.0 ? null : credit >= 0, rlAccount.findViewById(R.id.tvCredit));

					sMarginRequire = Utility.formatValue(balanceRecord.dInitialMargin * balanceRecord.creditRatio);
					((TextView)rlAccount.findViewById(R.id.tvInitialMargin)).setText(sMarginRequire);
					ColorController.setNumberColor(res, app.data.getBalanceRecord().dInitialMargin * app.data.getBalanceRecord().creditRatio == 0.0 ? null : app.data.getBalanceRecord().dInitialMargin * app.data.getBalanceRecord().creditRatio >= 0, rlAccount.findViewById(R.id.tvInitialMargin));

					((TextView)rlAccount.findViewById(R.id.tvUsableMargin)).setText(Utility.formatValue(balanceRecord.dFreeMargin * balanceRecord.creditRatio));
					ColorController.setNumberColor(res, app.data.getBalanceRecord().dFreeMargin * app.data.getBalanceRecord().creditRatio == 0.0 ? null : app.data.getBalanceRecord().dFreeMargin * app.data.getBalanceRecord().creditRatio >= 0, rlAccount.findViewById(R.id.tvUsableMargin));
				}else{
					sMarginRequire = Utility.formatValue(balanceRecord.dInitialMargin);

					((TextView)rlAccount.findViewById(R.id.tvInitialMargin)).setText(sMarginRequire);
					ColorController.setNumberColor(res, app.data.getBalanceRecord().dInitialMargin == 0.0 ? null : app.data.getBalanceRecord().dInitialMargin >= 0, rlAccount.findViewById(R.id.tvInitialMargin));

					((TextView)rlAccount.findViewById(R.id.tvUsableMargin)).setText(Utility.formatValue(balanceRecord.dFreeMargin));
					ColorController.setNumberColor(res, app.data.getBalanceRecord().dFreeMargin == 0.0 ? null : app.data.getBalanceRecord().dFreeMargin >= 0, rlAccount.findViewById(R.id.tvUsableMargin));
				}

				if(rlAccount.findViewById(R.id.tvBalInterest)!=null){
					((TextView)rlAccount.findViewById(R.id.tvBalInterest)).setText(Utility.formatValue(balanceRecord.dFreeMargin * balanceRecord.dbalin));
					ColorController.setNumberColor(res, balanceRecord.dFreeMargin == 0.0 ? null : balanceRecord.dFreeMargin >= 0, rlAccount.findViewById(R.id.tvBalInterest));
				}
				if (rlAccount.findViewById(R.id.tvAccuBalInterest) != null) {
					((TextView) rlAccount.findViewById(R.id.tvAccuBalInterest)).setText(Utility.formatValue(balanceRecord.dabalin + balanceRecord.dFreeMargin * balanceRecord.dbalin));
					ColorController.setNumberColor(res, balanceRecord.dFreeMargin == 0.0 ? null : balanceRecord.dFreeMargin >= 0, rlAccount.findViewById(R.id.tvAccuBalInterest));
				}

				double dMarginRatio = 0;
				int iNetType = balanceRecord.iNetType;
				if ((iNetType == 6 || iNetType == 7)){
					if (balanceRecord.dIMMContractValue > 0) {
						dMarginRatio = Utility.roundToDouble((balanceRecord.getCashValue() / balanceRecord.dIMMContractValue)*100, 2, 2);
					}
				}else {
					if (balanceRecord.dInitialMargin > 0) {
						dMarginRatio = Utility.roundToDouble(balanceRecord.getCashValue() / (balanceRecord.dInitialMargin* balanceRecord.dLeverage)*100, 2, 2);
					}
				}

				ProgressBar pb = rlAccount.findViewById(R.id.pbRate);
				pb.setMax(100);
				pb.setProgress((int) dMarginRatio);

				if("0.00".equals(sMarginRequire)){
					((TextView)rlAccount.findViewById(R.id.tvBuyPower)).setText("-%");
				}else{
					((TextView)rlAccount.findViewById(R.id.tvBuyPower)).setText(dMarginRatio + "%");
				}

				tvCreditLimit.ifPresent(textView -> textView.setText(Utility.formatValue(balanceRecord.dCreditLimit)));
				if(rlAccount.findViewById(Utility.getIdById("tvEquity")) != null) {
					((TextView) rlAccount.findViewById(Utility.getIdById("tvEquity"))).setText(Utility.formatValue(dEquity));
				}
				tvRealizedPNL.ifPresent(textView -> {
					textView.setText(Utility.formatValue(balanceRecord.realizedPNL));
					int compare = balanceRecord.realizedPNL.compareTo(BigDecimal.ZERO);
					ColorController.setNumberColor(res, compare == 0 ? null : compare > 0, textView);
				});
				tvEffectiveMargin.ifPresent(textView -> {
					double effectiveMargin = dDisplayEquity - balanceRecord.dInitialMargin;
					textView.setText(Utility.formatValue(effectiveMargin));
					ColorController.setNumberColor(res, effectiveMargin == 0.0 ? null : effectiveMargin > 0, textView);
				});
				tvCallMarginAmount.ifPresent(textView -> textView.setText(Utility.formatValue(balanceRecord.getCallMargin())));
				tvTradableMargin.ifPresent(textView -> textView.setText(Utility.formatValue(balanceRecord.tradableMargin)));
				tvNonTradableMargin.ifPresent(textView -> textView.setText(Utility.formatValue(balanceRecord.nonTradableMargin)));
				tvCreditLimitNonTradable.ifPresent(textView -> textView.setText(Utility.formatValue(balanceRecord.creditLimitNonTradable)));
				tvFreeLot.ifPresent(t -> t.setText(Utility.formatValue(balanceRecord.freeLot)));

				if (CompanySettings.newinterface) {
					btn_logout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							//Log.e("btnLogout", "onclick");
							if (bAccShow) {
								if (app.bLogon) {
									new AlertDialog.Builder(BaseActivity.this, CompanySettings.alertDialogTheme)
											.setIcon(android.R.drawable.ic_dialog_alert)
											.setTitle(res.getString(R.string.title_information))
											.setMessage(res.getString(R.string.msg_quit))
											.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
													goTo(ServiceFunction.SRV_LOGOUT);
												}

											})
											.setNegativeButton(R.string.no, null)
											.show();
								} else {
									goTo(ServiceFunction.SRV_MOVE_TO_LOGIN);
								}
							}

						}
					});

					btn_statement.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (bAccShow) {
								Intent intent = null;
								intent = new Intent(BaseActivity.this, OnLineStatementActivity.class);
								startActivity(intent);
							}
						}
					});

					bg_account.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							showAccountInformation();
						}
					});
				}
			}
		}catch(Exception e){
			Log.e(TAG, "[Unable to update Account] " + e.getMessage(), e.fillInStackTrace());
		}

		try{
			if(sysMsgAdapter != null){
				sysMsgAdapter.reload(app.data.getSystemMessages(), mService, mServiceMessengerHandler);
				sysMsgAdapter.notifyDataSetChanged();
			}
		}catch(Exception e){
			Log.e(TAG, "[Unable to update system message]", e.fillInStackTrace());
		}

		try{
			if(tvSysMsgCount != null){
				if(app.data.getSystemMessageCount() > 0 && app.data.getSystemMessages().size() > 0) {
					tvSysMsgCount.setText(String.valueOf(app.data.getSystemMessageCount()));
					tvSysMsgCount.setBackgroundResource(R.drawable.sys_no_red);
					fireMessageBarOnClick(true, false);
				}

			}
		}catch(Exception e){
			Log.e(TAG, "[Unable to update system message]", e.fillInStackTrace());
		}
	}

	/**
	 * Bindle tools bar event
	 */
	private void bindToolBarEvent() {
		ImageView btnLogout = findViewById(R.id.btnLogout);
		if (!CompanySettings.newinterface){
			btnLogout.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//Log.e("btnLogout", "onclick");
				if(app.bLogon){
				new AlertDialog.Builder(BaseActivity.this, CompanySettings.alertDialogTheme)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle(res.getString(R.string.title_information))
		        .setMessage(res.getString(R.string.msg_quit))
		        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	goTo(ServiceFunction.SRV_LOGOUT);
		            }

		        })
		        .setNegativeButton(R.string.no, null)
		        .show();}else{
		        	goTo(ServiceFunction.SRV_MOVE_TO_LOGIN);
		        }

				}
        	});
		}

		(findViewById(R.id.btnDash)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (CompanySettings.newinterface) {
					showMenu();
					if (bAccShow) {
						showAccountInformation();
					}
				} else {
					goTo(ServiceFunction.SRV_DASHBOARD);
				}

			}
        });

		//for content mode
		if((CompanySettings.ENABLE_CONTENT || CompanySettings.ENABLE_CONTENT_WEB_VIEW) && !app.bLogon) {
			btnLogout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					goTo(ServiceFunction.SRV_MOVE_TO_LOGIN);
				}
			});
			btnLogout.setImageResource(R.drawable.icon_login);
		}

		View tempV = findViewById(R.id.rlAccount);
        if(tempV != null){
			if (CompanySettings.newinterface) {
				bg_account = findViewById(R.id.bg_account);
				btn_statement = findViewById(R.id.btn_statement);
				btn_logout = findViewById(R.id.btn_logout);
				bg_account.setVisibility(View.INVISIBLE);
				btnAccount = findViewById(R.id.btnAccountTop);
			}
			rlAccount = (RelativeLayout)tempV;
			rlAccount.setVisibility(View.GONE);
			iOrigAccountBarTopMargin =  ((ViewGroup.MarginLayoutParams)rlAccount.getLayoutParams()).topMargin;
        	btnAccountDown = findViewById(R.id.btnAccount);
        	btnAccountUp = findViewById(R.id.btnAccUp);


        	OnClickListener accOnClick = new OnClickListener(){
    			@Override
    			public void onClick(View v) {
    				showAccountInformation();
					if (CompanySettings.newinterface) {
						if (llMenu.getVisibility() == View.VISIBLE) {
							showMenu();
						}
					}
    			}
            };

        	if(CompanySettings.newinterface){
				btnAccountUp.setOnClickListener(accOnClick);
				btnAccount.setOnClickListener(accOnClick);
			}
			else{
				btnAccountDown.setOnClickListener(accOnClick);
				btnAccountUp.setOnClickListener(accOnClick);
				findViewById(R.id.rlTop).setOnClickListener(accOnClick);
			}
        }

		View llBotton = findViewById(R.id.llBotton);

        if(llBotton != null){
        	rlMsg = (ViewGroup) llBotton;
        	//rlMsg.setOnHierarchyChangeListener(resizeAnimation);

        	iOrigStatusBarHeight = ((ViewGroup.MarginLayoutParams)rlMsg.getLayoutParams()).height;

			llBotton.setOnClickListener(v -> fireMessageBarOnClick(false, true));
        }
        //bindBackButtonEvent();
	}

	public void fireMessageBarOnClick(boolean bForce, boolean resetUnreadCount){
		if(bForce && bMsgShow)
			return;

		ViewGroup.MarginLayoutParams param = null;

		Display display = getWindowManager().getDefaultDisplay();
		int height = display.getHeight();

		//System.out.println(bMsgShow);

		Animation am = null;

		if(!bMsgShow){
			param = (ViewGroup.MarginLayoutParams)rlMsg.getLayoutParams();
			param.height = height / CompanySettings.systemMessageViewHeightRatio;
			am = new TranslateAnimation( 0, 0, param.height, 0);
		    am.setDuration(300);
		    am.setInterpolator(new AccelerateInterpolator());
		    rlMsg.setAnimation(am);
		    am.startNow();
		}else{
			param = (ViewGroup.MarginLayoutParams)rlMsg.getLayoutParams();
			param.height = iOrigStatusBarHeight;
			if(tvSysMsgCount != null) {
                tvSysMsgCount.setText("0");
                tvSysMsgCount.setBackgroundResource(R.drawable.sys_no_grey);
            }
			if (resetUnreadCount) {
				app.data.resetSystemMessageCount();
			}
		}

		rlMsg.setLayoutParams(param);

		bMsgShow = !bMsgShow;
	}
	/*
    public void bindBackButtonEvent(){
    	
        findViewById(R.id.btnBack).setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View arg0) {
        		finish();		     
        	}           
        });
      }

	 */
	/**
	 * Bindle to service
	 */
	public void doBindService(){
		if(!mBound){
			//Log.i(SLEEP, this.getClass().getSimpleName() + "@doBindService |"+this+">>>>>>>>>>>>>>>>>>>>>>");
			bindService(new Intent(this, FxMobileTraderService.class), this, Context.BIND_AUTO_CREATE);
		}
	}

	/**
	 * Unbindle from service
	 */
	public void doUnbindService(){
		if(mBound){
			if(mService != null){
				try{
					Message msg = Message.obtain(handler, ServiceFunction.SRV_UNREGISTER_ACTIVITY);
					msg.replyTo = mServiceMessengerHandler;
					mService.send(msg);
				}catch(Exception e){
					Log.e(TAG, "[Unbind service fail]", e.fillInStackTrace());
				}
				//Log.i(SLEEP, this.getClass().getSimpleName() + "@doUnbindService |"+this+">>>>>>>>>>>>>>>>>>>>>>");				
				unbindService(this);
				mBound = false;
			}
		}
	}

	/**
	 * Jump to other activity
	 * @param iService ref. "ServiceFunction"
	 */
    public void goTo(int iService){
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
	 * @param iService
	 *            ref. "ServiceFunction"
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

    /**
     * Let the sub class can handle the message
     * @param msg Message received from service
     */
    public abstract void handleByChild(Message msg);

    /**
     * Init the base GUI
     */
    public void initBaseView() {
    	if (getActivityServiceCode() == app.getDefaultPage()){
			app.firstActivity = this;
		}

    	if(isTopBarExist())
    		initTopBar();

    	if(isBottonBarExist())
    		initBottonBar();

    	if(CompanySettings.SHOW_Advertisement)
    		initAdvertisement();

	}

    /**
     * Is top bar exist in sub class
     * @return Is top bar exist in sub class
     */
    public abstract boolean isTopBarExist();
    /**
     * Is message bar exist in sub class
     * @return Is message bar exist in sub class
     */
    public abstract boolean isBottonBarExist();

    /**
     * Init the message bar
     */
	private void initBottonBar() {
    	sysMsgAdapter = new SystemMessageListAdapter(BaseActivity.this, app.data.getSystemMessages());
    	lvSysMsg = findViewById(R.id.lvMsg);
    	lvSysMsg.setAdapter(sysMsgAdapter);
    	tvSysMsgCount = findViewById(R.id.tvMsgCount);

    	if(showConnected() && app.bLogon){
    		((ToggleButton)findViewById(R.id.tbConnStatus1)).setChecked(true);
    		((ToggleButton)findViewById(R.id.tbConnStatus2)).setChecked(true);
    	}else{
    		((ToggleButton)findViewById(R.id.tbConnStatus1)).setChecked(false);
    		((ToggleButton)findViewById(R.id.tbConnStatus2)).setChecked(false);
    	}

    	reloadPlatformType();

    	if(findViewById(R.id.llShortCut) != null){
    		initNav(findViewById(R.id.llShortCut));
    	}

    	findViewById(R.id.btnSysInfo).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				PopupAboutUS pop = new PopupAboutUS(getApplicationContext(), findViewById(R.id.rlTop));
				pop.showLikeQuickAction();
			}
    	});
	}

	/**
	 * Init navigation bar
	 * @param llNav navigation bar container
	 */
	public void initNav(LinearLayout llNav){
		llNav.removeAllViews();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		 lp.setMargins(0, 0, 4, 0);

		ArrayList<ArrayList<DashboardItem>> alalItem = null;
		if (!app.bLogon) {
			alalItem = new ArrayList<ArrayList<DashboardItem>>();
			alalItem.add(DashboardItemRespository.alItem.get(0));
		} else {
			alalItem = DashboardItemRespository.alItem;
		}
		for(ArrayList<DashboardItem> alItem: alalItem){
			for(DashboardItem item:alItem){
				View v = inflater.inflate(R.layout.nav_item, null);
				v.findViewById(R.id.btnIcon).setBackgroundResource(item.iNavIcon);
				((TextView)v.findViewById(R.id.tvDesc)).setText(item.iNavDesc);

				v.setTag(item);
				v.findViewById(R.id.btnIcon).setTag(item);

				if(this.getServiceId() != item.iService){
					v.setOnClickListener(new OnClickListener(){
	    				@Override
	    				public void onClick(View v) {
	    			    	if (dialog !=null && dialog.isShowing()){
	    			    		return;
	    			    	}
	    			    	if(((DashboardItem)v.getTag()).iService!=ServiceFunction.SRV_SHOW_ACC_INFO&&app.firstActivity != BaseActivity.this)
	    			    		finish();
	    					if ( (app.bQuit || app.data.getContractList().size() == 0) ){
	    						goTo(ServiceFunction.ACT_DISCONNECT);
	    					} else
	    						goTo(((DashboardItem)v.getTag()).iService);
	    				}
	    			});
				}

				llNav.addView(v, lp);
				//System.out.println("my:"+this.getServiceId() +" db:"+ item.iService);
				if(this.getServiceId() == item.iService){
					v.findViewById(R.id.btnIcon).setEnabled(false);
					myButton = v;
				}

				/*
				Button btn = new Button(this);
				btn.setText(res.getText(item.iDesc));
				btn.setTextColor(res.getColor(R.color.normal_text));
				btn.setBackgroundResource(R.drawable.short_cut_btn);
				btn.setPadding(4, 4, 4, 4);
				btn.setTag(item);
    			btn.setOnClickListener(new OnClickListener(){
    				@Override
    				public void onClick(View v) {
    					goTo(((DashboardItem)v.getTag()).iService);
    				}
    			});
				llNav.addView(btn, lp);
				*/
			}
		}

	}

	/**
	 * Update platform type icon
	 */
	public void reloadPlatformType(){
    	TextView tbTmp = findViewById(R.id.vwConnType);

    	if(tbTmp == null)
    		return;

    	if(showPlatformType()&&app.bLogon){
    		tbTmp.setText(!app.isDemoPlatform ? R.string.lb_production_account_long : R.string.lb_practice_account_long);
    		tbTmp.setVisibility(View.VISIBLE);
    	}else{
    		tbTmp.setVisibility(View.INVISIBLE);
    	}
	}

	/**
	 * Init top bar
	 */
	private void initTopBar() {
		rlAccount = findViewById(R.id.rlAccount);
		if (CompanySettings.ENABLE_GRID_LAYOUT_ACCOUNT_BAR) {
			if (rlAccount != null) {
				ViewGroup parent = (ViewGroup) rlAccount.getParent();
				int index = parent.indexOfChild(rlAccount);
				parent.removeView(rlAccount);
				rlAccount = (RelativeLayout) getLayoutInflater().inflate(R.layout.bar_account_grid, parent, false);
				parent.addView(rlAccount, index);
			}
		}
		tvRealizedPNL = Optional.ofNullable(findViewById(R.id.tvRealizedPNL));
		tvEffectiveMargin = Optional.ofNullable(findViewById(R.id.tvEffectiveMargin));
		tvCallMarginAmount = Optional.ofNullable(findViewById(R.id.tvCallMarginAmount));
		tvTradableMargin = Optional.ofNullable(findViewById(R.id.tvTradableMargin));
		tvNonTradableMargin = Optional.ofNullable(findViewById(R.id.tvNonTradableMargin));
		tvCreditLimit = Optional.ofNullable(findViewById(R.id.tvCredit));
		viewCredit = Optional.ofNullable(findViewById(R.id.viewCredit));
		tvCreditLimitNonTradable = Optional.ofNullable(findViewById(R.id.tvCreditLimitNonTradable));
		tvFreeLot = Optional.ofNullable(findViewById(R.id.tvFreeLot));
		viewFreeLot = Optional.ofNullable(findViewById(R.id.viewFreeLot));
		viewMarginLevel = Optional.ofNullable(findViewById(R.id.viewMarginLevel));
		viewCreditLimitNonTradable = Optional.ofNullable(findViewById(R.id.viewCreditLimitNonTradable));
		bindToolBarEvent();

		if (CompanySettings.newinterface) {
			iniMenu();
		}
		else{
			if(showLogout()){
				findViewById(R.id.btnLogout).setVisibility(View.VISIBLE);
			}else{
				findViewById(R.id.btnLogout).setVisibility(View.INVISIBLE);
			}

			if (app.bLogon) {
				if(findViewById(R.id.btnLogout)!=null)
					((Button)findViewById(R.id.btnLogout)).setText(R.string.btn_logout);
			} else {
				if(findViewById(R.id.btnLogout)!=null)
					((Button)findViewById(R.id.btnLogout)).setText(R.string.btn_login);
			}
		}

		if(showTopNav()){
			findViewById(R.id.llNav).setVisibility(View.VISIBLE);

		}else{
			findViewById(R.id.llNav).setVisibility(View.INVISIBLE);
		}

		if (getTitleText() != null) {
			int iID = getTitleLayoutId();
			View vTitle;
			if (iID != -1) {
				if (getTitleLayerId() > 0) {
					vTitle = inflater.inflate(iID, null);

					((TextView) vTitle.findViewById(getTitleText()[0]))
							.setText(res.getText(getTitleText()[1]));

					RelativeLayout rlTitle = findViewById(getTitleLayerId());

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							(int) getResources().getDimension(getTitleHeight()));
					if (rlTitle != null) {
						rlTitle.removeAllViews();
						rlTitle.addView(vTitle, lp);
					}
				}
			}
		}

		if (CompanySettings.newinterface) {
			bg_menu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
						showMenu();
				}
			});
		}
	}

	public void setupMenu (int menu){
		ArrayList db = DashboardItemRespository.menuItem.get(menu);
		ArrayList<LinearLayout> llItem = new ArrayList<LinearLayout>(db.size());
		final float scale = getResources().getDisplayMetrics().density;

		for (int i = 0 ; i < db.size() ; i ++) {
			final DashboardItem dItem = (DashboardItem) db.get(i);
			LinearLayout temp = new LinearLayout(this);
			temp.setOrientation(LinearLayout.VERTICAL);
			temp.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					(int) (50 * scale)));
			temp.setBackgroundResource(R.drawable.bg_sub_nav);
			temp.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
					ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
					//ComponentName cn = am.getRunningAppProcesses().get(0).importanceReasonComponent;
					String currentActivity = cn.getShortClassName().replace(".","");
					currentActivity = currentActivity.replace("commfinanceandroidapp", "");
					System.out.println(" currentActivity "+currentActivity + " dItem.iActivityName " + dItem.iActivityName);
					if(currentActivity.equalsIgnoreCase(dItem.iActivityName)) {
						//closeAllMenu();
						//bMenuShow=true;
						showMenu();
						if (currentActivity.equalsIgnoreCase("OrderActivity") && app.getSelectedOpenPosition() != null) {
							finish();
							goTo(dItem.iService);
						}
					}else if(currentActivity.equalsIgnoreCase("DealActivity") || currentActivity.equalsIgnoreCase("OrderActivity") ||
							currentActivity.equalsIgnoreCase("LiquidationActivity") || currentActivity.equalsIgnoreCase("EditOrderActivity")) {
							finish();
							goTo(dItem.iService);
                    }else{
                            goTo(dItem.iService);
                    }
				}
			});
			//Text
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(33 * scale));
			params.setMargins((int) (70 * scale),(int) (15 * scale),0,0);
			TextView text = new TextView(this);
			text.setText(dItem.iDesc);
			text.setLayoutParams(params);
			text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
			text.setTextColor(getResources().getColor(R.color.wheel_text));
			//Text
        	//Line
			LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(2 * scale));
			ImageView img = new ImageView(this);
			img.setImageResource(R.drawable.bg_splitter);
			img.setLayoutParams(params1);
			img.setScaleType(ImageView.ScaleType.FIT_XY);

			//Line
			temp.addView(text);
			temp.addView(img);

			llItem.add(temp);
		}
	}

	/**
	 * Let the sub class init it own GUI
	 */
	public abstract void loadLayout();


	protected Consumer<Locale> changeLocale = l -> {};
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
        changeLocale = localeContextWrapper::changeLocale;
        super.attachBaseContext(localeContextWrapper);
	}


	private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (preferences, key) -> {
		if (key.equals("LANGUAGE")) {
			Locale locale = LocaleUtility.getLanguage(preferences);
			changeLocale.accept(locale);
		}
	};
	@Override
    public void onCreate(Bundle savedInstanceState) {
		release=Double.parseDouble(Build.VERSION.RELEASE.replaceAll("(\\d+[.]\\d+)(.*)","$1"));
		//System.out.println("Create "+this.getClass().getSimpleName());
		//Log.i(SLEEP, this.getClass().getSimpleName() +"@onCreate |"+this+">>>>>>>>>>>>>>>>>>>>>>");
		app = (MobileTraderApplication)getApplicationContext();
		res = this.getResources();
		setting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		setting.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

		super.onCreate(savedInstanceState);
		Pushy.listen(this);

		if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
		}

		try {
			String deviceToken = Pushy.register(this);
			app.data.setPushyToken(deviceToken);
		} catch (Exception e){
			Log.e(TAG, e.toString());
		}

		if (!isServiceRunning()){
			//Log.i(SLEEP, this.getClass().getSimpleName() + " @ startService |"+this+">>>>>>>>>>>>>>>>>>>>>>");
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				startForegroundService(new Intent(this, FxMobileTraderService.class));
			} else {
				startService(new Intent(this, FxMobileTraderService.class));
			}
		}


		inflater = getLayoutInflater();

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

		app.bPostMsgToFacebook = this.isPostFacebookEnable();

		loadLayout();
		bindEvent();

		initBaseView();
	
		/*
		System.out.println("Create "+this.getClass().getSimpleName());
		setting = getSharedPreferences(PREF_SETTING, 0);
		app = (MobileTraderApplication)getApplicationContext();
		app.changeLocale(app.locale);
		
		super.onCreate(savedInstanceState);
		
        startService(new Intent(this, FxMobileTraderService.class));
		res = getResources();
		inflater = getLayoutInflater();

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		vibrator = (Vibrator)getSystemService("vibrator");
		
		loadLayout();
		bindEvent();
		
		initBaseView();
		*/

    }

	private void refresh() {
		finish();
    	Intent i = new Intent(getApplicationContext(), this.getClass());
    	this.startActivity(i);
	}

	private boolean isChangingActivity = false;
	private Locale onPauseLocale = null;
	@Override
	protected void onPause() {
		if( !isInitialActivity() )
		{
			if(CompanySettings.ENABLE_CONTENT){
				//goTo(ServiceFunction.SRV_STOP_XML_TRANSFER);
				MobileTraderApplication.isBackground = true;
			}

			if (counter != null)
				counter.cancel();

			if( app.bLogon )
			{
				backgroundthread = true;
				app.stopTimeout();
				app.startTimeout(3*60*1000, this);
			}
		}

		isChangingActivity = false;
		//Log.i(SLEEP, this.getClass().getSimpleName() + "@onPause |"+this+">>>>>>>>>>>>>>>>>>>>>>");
		super.onPause();
		doUnbindService();
	}

	@Override
	protected void onResume() {
		isChangingActivity = false;
		//Log.i(SLEEP, this.getClass().getSimpleName() + "@onResume |"+this+">>>>>>>>>>>>>>>>>>>>>>");
		super.onResume();

		if(app.bQuit){

			finish();
		}else{

			if(bMsgShow){
				this.fireMessageBarOnClick(false, false);
			}

			reloadPlatformType();

			doBindService();


			uiHandler.postDelayed(new Runnable(){
				@Override
				public void run() {
					HorizontalScrollView hsv = findViewById(R.id.hsvNav);
					if(myButton != null && hsv != null){
						int iX = myButton.getLeft();
						hsv.smoothScrollTo(iX, hsv.getScrollY());
					}
				}
			}, 100);

			if (getLoadingViewId() > 0 )
				(new Thread(loadDate)).start();

			fireUIUpdate();
		}

	}

	Runnable loadDate = new Runnable(){
		@Override
		public void run() {
			try {
				visiableLoading();
				while(!isLoadedData()){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				inVisiableLoading();
				uiHandler.post(loadUIDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	Runnable loadUIDate = new Runnable() {
		public void run() {
			loadUIData();
		}
	};

	public void visiableLoading(){
		runOnUiThread(new Runnable(){
			@Override
			public void run() {
				if (getLoadingViewId() > 0 && findViewById(getLoadingViewId()) != null )
					findViewById(getLoadingViewId()).setVisibility(View.VISIBLE);
			}
		});
	}

	public void inVisiableLoading(){
		runOnUiThread(new Runnable(){
			@Override
			public void run() {
				if (getLoadingViewId() > 0 && findViewById(getLoadingViewId()) != null )
					findViewById(getLoadingViewId()).setVisibility(View.INVISIBLE);
			}
		});
	}

	private void resetTimeout(){
		app.stopTimeout();
		long lTimeout = getTimeout();
		long alTimeout = 0;
		if (app.data.sessTimeoutDisconn != -1){
			lTimeout = app.data.sessTimeoutDisconn*1000;
			alTimeout = app.data.sessTimeoutAlert*1000;
		}
		//System.out.println(lTimeout);
		backgroundthread = false;
		if(lTimeout > 0 )
			app.startTimeout(lTimeout, BaseActivity.this);
		if(alTimeout > 0 )
			app.startAlertTimeout(alTimeout, BaseActivity.this);
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {

		mService = new Messenger(service);
		mBound = true;

		try{
			Bundle data = new Bundle();
			Bundle extras = getIntent().getExtras();
			if (extras == null) {
				extras = new Bundle();
			}
			data.putBoolean(ServiceFunction.IS_LOGIN, isLoginActivity() || isInitialActivity() || isDemoRegistrationActivity() || isLostPasswordActivity() || isIdentityCheckActivity() );
			data.putBoolean(ServiceFunction.REQUIRE_LOGIN, extras.getBoolean(ServiceFunction.REQUIRE_LOGIN, true));
			Message msg = Message.obtain(handler, ServiceFunction.SRV_REGISTER_ACTIVITY);
			msg.setData(data);
			msg.replyTo = mServiceMessengerHandler;
			mService.send(msg);
		}catch(Exception e){
			Log.e(TAG, "Bind service fail", e.fillInStackTrace());
		}
		if(app.bLogon){
			if(app.isTimeout()){
				this.goTo(ServiceFunction.SRV_LOGOUT);
			}

			app.stopTimeout();
			long lTimeout = getTimeout();
			long alTimeout = 0;
			if (app.data.sessTimeoutDisconn != -1){
				lTimeout = app.data.sessTimeoutDisconn*1000;
				alTimeout = app.data.sessTimeoutAlert*1000;
			}
			//System.out.println(lTimeout);
			backgroundthread = false;
			if(lTimeout > 0 )
				app.startTimeout(lTimeout, this);
			if(alTimeout > 0 )
				app.startAlertTimeout(alTimeout, this);

		}else{
			app.stopTimeout();
		}

		if(CompanySettings.ENABLE_CONTENT){
			if( !isInitialActivity() )
			{
				//goTo(ServiceFunction.SRV_START_XML_TRANSFER);
				MobileTraderApplication.isBackground = false;
			}
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		mService = null;
	}

	/**
	 * Get default number of LOT
	 * @return number of LOT
	 */
	public void setDefaultLOT(String sLot){
		SharedPreferences.Editor editor = setting.edit();
		editor.putString("DEFAULT_LOT", sLot);
		editor.commit();
	}

	public String getDefaultLOT(){
		if (CompanySettings.ENABLE_CONTRACT_LEVEL_SETTING){
			String contractString = Optional.ofNullable(app.getSelectedContract()).map(c -> c.strContractCode).orElse(CompanySettings.getDefaultDefaultContract(app));
			ContractDefaultSetting contractDefaultSetting = app.data.getContractDefaultSettingMap().getOrDefault(contractString, Utility.EMPTY_CONTRACT_DEFAULT_SETTING);
			return Utility.getDecimalFormatLotSize().format(contractDefaultSetting.getDefaultLotSize());
		}else {
			double dMinLotLimit = 0.1;
			double dMinLotIncrementUnit = 0.1;
			try {
				dMinLotLimit = app.data.getBalanceRecord().dMinLotLimit;
				//Log.e("dMinLotLimit", String.valueOf(dMinLotLimit));
			} catch (Exception e) {
				dMinLotLimit = 0.1;
				//Log.e("dMinLotLimit", String.valueOf(dMinLotLimit));
			}

			try {
				dMinLotIncrementUnit = app.data.getBalanceRecord().dMinLotIncrementUnit;
				//Log.e("dMinLotIncrementUnit", String.valueOf(dMinLotIncrementUnit));
			} catch (Exception e) {
				dMinLotIncrementUnit = 0.1;
				//Log.e("dMinLotIncrementUnit", String.valueOf(dMinLotIncrementUnit));
			}
			String output = Utility.formatInputLot(Double.valueOf(setting.getString("DEFAULT_LOT", CompanySettings.DEFAULT_LOT_SIZE != null ? CompanySettings.DEFAULT_LOT_SIZE : String.valueOf(getMinLOT(dMinLotLimit, dMinLotIncrementUnit)))));
			return output;
		}
	}

	public double getMinLOT(double dMinLotLimit, double dMinLotIncrementUnit){
		boolean notfound=true;
		int count=1;
		double result = 0;
		while (notfound){
			result = dMinLotIncrementUnit * count ;
			if (result >= dMinLotLimit){
				//System.out.println("found la");
				notfound = false;
			} else {
				count++;
			}
		}
		return result;
	}

	/**
	 * Set default Contract to memory and preference
	 * @param sContract contract
	 */
	public void setDefaultContract(String sContract){
		SharedPreferences.Editor editor = setting.edit();
	    editor.putString("DEFAULT_CONTRACT", sContract);
	    app.setDefaultContract(sContract);
		editor.apply();
	}

	/**
	 * Get default Contract
	 * @return Default contract
	 */
	public ContractObj getDefaultContract(){
	    String sContract = setting.getString("DEFAULT_CONTRACT",  CompanySettings.getDefaultDefaultContract(app));
	    if (sContract==null)
	    	return null;
	    else {
	    	if( app.bLogon )
	    	{
	    		// Restrict user set non-tradable contract as default
	    		ContractObj c = app.data.getContract(sContract);
	    		if(c==null || c.isViewable() == false || DataRepository.getInstance().getTradableContract().contains(sContract) == false )
	    		{
	    			for( int i = 0; i < DataRepository.getInstance().getTradableContract().size(); i++)
	    			{
	    				sContract = DataRepository.getInstance().getTradableContract().get(i);
	    				if( app.data.getContract(sContract).isViewable() )
	    					break;
	    			}

	    		}
	    	}
		    app.setDefaultContract(sContract);
		    return app.data.getContract(sContract);
	    }
	}

	/**
	 * Enable or disable one click function
	 * @param isChecked true = enable, false = disable
	 */
	protected void setOneClickTradeEnable(boolean isChecked) {
		SharedPreferences.Editor editor = setting.edit();
	    editor.putBoolean("ONE_CLICK_TRADE_ENABLE", isChecked);
		editor.apply();
	}

	/**
	 * Enable or disable post to facebook
	 * @param isChecked true = enable, false = disable
	 */
	protected void setPostFacebookEnable(boolean isChecked) {
		SharedPreferences.Editor editor = setting.edit();
		app.bPostMsgToFacebook = isChecked;
	    editor.putBoolean("POST_FACEBOOK_ENABLE", isChecked);
		editor.apply();
	}

	public void setLanguage(String sLanguage) {
		SharedPreferences.Editor editor = setting.edit();
	    editor.putString("LANGUAGE", sLanguage);
		editor.apply();
		editor.commit();
	}

	public Locale getLanguage() {
		return LocaleUtility.getLanguage(setting);
	}

	private String getDeviceLanguage(){
		return LocaleUtility.getDeviceLanguage();
	}

	public void setTimeout(long lTimeout) {
		SharedPreferences.Editor editor = setting.edit();
	    editor.putLong("TIMEOUT", lTimeout);
		editor.commit();
	}

	public long getTimeout() {
		return setting.getLong("TIMEOUT", -1);
	}

	public String getLanguageName() {
		return setting.getString("LANGUAGE", getDeviceLanguage());
	}

	/**
	 * Get next locale 
	 * @return locale
	 */
	public String getNextLocale(){
		return setting.getString("NEXT_LOCALE", null);
	}

	/**
	 * Set next local to memory and preference
	 * @param sLocale
	 */
	public void setNextLocale(String sLocale){
		SharedPreferences.Editor editor = setting.edit();
	    editor.putString("NEXT_LOCALE", sLocale);
		editor.commit();
	}

	//自選合約排序
	public void setContractSequence(String sSeq){
		SharedPreferences.Editor editor = setting.edit();
	    editor.putString("CONTRACT_SEQUENCE", sSeq);
	    app.setDefaultPage(sSeq);
		editor.commit();
	}

	public String getContractSequence(){
		String sSeq = setting.getString("CONTRACT_SEQUENCE", null);
		app.setDefaultPage(sSeq);
		return sSeq;
	}

	/**
	 * Is one click enable
	 * @return true = enable, false = disable
	 */
	protected boolean isOneClickTradeEnable() {
		return setting.getBoolean("ONE_CLICK_TRADE_ENABLE", false);
	}

	/**
	 * Is post facebook enable
	 * @return true = enable, false = disable
	 */
	protected boolean isPostFacebookEnable() {
		return setting.getBoolean("POST_FACEBOOK_ENABLE", false);
	}

	/**
	 * Let the sub class update GUI
	 */
	public abstract void updateUI();
	/**
	 * Is logout button shown in sub class
	 * @return
	 */
	public abstract boolean showLogout();
	/**
	 * Is top button shown in sub class
	 * @return
	 */
	public abstract boolean showTopNav();
	/**
	 * Is connection icon shown in sub class
	 * @return
	 */
	public abstract boolean showConnected();
	/**
	 * Is platform type icon shown in sub class
	 * @return
	 */
	public abstract boolean showPlatformType();

	@Override
	public void timeout(){
		if (backgroundthread){
			logout();
		}
		else {
			if (app.data.timeoutAlert) {
				app.data.timeoutAlert = false;
				handler.post(showAlertMessage);
			} else
				handler.post(showMessage);
		}
	}

	private void logout(){
		this.goTo(ServiceFunction.SRV_LOGOUT);
	}

	private AlertDialog alertAlert = null;

	private Runnable showAlertMessage = new Runnable() {
		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this, CompanySettings.alertDialogTheme);
					builder.setMessage(res.getText(R.string.lb_alert_time_out))
							.setCancelable(false)
							.setPositiveButton(res.getText(R.string.btn_ok), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									resetTimeout();
								}
							});
					alertAlert = builder.create();
					alertAlert.show();
				}
			});
		}
	};

	private CountDownTimer counter = null;

	private Runnable showMessage = new Runnable() {
		@Override
		public void run() {
			if (alertAlert != null) {
				alertAlert.dismiss();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this, CompanySettings.alertDialogTheme);
						builder.setMessage(res.getText(R.string.lb_time_out))
								.setCancelable(false)
								.setPositiveButton(res.getText(R.string.btn_ok), new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										counter.cancel();
										resetTimeout();
									}
								});
						final AlertDialog alert = builder.create();
						alert.show();
						counter = new CountDownTimer(10000, 1000) {
							@Override
							public void onTick(long l) {
								final Button defaultButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
								defaultButton.setText(String.format(
										app.locale, "%s (%d)",
										res.getText(R.string.btn_ok),
										TimeUnit.MILLISECONDS.toSeconds(l) + 1 //add one so it never displays zero
								));
							}

							@Override
							public void onFinish() {
								goTo(ServiceFunction.SRV_LOGOUT);
							}
						}.start();
					}
				});
			}
		}
	};

	public int getServiceId(){
		return -1;
	}

	public int getActivityServiceCode(){
		return -1;
	}

	@Override
	protected void onDestroy() {
		//Log.i(SLEEP, this.getClass().getSimpleName()  + "@onDestroy |"+this+">>>>>>>>>>>>>>>>>>>>>>");
		super.onDestroy();
		setting.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
		unbindDrawables(findViewById(R.id.rlMain));

		uiHandler = null;
		handler = null;
		rlAccount = null;
		inflater = null;
		res = null;
		vibrator = null;

		System.gc();
	}

	protected void unbindDrawables(View view) {
			view.destroyDrawingCache();

	        if (view.getBackground() != null) {
	        	view.getBackground().setCallback(null);
	        }else{
	        }

       		if(view.getDrawingCache() != null){
       			view.getDrawingCache().recycle();
       		}else{
       		}

	        if (view instanceof ViewGroup) {
	            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
	            	unbindDrawables(((ViewGroup) view).getChildAt(i));
	            }
	            if(!(view instanceof AdapterView))
	            	((ViewGroup) view).removeAllViews();
	        }else if (view instanceof ImageView) {
	        	ImageView iv = (ImageView)view;

	        	if(iv.getDrawable() != null){
	        		iv.getDrawable().setCallback(null);
	        	}
	        	if(iv.getDrawingCache() != null){
	        		iv.getDrawingCache().recycle();
	        	}
	        }else if(view instanceof WheelView){
	        	((WheelView)view).setCallBackToNull();
	        }else if(view instanceof ImageZoomView){
	        	((ImageZoomView)view).recycle();
	        }else if(view instanceof WebView){
	        	//Log.i("unbindDrawables", "Web View setCallBackTONull-----------------------");
	        	((WebView) view).destroy();
	        }
	}

	@Override
	public void onBackPressed() {
		//System.out.println("------------------------------"+app);
        if (app.firstActivity == this || !app.bLogon && isTaskRoot()) {
    		new AlertDialog.Builder(this, CompanySettings.alertDialogTheme)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(res.getString(R.string.title_information))
            .setMessage(res.getString(R.string.msg_quit))
            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                	if(app.bLogon) {
						goTo(ServiceFunction.SRV_LOGOUT);
					} else {
                		finish();
					}
                }
            })
            .setNegativeButton(R.string.no, null)
            .show();
        } else {
			super.onBackPressed();
		}
	}

	public boolean isLoginActivity(){
		return false;
	}

	private boolean isServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

	        if ("com.mfinance.everjoy.app.service.FxMobileTraderService".equals(service.service.getClassName())
	        		&&service.service.getPackageName().equals(this.getPackageName())) {
	            return true;
	        }
	    }
	    return false;
	}

	public boolean isDemoRegistrationActivity(){
		return false;
	}

	public boolean isLostPasswordActivity(){
		return false;
	}

	public boolean isInitialActivity(){
		return false;
	}

	public boolean isIdentityCheckActivity(){
		return false;
	}

	public boolean isLoadedData(){
		return true;
	}

	public int getLoadingViewId() {
		return -1;
	}

	public void loadUIData(){
		//
	}

	public int[] getTitleText(){return null;};


	public int getTitleLayoutId(){
		return R.layout.bar_title;
	}


	public int getTitleLayerId(){
		return R.id.rlTitle;
	}


	public int getTitleHeight(){
		return R.dimen.bar_title_height;
	}

	public void setNextMasterIndex(){
		( app).setSelectedMasterIndex(( app).getSelectedMasterIndex()+1);
	}

	public void setPreMasterIndex(){
		( app).setSelectedMasterIndex(( app).getSelectedMasterIndex()-1);
	}

	public void setNextStrategyIndex(){
		( app).setSelectedStrategyIndex(( app).getSelectedStrategyIndex()+1);
	}

	public void setPreStrategyIndex(){
		( app).setSelectedStrategyIndex(( app).getSelectedStrategyIndex()-1);
	}

	public int getDefaultRefreshGeneralInfo() {
		int iTime = setting.getInt(
				MobileTraderApplication.DEFAULT_REFRESH_GENERAL_INFO_KEY, 5);
		(app).setDefaultRefreshGeneralInfo(iTime);
		return iTime;
	}

	public void setDefaultRefreshGeneralInfo(int iTime) {
		SharedPreferences.Editor editor = setting.edit();
		editor.putInt(MobileTraderApplication.DEFAULT_REFRESH_GENERAL_INFO_KEY,
				iTime);
		app.setDefaultRefreshGeneralInfo(iTime);
		editor.commit();
	}

	public void loginOrLogoutScreenUpdate(){

	}

	public void showAccountInformation() {
		final boolean bAccShowFinal = bAccShow;
		ViewGroup.MarginLayoutParams param = null;
		Animation am = null;
		if (!bAccShowFinal) {
			param = (ViewGroup.MarginLayoutParams) rlAccount.getLayoutParams();
			param.topMargin = 0;
			if (CompanySettings.newinterface)
				bg_account.setVisibility(View.VISIBLE);
			else
				btnAccountDown.setVisibility(View.INVISIBLE);
			am = new TranslateAnimation(0, 0, iOrigAccountBarTopMargin, 0);
		} else {
			param = (ViewGroup.MarginLayoutParams) rlAccount.getLayoutParams();
			param.topMargin = iOrigAccountBarTopMargin;
			if (CompanySettings.newinterface)
				bg_account.setVisibility(View.INVISIBLE);
			else
				btnAccountDown.setVisibility(View.VISIBLE);
			am = new TranslateAnimation(0, 0, iOrigAccountBarTopMargin * -1, 0);
		}
		rlAccount.setVisibility(View.VISIBLE);
		am.setDuration(300);
		am.setInterpolator(new AccelerateInterpolator());
		am.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				rlAccount.setVisibility(bAccShowFinal ? View.VISIBLE : View.GONE);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				rlAccount.setVisibility(!bAccShowFinal ? View.VISIBLE : View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		rlAccount.setAnimation(am);
		am.startNow();

		rlAccount.setLayoutParams(param);
		bAccShow = !bAccShow;
	}

	public Advertisement getRandomAdv(){
		try {
			if (((app).data.getAdvertisements().getAdvertisementList()) ==null) {
				AdvertisementDao advertisementDao = new AdvertisementDao(app);
				advertisementDao.updateXML();
			}
			Random r = new Random();
			int i =	r.nextInt((app).data.getAdvertisements().getAdvertisementList().size()-1);
			return (app).data.getAdvertisements().getAdvertisementList().get(i);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	Advertisement adv;
	WebView wv;
	private String url;
	public void initAdvertisement(){
		wv = findViewById(R.id.wvAdv);
		if (wv != null) {
		 	findViewById(R.id.lladv).setVisibility(View.VISIBLE);
		 	advTimer = new Timer();

		 	new Task().run();

			//It is setting webview will be fix the screen size
			WebSettings settings = wv.getSettings();
			settings.setUseWideViewPort(true);
			settings.setLoadWithOverviewMode(true);

			wv.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (adv.getTarget()!=null) {
					    Intent i = new Intent(Intent.ACTION_VIEW);
					    i.setData(Uri.parse(adv.getTarget()));
					    startActivity(i);
					}
					return false;
				}

			});
		}
	}

	private void showSystemAlertMessage(){
		new AlertDialog.Builder(BaseActivity.this, CompanySettings.alertDialogTheme)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(res.getString(R.string.title_information))
				.setMessage(app.data.getSystemAlertMessage())
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {	}
				})
				.show();
	}

	class Task extends TimerTask
	{
		@Override
 		public void run()
 		{
			Log.i("Timer", "countdown");
 			View v=findViewById(R.id.lladv);
 			if(v!=null)
 			{
 				//	v.setVisibility(View.GONE);
 				adv = getRandomAdv();
 				wv = findViewById(R.id.wvAdv);
 				if (adv != null && wv != null) {
 					if (Utility.isSimplifiedChineses())
 						url = adv.getUrl_gb();
 					else if (Utility.isSimplifiedChineses())
 						url = adv.getUrl_big5();
 					else
 						url = adv.getUrl_en();
 					runOnUiThread(new Runnable(){
 						public void run()
 						{
 							wv.loadDataWithBaseURL(null, "<html><head><meta name=\"viewport\" content=\"width=device-width, height=device-height , initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no\"> </head>" +
 		 							"<body style=\"margin:0; background-color:#000000;\"><center><img src=\""+url+"\" height=\"100%%\" /></center></body>"+
 		 							"</html>"
 		 							, mimetype, encoding, null);
 						}
 					});
 				}
 				if( adv == null )
 					advTimer.schedule(new Task(), 5 * 1000);
 				else
 					advTimer.schedule(new Task(), Long.parseLong(adv.getTimer()) * 1000);
 			}
 		}
	}


	@Override
	public void startActivity(Intent intent) {
		if (isChangingActivity)
			return;
		isChangingActivity = true;
		super.startActivity(intent);
	}

	public void iniMenu(){
		ArrayList<Menu> menuList = new ArrayList<>();
		ArrayList<Menu> subMenuList = new ArrayList<>();
		ArrayList<Menu> subMenuListAll = new ArrayList<>();
		BalanceRecord balanceRecord = app.data.getBalanceRecord();

		if (CompanySettings.ENABLE_CONTENT_WEB_VIEW) {
			Bundle b = new Bundle();
			b.putBoolean("guest", !app.bLogon);
			subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_price_list),ServiceFunction.SRV_PRICE, app.bLogon ? ContractListActivity.class.getSimpleName() : ContractListGuestActivity.class.getSimpleName(),null, app.bLogon,false, b));
			subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_price_summary),ServiceFunction.SRV_CONTRACT_DETAIL, ContractDetailActivity.class.getSimpleName(),null, app.bLogon,false, b));
			if (CompanySettings.ENABLE_PRICE_ALERT){
				subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_price_alert),ServiceFunction.SRV_PRICE_ALERT, PriceAlertActivity.class.getSimpleName(),null, true,false));
				subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_price_alert_history),ServiceFunction.SRV_PRICE_ALERT_HISTORY, PriceAlertHistoryActivity.class.getSimpleName(),null, true,false));
			}
			menuList.add(new Menu(findViewById(R.id.llPriceMenu),0, null, subMenuList, false,false));
			subMenuListAll.addAll(subMenuList);
		} else {
			Bundle b = new Bundle();
			b.putBoolean("guest", false);
			subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_price_list),ServiceFunction.SRV_PRICE, "ContractListActivity",null,true,false, b));
			subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_price_summary),ServiceFunction.SRV_CONTRACT_DETAIL, "ContractDetailActivity",null,true,false));
			if (CompanySettings.ENABLE_PRICE_ALERT){
				subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_price_alert),ServiceFunction.SRV_PRICE_ALERT, "PriceAlertActivity",null,true,false));
				subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_price_alert_history),ServiceFunction.SRV_PRICE_ALERT_HISTORY, "PriceAlertHistoryActivity",null,true,false));
			}
			menuList.add(new Menu(findViewById(R.id.llPriceMenu),0, null, subMenuList, true,false));
			subMenuListAll.addAll(subMenuList);
		}




		subMenuList = new ArrayList<>();
		subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_new_position),ServiceFunction.SRV_DEAL, "DealActivity",null,true,false));
		subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_new_order),ServiceFunction.SRV_ORDER, "OrderActivity",null,true,false));
		subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_pending_order_listing),ServiceFunction.SRV_RUNNING_ORDER, "RunningOrderListActivity",null,true,false));
		menuList.add(new Menu(findViewById(R.id.llTradeMenu),0, null, subMenuList,true,false));
		subMenuListAll.addAll(subMenuList);

		subMenuList = new ArrayList<>();
		subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_open_position_listing),ServiceFunction.SRV_OPEN_POSITION, "OpenPositionListActivity",null,true,false));
		subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_open_position_summary),ServiceFunction.SRV_OPEN_POSITION_SUMMARY, "OpenPositionSummaryActivity",null,true,false));
		menuList.add(new Menu(findViewById(R.id.llPositionMenu),0, null, subMenuList,true,false));
		subMenuListAll.addAll(subMenuList);

		subMenuList = new ArrayList<>();
		subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_transaction_status),ServiceFunction.SRV_TRANSACTION, "TransactionListActivity",null,true,false));
		subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_liquidation_history),ServiceFunction.SRV_LIQUIDATION_HISTORY, "LiquidationHistoryListActivity",null,true,false));
		subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_executed_order_history),ServiceFunction.SRV_EXECUTED_ORDER, "ExecutedOrderListActivity",null,true,false));
		subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_cancelled_order_history),ServiceFunction.SRV_CANCELLED_ORDER, "CancelledOrderListActivity",null,true,false));
		menuList.add(new Menu(findViewById(R.id.llHistoryMenu),0, null, subMenuList,true,false));

		subMenuListAll.addAll(subMenuList);

		subMenuList = new ArrayList<>();
		if (CompanySettings.ENABLE_WITHDRAWAL_HISTORY) {
            subMenuList.add(new Menu(findViewById(R.id.sub_menu_cash_movement_history),ServiceFunction.SRV_CASH_MOVEMENT_HISTORY, "CashMovementHistoryListActivity",null,true,false));
        }
		if (balanceRecord != null && balanceRecord.allowWithdrawal) {
			Bundle bundle = new Bundle();
			bundle.putInt("isDeposit", 0);
			bundle.putInt("menuServiceID", MenuServiceID.WITHDRAWAL.getValue());
			subMenuList.add(new Menu(findViewById(R.id.sub_menu_withdrawal),ServiceFunction.SRV_CASH_MOVEMENT, "CashMovementActivity",null,true,false, bundle));
		}
		if (balanceRecord != null && balanceRecord.allowDeposit) {
			Bundle bundleDeposit = new Bundle();
			bundleDeposit.putInt("isDeposit", 1);
			bundleDeposit.putInt("menuServiceID", MenuServiceID.DEPOSIT.getValue());
			subMenuList.add(new Menu(findViewById(R.id.sub_menu_deposit),ServiceFunction.SRV_CASH_MOVEMENT, "CashMovementActivity",null,true,false, bundleDeposit));
		}
		if (subMenuList.size() > 0) {
//            menuList.add(new Menu(findViewById(R.id.llFundMenu),0, null, subMenuList,true,false));
//            subMenuListAll.addAll(subMenuList);
        }
		if (CompanySettings.ENABLE_CONTENT_WEB_VIEW) {
			Bundle b = new Bundle();
			b.putString("title", getString(R.string.menu_announcement));
			Uri content;
			Locale language = getLanguage();
			if (language.equals(Locale.TRADITIONAL_CHINESE)) {
				content = app.announcementUriTC;
			} else if (language.equals(Locale.SIMPLIFIED_CHINESE)) {
				content = app.announcementUriSC;
			} else {
				content = app.announcementUri;
			}
			b.putString("content", content.toString());
			b.putInt("menuServiceID", MenuServiceID.ANNOUNCEMENT.getValue());
			menuList.add(new Menu(findViewById(R.id.llAnnounceMenu), ServiceFunction.SRV_WEB_VIEW, WebViewActivity.class.getSimpleName(), null, false, false, b));
		}
		if (CompanySettings.ENABLE_SYSTEM_MESSAGE_FROM_FXSERVER) {
            menuList.add(new Menu(findViewById(R.id.llSystemMessage),ServiceFunction.SRV_ANNOUNCEMENT, "AnnouncementActivity",null,true,false));
        }
        if (!CompanySettings.ENABLE_CONTENT_WEB_VIEW) {
			Bundle b = new Bundle();
			b.putString("title", getString(R.string.menu_news));
			Uri content;
			Locale language = getLanguage();
			if (language.equals(Locale.TRADITIONAL_CHINESE)) {
				content = app.newsUriTC;
			} else if (language.equals(Locale.SIMPLIFIED_CHINESE)) {
				content = app.newsUriSC;
			} else {
				content = app.newsUri;
			}
			b.putString("content", content.toString());
			b.putInt("menuServiceID", MenuServiceID.NEWS.getValue());
            menuList.add(new Menu(findViewById(R.id.llNewsMenu), ServiceFunction.SRV_NEWS, NewsActivity.class.getSimpleName(), null, false, true));
        } else {
            Bundle b = new Bundle();
            b.putString("title", getString(R.string.menu_news));
            Uri content;
			Locale language = getLanguage();
			if (language.equals(Locale.TRADITIONAL_CHINESE)) {
                content = app.newsUriTC;
			} else if (language.equals(Locale.SIMPLIFIED_CHINESE)) {
                content = app.newsUriSC;
            } else {
                content = app.newsUri;
            }
            b.putString("content", content.toString());
			b.putInt("menuServiceID", MenuServiceID.NEWS.getValue());
            menuList.add(new Menu(findViewById(R.id.llNewsMenu), ServiceFunction.SRV_WEB_VIEW, WebViewActivity.class.getSimpleName(),null,false,false, b));
        }
		if (!CompanySettings.ENABLE_CONTENT_WEB_VIEW) {
			menuList.add(new Menu(findViewById(R.id.llEconomicMenu),ServiceFunction.SRV_ECONOMIC, EconomicActivity.class.getSimpleName(),null,false,true));
		} else {
			Bundle b = new Bundle();
			b.putString("title", getString(R.string.menu_economic));
			Uri content;
			Locale language = getLanguage();
			if (language.equals(Locale.TRADITIONAL_CHINESE)) {
			    content = app.economicUriTC;
			} else if (language.equals(Locale.SIMPLIFIED_CHINESE)) {
			    content = app.economicUriSC;
            } else {
			    content = app.economicUri;
            }
			b.putString("content", content.toString());
			b.putInt("menuServiceID", MenuServiceID.ECONOMIC_IND.getValue());
			menuList.add(new Menu(findViewById(R.id.llEconomicMenu), ServiceFunction.SRV_WEB_VIEW, WebViewActivity.class.getSimpleName(),null,false,false, b));
		}
		if (!CompanySettings.ENABLE_CONTENT_WEB_VIEW) {
			menuList.add(new Menu(findViewById(R.id.llContactUsMenu),ServiceFunction.SRV_CONTACTUS, "ContactActivity",null,false,true));
		} else {
			Bundle b = new Bundle();
			b.putString("title", getString(R.string.menu_contact_us));
			Uri content;
			Locale language = getLanguage();
			if (language.equals(Locale.TRADITIONAL_CHINESE)) {
				content = app.contactUsUriTC;
			} else if (language.equals(Locale.SIMPLIFIED_CHINESE)) {
				content = app.contactUsUriSC;
			} else {
				content = app.contactUsUri;
			}
			b.putString("content", content.toString());
			b.putInt("menuServiceID", MenuServiceID.CONTRACT_US.getValue());
			menuList.add(new Menu(findViewById(R.id.llContactUsMenu), ServiceFunction.SRV_WEB_VIEW, WebViewActivity.class.getSimpleName(),null,false,false, b));
		}

		if (!CompanySettings.ENABLE_CONTENT_WEB_VIEW) {
			menuList.add(new Menu(findViewById(R.id.llTermsMenu),ServiceFunction.SRV_TERMSNCONDITION, "TermsNConditionActivity",null,false,true));
		} else {
			Bundle b = new Bundle();
			b.putString("title", getString(R.string.menu_terms));
			Uri content;
			Locale language = getLanguage();
			if (language.equals(Locale.TRADITIONAL_CHINESE)) {
				content = app.termsUriTC;
			} else if (language.equals(Locale.SIMPLIFIED_CHINESE)) {
				content = app.termsUriSC;
			} else {
				content = app.termsUri;
			}
			b.putString("content", content.toString());
			b.putInt("menuServiceID", MenuServiceID.TERMS.getValue());
			menuList.add(new Menu(findViewById(R.id.llTermsMenu), ServiceFunction.SRV_WEB_VIEW, WebViewActivity.class.getSimpleName(),null,false,false, b));
		}
		if (!CompanySettings.ENABLE_CONTENT_WEB_VIEW || app.bLogon) {
			findViewById(R.id.llLogin).setVisibility(View.GONE);
		} else {
			menuList.add(new Menu(findViewById(R.id.llLogin), ServiceFunction.SRV_MOVE_TO_LOGIN, LoginActivity.class.getSimpleName(),null,false,false));
		}

		subMenuList = new ArrayList<>();
		subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_setting),ServiceFunction.SRV_SETTING, "SettingActivity",null,true,false));
		subMenuList.add(new Menu(findViewById(R.id.sub_menu_db_setting_id),ServiceFunction.SRV_SETTING_ID, "SettingIDActivity",null,true,false));
		menuList.add(new Menu(findViewById(R.id.llSettingMenu),0, null, subMenuList,true,false));
		subMenuListAll.addAll(subMenuList);

		final ArrayList<Menu> subMenuListTotal = subMenuListAll;
		final ArrayList<Menu> menuListTotal = menuList;

		bg_menu = findViewById(R.id.bg_menu);
		llMenu = findViewById(R.id.llMenu);
		rlMenu = findViewById(R.id.rlMenu);

		if (rlMenu != null) {
			rlMenu.setVisibility(View.INVISIBLE);
		}
		llMenu.setVisibility(View.INVISIBLE);
		bg_menu.setVisibility(View.INVISIBLE);

		if(!CompanySettings.ENABLE_CONTENT && !CompanySettings.ENABLE_CONTENT_WEB_VIEW && !app.bLogon){
			(findViewById(R.id.btnDash)).setVisibility(View.INVISIBLE);
		}

		if(app.bLogon){
			(findViewById(R.id.btnAccountTop)).setVisibility(View.VISIBLE);
		}else{
			(findViewById(R.id.btnAccountTop)).setVisibility(View.INVISIBLE);
		}

		if (!app.bLogon){
			(findViewById(R.id.btnLogout)).setVisibility(showLogout() ? View.VISIBLE : View.INVISIBLE);
			ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
			ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
			String currentActivity = cn.getShortClassName().replace(".","");
			currentActivity = currentActivity.replace("commfinanceandroidapp", "");
			if(currentActivity.equalsIgnoreCase("LoginActivity")) {
				findViewById(R.id.main_title).setVisibility(View.INVISIBLE);
				findViewById(R.id.btnLogout).setVisibility(View.INVISIBLE);
				findViewById(R.id.logo).setVisibility(View.INVISIBLE);
			}
		}else{
			(findViewById(R.id.btnLogout)).setVisibility(View.INVISIBLE);
		}


		for(final Menu mainMenu : menuList){
			if(mainMenu.isShowAfterLogin() && app.bLogon) {
				mainMenu.getMenuid().setVisibility(View.VISIBLE);
			}else if (!mainMenu.isShowAfterLogin()){
				mainMenu.getMenuid().setVisibility(View.VISIBLE);
			}else {
				mainMenu.getMenuid().setVisibility(View.GONE);
			}

			if(CompanySettings.ENABLE_CONTENT && mainMenu.isContentMode()){
				mainMenu.getMenuid().setVisibility(View.VISIBLE);
			}else if(!CompanySettings.ENABLE_CONTENT && mainMenu.isContentMode()) {
				mainMenu.getMenuid().setVisibility(View.GONE);
			}

			mainMenu.getMenuid().setBackgroundResource(0);

			// assign onClick action for each menu
			if(mainMenu.getServiceCode()==0){
					mainMenu.getMenuid().setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							//check if main menu is opened
							boolean openedMainMenu = false;
							if(mainMenu.getMenuid().getBackground()!=null)
								openedMainMenu=true;

							//close all mainMenu
							for (Menu mainMenu : menuListTotal) {
								mainMenu.getMenuid().setBackgroundResource(0);
							}

							mainMenu.getMenuid().setBackgroundResource(R.drawable.btn_navactive);

							if(openedMainMenu){
								mainMenu.getMenuid().setBackgroundResource(0);
							}

							//check if existing submenu is opened
							boolean openedSubMenu = false;
							for (Menu subMenu : mainMenu.getSubMenu()) {
								if(subMenu.getMenuid().getVisibility()==View.VISIBLE) {
									openedSubMenu = true;
									break;
								}
							}

							//close all submenu
							for (final Menu subMenu : subMenuListTotal) {
								subMenu.getMenuid().setVisibility(View.GONE);
							}

							//open corresponding submenu if the submenu is not opened
							if (!openedSubMenu) {
								for (Menu subMenu : mainMenu.getSubMenu()) {
									if(subMenu.isShowAfterLogin() && app.bLogon ) {
										subMenu.getMenuid().setVisibility(View.VISIBLE);
									}else if(!subMenu.isShowAfterLogin()) {
										subMenu.getMenuid().setVisibility(View.VISIBLE);
									}else {
										subMenu.getMenuid().setVisibility(View.GONE);
									}
									// for the case that main menu in content mode has submenu, now has no submenu yet, uncomment first
									/*if(CompanySettings.ENABLE_CONTENT && subMenu.isContentMode()){
										subMenu.getMenuid().setVisibility(View.VISIBLE);
									}else if(!CompanySettings.ENABLE_CONTENT && subMenu.isContentMode()) {
										subMenu.getMenuid().setVisibility(View.GONE);
									}*/
								}
							}
						}
					});

					for (final Menu subMenu : mainMenu.getSubMenu()) {
						subMenu.getMenuid().setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								menuGotoService(subMenu);
							}
						});
					}
			}else{
				mainMenu.getMenuid().setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mainMenu.getMenuid().setBackgroundResource(R.drawable.btn_navactive);
						menuGotoService(mainMenu);
					}
				});
			}
		}
	}

	private void menuGotoService(Menu menu){
		ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;

		String currentActivity = cn.getShortClassName().replace(".","");
		currentActivity = currentActivity.replace("commfinanceandroidapp", "");

		if(currentActivity.equalsIgnoreCase(menu.getActivity())) {
			showMenu();
			Optional<Bundle> extras = Optional.ofNullable(getIntent().getExtras());
			if (currentActivity.equalsIgnoreCase("OrderActivity") && app.getSelectedOpenPosition() != null) {
				finish();
				goTo(menu.getServiceCode(), menu.getBundle());
			} else if (
					extras.map(e -> !Objects.equals(e.getInt("menuServiceID"), menu.getBundle().getInt("menuServiceID")))
							.orElse(false)
			) {
				finish();
				goTo(menu.getServiceCode(), menu.getBundle());
			}
		}else if(currentActivity.equalsIgnoreCase("DealActivity") || currentActivity.equalsIgnoreCase("OrderActivity") ||
				currentActivity.equalsIgnoreCase("LiquidationActivity") || currentActivity.equalsIgnoreCase("EditOrderActivity")) {
			finish();
			goTo(menu.getServiceCode(), menu.getBundle());
		}else{
			goTo(menu.getServiceCode(), menu.getBundle());
		}
	}

	public void showMenu(){
		if (llMenu.getVisibility() == View.VISIBLE)
			llMenu.setVisibility(View.INVISIBLE);
		else
			llMenu.setVisibility(View.VISIBLE);

		if (rlMenu != null) {
			rlMenu.setVisibility(rlMenu.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
		}
		if (bg_menu.getVisibility() == View.VISIBLE)
			bg_menu.setVisibility(View.INVISIBLE);
		else
			bg_menu.setVisibility(View.VISIBLE);
	}


	private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
		try {
			packageManager.getPackageInfo(packageName, 0);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}
}

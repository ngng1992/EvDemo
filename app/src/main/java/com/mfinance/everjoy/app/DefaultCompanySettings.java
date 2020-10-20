package com.mfinance.everjoy.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.graphics.Color;
import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.MobileTraderApplication.LoginInfo;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Hourproducts;
import com.mfinance.everjoy.hungkee.xml.Strategies;

public class DefaultCompanySettings {
	public static final String CHART_URL = "";
	public static final boolean CHECK_APPLAUNCHER_IN_TEST = false;
	public static final boolean VISIBLE_DEMO_LOGIN = true;
	public static final boolean ENABLE_DEMO_REGISTER = true;
	public static final boolean VISIBLE_DEMO_REGISTER = true;
	public static final boolean VISIBLE_FORGOT_PASSWORD = true;
	public static final boolean VISIBLE_PLATFORM_SWITCH = true;
	public static final boolean ENABLE_EDIT_ORDER = true;
	
	public static final String CONTENT_KEY = "3KCakxUt4begfkgTl1gb";
			
	public static final String[] s1 = new String[] { "0", "1", "2", "3", "4" };
	public static final String[] s2 = new String[] { "0", "1", "2", "3", "4",
			"5", "6", "7", "8", "9" };
	public static final String[] s3 = new String[] { ".0", ".1", ".2", ".3",
			".4", ".5", ".6", ".7", ".8", ".9" };
	
	public static HashMap<Integer, String> hmFxServerMessageKeyMap = null;
	
	public static final String HTTP2_KEY = null;
	public static final String HTTP3_KEY = null;
	public static final String HTTP4_KEY = null;
	public static final String HTTP5_KEY = null;
	public static final String HTTP6_KEY = null;
	public static final String HTTP7_KEY = null;
	public static final String HTTP8_KEY = null;
	public static final String HTTP9_KEY = null;
	public static final String HTTP10_KEY = null;
	public static final String HTTP_KEY_DEMO = null;
	public static final String PROD2_LICENSE_KEY=null;
	public static final String PROD3_LICENSE_KEY=null;
	public static final String PROD4_LICENSE_KEY=null;
	public static final String PROD5_LICENSE_KEY=null;
	public static final String PROD6_LICENSE_KEY=null;
	public static final String PROD7_LICENSE_KEY=null;
	public static final String PROD8_LICENSE_KEY=null;
	public static final String PROD9_LICENSE_KEY=null;
	public static final String PROD10_LICENSE_KEY=null;
	public static final boolean ENABLE_SLIPPAGE = false;
	public static final boolean ENABLE_SLIPPAGE_PROD2 = false;
	public static final boolean ENABLE_SLIPPAGE_PROD3 = false;
	public static final boolean ENABLE_SLIPPAGE_PROD4 = false;
	public static final boolean ENABLE_SLIPPAGE_PROD5 = false;
	public static final boolean ENABLE_SLIPPAGE_PROD6 = false;
	public static final boolean ENABLE_SLIPPAGE_PROD7 = false;
	public static final boolean ENABLE_SLIPPAGE_PROD8 = false;
	public static final boolean ENABLE_SLIPPAGE_PROD9 = false;
	public static final boolean ENABLE_SLIPPAGE_PROD10 = false;
	public static final boolean ENABLE_SLIPPAGE_DEMO = false;
	public static int PLATFORM_ID_FROM_MOBILE_SERVICE = 1;
	public static final boolean ENABLE_FATCH_PLATFORM_ID_FROM_MOBILE_SERVICE = false;
	public static final boolean ENABLE_FATCH_REPORT_GROUP = false;
	public static final boolean ENABLE_FATCH_REPORT_GROUP_OTX = false;
	public static final boolean ENABLE_FATCH_SERVER = false;
	public static final boolean ENABLE_CREDIT_RATIO = false;
	public static final boolean ENABLE_Declaration_Screen = false;
	public static final boolean ENABLE_Declaration_Screen_ForEach_Login = false;
	public static final boolean ENABLE_Trailing_STOP = false;
	public static final boolean USE_DEFAULT_NEW_HEDGE_POSITION_MARGIN = false;
	public static final boolean ENABLE_CONTENT = false;
	public static final boolean ENABLE_CONTENT_WEB_VIEW = false;
	public static final boolean SHOW_DASHBOARD_BEFORE_LOGIN = false;
	public static final boolean ENABLE_ORDER = true;
	public static final boolean ENABLE_DEAL_OCO = false;
	public static final boolean ENABLE_DEAL_OCO_PROD2 = false;
	public static final boolean ENABLE_DEAL_OCO_PROD3 = false;
	public static final boolean ENABLE_DEAL_OCO_PROD4 = false;
	public static final boolean ENABLE_DEAL_OCO_PROD5 = false;
	public static final boolean ENABLE_DEAL_OCO_PROD6 = false;
	public static final boolean ENABLE_DEAL_OCO_PROD7 = false;
	public static final boolean ENABLE_DEAL_OCO_PROD8 = false;
	public static final boolean ENABLE_DEAL_OCO_PROD9 = false;
	public static final boolean ENABLE_DEAL_OCO_PROD10 = false;
	public static final boolean ENABLE_ORDER_OCO = false;
	public static final boolean ENABLE_ORDER_OCO_PROD2 = false;
	public static final boolean ENABLE_ORDER_OCO_PROD3 = false;
	public static final boolean ENABLE_ORDER_OCO_PROD4 = false;
	public static final boolean ENABLE_ORDER_OCO_PROD5 = false;
	public static final boolean ENABLE_ORDER_OCO_PROD6 = false;
	public static final boolean ENABLE_ORDER_OCO_PROD7 = false;
	public static final boolean ENABLE_ORDER_OCO_PROD8 = false;
	public static final boolean ENABLE_ORDER_OCO_PROD9 = false;
	public static final boolean ENABLE_ORDER_OCO_PROD10 = false;
	public static final boolean ENABLE_DEAL_OCO_DEMO = false;
	public static final boolean ENABLE_ORDER_OCO_DEMO = false;
	public static boolean ALLOW_STP_ORDER = false;
	public static final boolean GET_LatestMargin_FROM_SERVER = true;
	public static final String APP_UPDATE_URL=null;
	public static final String APP_UPDATE_URL_TYPE = null;
	public static String APP_UPDATE_URL_DEMO=null;
	public static String APP_UPDATE_URL_PROD1=null;
	public static String APP_UPDATE_URL_PROD2=null;
	public static String APP_UPDATE_URL_PROD3=null;
	public static String APP_UPDATE_URL_PROD4=null;
	public static String APP_UPDATE_URL_PROD5=null;
	public static String APP_UPDATE_URL_PROD6=null;
	public static String APP_UPDATE_URL_PROD7=null;
	public static String APP_UPDATE_URL_PROD8=null;
	public static String APP_UPDATE_URL_PROD9=null;
	public static String APP_UPDATE_URL_PROD10=null;
	
	public static final boolean FORCE_HARD_CODE_CHART_URL = false;
	public static final boolean CHECK_SERVER_AVALIABLE = false;
	public static final boolean GET_LAST_TRADEDATE_STATEMENT = false;
	public static final boolean SHOW_NON_TRADEABLE_ITEM = false;
	
	public final static boolean DISPLAY_EQUITY_WITH_CREDIT = true;
	public final static String REGISTER_URL = ":8888/mobile_service/account?";
	public final static String FORGOT_PASSWORD_URL = ":8888/mobile_service/forgot?";
	public final static String REPORTGROUP_URL = ":8888/mobile_service/rptgroup?";
	public final static String REPORTGROUP_URL_PROD2 = ":8888/mobile_service/rptgroup?";
	public final static String SERVERBYID_URL = ":8888/mobile_service/getServer?";
	public final static String CONTRACTLIST_URL = ":8888/mobile_service/getContractList?";
	public final static String STATEMENT_DEMO_URL = "demo/accountbalance.asp?";
	public final static String STATEMENT_PROD_URL = "web/accountbalance.asp?";
	public static int CHART_PORT = 8081;
	public static int REALTIME_CHART_PORT = 8082;
	public static String DEMO_REPORT_GROUP_NAME = "DEMOPLATFM";
	public static String DEMO_REPORT_GROUP = "2";
	public static String PRODUCTION_REPORT_GROUP = "1";
	public static final String TRADE_CURR = null;
	public static final boolean USE_NEW_DEAL_PROTOCOL = false;
	public static final boolean USE_NEW_DEAL_PROTOCOL_PROD2 = false;
	public static final boolean USE_NEW_DEAL_PROTOCOL_PROD3 = false;
	public static final boolean USE_NEW_DEAL_PROTOCOL_PROD4 = false;
	public static final boolean USE_NEW_DEAL_PROTOCOL_PROD5 = false;
	public static final boolean USE_NEW_DEAL_PROTOCOL_PROD6 = false;
	public static final boolean USE_NEW_DEAL_PROTOCOL_PROD7 = false;
	public static final boolean USE_NEW_DEAL_PROTOCOL_PROD8 = false;
	public static final boolean USE_NEW_DEAL_PROTOCOL_PROD9 = false;
	public static final boolean USE_NEW_DEAL_PROTOCOL_PROD10 = false;
	public static final boolean USE_NEW_DEAL_PROTOCOL_DEMO = false;
	public static final boolean USE_NEW_PnL_CALCULATION = true;
	public static final int NUM_OF_LOT_DP = 1;
	
	public final static String DEFAULT_LANGUAGE=null;
	public final static String[] AVALIABLE_LANGUAGE={"English","繁體","简体"};
	
	public final static String CONTENT_FOLDER_NAME = null;
	
	public final static String DEFAULT_LOT_SIZE=null;
	public static int MINIMUM_PASSWORD_SIZE = 8;
	public static String appVersionStr = "Application Version : ";
	public static final boolean AUTO_FILL_LOGIN_AFTER_REG = false;
	
	public static final boolean USE_HTTPKEY_AS_CONTENTKEY = false;
	public static final Class<? extends Strategies> USE_CUSTOM_DAY_PLAN = null;
	public static final Class<? extends Hourproducts> USE_CUSTOM_HOURPRODUCT = null;
	public static String USE_CUSTOM_HOURPRODUCT_URL = null;
	public static final boolean GET_NEWS_CONTENT_FROM_SERVLET = true;
	public static final boolean GET_COMPANY_INFO_CONTENT_FROM_SERVLET = false;
	public static final boolean GET_MASTER_FROM_SERVLET = true;
	public static final boolean SHOW_Advertisement = false;
	public static String Advertisement_XML_Name = "Advertisement.xml";
	public static String CompanyProfile_XML_Name = "CompanyInfo.xml";
	public static String ContactUs_XML_Name = "ContactUs.xml";
	
	public static int chartUpColor = Color.rgb(0, 255, 0);
	public static int chartDownColor = Color.rgb(255, 0, 0);
	
	public static final boolean SHOW_BS_COLUMN_ON_LISTVIEW = false;
	public static final boolean SHOW_ORDERTYPE_COLUMN_ON_LISTVIEW = false;
	
	public static final boolean LOCK_LIQ_ORDER_LIMIT_STOP = false;
	public static final boolean LIQ_DETAIL_BS_TO_OPEN_LIQ = false;
	
	public static final boolean CHART_SHOW_CHINESE = false;
	
	public static final boolean SHOW_HIGHLOW_ASK = false;
	
	public static final boolean ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST = false;
	
	public static String COMPANY_PREFIX = "";
	
	public final static int[] CHART_DEFAULT_SELECTION={0,1};
	
	public static boolean ENABLE_CHART_SERSOR_ROTATION = false;

	public static final String echoServer = "http://echoserver.mfcloud.net:17000";
	
	// if true, margin value by ratio method no net will use market price instead of execution price
	public static final boolean MARGIN_RATIO_NO_NET_CALCULATE_BY_MKT_PRICE = false;
	
	public static final boolean ENABLE_WEBVIEW_MASTER = false;
	public static HashMap<String, String> MASTER_KEYS = null; 
	
	public static final boolean ENABLE_WEBVIEW_CONTACT_US = false;
	public static HashMap<String, String> CONTACT_US_KEYS = null; 
	
	public static final boolean ENABLE_WEBVIEW_DEMO_REGISTRATION = false;
	public static HashMap<String, String> DEMO_REGISTRATION_KEYS = null;

	public static final boolean ENABLE_WEBVIEW_LOST_PWD = false;
	public static HashMap<String, String> LOST_PWD_KEYS = null;
	
	public static HashMap<String, String> IDENTITY_CHECK_KEYS = null;
	
	public static final boolean FORCE_NEW_PRICE_STREAMING_PROTOCOL = true; 
	
	public static final boolean VALIDATE_MINLOT_AND_INCLOT = true;
	
	public static final boolean Inverse_Commission_Sign = false;
	
	public static final int RateWheelRange = 10; 
	
	public static final int DEFAULTDEFAULTPAGE = ServiceFunction.SRV_DASHBOARD;  
	
	enum PlatformType{
		ALL,DEMO,PRODUCTION
	}
	
	enum OrderTimeLimit{
		DefaultDaily,DefaultFriday,DefaultCancel,DailyOnly,FridayOnly
	}
	
	public static final OrderTimeLimit ORDER_TIME_LIMIT = OrderTimeLimit.DefaultDaily;
	public static final PlatformType PLAYFORM_TYPE = PlatformType.ALL;
	
	public static final boolean FOR_UAT = false; //USE DEMO PLATFORM ONLY ANYTIME
	public static final boolean FOR_TEST = false; //OVERRIDE ALL CONNECTION TO loginInfoTest
	public static final LoginInfo loginInfoTest = null;
	
	public final static int PageControlActiveColor = 0xffcccccc;
	public final static int PageControlInactiveColor = 0xff444444;
	
	public final static boolean IsUseCustomPriceColor = false;
	public final static int CustomPriceColor = 0xffffffff;
	
	public static int DefaultSlippageValue = 0;
	
	public static int checkProdServer(){
		return 1;
	}

	public static final int MARKET_DEPTH_LEVEL = 1;
	public static final boolean isEnableMarketDepth = false;
	public static final int DEPTH_PRICE = 0;

	public static final boolean isHexMessageEncode = false;

	public static final double MAINTAINENCE_MARGIN_RATIO = 1.0;
	
	public static boolean newinterface = false; //If true, using bar_account_new and bar_top layout. Rename with bar_account and bar_top

	public static boolean blackNotificationBarText = false;

	public static boolean ENABLE_TRADER_PRICE_AGENT_CONNECTION = false;
	public static String PRICEAGENT_LOGIN_SECRET = "";
	public static String m_strWebLink = "";
	public static String PriceAgentIP = "127.0.0.1:8888";
	public static String PriceAgentDisplayName = "1";

	public static int alertDialogTheme = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
	public static boolean ENABLE_OPENPOSITION_SUMMARY_LIQUIDATION = true;
	public static boolean FORCE_FIRST_LOGIN_CHANGE_PASSWORD_FUNC = true;
	public static final String DEPOSIT_HTTP_KEYS = null;
	public static final String ECONOMIC_IND_HTTP_KEY = null;
	public static final String NEWS_HTTP_KEY = null;
	public static final String DEPOSIT_HTTP_KEYS_SC = null;
	public static final String ECONOMIC_IND_HTTP_KEY_SC = null;
	public static final String NEWS_HTTP_KEY_SC = null;
	public static final String DEPOSIT_HTTP_KEYS_TC = null;
	public static final String ECONOMIC_IND_HTTP_KEY_TC = null;
	public static final String NEWS_HTTP_KEY_TC = null;
	public static final String CONTACT_US_HTTP_KEY = null;
	public static final String CONTACT_US_HTTP_KEY_TC = null;
	public static final String CONTACT_US_HTTP_KEY_SC = null;
	public static final String TERMS_HTTP_KEY = null;
	public static final String TERMS_HTTP_KEY_TC = null;
	public static final String TERMS_HTTP_KEY_SC = null;
	public static final String ANNOUNCEMENT_HTTP_KEY = null;
	public static final String ANNOUNCEMENT_HTTP_KEY_TC = null;
	public static final String ANNOUNCEMENT_HTTP_KEY_SC = null;
	public static final String CHART_SERVER_URL_KEY = null;
	public static int dealTimeOut = -1; //seconds to wait to cancel deal
	public static final double PRICE_QUOTE_REFRESH_PER_SECOND = 3;
	public static final boolean ENABLE_ONE_CLICK_TRADE = true;
	public static final boolean ENABLE_LIQ_ALL = false;
	public static final boolean ENABLE_MULTIPLE_LIQ_PARTIAL_LIQ = false;
	public static final boolean ENABLE_WITHDRAWAL_HISTORY = false;
	public static final boolean ENABLE_SYSTEM_MESSAGE_FROM_FXSERVER = false;
	public static final boolean ENABLE_CONTRACT_SORT = false;
	public static final boolean ENABLE_CONTRACT_LEVEL_SETTING = false;
	public static final boolean ENABLE_GRID_LAYOUT_ACCOUNT_BAR = false;
	public static final boolean ENABLE_MULTI_CANCEL_ORDER = false;
	public static final boolean ENABLE_INTEREST_RATE_DISPLAY = false;
	public static final boolean ENABLE_CONTRACT_DETAIL_LIQUIDATE_SELECTION = false;
	public static final boolean ENABLE_CONTRACT_DETAIL_LIQUIDATE_BID_SHOW_BUY_POSITION = false;
	public static final int INTEREST_RATE_PERCENTAGE_DECIMAL_PLACE = 4;

	public static final boolean ENABLE_PRICE_ALERT = false;
	public static final int AlertRateWheelRange = 1000;
	public static final int getPriceAlertDefaultPips = 1000;
	public static final int getPriceAlertMinPips = 10;

	public static final boolean getSelfIPBySSL = true;
	public static final String echoiplink = "https://echosrv.m-finance.net:19000/echoip.php";

	public static final boolean AESCrypto = false;
	public static final boolean getMaxlotFromServer = true;

	public static final int systemMessageViewCount = -1;
	public static final int systemMessageViewHeightRatio = 2;

	public static void parseJson(MobileTraderApplication app, String json){
		try {
			JSONArray a = new JSONArray(json);
			ArrayList<String[]> jo = new ArrayList<String[]>();

			for(int i=0;i<a.length();i++){
				JSONObject o=a.getJSONObject(i);
				
				String contract=o.isNull("contract")?null:o.getString("contract");
				String chart=o.isNull("chart")?null:o.getString("chart");
				String curr1=o.isNull("curr1")?null:o.getString("curr1");
				String curr2=o.isNull("curr2")?null:o.getString("curr2");
				
				if(contract!=null){
					String[] obj = new String[]{contract,chart,curr1,curr2};
					jo.add(obj);	
				}
			}
			
			if(jo.size()>0){
				app.data.clearAllChartAva();
				app.data.clearAllContractImage();
				
				for(String[] obj:jo){
					if(obj[0]!=null&&obj[1]!=null){
						app.data.addChartAva(obj[0]);
						Utility.getChartMap().put(obj[0], obj[1]);
					}
					if(obj[0]!=null&&obj[2]!=null&&obj[3]!=null){
						app.data.addContractImage(obj[0], obj[2], obj[3]);
					}	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (BuildConfig.DEBUG)
				Log.d("parseContractJson", "json:"+json, e);
		}
		
	}
	
	public static String decryptPassword(String user, String str){
		CommonFunction cf = new CommonFunction();
        cf.setKey(Utility.getPasswdKey(user));
        return cf.decryptText(str);
	}
	public static String getDefaultDefaultContract(MobileTraderApplication app)
	{
		String contract = null;
		if (app.data.getContractList().size() > 0){
			for (int i = 0 ; i < app.data.getContractList().size() ; i++){
				if (app.data.getContractList().get(i).isViewable()) {
					contract = app.data.getContractList().get(i).strContractCode;
					break;
				}
			}
		}

		return contract;
	}

	public static byte[] getSecretKey()
	{
		//12345678901234567890123456789032
		return (new Object() {int t;public byte[] process() {byte[] buf = new byte[32];t = 17168; buf[0] = (byte) (t >>> 4);t = 12787712; buf[1] = (byte) (t >>> 12);t = 537344; buf[2] = (byte) (t >>> 8);t = 5873664; buf[3] = (byte) (t >>> 11);t = 889192448; buf[4] = (byte) (t >>> 24);t = 1130364928; buf[5] = (byte) (t >>> 20);t = 2677760; buf[6] = (byte) (t >>> 10);t = -1677721600; buf[7] = (byte) (t >>> 23);t = 956301312; buf[8] = (byte) (t >>> 24);t = 101376; buf[9] = (byte) (t >>> 6);t = 5867520; buf[10] = (byte) (t >>> 11);t = 59539456; buf[11] = (byte) (t >>> 14);t = 42592; buf[12] = (byte) (t >>> 5);t = 5873664; buf[13] = (byte) (t >>> 11);t = 27697152; buf[14] = (byte) (t >>> 13);t = 1207296; buf[15] = (byte) (t >>> 9);t = 6584; buf[16] = (byte) (t >>> 3);t = 12812288; buf[17] = (byte) (t >>> 12);t = -1669332992; buf[18] = (byte) (t >>> 23);t = 42496; buf[19] = (byte) (t >>> 5);t = 271646720; buf[20] = (byte) (t >>> 16);t = -1728053248; buf[21] = (byte) (t >>> 23);t = 27680768; buf[22] = (byte) (t >>> 13);t = 271843328; buf[23] = (byte) (t >>> 16);t = 1221853184; buf[24] = (byte) (t >>> 18);t = 271974400; buf[25] = (byte) (t >>> 16);t = 1222377472; buf[26] = (byte) (t >>> 18);t = 6592; buf[27] = (byte) (t >>> 3);t = 626; buf[28] = (byte) (t >>> 1);t = -1509949440; buf[29] = (byte) (t >>> 21);t = 1221328896; buf[30] = (byte) (t >>> 18);t = -1505755136; buf[31] = (byte) (t >>> 21);return buf;}}.process());
	}
	public static byte[] getTradeKey()
	{
		//12345678901234567890123456789032
		return (new Object() {int t;public byte[] process() {byte[] buf = new byte[32];t = 17168; buf[0] = (byte) (t >>> 4);t = 12787712; buf[1] = (byte) (t >>> 12);t = 537344; buf[2] = (byte) (t >>> 8);t = 5873664; buf[3] = (byte) (t >>> 11);t = 889192448; buf[4] = (byte) (t >>> 24);t = 1130364928; buf[5] = (byte) (t >>> 20);t = 2677760; buf[6] = (byte) (t >>> 10);t = -1677721600; buf[7] = (byte) (t >>> 23);t = 956301312; buf[8] = (byte) (t >>> 24);t = 101376; buf[9] = (byte) (t >>> 6);t = 5867520; buf[10] = (byte) (t >>> 11);t = 59539456; buf[11] = (byte) (t >>> 14);t = 42592; buf[12] = (byte) (t >>> 5);t = 5873664; buf[13] = (byte) (t >>> 11);t = 27697152; buf[14] = (byte) (t >>> 13);t = 1207296; buf[15] = (byte) (t >>> 9);t = 6584; buf[16] = (byte) (t >>> 3);t = 12812288; buf[17] = (byte) (t >>> 12);t = -1669332992; buf[18] = (byte) (t >>> 23);t = 42496; buf[19] = (byte) (t >>> 5);t = 271646720; buf[20] = (byte) (t >>> 16);t = -1728053248; buf[21] = (byte) (t >>> 23);t = 27680768; buf[22] = (byte) (t >>> 13);t = 271843328; buf[23] = (byte) (t >>> 16);t = 1221853184; buf[24] = (byte) (t >>> 18);t = 271974400; buf[25] = (byte) (t >>> 16);t = 1222377472; buf[26] = (byte) (t >>> 18);t = 6592; buf[27] = (byte) (t >>> 3);t = 626; buf[28] = (byte) (t >>> 1);t = -1509949440; buf[29] = (byte) (t >>> 21);t = 1221328896; buf[30] = (byte) (t >>> 18);t = -1505755136; buf[31] = (byte) (t >>> 21);return buf;}}.process());
	}
	public static byte[] getEchoServerCertsSN()
	{
		//3715863690024153846
		return new byte[] {51, -111, 102, 86, -83, -87, 114, -10};
	}
	public static byte[] getEchoServerCertsMD5()
	{
		//0D00794E8A210047398545F70B366FAC
		return new byte[] {13, 0, 121, 78, -118, 33, 0, 71, 57, -123, 69, -9, 11, 54, 111, -84};
	}
	public static byte[] getIV()
	{
		//97,148,209,161,23,119,150,60,28,223,153,63,15,247,39,86
		return (new Object() {int t;public byte[] process() {byte[] buf = new byte[16];t = -1407188992; buf[0] = (byte) (t >>> 21);t = 278134784; buf[1] = (byte) (t >>> 16);t = -788529152; buf[2] = (byte) (t >>> 24);t = 1262080; buf[3] = (byte) (t >>> 9);t = 2140; buf[4] = (byte) (t >>> 2);t = 2743296; buf[5] = (byte) (t >>> 10);t = 278265856; buf[6] = (byte) (t >>> 16);t = 578289664; buf[7] = (byte) (t >>> 17);t = 1193984; buf[8] = (byte) (t >>> 9);t = 2940; buf[9] = (byte) (t >>> 2);t = 18832; buf[10] = (byte) (t >>> 4);t = -1883242496; buf[11] = (byte) (t >>> 22);t = 542; buf[12] = (byte) (t >>> 1);t = 1306112; buf[13] = (byte) (t >>> 9);t = 575537152; buf[14] = (byte) (t >>> 17);t = 43712; buf[15] = (byte) (t >>> 5);return buf;}}.process());
	}
}

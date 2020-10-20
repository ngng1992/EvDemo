package com.mfinance.everjoy.app.constant;

// common constants declarations
public class FXConstants
{
    public static final int ENGLISH = 0;
	public static final int TRAD_CHINESE = 1;
	public static final int SIMP_CHINESE = 2;
    public static final int JAPANESE = 3;
    
    public static final String COMMON_ADD = "1";
    public static final String COMMON_UPDATE = "2";
    public static final String COMMON_DELETE = "3";
    public static final String COMMON_NEW = "4";

    //public static final double NUM_OF_LOT_DP2 = 0.1;
    
	public static final boolean PASSWORD_ONLY_NUMBERIC = false;
    public static final String FXTRADER_APPLICATION_VERSION = "5.2.8";
    
    public static final String TRADER_ROLE = "2";
    public static String CLIENT_TYPE = "1";
    
    public static final boolean SHOW_CALL_MARGIN_COLUMN_FUNC = true;
    
    public static final String SETTING_SAVE_LOGIN_INFO = "SETTING_SAVE_LOGIN_INFO";
    public static final String SETTING_USER_ID = "SETTING_USER_ID";
    public static final String SETTING_USER_PASSWORD = "SETTING_USER_PASSWORD";
    public static final String SETTING_PLATFORM_TYPE = "SETTING_PLATFORM_TYPE";
    public static final String SETTING_SAVE_TOUCH_ID = "SETTING_SAVE_TOUCH_ID";
    
    public static final String DEMO ="DEMO";
    public static final String PRODUCT = "PRODUCTION";

    //Message length
    public static int MESSAGE_NORMAL_LENGTH = 7; //Do not modify. It only implements for dealer platform at this stage(2017/12/04), if it needs to modify, please implement for trader platform first.
    public static int MESSAGE_LARGE_LENGTH = 8; //It implements for dealer open positions(service type: DEALER_HISTORY_LIST_TYPE, function type:DEALER_TRADES_OPEN_DEAL) at this stage(2017/12/04)

    public static String MESSAGE_KEY_LAST_VERSION = "";
}

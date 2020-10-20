package com.mfinance.everjoy.app.constant;

public class ServiceFunction {
    public final static int V_SUCCESS = 0;
    public final static int V_FAIL = 1;

    public final static String P_RESULT = "result";

    /**
     * 关闭服务
     */
    public final static int SRV_CLOSE_SERVCIE = -1;
    public final static int SRV_REGISTER_ACTIVITY = 0;
    public final static int SRV_UNREGISTER_ACTIVITY = 1;
    /**
     * 登录
     */
    public final static int SRV_LOGIN = 2;
    /**
     * 退出登录
     */
    public final static int SRV_LOGOUT = 3;
    public final static int SRV_DASHBOARD = 4;
    /**
     * 报价服务
     */
    public final static int SRV_PRICE = 5;
    public final static int SRV_OPEN_POSITION = 6;
    public final static int SRV_CONTRACT_DETAIL = 7;
    public final static int SRV_SEND_DEAL_REQUEST = 8;
    public final static int SRV_SEND_TRANSACTION_REQUEST = 9;
    public final static int SRV_TRANSACTION = 10;
    public final static int SRV_PRICE_BROADCAST = 11;
    public final static int SRV_SEND_LIQUIDATION_REQUEST = 12;
    public final static int SRV_DEAL = 14;
    public final static int SRV_LIQUIDATE = 15;
    public final static int SRV_RUNNING_ORDER = 16;
    public final static int SRV_ORDER = 17;
    public final static int SRV_SEND_ADD_ORDER_REQUEST = 18;
    public final static int SRV_SEND_CANCEL_ORDER_REQUEST = 19;
    public final static int SRV_EDIT_ORDER = 20;
    public final static int SRV_SEND_EDIT_ORDER_REQUEST = 21;
    public final static int SRV_EXECUTED_ORDER = 22;
    public final static int SRV_SEND_EXECUTED_ORDER_HISTORY_REQUEST = 23;
    public final static int SRV_CANCELLED_ORDER = 24;
    public final static int SRV_SEND_CANCELLED_ORDER_HISTORY_REQUEST = 25;
    public final static int SRV_LIQUIDATION_HISTORY = 26;
    public final static int SRV_SEND_LIQUIDATION_HISTORY_REQUEST = 27;
    public final static int SRV_CHART = 28;
    public final static int SRV_DISCONNECT = 29;
    public final static int SRV_OPEN_POSITION_SUMMARY = 30;
    public final static int SRV_SETTING = 31;
    public final static int SRV_HISTORY = 32;
    public final static int SRV_OPEN_POSITION_DETAIL = 33;
    public final static int SRV_OPEN_POSITION_SUMMARY_DETAIL = 34;
    public final static int SRV_TRANSACTION_STATUS_DETAIL = 36;
    public final static int SRV_LIQUIDATE_DETAIL = 37;
    public final static int SRV_CANCELLED_ORDER_DETAIL = 38;
    public final static int SRV_EXECUTED_ORDER_DETAIL = 39;
    public final static int SRV_ANDROID_MARKET = 40;
    public final static int SRV_ON_LINE_STATEMENT = 41;
    public final static int SRV_SHOW_TOAST = 42;
    public final static int SRV_UNBIND_SERVICE = 43;
    public final static int SRV_MOVE_TO_LOGIN = 44;
    public final static int SRV_TO_SHOW_ANDROID_MARKET_MSG = 45;
    public static final int SRV_MOVE_TO_NEWS_LIST = 46;
    public static final int SRV_MOVE_TO_STRATEGY_LIST = 47;
    public static final int SRV_MOVE_TO_ECONOMIC_DATA_LIST = 48;
    public static final int SRV_MOVE_TO_NEWS_CONTENT_LIST = 49;
    public static final int SRV_MOVE_TO_MASTER_LIST = 50;
    public static final int SRV_MOVE_TO_NEWS_CONTENT_DETAIL = 51;
    public static final int SRV_MOVE_TO_ECONOMIC_DATA_DETAIL = 52;
    public static final int SRV_MOVE_TO_MASTER_DETAIL = 53;
    public static final int SRV_MOVE_TO_STRATEGY_DETAIL = 54;
    public static final int SRV_MOVE_TO_COMPANY_PROFILE = 55;
    public static final int SRV_MOVE_TO_CONTACT_US = 56;
    public static final int SRV_RESTART_GENERAL_XML_TIMER = 57;
    public static final int SRV_START_XML_TRANSFER = 58;
    public static final int SRV_MOVE_TO_NEWS_DETAIL = 59;
    public static final int SRV_FINISH_ACTIVITY = 60;
    public static final int SRV_MOVE_TO_TERMS = 61;
    public static final int SRV_MOVE_TO_HOUR_PRODUCT_DETAIL = 62;
    public static final int SRV_MOVE_TO_HOUR_PRODUCT = 63;
    public static final int SRV_SHOW_ACC_INFO = 64;
    public final static int SRV_CASH_MOVEMENT = 65;
    public final static int SRV_CASH_MOVEMENT_HISTORY = 66;
    public final static int SRV_SEND_CASH_MOVEMENT_HISTORY_REQUEST = 68;
    public final static int SRV_SEND_CASH_MOVEMENT_REQUEST = 69;
    public final static int SRV_SEND_BANK_INFO_REQUEST = 70;
    public final static int SRV_REPORT_ERROR = 71;
    public final static int SRV_DEAL_INPUT_FRAME_OPEN_CLOSE = 72;
    public final static int SRV_MOVE_TO_DEMO_REGISTRATION = 73;
    public final static int SRV_MOVE_TO_LOST_PASSWORD = 74;
    public final static int SRV_MOVE_TO_IDENTITY_CHECK = 75;
    public final static int SRV_SEND_COMPANY_INFO_REQUEST = 76;
    public final static int SRV_CUST_LIST = 77;
    public final static int SRV_SETTING_ID = 78;
    public final static int SRV_TWO_FA = 79;
    public final static int SRV_CHANGE_PASSWORD = 80;
    public final static int SRV_SEND_CHANGE_PASSWORD_REQUEST = 81;
    public final static int SRV_CONTRACT_SORT = 82;
    public final static int SRV_ANNOUNCEMENT = 83;
    public final static int SRV_NEWS = 84;
    public final static int SRV_ECONOMIC = 85;
    public final static int SRV_CONTACTUS = 86;
    public final static int SRV_TERMSNCONDITION = 87;
    public final static int SRV_LIQUIDATE_ALL = 88;
    public final static int SRV_SETTING_CONTRACT = 89;
    public final static int SRV_PRICE_ALERT = 90;
    public final static int SRV_PRICE_ALERT_HISTORY = 91;
    public final static int SRV_PRICE_ALERT_UPDATE = 92;
    public final static int SRV_NEW_PRICE_ALERT = 93;

    public final static int ACT_UPDATE_UI = 0;
    public final static int ACT_GO_TO_DASHBOARD = 1;
    public final static int ACT_GO_TO_LOGIN = 2;
    public final static int ACT_GO_TO_PRICE = 3;
    public final static int ACT_GO_TO_OPEN_POSITION = 4;
    public final static int ACT_GO_TO_CONTRACT_DETAIL = 5;
    public final static int ACT_GO_TO_TRANSACTION = 6;
    public final static int ACT_GO_TO_DEAL = 7;
    public final static int ACT_GO_TO_LIQUIDATE = 8;
    public final static int ACT_GO_TO_RUNNING_ORDER = 9;
    public final static int ACT_GO_TO_ORDER = 10;
    public final static int ACT_GO_TO_EDIT_ORDER = 11;
    public final static int ACT_GO_TO_EXECUTED_ORDER = 12;
    public final static int ACT_GO_TO_CANCELLED_ORDER = 13;
    public final static int ACT_GO_TO_LIQUIDATION_HISTORY = 14;
    public final static int ACT_GO_TO_CHART = 15;
    public final static int ACT_UPDATE_CONNECTION_STATUS = 16;
    public final static int ACT_GO_TO_OPEN_POSITION_SUMMARY = 17;
    public final static int ACT_GO_TO_SETTING = 18;
    public final static int ACT_VIBRATE = 19;
    public final static int ACT_VISIBLE_POP_UP = 20;
    public final static int ACT_INVISIBLE_POP_UP = 21;
    public final static int ACT_GO_TO_HISTORY = 22;
    public final static int ACT_OPEN_POSITION_DETAIL = 23;
    public final static int ACT_OPEN_POSITION_SUMMARY_DETAIL = 24;
    public final static int ACT_TRANSACTION_STATUS_DETAIL = 26;
    public final static int ACT_LIQUIDATE_DETAIL = 27;
    public final static int ACT_CANCELLED_ORDER_DETAIL = 28;
    public final static int ACT_EXECUTED_ORDER_DETAIL = 29;
    public final static int ACT_GO_TO_ANDROID_MARKET = 30;
    public final static int ACT_DISCONNECT = 31;
    public final static int ACT_POST_FB = 32;
    public final static int ACT_GO_TO_ON_LINE_STATEMENT = 33;
    public final static int ACT_SHOW_TOAST = 34;
    public final static int ACT_UNBIND_SERVICE = 35;
    public final static int ACT_GO_TO_SHOW_ANDROID_MARKET_MSG = 36;
    public final static int ACT_GO_TO_COMPANY_PROFILE = 37;
    public final static int ACT_GO_TO_CONTACT_US = 38;
    public final static int ACT_GO_TO_ECONOMIC_DATA_DETAIL = 39;
    public final static int ACT_GO_TO_ECONOMIC_DATA_LIST = 40;
    public final static int ACT_GO_TO_MASTER_DETAIL = 41;
    public final static int ACT_GO_TO_MASTER_LIST = 42;
    public final static int ACT_GO_TO_NEWS_CONTENT_DETAIL = 43;
    public final static int ACT_GO_TO_NEWS_CONTENT_LIST = 44;
    public final static int ACT_GO_TO_NEWS_DETAIL = 45;
    public final static int ACT_GO_TO_NEWS_LIST = 46;
    public final static int ACT_GO_TO_STRATEGY_DETAIL = 47;
    public final static int ACT_GO_TO_DAY_PLAN_LIST = 48;
    public final static int ACT_FINISH_ACTIVITY = 49;
    public final static int ACT_GO_TO_HOUR_PRODUCT = 50;
    public final static int ACT_GO_TO_HOUR_PRODUCT_DETAIL = 51;
    public final static int ACT_GO_TO_TERMS = 52;
    public final static int ACT_SHOW_ACC_INFO = 53;
    public final static int ACT_GO_TO_CASH_MOVEMENT = 54;
    public final static int ACT_GO_TO_CASH_MOVEMENT_HISTORY = 55;
    public final static int ACT_RELOAD_DASHBOARD = 57;
    public final static int ACT_TRADER_LIQUIDATE_RETURN = 58;
    public final static int ACT_GO_TO_DEMO_REGISTRATION = 59;
    public final static int ACT_GO_TO_LOST_PASSWORD = 60;
    public final static int ACT_GO_TO_IDENTITY_CHECK = 61;
    public final static int ACT_GO_TO_CUST_LIST = 62;
    public final static int ACT_GO_TO_SETTING_ID = 63;
    public final static int ACT_GO_TO_TWO_FA = 64;
    public final static int ACT_GO_TO_CHANGE_PASSWORD = 65;
    public final static int ACT_SYSTEM_ALERT_MESSAGE = 66;
    public final static int ACT_GO_TO_CONTRACT_SORT = 67;
    public final static int ACT_GO_TO_ANNOUNCEMENT = 68;
    public final static int ACT_GO_TO_NEWS = 69;
    public final static int ACT_GO_TO_ECONOMIC = 70;
    public final static int ACT_GO_TO_CONTACTUS = 72;
    public final static int ACT_GO_TO_TERMSNCONDITION = 73;
    public final static int ACT_GO_TO_LIQUIDATE_ALL = 74;
    public final static int ACT_GO_TO_CASH_MOVEMENT_HISTORY_DETAIL = 75;
    public final static int ACT_PENDING_ORDER_DETAIL = 76;
    public final static int ACT_GO_TO_PRICE_ALERT = 77;
    public final static int ACT_GO_TO_PRICE_ALERT_HISTORY = 78;
    public final static int ACT_GO_TO_NEW_PRICE_ALERT = 79;
    public final static int ACT_TRADER_RECEIVE_MULTIPLE_LIQUIDATE_RETURN = 10075;
    public final static int SRV_WEB_VIEW = 10076;
    public final static int SRV_GUEST_PRICE_AGENT = 10077;

    public static final String SEND_SELECTED_KEY = "key";

    public final static String IS_LOGIN = "islogin";
    public final static String REQUIRE_LOGIN = "requestlogin";

    /**
     * Login request service parameter
     **/
    public final static String LOGIN_ID = "login_id";
    public final static String LOGIN_URL = "url";
    public final static String LOGIN_PORT = "port";
    public final static String LOGIN_PASSWORD = "password";
    public final static String LOGIN_PLATFORM_TYPE = "ptype";
    public final static String LOGIN_CONN_INDEX = "connidx";
    /**Login request service parameter **/

    /**Login response service parameter **/

    /**Login response service parameter **/

    /**
     * Move to open position listing parameter
     **/
    public final static String SRV_OPEN_POSITION_CONTRACT = "contract";
    public final static String SRV_OPEN_POSITION_BUY_SELL = "b/s";
    public final static String SRV_OPEN_POSITION_FROM_SUMMARY = "fs";
    /**
     * Move to open position listing parameter
     **/

    public final static String PRICE_NAME = "mkt";
    public final static String BID = "bid";
    public final static String ASK = "ask";

    public final static String CONTRACT_DETAIL_CONTRACT = "contract";

    /**
     * Send deal parameter
     **/
    public final static String CHART_CONTRACT = "contract";
    /**Send deal parameter**/

    /**
     * Send deal parameter
     **/
    public final static String DEAL_CONTRACT = "contract";
    public final static String DEAL_BUY_SELL = "bs";
    /**Send deal parameter**/

    /**
     * Send order parameter
     **/
    public final static String ORDER_CONTRACT = "contract";
    public final static String ORDER_BUY_SELL = "bs";
    public final static String ORDER_LIMIT_STOP = "l/s";
    public final static String ORDER_DEAL_REF = "did";
    /**Send order parameter**/

    /**
     * Send deal request parameter
     **/
    public final static String SEND_DEAL_REQUEST_ID = "pid";
    public final static String SEND_DEAL_REQUEST_CONTRACT = "contract";
    public final static String SEND_DEAL_REQUEST_BUY_SELL = "bs";
    public final static String SEND_DEAL_REQUEST_REQUEST_RATE = "rr";
    public final static String SEND_DEAL_REQUEST_LOT = "lt";
    public final static String SEND_DEAL_REQUEST_CONTRACT_SIZE = "cs";
    public final static String SEND_DEAL_REQUEST_LIMIT_PRICE = "lprice";
    public final static String SEND_DEAL_REQUEST_LIMIT_GT = "lgoodtil";
    public final static String SEND_DEAL_REQUEST_STOP_PRICE = "sprice";
    public final static String SEND_DEAL_REQUEST_STOP_GT = "sgoodtil";
    //public final static String SEND_DEAL_REQUEST_SLIPPAGE = "acceptpips";
    // Use new field name, no compatibility concern since this is internal usage only
    public final static String SEND_DEAL_REQUEST_SLIPPAGE = "mktrange";
    public final static String SEND_DEAL_REQUEST_UNRESTRICTEDMARKET = "bUnrestrictedMarket";
    public final static String SEND_DEAL_REQUEST_CTIME = "ctime";
    public final static String SEND_DEAL_REQUEST_TAG = "tag";
    /**Send deal request parameter**/

    /**
     * Send moving to liquidation request parameter
     **/
    public final static String LIQUIDATE_REF = "ref";
    /**Send deal request parameter**/

    /**
     * Send liquidation request parameter
     **/
    public final static String SEND_LIQUIDATION_REQUEST_ID = "pid";
    public final static String SEND_LIQUIDATION_REQUEST_REF = "ref";
    public final static String SEND_LIQUIDATION_REQUEST_REFS = "refs";
    public final static String SEND_LIQUIDATION_REQUEST_CONTRACT = "contract";
    public final static String SEND_LIQUIDATION_REQUEST_CONTRACT_SIZE = "cs";
    public final static String SEND_LIQUIDATION_REQUEST_BUY_SELL = "bs";
    public final static String SEND_LIQUIDATION_REQUEST_REQUEST_RATE = "rr";
    public final static String SEND_LIQUIDATION_REQUEST_LOT = "lt";
    public final static String SEND_LIQUIDATION_REQUEST_AMOUNT = "am";
    //public final static String SEND_LIQUIDATION_REQUEST_SLIPPAGE = "acceptpips";
    // Use new field name, no compatibility concern since this is internal usage only
    public final static String SEND_LIQUIDATION_REQUEST_SLIPPAGE = "mktrange";
    public final static String SEND_LIQUIDATION_REQUEST_UNRESTRICTEDMARKET = "bUnrestrictedMarket";
    public final static String SEND_LIQUIDATION_REQUEST_CTIME = "ctime";
    public final static String SEND_LIQUIDATION_REQUEST_TAG = "tag";
    /**Send liquidation request parameter**/

    /**
     * Send order request parameter
     **/
    public final static String SEND_ORDER_REQUEST_ACTION = "order";
    public final static String SEND_ORDER_REQUEST_CONTRACT = "contract";
    public final static String SEND_ORDER_REQUEST_CONTRACT_SIZE = "cs";
    public final static String SEND_ORDER_REQUEST_BUY_SELL = "bs";
    public final static String SEND_ORDER_REQUEST_REQUEST_RATE = "rr";
    public final static String SEND_ORDER_REQUEST_LIMIT_STOP = "l/s";
    public final static String SEND_ORDER_REQUEST_OCO = "oco";
    public final static String SEND_ORDER_REQUEST_LOT = "lt";
    public final static String SEND_ORDER_REQUEST_POSITION_REF = "ref";
    //	public final static String SEND_ORDER_REQUEST_POSITION_AMOUNT = "amount";
    public final static String SEND_ORDER_REQUEST_GOOD_TILL = "gt";
    public final static String SEND_ORDER_REQUEST_LIQ_REF = "liqrate";
    public final static String SEND_ORDER_REQUEST_TRAILING_STOP_SIZE = "trail";
    public final static String SEND_ORDER_REQUEST_CURR_RATE = "cr";
    public final static String SEND_ORDER_REQUEST_PROFIT_RATE = "profitrate";
    public final static String SEND_ORDER_REQUEST_CUT_RATE = "cutrate";
    /**Send order request parameter**/

    /**
     * Send order request parameter
     **/
    public final static String SEND_CANCEL_ORDER_REQUEST_ORDER_REF = "orderno";
    /**Send order request parameter**/

    /**
     * Send moving to edit order request parameter
     **/
    public final static String EDIT_ORDER_REF = "ref";
    /**Send deal request parameter**/

    /**
     * Send order editing request parameter
     **/
    public final static String SEND_EDIT_ORDER_REQUEST_ORDER_REF = "orderno";
    public final static String SEND_EDIT_ORDER_REQUEST_ACTION = "order";
    public final static String SEND_EDIT_ORDER_REQUEST_CONTRACT = "contract";
    public final static String SEND_EDIT_ORDER_REQUEST_CONTRACT_SIZE = "cs";
    public final static String SEND_EDIT_ORDER_REQUEST_BUY_SELL = "bs";
    public final static String SEND_EDIT_ORDER_REQUEST_REQUEST_RATE = "rr";
    public final static String SEND_EDIT_ORDER_REQUEST_LIMIT_STOP = "l/s";
    public final static String SEND_EDIT_ORDER_REQUEST_OCO = "oco";
    public final static String SEND_EDIT_ORDER_REQUEST_LOT = "lt";
    public final static String SEND_EDIT_ORDER_REQUEST_POSITION_REF = "ref";
    public final static String SEND_EDIT_ORDER_REQUEST_POSITION_AMOUNT = "amount";
    public final static String SEND_EDIT_ORDER_REQUEST_GOOD_TILL = "gt";
    public final static String SEND_EDIT_ORDER_REQUEST_LIQ_REF = "liqrate";
    public final static String SEND_EDIT_ORDER_REQUEST_TRAILING_STOP_SIZE = "trail";
    public final static String SEND_EDIT_ORDER_REQUEST_CURR_RATE = "cr";
    public final static String SEND_EDIT_ORDER_REQUEST_LIQ_METHOD = "liqmethod";
    public final static String SEND_EDIT_ORDER_REQUEST_PROFIT_RATE = "profitrate";
    public final static String SEND_EDIT_ORDER_REQUEST_CUT_RATE = "cutrate";
    /**Send order editing parameter**/

    /**
     * Send executed order enquery parameter
     **/
    public final static String SRV_SEND_EXECUTED_ORDER_HISTORY_REQUEST_FROM = "fd";
    public final static String SRV_SEND_EXECUTED_ORDER_HISTORY_REQUEST_TO = "td";
    /**Send executed order enquery parameter**/

    /**
     * Send cancelled order enquery parameter
     **/
    public final static String SRV_SEND_CANCELLED_ORDER_HISTORY_REQUEST_FROM = "fd";
    public final static String SRV_SEND_CANCELLED_ORDER_HISTORY_REQUEST_TO = "td";
    /**Send cancelled order enquery parameter**/

    /**
     * Send liquidation enquery parameter
     **/
    public final static String SRV_SEND_LIQUIDATION_HISTORY_REQUEST_FROM = "fd";
    public final static String SRV_SEND_LIQUIDATION_HISTORY_REQUEST_TO = "td";
    /**
     * Send liquidation enquery parameter
     **/

    public final static String FACEBOOK_MATCHER = "Executed.";
    public final static String FACEBOOK_MESSAGE = "facebook_msg";

    public final static String MESSAGE = "MESSAGE";

    /**
     * Send Deal Frame Open Close parameter
     **/
    public final static String SEND_DEAL_FRAME_OPEN_CLOSE_ACTION = "act";
    public final static String SEND_DEAL_FRAME_OPEN_CLOSE_ACCOUNT = "ac";
    public final static String SEND_DEAL_FRAME_OPEN_CLOSE_CONTRACT = "contract";
    public final static String SEND_DEAL_FRAME_OPEN_CLOSE_BUY_SELL = "b/s";
    public final static String SEND_DEAL_FRAME_OPEN_CLOSE_TIME = "time";
    public final static String SEND_DEAL_FRAME_OPEN_NUM_OF_ITEM = "noitem";
    /**
     * Send Deal Frame Open Close parameter
     **/

    public final static String PRICE_ALERT_REF = "ref";
    public final static String PRICE_ALERT_CONTRACT = "contract";
    /**
     * Send Price Alert parameter
     **/
    public final static String SEND_PRICE_ALERT_MODE = "mode";
    public final static String SEND_PRICE_ALERT_CONTRACT = "contract";
    public final static String SEND_PRICE_ALERT_ACCOUNT = "acc";
    public final static String SEND_PRICE_ALERT_TYPE = "type";
    public final static String SEND_PRICE_ALERT_GOODTILL = "goodtill";
    public final static String SEND_PRICE_ALERT_PRICE = "alertprice";
    public final static String SEND_PRICE_ALERT_VOLATILITY = "alertvolatility";
    public final static String SEND_PRICE_ALERT_ID = "id";
    public final static String SEND_PRICE_ALERT_ACTIVE = "active";
    /**Send Price Alert parameter**/
}



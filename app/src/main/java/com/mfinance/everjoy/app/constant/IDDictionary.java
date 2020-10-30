package com.mfinance.everjoy.app.constant;

public class IDDictionary {

    /***************** trader service **********************/
    public static final int TRADER_LOGIN_SERVICE_TYPE = 1;
    public static final int TRADER_IO_SERVICE_TYPE = 2;
    public static final int TRADER_ORDER_SERVICE_TYPE = 4;
    public static final int TRADER_DEAL_SERVICE_TYPE = 5;
    public static final int TRADER_SYSTEM_TYPE = 6;
    public static final int TRADER_LIVE_PRICE_TYPE = 7;
    public static final int TRADER_HISTORY_LIST_TYPE = 8;
    public static final int TRADER_HEARTBEAT_TYPE = 9;
    public static final int TRADER_ACCOUNT_INFO_TYPE = 10;
    public static final int TRADER_NEWS_LIST_TYPE = 11;
    public static final int TRADER_BANK_INFORMATION_TYPE = 13;
    public static final int TRADER_COMPANY_INFORMATION_TYPE = 15;
    public static final int TRADER_WITHDRAWAL_LIST_TYPE = 16;
    public static final int TRADER_PRICE_ALERT_TYPE = 20;
    /***************** end of trader Service ****************/

    /***************** Trader Functions of Live Price Type **********************/
    public static final int TRADER_UPDATE_LIVE_PRICE = 1;
    public static final int TRADER_UPDATE_PRICE_SPREAD = 3;
    public static final int TRADER_UPDATE_STREAM_PRICE = 4;
    public static final int TRADER_UPDATE_REPORT_GROUP_CONTRACT_SETTING = 4;
    public static final int TRADER_UPDATE_HIGH_LOW = 5;
    public static final int TRADER_UPDATE_LIVE_PRICE_WITH_PRICE_AGENT_CONNECTION = 255;

    public static final int TRADER_UPDATE_LIVE_PRICE_WITH_DEPTH = 7;

    /***************** Trader Functions of Account Info Type **********************/
    public static final int TRADER_UPDATE_ACCOUNT_INFO = 1;

    /***************** Trader Functions of Login Service Type **********************/
    public static final int TRADER_REQUEST_LOGIN_RETURN = 1;
    public static final int TRADER_REQUEST_LOGIN_SECURITY_RETURN = 2;
    public static final int TRADER_REQUEST_LOGIN_SECURITY_OTP_RETURN = 3;
    public static final int TRADER_REQUEST_LOGIN_SECURITY_LOGOUT_RETURN = 4;

    /***************** Trader Functions of IO Service Type **********************/
    public static final int TRADER_REQUEST_CHANGE_SECURITY_PASSWORD_RETURN = 1;
    public static final int TRADER_REQUEST_CHANGE_PASSWORD_RETURN = 3;

    /***************** Trader Functions of deal Service Type **********************/
    public static final int TRADER_REQUEST_DEAL_RETURN = 1;
    public static final int TRADER_RECEIVE_DEALER_ESTABLISH_DEAL = 2;
    public static final int TRADER_RECEIVE_DEAL_MSG = 3;
    public static final int TRADER_RECEIVE_MULTIPLE_LIQUIDATE_RETURN = 7;

    /******************* Trader Functions of System Type ************************/
    public static final int TRADER_SHOW_SYSTEM_MSG = 1;
    public static final int TRADER_SHOW_ALERT_MSG = 2;
    @Deprecated // TODO Why can't find in Platform?
    public static final int TRADER_SHOW_MARGIN_MSG = 3;

    /***************** Trader Functions of order Service Type **********************/
    public static final int TRADER_REQUEST_ORDER_RETURN = 1;
    public static final int TRADER_RECEIVE_DEALER_ESTABLISH_ORDER = 2;
    public static final int TRADER_RECEIVE_ORDER_MSG = 3;

    /***************** Trader Functions of deal history list Type **********************/
    public static final int TRADER_WORKING_ORDER_HISTORY = 2;
    public static final int TRADER_EXECUTE_ORDER_HISTORY = 3;
    public static final int TRADER_CANCEL_ORDER_HISTORY = 4;
    public static final int TRADER_TRADES_OPEN_DEAL = 7;
    public static final int TRADER_TRADES_LIQUIDATION = 8;

    /***************** Trader Functions of Bank Information Type *******************/
    public static final int TRADER_BANK_INFORMATION = 1;

    /***************** Trader Functions of Bank Information Type *******************/
    public static final int TRADER_COMPANY_INFORMATION = 1;

    /***************** Trader Functions of WithDrawal List Type **********************/
    public static final int TRADER_WITHDRAWAL_HISTORY = 1;
    public static final int TRADER_WITHDRAWAL_STATUS_NO_CHANGE = 2;
    public static final int TRADER_WITHDRAWAL_STATUS_UPDATED = 3;

    /***************** Trader Functions of Price Alert Type **********************/
    public static final int TRADER_RECEIVE_PRICE_ALERTS = 1;
    public static final int TRADER_UPDATE_PRICE_ALERTS = 2;
    public static final int TRADER_REMOVE_PRICE_ALERTS = 3;
    public static final int TRADER_ALERT_PRICE_ALERTS = 4;
    public static final int TRADER_PUSH_PRICE_ALERTS = 99;

    /***************** server service **********************/
    public static final int SERVER_LOGIN_SERVICE_TYPE = 1;
    public static final int SERVER_IO_SERVICE_TYPE = 2;
    public static final int SERVER_ORDER_SERVICE_TYPE = 4;
    public static final int SERVER_DEAL_SERVICE_TYPE = 5;
    public static final int SERVER_HEARTBEAT_SERVICE_TYPE = 8;
    /***************** server service **********************/

    /******************* Server Functions of Login Service ************************/
    public static final int SERVER_LOGIN_LOGIN = 1;
    public static final int SERVER_LOGIN_SECURITY_LOGOUT = 2;
    public static final int SERVER_LOGIN_LOGOUT_BY_SESSION_TIMEOUT = 3;
    public static final int SERVER_LOGIN_KEYEXHANGE = 5;
    public static final int SERVER_LOGIN_PRICE_AGENT = 6;
    public static final int SERVER_LOGIN_LOGIN_SECURITY = 7;
    public static final int SERVER_LOGIN_LOGIN_SECURITY_OTP = 8;

    /******************* Server Functions of IO Service ************************/
    public static final int SERVER_IO_REQUEST_CHANGE_PASSWORD = 6;
    public static final int SERVER_IO_REQUEST_CHANGE_SECURITY_PASSWORD = 7;

    public static final int SERVER_IO_REQUEST_EXECUTE_ORDER_HISTORY = 10;
    @Deprecated
    public static final int SERVER_IO_REQUEST_CANCEL_ORDER_OLD = 11;
    public static final int SERVER_IO_REQUEST_CANCEL_ORDER_HISTORY = 11;
    public static final int SERVER_IO_REPORT_ERRORS = 22;
    public static final int SERVER_IO_REQUEST_CANCEL_ORDER = 73;
    public static final int SERVER_IO_REQUEST_TRADES_LIQUIDATION = 32;
    public static final int SERVER_IO_REQUEST_DEPOSIT_WITHDRAWAL = 47;
    public static final int SERVER_IO_REQUEST_BANK_INFORMATION = 51;
    public static final int SERVER_IO_REQUEST_COMPANY_TERMS = 63;
    public static final int SERVER_IO_REQUEST_WITHDRAWAL_LIST = 64;
    public static final int SERVER_IO_TRADER_RESPONSE_DISCONNECT_ACCOUNT = 79;
    public static final int SERVER_IO_TRADER_SEND_DEAL_INPUT_FRAME_OPEN_CLOSE = 86;
    @Deprecated
    public static final int SERVER_IO_TRADER_SEND_DEAL_INPUT_FRAME_OPEN_CLOSE_OLD = 84;
    public static final int SERVER_IO_REQUEST_UPDATE_PRICE_ALERT = 106;

    /******************* Server Functions of Deal Service ************************/
    public static final int SERVER_DEAL_REQUEST_DEAL = 1;
    @Deprecated // Success new protocol?
    public static final int SERVER_DEAL_REQUEST_DEAL_NEW = 16;

    /******************* Server Functions of Order Service ************************/
    public static final int SERVER_ORDER_REQUEST_ORDER = 1;
    @Deprecated // Success new protocol?
    public static final int SERVER_ORDER_REQUEST_ORDER_NEW = 15;
    public static final int SERVER_DEAL_REQUEST_MULTIPLE_LIQUIDATE = 16;
    /***************** Trader Functions of News List Type **********************/
    public static final int TRADER_NEWS_HISTORY = 1;
}

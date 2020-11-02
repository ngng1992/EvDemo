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
    public final static int SRV_DISCONNECT = 5;
    public final static int SRV_ANDROID_MARKET = 6;
    public final static int SRV_SHOW_TOAST = 7;
    public final static int SRV_MOVE_TO_LOGIN = 8;
    public final static int SRV_TO_SHOW_ANDROID_MARKET_MSG = 9;
    public static final int SRV_MOVE_TO_COMPANY_PROFILE = 10;
    public static final int SRV_FINISH_ACTIVITY = 11;
    public final static int SRV_CHANGE_PASSWORD = 12;
    public final static int SRV_SEND_CHANGE_PASSWORD_REQUEST = 13;
    public final static int SRV_DEFAULT_LOGIN_PAGE = 14;
    public final static int SRV_RESET_PASSWORD = 15;
    public final static int SRV_MOVE_TO_MAIN_PAGE = 16;
    public final static int SRV_LOGIN_SECURITY = 17;
    public final static int SRV_LOGOUT_SECURITY = 18;
    public final static int SRV_MOVE_TO_FORGOT_PASSWORD_OTP = 19;
    public final static int SRV_SEND_CHANGE_PASSWORD_OTP_REQUEST = 20;

    public final static int ACT_UPDATE_UI = 0;
    public final static int ACT_GO_TO_DASHBOARD = 1;
    public final static int ACT_GO_TO_LOGIN = 2;
    public final static int ACT_GO_TO_PRICE = 3;
    public final static int ACT_VIBRATE = 4;
    public final static int ACT_VISIBLE_POP_UP = 5;
    public final static int ACT_INVISIBLE_POP_UP = 6;
    public final static int ACT_GO_TO_ANDROID_MARKET = 7;
    public final static int ACT_DISCONNECT = 8;
    public final static int ACT_SHOW_TOAST = 9;
    public final static int ACT_UNBIND_SERVICE = 10;
    public final static int ACT_GO_TO_SHOW_ANDROID_MARKET_MSG = 11;
    public final static int ACT_GO_TO_COMPANY_PROFILE = 12;
    public final static int ACT_FINISH_ACTIVITY = 13;
    public final static int ACT_SHOW_ACC_INFO = 14;
    public final static int ACT_GO_TO_LOST_PASSWORD = 15;
    public final static int ACT_GO_TO_CHANGE_PASSWORD = 16;
    public final static int ACT_SYSTEM_ALERT_MESSAGE = 17;
    public final static int ACT_GO_TO_DEFAULT_LOGIN_PAGE = 18;
    public final static int ACT_GO_TO_MAIN_PAGE = 19;
    public final static int ACT_DISCONNECT_DUPLICATE = 20;
    public final static int ACT_GO_TO_OTP_LOGIN_PAGE = 21;
    public final static int ACT_SHOW_DIALOG = 22;
    public final static int ACT_GO_TO_FORGOT_PASSWORD_OTP_PAGE = 23;
    public final static int ACT_GO_TO_FINGER_ID_PAGE = 24;
    public final static int ACT_RECONNECT_SECURITY = 25;
    public final static int ACT_SHOW_FAIL_PASSWORD_DIALOG = 26;
    public final static int ACT_LOGOUT_SECURITY = 27;

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
    public final static String LOGIN_PLATFORM_TYPE = "ptype";
    public final static String LOGIN_CONN_INDEX = "connidx";
    public final static String LOGIN_LEVEL = "level";
    public final static String LOGIN_TYPE = "type";
    public final static String LOGIN_EMAIL = "email";
    public final static String LOGIN_PASSWORD = "passwd";
    public final static String LOGIN_PASSWORDTOKEN = "pwdToken";
    public final static String LOGIN_OTYPE = "otype";
    public final static String LOGIN_OPENID = "open_id";
    public final static String LOGIN_USERNAME = "username";
    public final static String LOGIN_SEC_PREFIX = "prefix";
    public final static String LOGIN_SEC_EMAIL = "email";
    public final static String LOGIN_SEC_OTP = "otp";

    public final static String RESETPASSWORD_TYPE = "type";
    public final static String RESETPASSWORD_OLDPASSWORD = "oldpwd";
    public final static String RESETPASSWORD_NEWPASSWORD = "newpwd";

    public final static String FORGETPASSWORD_EMAIL = "email";
    public final static String FORGETPASSWORD_TYPE = "type";
    public final static String FORGETPASSWORD_PREFIX = "prefix";
    public final static String FORGETPASSWORD_OTP = "otp";
    public final static String FORGETPASSWORD_RESEND = "resend";

    public final static String FINISH = "FINISH";
    public final static String MESSAGE = "MESSAGE";
    public final static String TITLE = "TITLE";
    public final static String COUNT = "COUNT";
}



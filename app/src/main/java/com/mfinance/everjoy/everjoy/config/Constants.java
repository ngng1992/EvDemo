package com.mfinance.everjoy.everjoy.config;

/**
 * 全局配置
 */
public class Constants {

    public static final String APP_LAUNCHER_URL_DOMAIN_1 = "applauncher.mfcloud.net";
    public static final String APP_LAUNCHER_URL_DOMAIN_2 = "applauncher.tradingengine.net";

    public static final int APP_LAUNCHER_URL_PORT_1 = 2083;
    public static final int APP_LAUNCHER_URL_PORT_2 = 2083;

    public static final String EMAIL = "email";
    public static final String OTP = "otp";
    public static final String PREFIX = "prefix";
    public static final String IS_FORGET_PWD = "isForgetPwd";

    public static final String LANGUAGE = "language";

    /**
     * 身份类型
     * 1:hk
     * 2：ch
     * 3：其他
     */
    public static final String CARD_TYPE = "card_type";
    public static final int CARD_HK_TYPE = 1;
    public static final int CARD_CH_TYPE = 2;
    public static final int CARD_OTHER_TYPE = 3;

    /**
     * 文件路径
     */
    public static final String KEY_FILE_PATH = "file_path";


    // 选择照片，调用原生
    public static final int REQUEST_CODE_SELECT_PHOTO = 100;
    // 选择拍照，调用原生
    public static final int REQUEST_CODE_SELECT_CAMERA = 101;


    /**
     * 拍照返回码
     */
    public static final int KEY_CAMERA_RESULT_OK = 200;
    /**
     * 拍照请求码
     */
    public static final int KEY_CAMERA_REQUEST_CODE = 201;

    // 是否横屏
    public static final String IS_NEED_LAND = "isNeedLand";
}

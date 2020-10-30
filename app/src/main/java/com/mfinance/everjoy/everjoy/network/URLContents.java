package com.mfinance.everjoy.everjoy.network;

/**
 * 接口地址
 */
public class URLContents {

    /**
     * IP地址
     */
    public static final String BASE_URL = "http://202.155.229.105:8628/webservice/";

    /**
     * 邮箱注册
     */
    public static final String REGISTER = BASE_URL + "register";

    public static final String FORGOTPASSWORD = BASE_URL + "forgotpassword";
    public static final String FORGOTSECPASSWORD = BASE_URL + "forgotsecuritypassword";

    /**
     * 开通证券账户
     */
    public static final String REGISTER_SECURITY_ACCOUNT = BASE_URL + "registersecurityaccount";

}

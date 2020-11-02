package com.mfinance.everjoy.everjoy.service;

import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.service.ServerConnection;

/**
 * 登录伺服器
 */
public class LoginFunction {

    private static final String TAG = LoginFunction.class.getSimpleName();


    public void login(String username, String password, MobileTraderApplication application) {
//        String strURL = "";
//        int iPort = -1;
//
//        String sPlatformType = "DEMO";
//        String strUserID = CompanySettings.COMPANY_PREFIX + username;
//
//        int iConnIdx = -1 ;
//
//       application.data.setStrUser(strUserID);
//
//        if(FXConstants.DEMO.equals(sPlatformType)){
//            if(iConnIdx == -1 || iConnIdx >=application.alLoginInfoDemo.size() )
//            {
//                strURL =application.loginInfoDemo.sURL;
//                iPort = Utility.toInteger(application.loginInfoDemo.sPort, 15000);
//            }
//            else
//            {
//                strURL =application.alLoginInfoDemo.get(iConnIdx).sURL;
//                iPort = Utility.toInteger(application.alLoginInfoDemo.get(iConnIdx).sPort, 15000);
//            }
//           application.isDemoPlatform = true;
//            sPlatformType = CompanySettings.DEMO_REPORT_GROUP;
//        }else{
//            if (CompanySettings.checkProdServer() == 1) {
//                if(iConnIdx == -1 || iConnIdx >=application.alLoginInfoProd.size() )
//                {
//                    strURL =application.loginInfoProd.sURL;
//                    iPort = Utility.toInteger(application.loginInfoProd.sPort, 15000);
//                }
//                else
//                {
//                    strURL =application.alLoginInfoProd.get(iConnIdx).sURL;
//                    iPort = Utility.toInteger(application.alLoginInfoProd.get(iConnIdx).sPort, 15000);
//                }
//            } else if (CompanySettings.checkProdServer() == 2) {
//                if(iConnIdx == -1 || iConnIdx >=application.alLoginInfoProd2.size() )
//                {
//                    strURL =application.loginInfoProd2.sURL;
//                    iPort = Utility.toInteger(application.loginInfoProd2.sPort, 15000);
//                }
//                else
//                {
//                    strURL =application.alLoginInfoProd2.get(iConnIdx).sURL;
//                    iPort = Utility.toInteger(application.alLoginInfoProd2.get(iConnIdx).sPort, 15000);
//                }
//            } else if (CompanySettings.checkProdServer() == 3) {
//                if(iConnIdx == -1 || iConnIdx >=application.alLoginInfoProd3.size() )
//                {
//                    strURL =application.loginInfoProd3.sURL;
//                    iPort = Utility.toInteger(application.loginInfoProd3.sPort, 15000);
//                }
//                else
//                {
//                    strURL =application.alLoginInfoProd3.get(iConnIdx).sURL;
//                    iPort = Utility.toInteger(application.alLoginInfoProd3.get(iConnIdx).sPort, 15000);
//                }
//            } else if (CompanySettings.checkProdServer() == 4) {
//                if(iConnIdx == -1 || iConnIdx >=application.alLoginInfoProd4.size() )
//                {
//                    strURL =application.loginInfoProd4.sURL;
//                    iPort = Utility.toInteger(application.loginInfoProd4.sPort, 15000);
//                }
//                else
//                {
//                    strURL =application.alLoginInfoProd4.get(iConnIdx).sURL;
//                    iPort = Utility.toInteger(application.alLoginInfoProd4.get(iConnIdx).sPort, 15000);
//                }
//            } else if (CompanySettings.checkProdServer() == 5) {
//                if(iConnIdx == -1 || iConnIdx >=application.alLoginInfoProd5.size() )
//                {
//                    strURL =application.loginInfoProd5.sURL;
//                    iPort = Utility.toInteger(application.loginInfoProd5.sPort, 15000);
//                }
//                else
//                {
//                    strURL =application.alLoginInfoProd5.get(iConnIdx).sURL;
//                    iPort = Utility.toInteger(application.alLoginInfoProd5.get(iConnIdx).sPort, 15000);
//                }
//            } else if (CompanySettings.checkProdServer() == 6) {
//                if(iConnIdx == -1 || iConnIdx >=application.alLoginInfoProd6.size() )
//                {
//                    strURL =application.loginInfoProd6.sURL;
//                    iPort = Utility.toInteger(application.loginInfoProd6.sPort, 15000);
//                }
//                else
//                {
//                    strURL =application.alLoginInfoProd6.get(iConnIdx).sURL;
//                    iPort = Utility.toInteger(application.alLoginInfoProd6.get(iConnIdx).sPort, 15000);
//                }
//            } else if (CompanySettings.checkProdServer() == 7) {
//                if(iConnIdx == -1 || iConnIdx >=application.alLoginInfoProd7.size() )
//                {
//                    strURL =application.loginInfoProd7.sURL;
//                    iPort = Utility.toInteger(application.loginInfoProd7.sPort, 15000);
//                }
//                else
//                {
//                    strURL =application.alLoginInfoProd7.get(iConnIdx).sURL;
//                    iPort = Utility.toInteger(application.alLoginInfoProd7.get(iConnIdx).sPort, 15000);
//                }
//            } else if (CompanySettings.checkProdServer() == 8) {
//                if(iConnIdx == -1 || iConnIdx >=application.alLoginInfoProd8.size() )
//                {
//                    strURL =application.loginInfoProd8.sURL;
//                    iPort = Utility.toInteger(application.loginInfoProd8.sPort, 15000);
//                }
//                else
//                {
//                    strURL =application.alLoginInfoProd8.get(iConnIdx).sURL;
//                    iPort = Utility.toInteger(application.alLoginInfoProd8.get(iConnIdx).sPort, 15000);
//                }
//            } else if (CompanySettings.checkProdServer() == 9) {
//                if(iConnIdx == -1 || iConnIdx >=application.alLoginInfoProd9.size() )
//                {
//                    strURL =application.loginInfoProd9.sURL;
//                    iPort = Utility.toInteger(application.loginInfoProd9.sPort, 15000);
//                }
//                else
//                {
//                    strURL =application.alLoginInfoProd9.get(iConnIdx).sURL;
//                    iPort = Utility.toInteger(application.alLoginInfoProd9.get(iConnIdx).sPort, 15000);
//                }
//            } else if (CompanySettings.checkProdServer() == 10) {
//                if(iConnIdx == -1 || iConnIdx >=application.alLoginInfoProd10.size() )
//                {
//                    strURL =application.loginInfoProd10.sURL;
//                    iPort = Utility.toInteger(application.loginInfoProd10.sPort, 15000);
//                }
//                else
//                {
//                    strURL =application.alLoginInfoProd10.get(iConnIdx).sURL;
//                    iPort = Utility.toInteger(application.alLoginInfoProd10.get(iConnIdx).sPort, 15000);
//                }
//            }
//
//           application.isDemoPlatform = false;
//            sPlatformType = CompanySettings.PRODUCTION_REPORT_GROUP;
//        }
//        if (CompanySettings.FOR_UAT) {
//            if(iConnIdx == -1 || iConnIdx >=application.alLoginInfoDemo.size() )
//            {
//                strURL =application.loginInfoDemo.sURL;
//                iPort = Utility.toInteger(application.loginInfoDemo.sPort, 15000);
//            }
//            else
//            {
//                strURL =application.alLoginInfoDemo.get(iConnIdx).sURL;
//                iPort = Utility.toInteger(application.alLoginInfoDemo.get(iConnIdx).sPort, 15000);
//            }
//           application.isDemoPlatform = true;
//            sPlatformType = CompanySettings.DEMO_REPORT_GROUP;
//        }
//
//        InetAddress giriAddress = null;
//        try {
//            giriAddress = java.net.InetAddress.getByName(strURL);
//        } catch (UnknownHostException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//            giriAddress = null;
//        }
//
//        String strOTP = "";
//
//       application.data.setStrURL(strURL);
//       application.data.setiPort(iPort);
//       application.data.setStrPassword(password);
//
//        try {
////            service.startConnection(strURL, iPort);
//
//
//            MessageObj loginMsg = MessageObj.getMessageObj(IDDictionary.SERVER_LOGIN_SERVICE_TYPE, IDDictionary.SERVER_LOGIN_LOGIN);
//
//            try {
//                loginMsg.addField(Protocol.LoginRequest.JAVA_VERSION, System.getProperty("java.version"));
//                loginMsg.addField(Protocol.LoginRequest.OS_ARCH, System.getProperty("os.arch"));
//                loginMsg.addField(Protocol.LoginRequest.OS_NAME, System.getProperty("os.name"));
//                loginMsg.addField(Protocol.LoginRequest.OS_VERSION, System.getProperty("os.version"));
//                loginMsg.addField(Protocol.LoginRequest.USER_DIR, System.getProperty("user.dir"));
//                loginMsg.addField(Protocol.LoginRequest.USER_HOME, System.getProperty("user.home"));
//            } catch(Exception e){}
//
//            loginMsg.addField(Protocol.LoginRequest.VERSION, FXConstants.FXTRADER_APPLICATION_VERSION);
//            loginMsg.addField(Protocol.LoginRequest.MY_ID, strUserID);
//            loginMsg.addField(Protocol.LoginRequest.MY_NAME, strUserID);
//            loginMsg.addField(Protocol.LoginRequest.PASSWORD, password);
//            loginMsg.addField(Protocol.LoginRequest.ROLE, FXConstants.TRADER_ROLE);
//            loginMsg.addField(Protocol.LoginRequest.LOGIN_TYPE, FXConstants.CLIENT_TYPE);
//            if(giriAddress != null)
//                loginMsg.addField(Protocol.LoginRequest.SERVER_IP, strURL + "/" + giriAddress.getHostAddress() );
//            else
//                loginMsg.addField(Protocol.LoginRequest.SERVER_IP, strURL);
//
//
//            loginMsg.addField(Protocol.LoginRequest.RPT_GROUP, sPlatformType);
//            loginMsg.addField(Protocol.LoginRequest.TYPE,"a");
//            if(CompanySettings.TRADE_CURR!=null){
//                loginMsg.addField(Protocol.LoginRequest.TRADE_CURR,CompanySettings.TRADE_CURR);
//            }
//            if (strURL != null && !strURL.startsWith("127.0.0") && !strURL.startsWith("192.168")) {
//                try {
//                    final String selfIP = CompanySettings.getSelfIPBySSL ? IPRetriever.httpsGet(CompanySettings.echoiplink) : IPRetriever.get(new URL(CompanySettings.echoServer));
//                    if (selfIP != null) {
//                        loginMsg.addField(Protocol.LoginRequest.SELF_IP, selfIP);
//                       application.setSelfIP(selfIP);
//                    }
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//            }
//            try {
//                WifiManager manager = (WifiManager) application.getSystemService(Context.WIFI_SERVICE);
//                @SuppressLint("HardwareIds") String macAddress = manager.getConnectionInfo().getMacAddress();
//                loginMsg.addField(Protocol.LoginRequest.NETWORK_MAC, macAddress);
//                loginMsg.addField(Protocol.LoginRequest.NETWORK_MAC_OLD, macAddress);
//                loginMsg.addField(Protocol.LoginRequest.NETWORK_MAC_SUCCESS, macAddress);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if(FXConstants.DEMO.equals(sPlatformType))
//                loginMsg.addField(Protocol.LoginRequest.IDENTITY_PASSED, "1");
//            else
//            {
//                loginMsg.addField(Protocol.LoginRequest.IDENTITY_PASSED, LoginActivity.identityPassed ? "1" : "0");
//            }
//
//            if(strOTP != null){
//                loginMsg.addField(Protocol.LoginRequest.TWO_FA_OTP, strOTP);
//            }
//
//            loginMsg.addField(Protocol.LoginRequest.BY_MANUAL, "1");
//            loginMsg.addField("enable_price_listen", CompanySettings.ENABLE_TRADER_PRICE_AGENT_CONNECTION ? "0" : "1");
//
//            if (CompanySettings.ENABLE_PRICE_ALERT &&application.data.getPushyToken()!= null) {
//                loginMsg.addField(Protocol.LoginRequest.PUSH_TOKEN,application.data.getPushyToken());
//                String sLang = "0";
//                if (application.locale == Locale.SIMPLIFIED_CHINESE)
//                    sLang = "2";
//                else if (application.locale == Locale.TRADITIONAL_CHINESE)
//                    sLang = "1";
//                loginMsg.addField(Protocol.LoginRequest.PUSH_LOCALE, sLang);
//            }
//
//            String msg = loginMsg.convertToString(true);
//            startConnection(strURL, iPort, msg);
//            Log.e(TAG, "加密后的msg = " + msg);
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
    }

    private ServerConnection connection;

    /**
     * Start connect to server
     *
     * @param strURL URL
     * @param iPort  port
     */
    public void startConnection(String strURL, int iPort, String msg) {
        ServerConnectionFunction serverConnectionFunction = new ServerConnectionFunction(strURL, iPort, msg);
        serverConnectionFunction.startConnection();
    }




}

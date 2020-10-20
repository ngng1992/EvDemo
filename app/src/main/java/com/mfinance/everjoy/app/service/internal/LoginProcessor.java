package com.mfinance.everjoy.app.service.internal;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Locale;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.LoginActivity;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.constant.Protocol.LoginRequest;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.IPRetriever;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;

public class LoginProcessor implements MessageProcessor{
	private final String TAG = "LoginProcessor";

	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		Bundle data = msg.getData();

		String strURL = "";
		int iPort = -1;

		String sPlatformType = data.getString(ServiceFunction.LOGIN_PLATFORM_TYPE);

		String strUserID = CompanySettings.COMPANY_PREFIX + data.getString(ServiceFunction.LOGIN_ID);
		
		int iConnIdx = data.getInt(ServiceFunction.LOGIN_CONN_INDEX);
		
		service.app.data.setStrUser(strUserID);
		
		if(FXConstants.DEMO.equals(sPlatformType)){
			if(iConnIdx == -1 || iConnIdx >= service.app.alLoginInfoDemo.size() )
			{
				strURL = service.app.loginInfoDemo.sURL;
				iPort = Utility.toInteger(service.app.loginInfoDemo.sPort, 15000);
			}
			else
			{
				strURL = service.app.alLoginInfoDemo.get(iConnIdx).sURL;
				iPort = Utility.toInteger(service.app.alLoginInfoDemo.get(iConnIdx).sPort, 15000);
			}
			service.app.isDemoPlatform = true;
			sPlatformType = CompanySettings.DEMO_REPORT_GROUP;
		}else{
			if (CompanySettings.checkProdServer() == 1) {
				if(iConnIdx == -1 || iConnIdx >= service.app.alLoginInfoProd.size() )
				{
					strURL = service.app.loginInfoProd.sURL;
					iPort = Utility.toInteger(service.app.loginInfoProd.sPort, 15000);
				}
				else
				{
					strURL = service.app.alLoginInfoProd.get(iConnIdx).sURL;
					iPort = Utility.toInteger(service.app.alLoginInfoProd.get(iConnIdx).sPort, 15000);
				}
			} else if (CompanySettings.checkProdServer() == 2) {
				if(iConnIdx == -1 || iConnIdx >= service.app.alLoginInfoProd2.size() )
				{
					strURL = service.app.loginInfoProd2.sURL;
					iPort = Utility.toInteger(service.app.loginInfoProd2.sPort, 15000);
				}
				else
				{
					strURL = service.app.alLoginInfoProd2.get(iConnIdx).sURL;
					iPort = Utility.toInteger(service.app.alLoginInfoProd2.get(iConnIdx).sPort, 15000);
				}
			} else if (CompanySettings.checkProdServer() == 3) {
				if(iConnIdx == -1 || iConnIdx >= service.app.alLoginInfoProd3.size() )
				{
					strURL = service.app.loginInfoProd3.sURL;
					iPort = Utility.toInteger(service.app.loginInfoProd3.sPort, 15000);
				}
				else
				{
					strURL = service.app.alLoginInfoProd3.get(iConnIdx).sURL;
					iPort = Utility.toInteger(service.app.alLoginInfoProd3.get(iConnIdx).sPort, 15000);
				}
			} else if (CompanySettings.checkProdServer() == 4) {
				if(iConnIdx == -1 || iConnIdx >= service.app.alLoginInfoProd4.size() )
				{
					strURL = service.app.loginInfoProd4.sURL;
					iPort = Utility.toInteger(service.app.loginInfoProd4.sPort, 15000);
				}
				else
				{
					strURL = service.app.alLoginInfoProd4.get(iConnIdx).sURL;
					iPort = Utility.toInteger(service.app.alLoginInfoProd4.get(iConnIdx).sPort, 15000);
				}
			} else if (CompanySettings.checkProdServer() == 5) {
				if(iConnIdx == -1 || iConnIdx >= service.app.alLoginInfoProd5.size() )
				{
					strURL = service.app.loginInfoProd5.sURL;
					iPort = Utility.toInteger(service.app.loginInfoProd5.sPort, 15000);
				}
				else
				{
					strURL = service.app.alLoginInfoProd5.get(iConnIdx).sURL;
					iPort = Utility.toInteger(service.app.alLoginInfoProd5.get(iConnIdx).sPort, 15000);
				}
			} else if (CompanySettings.checkProdServer() == 6) {
				if(iConnIdx == -1 || iConnIdx >= service.app.alLoginInfoProd6.size() )
				{
					strURL = service.app.loginInfoProd6.sURL;
					iPort = Utility.toInteger(service.app.loginInfoProd6.sPort, 15000);
				}
				else
				{
					strURL = service.app.alLoginInfoProd6.get(iConnIdx).sURL;
					iPort = Utility.toInteger(service.app.alLoginInfoProd6.get(iConnIdx).sPort, 15000);
				}
			} else if (CompanySettings.checkProdServer() == 7) {
				if(iConnIdx == -1 || iConnIdx >= service.app.alLoginInfoProd7.size() )
				{
					strURL = service.app.loginInfoProd7.sURL;
					iPort = Utility.toInteger(service.app.loginInfoProd7.sPort, 15000);
				}
				else
				{
					strURL = service.app.alLoginInfoProd7.get(iConnIdx).sURL;
					iPort = Utility.toInteger(service.app.alLoginInfoProd7.get(iConnIdx).sPort, 15000);
				}
			} else if (CompanySettings.checkProdServer() == 8) {
				if(iConnIdx == -1 || iConnIdx >= service.app.alLoginInfoProd8.size() )
				{
					strURL = service.app.loginInfoProd8.sURL;
					iPort = Utility.toInteger(service.app.loginInfoProd8.sPort, 15000);
				}
				else
				{
					strURL = service.app.alLoginInfoProd8.get(iConnIdx).sURL;
					iPort = Utility.toInteger(service.app.alLoginInfoProd8.get(iConnIdx).sPort, 15000);
				}
			} else if (CompanySettings.checkProdServer() == 9) {
				if(iConnIdx == -1 || iConnIdx >= service.app.alLoginInfoProd9.size() )
				{
					strURL = service.app.loginInfoProd9.sURL;
					iPort = Utility.toInteger(service.app.loginInfoProd9.sPort, 15000);
				}
				else
				{
					strURL = service.app.alLoginInfoProd9.get(iConnIdx).sURL;
					iPort = Utility.toInteger(service.app.alLoginInfoProd9.get(iConnIdx).sPort, 15000);
				}
			} else if (CompanySettings.checkProdServer() == 10) {
				if(iConnIdx == -1 || iConnIdx >= service.app.alLoginInfoProd10.size() )
				{
					strURL = service.app.loginInfoProd10.sURL;
					iPort = Utility.toInteger(service.app.loginInfoProd10.sPort, 15000);
				}
				else
				{
					strURL = service.app.alLoginInfoProd10.get(iConnIdx).sURL;
					iPort = Utility.toInteger(service.app.alLoginInfoProd10.get(iConnIdx).sPort, 15000);
				}
			}
			
			service.app.isDemoPlatform = false;
			sPlatformType = CompanySettings.PRODUCTION_REPORT_GROUP;
		}
		if (CompanySettings.FOR_UAT) {
			if(iConnIdx == -1 || iConnIdx >= service.app.alLoginInfoDemo.size() )
			{
				strURL = service.app.loginInfoDemo.sURL;
				iPort = Utility.toInteger(service.app.loginInfoDemo.sPort, 15000);
			}
			else
			{
				strURL = service.app.alLoginInfoDemo.get(iConnIdx).sURL;
				iPort = Utility.toInteger(service.app.alLoginInfoDemo.get(iConnIdx).sPort, 15000);
			}
			service.app.isDemoPlatform = true;
			sPlatformType = CompanySettings.DEMO_REPORT_GROUP;
		}

		InetAddress giriAddress = null;
		try {
			giriAddress = java.net.InetAddress.getByName(strURL);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			giriAddress = null;
		}
		
		String strPassword = data.getString(ServiceFunction.LOGIN_PASSWORD);

		String strOTP = data.getString(Protocol.LoginRequest.TWO_FA_OTP);

		service.app.data.setStrURL(strURL);
		service.app.data.setiPort(iPort);
		service.app.data.setStrPassword(strPassword);

		try {
			service.startConnection(strURL, iPort);

			MessageObj loginMsg = MessageObj.getMessageObj(IDDictionary.SERVER_LOGIN_SERVICE_TYPE, IDDictionary.SERVER_LOGIN_LOGIN);

			try {
				loginMsg.addField(Protocol.LoginRequest.JAVA_VERSION, System.getProperty("java.version"));
				loginMsg.addField(Protocol.LoginRequest.OS_ARCH, System.getProperty("os.arch"));
				loginMsg.addField(Protocol.LoginRequest.OS_NAME, System.getProperty("os.name"));
				loginMsg.addField(Protocol.LoginRequest.OS_VERSION, System.getProperty("os.version"));
				loginMsg.addField(Protocol.LoginRequest.USER_DIR, System.getProperty("user.dir"));
				loginMsg.addField(Protocol.LoginRequest.USER_HOME, System.getProperty("user.home"));
			} catch(Exception e){}

			loginMsg.addField(Protocol.LoginRequest.VERSION, FXConstants.FXTRADER_APPLICATION_VERSION);
			loginMsg.addField(Protocol.LoginRequest.MY_ID, strUserID);
			loginMsg.addField(Protocol.LoginRequest.MY_NAME, strUserID);
			loginMsg.addField(Protocol.LoginRequest.PASSWORD, strPassword);
			loginMsg.addField(Protocol.LoginRequest.ROLE, FXConstants.TRADER_ROLE);
			loginMsg.addField(Protocol.LoginRequest.LOGIN_TYPE, FXConstants.CLIENT_TYPE);
			if(giriAddress != null)
				loginMsg.addField(Protocol.LoginRequest.SERVER_IP, strURL + "/" + giriAddress.getHostAddress() );
			else
				loginMsg.addField(Protocol.LoginRequest.SERVER_IP, strURL);
			

			loginMsg.addField(Protocol.LoginRequest.RPT_GROUP, sPlatformType);
			loginMsg.addField(Protocol.LoginRequest.TYPE,"a");
			if(CompanySettings.TRADE_CURR!=null){
				loginMsg.addField(Protocol.LoginRequest.TRADE_CURR,CompanySettings.TRADE_CURR);
			}
			if (strURL != null && !strURL.startsWith("127.0.0") && !strURL.startsWith("192.168")) {
				try {
					final String selfIP = CompanySettings.getSelfIPBySSL ? IPRetriever.httpsGet(CompanySettings.echoiplink) : IPRetriever.get(new URL(CompanySettings.echoServer));
					if (selfIP != null) {
						loginMsg.addField(Protocol.LoginRequest.SELF_IP, selfIP);
						service.app.setSelfIP(selfIP);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
			try {
				WifiManager manager = (WifiManager) service.getSystemService(Context.WIFI_SERVICE);
				String macAddress = manager.getConnectionInfo().getMacAddress();
				loginMsg.addField(Protocol.LoginRequest.NETWORK_MAC, macAddress);
				loginMsg.addField(LoginRequest.NETWORK_MAC_OLD, macAddress);
				loginMsg.addField(Protocol.LoginRequest.NETWORK_MAC_SUCCESS, macAddress);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if(FXConstants.DEMO.equals(sPlatformType))
				loginMsg.addField(Protocol.LoginRequest.IDENTITY_PASSED, "1");
			else
			{
				loginMsg.addField(Protocol.LoginRequest.IDENTITY_PASSED, LoginActivity.identityPassed ? "1" : "0");
			}

			if(strOTP != null){
				loginMsg.addField(Protocol.LoginRequest.TWO_FA_OTP, strOTP);
			}

			loginMsg.addField(LoginRequest.BY_MANUAL, "1");
			loginMsg.addField("enable_price_listen", CompanySettings.ENABLE_TRADER_PRICE_AGENT_CONNECTION ? "0" : "1");

			if (CompanySettings.ENABLE_PRICE_ALERT && service.app.data.getPushyToken()!= null) {
				loginMsg.addField(LoginRequest.PUSH_TOKEN, service.app.data.getPushyToken());
				String sLang = "0";
				if (service.app.locale == Locale.SIMPLIFIED_CHINESE)
					sLang = "2";
				else if (service.app.locale == Locale.TRADITIONAL_CHINESE)
					sLang = "1";
				loginMsg.addField(LoginRequest.PUSH_LOCALE, sLang);
			}

			service.connection.sendMessage(loginMsg.convertToString(true));
//			Log.i(TAG, loginMsg.convertToString(false));
//			Log.i(TAG, "[LoginProcessor]["+loginMsg.convertToString(true)+"]");
//			Log.i(TAG, "[LoginProcessor]["+loginMsg.convertToString(false)+"]");


			//service.startResponseHandler();
			service.bReloadStatus = false;

			return true;
		} catch (Exception e) {
			service.app.isLoading = false;
			service.broadcast(ServiceFunction.ACT_INVISIBLE_POP_UP, null);
			service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);  
			e.printStackTrace();
			return false;
		}	
	}
}

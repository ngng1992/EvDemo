package com.mfinance.everjoy.app.service.internal;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import com.mfinance.everjoy.app.CompanySettings;
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

public class LoginProcessor implements MessageProcessor{
	private final String TAG = "LoginProcessor";
	private static class Type {
		private static int EMAIL = -1;
		private static int OAUTH = 0;
	}

	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {

		Bundle data = msg.getData();
		String level = data.getString(ServiceFunction.LOGIN_LEVEL);

		try {
			if (level.equals("2")) {
				int type = Integer.parseInt(data.getString(ServiceFunction.LOGIN_TYPE));
				String strURL = "";
				int iPort = -1;

				String strUserID = "";
				String strPwdToken = "";
				String strOpenID = "";
				String strOType = "";
				String strPassword = "";
				String strSelfIP = "";

				try {
					final String selfIP = CompanySettings.getSelfIPBySSL ? IPRetriever.httpsGet(CompanySettings.echoiplink) : IPRetriever.get(new URL(CompanySettings.echoServer));
					if (selfIP != null) {
						//
						strSelfIP = selfIP;
						service.app.setSelfIP(selfIP);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

				int iConnIdx = data.getInt(ServiceFunction.LOGIN_CONN_INDEX);



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
				}

				InetAddress giriAddress = null;
				try {
					giriAddress = java.net.InetAddress.getByName(strURL);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					giriAddress = null;
				}

				service.app.setLoginType(type);

				if (type == Type.EMAIL){
					strUserID = data.getString(ServiceFunction.LOGIN_EMAIL);
					strPwdToken = data.getString(ServiceFunction.LOGIN_PASSWORDTOKEN);
					service.app.data.setStrEmail(strUserID);
				}else{
					strUserID = data.getString(ServiceFunction.LOGIN_USERNAME);
					strOType = data.getString(ServiceFunction.LOGIN_OTYPE);
					strOpenID = data.getString(ServiceFunction.LOGIN_OPENID);
				}

				service.app.setLoginID(strUserID);
				strPassword = data.getString(ServiceFunction.LOGIN_PASSWORD);

				service.app.data.setStrURL(strURL);
				service.app.data.setiPort(iPort);
				service.app.data.setStrPassword(strPassword);

				service.startConnection(strURL, iPort);

				MessageObj loginMsg = MessageObj.getMessageObj(IDDictionary.SERVER_LOGIN_SERVICE_TYPE, IDDictionary.SERVER_LOGIN_LOGIN);

				if (type == Type.EMAIL) {
					loginMsg.addField(LoginRequest.EMAIL, strUserID);
					if (strPwdToken != null) {
						loginMsg.addField(LoginRequest.PASSWORD_TOKEN, strPwdToken);
					}
				} else {
					loginMsg.addField(LoginRequest.USERNAME, strUserID);
					loginMsg.addField(LoginRequest.OPENID, strOpenID);
					loginMsg.addField(LoginRequest.OTYPE, strOType);
				}

				loginMsg.addField(Protocol.LoginRequest.PASSWORD, strPassword);
				if (giriAddress != null)
					loginMsg.addField(Protocol.LoginRequest.SERVER_IP, strURL + "/" + giriAddress.getHostAddress());
				else
					loginMsg.addField(Protocol.LoginRequest.SERVER_IP, strURL);

				try {
					loginMsg.addField(Protocol.LoginRequest.OS_VERSION, System.getProperty("os.version"));
				} catch (Exception e) {
				}
				loginMsg.addField(Protocol.LoginRequest.SELF_IP, strSelfIP);
				loginMsg.addField(Protocol.LoginRequest.TYPE, "a");

				service.connection.sendMessage(loginMsg.convertToString(true));
				Log.i(TAG, loginMsg.convertToString(false));
				Log.i(TAG, "[LoginProcessor]["+loginMsg.convertToString(true)+"]");
				Log.i(TAG, "[LoginProcessor]["+loginMsg.convertToString(false)+"]");
			}
			else if (level.equals("3")){
				String strPwdToken = data.getString(ServiceFunction.LOGIN_PASSWORDTOKEN);
				String strUserID = data.getString(ServiceFunction.LOGIN_USERNAME);
				String strPassword = data.getString(ServiceFunction.LOGIN_PASSWORD);

				service.app.setSecLoginID(strUserID);
				service.app.tempSecPwd = strPassword;

				MessageObj loginMsg = MessageObj.getMessageObj(IDDictionary.SERVER_LOGIN_SERVICE_TYPE, IDDictionary.SERVER_LOGIN_LOGIN_SECURITY);
				loginMsg.addField(LoginRequest.ACC, strUserID);
				if (strPwdToken == null)
					loginMsg.addField(LoginRequest.PASSWORD, strPassword);
				else {
					loginMsg.addField(LoginRequest.PASSWORD_TOKEN, service.app.getSecPasswordToken());
				}

				service.connection.sendMessage(loginMsg.convertToString(true));
				Log.i(TAG, loginMsg.convertToString(false));
				Log.i(TAG, "[LoginProcessor]["+loginMsg.convertToString(true)+"]");
				Log.i(TAG, "[LoginProcessor]["+loginMsg.convertToString(false)+"]");
			}
			else if (level.equals("3.1")){
				String strOTP = data.getString(ServiceFunction.LOGIN_SEC_OTP);

				MessageObj loginMsg = MessageObj.getMessageObj(IDDictionary.SERVER_LOGIN_SERVICE_TYPE, IDDictionary.SERVER_LOGIN_LOGIN_SECURITY_OTP);
				loginMsg.addField(LoginRequest.ACC, service.app.getSecLoginID());
				loginMsg.addField(LoginRequest.OTP, strOTP);

				service.connection.sendMessage(loginMsg.convertToString(true));
				Log.i(TAG, loginMsg.convertToString(false));
				Log.i(TAG, "[LoginProcessor]["+loginMsg.convertToString(true)+"]");
				Log.i(TAG, "[LoginProcessor]["+loginMsg.convertToString(false)+"]");
			}

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

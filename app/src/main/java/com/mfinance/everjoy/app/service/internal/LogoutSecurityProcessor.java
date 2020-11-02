package com.mfinance.everjoy.app.service.internal;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.Protocol.LoginRequest;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;

public class LogoutSecurityProcessor implements MessageProcessor{
	private final String TAG = "LoginSecurityProcessor";
	
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {

		Bundle data = msg.getData();

		try {
			MessageObj loginMsg = MessageObj.getMessageObj(IDDictionary.SERVER_LOGIN_SERVICE_TYPE, IDDictionary.SERVER_LOGIN_SECURITY_LOGOUT);

			service.connection.sendMessage(loginMsg.convertToString(true));
			Log.i(TAG, loginMsg.convertToString(false));
			Log.i(TAG, "[LogoutSecurityProcessor]["+loginMsg.convertToString(true)+"]");
			Log.i(TAG, "[LogoutSecurityProcessor]["+loginMsg.convertToString(false)+"]");

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

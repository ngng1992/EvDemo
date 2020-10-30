package com.mfinance.everjoy.app.service.external;


import java.util.Date;

import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;

public class HeartBeatMessageHandler extends ServerMessageHandler {

	public HeartBeatMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {		
		if(msgObj == null)
			return;
		
		if(msgObj.getField("tradedate") != null){
			service.bQuit = true;		
			if (BuildConfig.DEBUG)
				Log.i(TAG, "[Change Trade Date]");
			try{
//				MessageObj messageObj = MessageObj.getMessageObj(IDDictionary.SERVER_LOGIN_SERVICE_TYPE,  IDDictionary.SERVER_LOGIN_LOGOUT);
//				service.connection.sendFlushMessage(messageObj.convertToString(true));
//				service.connection.closeConnection();
//				//LoginActivity.identityPassed = false;
//				service.app.data.clear();
			}catch(Exception e){
				Log.e(TAG, e.getLocalizedMessage(), e.fillInStackTrace());			
			}
			service.broadcast(ServiceFunction.ACT_GO_TO_LOGIN, null);	
		}else{
			Date date = new Date(Long.valueOf(msgObj.getField("servertime")));
			service.app.dServerDateTime = date;
			if (BuildConfig.DEBUG)
				Log.i(TAG, "Server Time : "+ date.toLocaleString());
		}
	}

	@Override
	public boolean isStatusLess() {
		return false;
	}

	@Override
	public boolean isBalanceRecalRequire() {
		return false;
	}

}

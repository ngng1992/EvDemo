package com.mfinance.everjoy.app.service.external;

import android.os.Bundle;
import android.util.Log;

import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.LoginSecurityProgress;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.MessageObj;

public class LogoutSecurityMessageHandler extends ServerMessageHandler {

	public LogoutSecurityMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		//Log.i(TAG, msgObj.convertToString());
		Log.e("login", "login:" + msgObj.convertToString());

		try {
			String field1 = msgObj.getField(Protocol.LoginResponse.STATUS);
			// success表示成功
			Log.e("login", "登录成功 ============================ field1 = " + field1);

			if("success".equals(msgObj.getField(Protocol.LoginResponse.STATUS))){
				service.app.bSecurityLogon = false;
				service.app.setSecLoginID(null);
				service.app.setSecPasswordToken(null);
				service.broadcast(ServiceFunction.ACT_GO_TO_MAIN_PAGE, null);
			}
			else{
				service.app.isLoading = false;
				//service.app.data.setStrSecurityLoginID(null);
			}
			service.app.isLoading = false;
			
		} catch (Exception e) {
			Log.e(TAG, "Unable to send login message", e);
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


package com.mfinance.everjoy.app.service.external;

import android.os.Bundle;
import android.util.Log;

import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.LoginSecurityProgress;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.MessageObj;

public class LoginSecurityOTPMessageHandler extends ServerMessageHandler {

	public LoginSecurityOTPMessageHandler(FxMobileTraderService service) {
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
				String firstLogin = msgObj.getField(Protocol.LoginResponse.FIRST_LOGIN);
				//firstLogin = "1";
				if (firstLogin != null){
					Bundle data = new Bundle();
					data.putString(ServiceFunction.RESETPASSWORD_TYPE, "3"); //Reset login type level "2"/"3"
					service.broadcast(ServiceFunction.ACT_GO_TO_CHANGE_PASSWORD, data);
				}
				else {
					service.app.bSecurityLogon = true;
					LoginSecurityProgress.setLoginUpdated(service);
				}
			}
			else{
				service.app.isLoading = false;
				//service.app.data.setStrSecurityLoginID(null);

				String strToastMsg = MessageMapping.getStringById(service.getRes(), "login_sec_otp_fail", service.app.locale);

				Bundle data = new Bundle();
				data.putString(ServiceFunction.MESSAGE, strToastMsg);
				service.broadcast(ServiceFunction.ACT_SHOW_TOAST, data);
				service.broadcast(ServiceFunction.ACT_INVISIBLE_POP_UP, null);
				service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
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


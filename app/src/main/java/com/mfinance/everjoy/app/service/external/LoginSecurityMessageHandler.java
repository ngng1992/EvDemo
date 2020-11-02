package com.mfinance.everjoy.app.service.external;

import android.os.Bundle;
import android.util.Log;

import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.LoginProgress;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.MessageObj;

import net.mfinance.commonlib.share.Utils;

public class LoginSecurityMessageHandler extends ServerMessageHandler {

	public LoginSecurityMessageHandler(FxMobileTraderService service) {
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

			if (service.app.isAutoRelogin){
				service.app.isAutoRelogin = false;
				if ("success".equals(msgObj.getField(Protocol.LoginResponse.STATUS))) {
					service.app.bSecurityLogon = true;
					Bundle data = new Bundle();
					data.putString(ServiceFunction.MESSAGE, "Reconnected");
					service.broadcast(ServiceFunction.ACT_SHOW_TOAST, data);
					service.broadcast(ServiceFunction.ACT_INVISIBLE_POP_UP, null);
				}
				else {
					service.app.bSecurityLogon = false;
					service.app.setSecLoginID(null);
					service.app.setSecPasswordToken(null);
					service.broadcast(ServiceFunction.ACT_INVISIBLE_POP_UP, null);
					service.broadcast(ServiceFunction.ACT_GO_TO_MAIN_PAGE, null);
				}
			}
			else {
				if ("success".equals(msgObj.getField(Protocol.LoginResponse.STATUS))) {
					//service.app.bSecurityLogon = true;
					String firstLogin = msgObj.getField(Protocol.LoginResponse.FIRST_LOGIN_CHANGE_PWD);
					service.app.bResetSecPassword = firstLogin.equals("1");
					String pwdToken = msgObj.getField(Protocol.LoginResponse.PASSWORD_TOKEN);
					service.app.tempSecPwdToken = pwdToken;

					String strPrefix = msgObj.getField(Protocol.LoginResponse.PREFIX);
					Bundle data = new Bundle();
					data.putString(ServiceFunction.LOGIN_SEC_EMAIL, "noreply@m-finance.net");
					data.putString(ServiceFunction.LOGIN_SEC_PREFIX, strPrefix);
					service.broadcast(ServiceFunction.ACT_GO_TO_OTP_LOGIN_PAGE, data);

				} else {
					service.app.isLoading = false;
					service.app.setSecLoginID(null);

					String strMsg = msgObj.getField(Protocol.LoginResponse.MSG);
					String strToastMsg = "";
					if (strMsg.equals("-1")) {
						strToastMsg = MessageMapping.getStringById(service.getRes(), "login_sec_fail_1", service.app.locale);
					} else if (strMsg.equals("-2")) {
						strToastMsg = MessageMapping.getStringById(service.getRes(), "login_sec_fail_2", service.app.locale);
					} else if (strMsg.equals("-3")) {
						strToastMsg = MessageMapping.getStringById(service.getRes(), "login_sec_fail_3", service.app.locale);
					}
					Bundle data = new Bundle();
					data.putString(ServiceFunction.MESSAGE, strToastMsg);
					service.broadcast(ServiceFunction.ACT_SHOW_TOAST, data);
					service.broadcast(ServiceFunction.ACT_INVISIBLE_POP_UP, null);
					service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
				}
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


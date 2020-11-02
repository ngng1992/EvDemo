package com.mfinance.everjoy.app.service.external;

import android.os.Bundle;
import android.util.Log;

import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.LoginProgress;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.everjoy.dialog.PwdErrorDialog;

public class LoginMessageHandler extends ServerMessageHandler {

	public LoginMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		//Log.i(TAG, msgObj.convertToString());
		Log.e("login", "login:" + msgObj.convertToString());

		service.app.autoLoginRetryCount = 0;

		try {
			String field1 = msgObj.getField(Protocol.LoginResponse.STATUS);
			// success表示成功
			Log.e("login", "登录成功 ============================ field1 = " + field1);

			if("success".equals(msgObj.getField(Protocol.LoginResponse.STATUS))){
				service.connection.startHeartBeat();
				service.app.bLogon = true;

				if (service.app.getLoginType() == -1 ) {
					String strPwdToken = msgObj.getField(Protocol.LoginResponse.PASSWORD_TOKEN);
					String strUsername = msgObj.getField(Protocol.LoginResponse.USERNAME);
					service.app.setPasswordToken(strPwdToken);
					service.app.strUsername = strUsername;
					service.app.setOpenID(null);
				}
				else {
					String strPwdToken = msgObj.getField(Protocol.LoginResponse.ID);
					String strUsername = msgObj.getField(Protocol.LoginResponse.USERNAME);
					service.app.setPasswordToken(strPwdToken); //Save FxServer ID return as OAuth passwordtoken
					service.app.strUsername = strUsername;
				}

				LoginProgress.setLoginUpdated(service);
//				service.onLogin();
//				if (CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN)
//					service.app.bPriceReload = true;
//				// System.out.println(msgObj.convertToString());
//				service.app.reloadDefaultPageInMemory();
//				service.updateProcessingMsgToTransactionFail();
//				service.app.data.setTradableContract(msgObj.getField("tc"));
//
//				service.app.data.clearSystemMessage();

//				if (CompanySettings.ENABLE_TRADER_PRICE_AGENT_CONNECTION) {
//					MessageObj messageObj = MessageObj.getMessageObj(IDDictionary.TRADER_LIVE_PRICE_TYPE, IDDictionary.TRADER_UPDATE_LIVE_PRICE_WITH_PRICE_AGENT_CONNECTION);
//					service.postMessage(messageObj);
//				}
//				else{
//					service.app.setPriceAgentConnectionStatus(true);
//				}
			}
			else{
				service.app.isLoading = false;

//				String strMsg = msgObj.getField(Protocol.LoginResponse.MSG);
//				String strToastMsg = "";
//				if (strMsg.equals("-1")){
//					strToastMsg = MessageMapping.getStringById(service.getRes(), "login_fail_1", service.app.locale);
//				}else if (strMsg.equals("-2")){
//					strToastMsg = MessageMapping.getStringById(service.getRes(), "login_fail_2", service.app.locale);
//				}else if (strMsg.equals("-3")){
//					strToastMsg = MessageMapping.getStringById(service.getRes(), "login_fail_3", service.app.locale);
//				}
//				Bundle data = new Bundle();
//				data.putString(ServiceFunction.MESSAGE, strToastMsg);
//				service.broadcast(ServiceFunction.ACT_SHOW_TOAST, data);
//				service.broadcast(ServiceFunction.ACT_INVISIBLE_POP_UP, null);
//				service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);

				String failcount = msgObj.getField(Protocol.LoginResponse.FAIL_COUNT);
				Bundle data2 = new Bundle();
				data2.putString(ServiceFunction.COUNT, failcount);
				service.broadcast(ServiceFunction.ACT_SHOW_FAIL_PASSWORD_DIALOG, data2);

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


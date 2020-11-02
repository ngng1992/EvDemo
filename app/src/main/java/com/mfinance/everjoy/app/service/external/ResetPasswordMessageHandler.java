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

public class ResetPasswordMessageHandler extends ServerMessageHandler {

	public ResetPasswordMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		//Log.i(TAG, msgObj.convertToString());
		String type = service.app.resetPasswordLevel;
		Log.e("Reset Password", "msg:" + msgObj.convertToString());
		if("success".equals(msgObj.getField(Protocol.ResetPasswordResponse.STATUS))) {
			String strToastMsg = MessageMapping.getStringById(service.getRes(), "resetpassword_success", service.app.locale);
			Bundle data = new Bundle();
			data.putString(ServiceFunction.TITLE, strToastMsg);
			data.putString(ServiceFunction.MESSAGE, strToastMsg);
			data.putString(ServiceFunction.FINISH, "1");
			//service.broadcast(ServiceFunction.ACT_SHOW_DIALOG, null);
			if (type.equals(Utils.LevelType.LEVEL3_FIRST_LOGIN) && !service.app.getFingerID())
				service.broadcast(ServiceFunction.ACT_GO_TO_FINGER_ID_PAGE, null);
			else
				service.broadcast(ServiceFunction.ACT_GO_TO_MAIN_PAGE, null);
		} else {
			String strToastMsg = MessageMapping.getStringById(service.getRes(), "resetpassword_fail", service.app.locale);
			Bundle data = new Bundle();
			data.putString(ServiceFunction.TITLE, strToastMsg);
			data.putString(ServiceFunction.MESSAGE, strToastMsg);
			data.putString(ServiceFunction.FINISH, "1");
			service.broadcast(ServiceFunction.ACT_SHOW_DIALOG, null);
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


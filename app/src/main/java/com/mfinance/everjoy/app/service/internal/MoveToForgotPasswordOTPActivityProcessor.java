package com.mfinance.everjoy.app.service.internal;

import android.os.Bundle;
import android.os.Message;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

public class MoveToForgotPasswordOTPActivityProcessor implements MessageProcessor{
	private final String TAG = "MoveToMainPageActivityProcessor";
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		Bundle data = msg.getData();
		service.broadcast(ServiceFunction.ACT_GO_TO_FORGOT_PASSWORD_OTP_PAGE, data);
		return true;
	}
}

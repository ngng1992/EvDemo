package com.mfinance.everjoy.app.service.internal;

import android.os.Message;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

public class MoveToNewsProcessor implements MessageProcessor {
	private final String TAG = "MoveToNewsProcessor";
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		service.broadcast(ServiceFunction.ACT_GO_TO_NEWS, null);
		return true;
	}

}

package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;
import android.util.Log;

public class MoveToRunningOrderProcessor implements MessageProcessor {
	private final String TAG = "MoveToPriceProcessor";
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		service.broadcast(ServiceFunction.ACT_GO_TO_RUNNING_ORDER, null);
		return true;
	}

}

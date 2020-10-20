package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;

public class MoveToPriceProcessor implements MessageProcessor {
	private final String TAG = "MoveToPriceProcessor";
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		service.broadcast(ServiceFunction.ACT_GO_TO_PRICE, msg.getData());
		return true;
	}

}

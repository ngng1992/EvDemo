package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;

public class MoveToShowGoToAndroidMarketMessageProcessor implements MessageProcessor {
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		//System.out.println("ACT_GO_TO_SHOW_ANDROID_MARKET_MSG~~");
		service.broadcast(ServiceFunction.ACT_GO_TO_SHOW_ANDROID_MARKET_MSG, null);
		return true;
	}

}

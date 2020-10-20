package com.mfinance.everjoy.app.service.internal;

import android.os.Message;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

public class MoveToStrategyDetailActivityHandler implements MessageProcessor{

	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		(service.app).setSelectedStrategy(msg.getData().getString(ServiceFunction.SEND_SELECTED_KEY));
		service.broadcast(ServiceFunction.ACT_GO_TO_STRATEGY_DETAIL , msg.getData());
		return true;
	}

}

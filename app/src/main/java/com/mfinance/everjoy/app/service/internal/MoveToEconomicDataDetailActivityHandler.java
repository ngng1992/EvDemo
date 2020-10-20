package com.mfinance.everjoy.app.service.internal;

import android.os.Message;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;


public class MoveToEconomicDataDetailActivityHandler implements MessageProcessor{

	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		(service.app).setSelectedEconomicdata(msg.getData().getInt(ServiceFunction.SEND_SELECTED_KEY));
		service.broadcast(ServiceFunction.ACT_GO_TO_ECONOMIC_DATA_DETAIL , msg.getData());
		return true;
	}

}

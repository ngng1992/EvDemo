package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;

public class MoveToOpenPositionSummaryDetailProcessor implements MessageProcessor {
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		String sBuySell = msg.getData().getString(ServiceFunction.SRV_OPEN_POSITION_BUY_SELL);
		String sContract = msg.getData().getString(ServiceFunction.SRV_OPEN_POSITION_CONTRACT);
		
		service.app.setSelectedContract(sContract);
		service.app.setSelectedBuySell(sBuySell);
		
		service.broadcast(ServiceFunction.ACT_OPEN_POSITION_SUMMARY_DETAIL, null);
		return true;
	}

}

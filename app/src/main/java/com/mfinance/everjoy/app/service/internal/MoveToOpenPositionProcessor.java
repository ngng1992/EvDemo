package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;
import android.util.Log;

public class MoveToOpenPositionProcessor implements MessageProcessor {
	private final String TAG = "MoveToPriceProcessor";
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		String sBuySell = msg.getData().getString(ServiceFunction.SRV_OPEN_POSITION_BUY_SELL);
		String sContract = msg.getData().getString(ServiceFunction.SRV_OPEN_POSITION_CONTRACT);
		boolean bSummary = false;
		try{
			bSummary = msg.getData().getBoolean(ServiceFunction.SRV_OPEN_POSITION_FROM_SUMMARY);	
		}catch(Exception e){}
		
		service.app.setSelectedContract(sContract);
		service.app.setSelectedBuySell(sBuySell);
		
		service.broadcast(ServiceFunction.ACT_GO_TO_OPEN_POSITION, msg.getData());
		return true;
	}

}

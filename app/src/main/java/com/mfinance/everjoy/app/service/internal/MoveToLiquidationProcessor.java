package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Bundle;
import android.os.Message;

public class MoveToLiquidationProcessor implements MessageProcessor {
	private final String TAG = this.getClass().getSimpleName();
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		int iRef = msg.getData().getInt(ServiceFunction.LIQUIDATE_REF);
		
		service.app.setSelectedOpenPosition(iRef);
		
		if(service.app.getSelectedOpenPosition()!=null)
			service.broadcast(ServiceFunction.ACT_GO_TO_LIQUIDATE, msg.getData());
		
		return true;
	}

}

package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Bundle;
import android.os.Message;

public class MoveToEditOrderProcessor implements MessageProcessor {
	private final String TAG = this.getClass().getSimpleName();
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		int iRef = msg.getData().getInt(ServiceFunction.EDIT_ORDER_REF);			
		service.app.setSelectedRunningOrder(iRef);		
		service.broadcast(ServiceFunction.ACT_GO_TO_EDIT_ORDER, null);
		return true;
	}

}

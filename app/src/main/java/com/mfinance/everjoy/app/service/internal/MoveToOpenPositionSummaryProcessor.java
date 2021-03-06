package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;
import android.util.Log;

public class MoveToOpenPositionSummaryProcessor implements MessageProcessor {
	private final String TAG = "MoveToOpenPositionSummaryProcessor";
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		service.broadcast(ServiceFunction.ACT_GO_TO_OPEN_POSITION_SUMMARY, null);
		return true;
	}

}

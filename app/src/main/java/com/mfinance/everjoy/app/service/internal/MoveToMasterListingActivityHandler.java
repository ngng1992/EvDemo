package com.mfinance.everjoy.app.service.internal;

import android.os.Message;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;


public class MoveToMasterListingActivityHandler implements MessageProcessor{

	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		service.app.setSelectedMasterIndex(0);
		service.broadcast(ServiceFunction.ACT_GO_TO_MASTER_LIST , null);
		return true;
	}

}

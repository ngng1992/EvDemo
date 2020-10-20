package com.mfinance.everjoy.app.service.internal;

import android.os.Message;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

public class MoveToAnnouncementProcessor implements MessageProcessor {
	private final String TAG = "MoveToAnnouncementProcessor";
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		service.broadcast(ServiceFunction.ACT_GO_TO_ANNOUNCEMENT, null);
		return true;
	}

}

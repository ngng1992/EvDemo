package com.mfinance.everjoy.app.service.internal;

import android.os.Message;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

public class MoveToSettingIDProcessor implements MessageProcessor {
	private final String TAG = this.getClass().getSimpleName();
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		service.broadcast(ServiceFunction.ACT_GO_TO_SETTING_ID, null);
		return true;
	}

}

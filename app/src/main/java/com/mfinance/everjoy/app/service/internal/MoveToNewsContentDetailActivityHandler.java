package com.mfinance.everjoy.app.service.internal;

import android.os.Message;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

public class MoveToNewsContentDetailActivityHandler implements MessageProcessor{

	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		(service.app).setSelectedNewscontent(msg.getData().getInt(ServiceFunction.SEND_SELECTED_KEY));
		service.broadcast(ServiceFunction.ACT_GO_TO_NEWS_CONTENT_DETAIL  , msg.getData());
		return true;
	}

}

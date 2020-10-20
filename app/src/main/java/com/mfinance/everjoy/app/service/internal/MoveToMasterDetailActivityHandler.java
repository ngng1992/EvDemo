package com.mfinance.everjoy.app.service.internal;

import android.os.Message;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;


public class MoveToMasterDetailActivityHandler implements MessageProcessor{

	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		if(CompanySettings.ENABLE_WEBVIEW_MASTER == false)
		{
			(service.app).setSelectedMaster(msg.getData().getInt(ServiceFunction.SEND_SELECTED_KEY));
		}
		service.broadcast(ServiceFunction.ACT_GO_TO_MASTER_DETAIL, msg.getData());
		return true;
	}

}

package com.mfinance.everjoy.app.service.internal;

import android.os.Message;

import com.mfinance.everjoy.app.service.FxMobileTraderService;

public class RefreshXMLTransferHandler implements MessageProcessor{

	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		service.refreshXMLTransfer(); 
		return true;
	}

}

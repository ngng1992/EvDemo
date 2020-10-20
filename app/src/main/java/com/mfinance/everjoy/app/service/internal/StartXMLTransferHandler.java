package com.mfinance.everjoy.app.service.internal;

import android.os.Message;

import com.mfinance.everjoy.app.service.FxMobileTraderService;

public class StartXMLTransferHandler implements MessageProcessor{

	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		service.startXMLTransfer(); 
		return true;
	}

}

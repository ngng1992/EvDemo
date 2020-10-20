package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;
import android.util.Log;

public class ServiceCloseProcessor implements MessageProcessor {
	private final String TAG = "ServiceCloseProcessor";
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {		
		service.bQuit = true;
		service.app.bLogon = false;
		service.onLogout();
		service.connection.closeConnection();
		if (BuildConfig.DEBUG)
			Log.i(TAG, "[Close Connection]");		
		return false;
	}

}

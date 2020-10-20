package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;
import android.util.Log;

public class ServiceUnregisterProcessor implements MessageProcessor {
	private final String TAG = "ServiceUnregisterProcessor";
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		if (BuildConfig.DEBUG)
			Log.i(TAG, "[MSG_UNREGISTER_ACTIVITY]["+msg.replyTo.getClass().getSimpleName()+"]");

		Log.i(TAG, "[MSG_UNREGISTER_ACTIVITY]["+msg+"]");
		
		if(service.isClientActivityExist(msg.replyTo)){
			service.removeClient(msg.replyTo);			
		}
		if (service.getNumberOfClient() == 0) {
			service.startClearServiceTimeout();
		}
		
		return true;
	}

}

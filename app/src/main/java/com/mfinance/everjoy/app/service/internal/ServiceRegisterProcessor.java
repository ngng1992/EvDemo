package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public class ServiceRegisterProcessor implements MessageProcessor {
	private final String TAG = "ServiceRegisterProcessor";
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		if(!service.isClientActivityExist(msg.replyTo)){						
			service.addClient(msg.replyTo);
			if (BuildConfig.DEBUG)
				Log.i(TAG, "[MSG_REGISTER_ACTIVITY]["+msg.replyTo.getClass().getSimpleName()+"]");
			
			if(!CompanySettings.ENABLE_CONTENT){
			Message reply = Message.obtain(service.handler, ServiceFunction.SRV_REGISTER_ACTIVITY);
			reply.getData().putInt(ServiceFunction.P_RESULT, ServiceFunction.V_SUCCESS);
			reply.replyTo = service.mMessenger;
			
			try {
				msg.replyTo.send(reply);							
			} catch (RemoteException e) {
				e.printStackTrace();
				//Log.i(TAG, "[MSG_REPLY fail]["+msg.replyTo.getClass().getSimpleName()+"]");
			}}		
		}
		if(!CompanySettings.ENABLE_CONTENT){
			boolean bLogin = msg.getData().getBoolean(ServiceFunction.IS_LOGIN);
			boolean requireLogin = msg.getData().getBoolean(ServiceFunction.REQUIRE_LOGIN);
			if(!bLogin && !service.app.bLogon){
				if (requireLogin) {
					service.broadcast(ServiceFunction.ACT_GO_TO_LOGIN, null);
				}
			}
		}
		service.stopClearServiceTimeout();
		
		return true;
	}

}

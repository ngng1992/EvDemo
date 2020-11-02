package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;
import android.util.Log;

public class DisconnectProcessor implements MessageProcessor {
	private final String TAG = "LogoutProcessor";
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		System.out.println("LogoutProcessor " + msg.toString());

		if (service.app.bLogon && !service.app.bSecurityLogon)
			service.app.disconnectLevel = 2;
		else if (service.app.bLogon && service.app.bSecurityLogon)
			service.app.disconnectLevel = 3;

		System.out.println("LogoutProcessor " + service.app.disconnectLevel);

		if(!service.bQuit){
			if( CompanySettings.FOR_TEST == false )
			{
				if( service.app.isDemoPlatform )
				{
					service.app.iTrialIndexDemo = (service.app.iTrialIndexDemo + 1) % service.app.alLoginInfoDemo.size();
					service.app.RoundRobinIndexDemo = service.app.iTrialIndexDemo;
				}			
				else if( CompanySettings.checkProdServer() == 1 )
				{
					service.app.iTrialIndexProd = (service.app.iTrialIndexProd + 1) % service.app.alLoginInfoProd.size();
					service.app.RoundRobinIndexProd = service.app.iTrialIndexProd;
				}
			}
			
			service.bQuit = true;
			service.app.bLogon = false;
			service.app.bSecurityLogon = false;
			service.onLogout();
			if (BuildConfig.DEBUG)
				Log.i(TAG, "[Close Connection]");

			//LoginActivity.identityPassed = false;
			
			try{
				service.connection.closeConnection();
				service.app.data.clear();
			}catch(Exception e){
				Log.e(TAG, e.getLocalizedMessage(), e);			
			}
				
			service.broadcast(ServiceFunction.ACT_DISCONNECT, null);
						
		}
		return true;
	}
}

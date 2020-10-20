package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.LoginActivity;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;
import android.util.Log;

public class DisconnectProcessor implements MessageProcessor {
	private final String TAG = "LogoutProcessor";
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
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
				else if( CompanySettings.checkProdServer() == 2 )
				{
					service.app.iTrialIndexProd2 = (service.app.iTrialIndexProd2 + 1) % service.app.alLoginInfoProd2.size();
					service.app.RoundRobinIndexProd2 = service.app.iTrialIndexProd2;
				}
				else if( CompanySettings.checkProdServer() == 3 )
				{
					service.app.iTrialIndexProd3 = (service.app.iTrialIndexProd3 + 1) % service.app.alLoginInfoProd3.size();
					service.app.RoundRobinIndexProd3 = service.app.iTrialIndexProd3;
				}
				else if( CompanySettings.checkProdServer() == 4 )
				{
					service.app.iTrialIndexProd4 = (service.app.iTrialIndexProd4 + 1) % service.app.alLoginInfoProd4.size();
					service.app.RoundRobinIndexProd4 = service.app.iTrialIndexProd4;
				}
				else if( CompanySettings.checkProdServer() == 5 )
				{
					service.app.iTrialIndexProd5 = (service.app.iTrialIndexProd5 + 1) % service.app.alLoginInfoProd5.size();
					service.app.RoundRobinIndexProd5 = service.app.iTrialIndexProd5;
				}
				else if( CompanySettings.checkProdServer() == 6 )
				{
					service.app.iTrialIndexProd6 = (service.app.iTrialIndexProd6 + 1) % service.app.alLoginInfoProd6.size();
					service.app.RoundRobinIndexProd6 = service.app.iTrialIndexProd6;
				}
				else if( CompanySettings.checkProdServer() == 7 )
				{
					service.app.iTrialIndexProd7 = (service.app.iTrialIndexProd7 + 1) % service.app.alLoginInfoProd7.size();
					service.app.RoundRobinIndexProd7 = service.app.iTrialIndexProd7;
				}
				else if( CompanySettings.checkProdServer() == 8 )
				{
					service.app.iTrialIndexProd8 = (service.app.iTrialIndexProd8 + 1) % service.app.alLoginInfoProd8.size();
					service.app.RoundRobinIndexProd8 = service.app.iTrialIndexProd8;
				}
				else if( CompanySettings.checkProdServer() == 9 )
				{
					service.app.iTrialIndexProd9 = (service.app.iTrialIndexProd9 + 1) % service.app.alLoginInfoProd9.size();
					service.app.RoundRobinIndexProd9 = service.app.iTrialIndexProd9;
				}
				else if( CompanySettings.checkProdServer() == 10 )
				{
					service.app.iTrialIndexProd10 = (service.app.iTrialIndexProd10 + 1) % service.app.alLoginInfoProd10.size();
					service.app.RoundRobinIndexProd10 = service.app.iTrialIndexProd10;
				}
			}
			
			service.bQuit = true;
			service.app.bLogon = false;
			service.onLogout();
			if (BuildConfig.DEBUG)
				Log.i(TAG, "[Close Connection]");

			LoginActivity.identityPassed = false;
			
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

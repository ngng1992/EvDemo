package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;
import android.util.Log;
import java.net.Socket;
import java.io.IOException;

public class LogoutProcessor implements MessageProcessor {
	private final String TAG = "LogoutProcessor";
	
	private final CloseConnection closeConnection = new CloseConnection();
	
	class CloseConnection implements Runnable{
		FxMobileTraderService service;
		public void setService(FxMobileTraderService service){
			this.service = service;			
		}
		@Override
		public void run() {
			if(service != null){
				
			}
		}
		
	};
	
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		service.bQuit = true;
		service.app.bLogon = false;
		service.app.setLoginID(null);
		service.app.setPasswordToken(null);
		service.app.setOpenID(null);
		service.onLogout();
		if (BuildConfig.DEBUG)
			Log.i(TAG, "[Close Connection]");

		try{
			if (CompanySettings.ENABLE_TRADER_PRICE_AGENT_CONNECTION){
				Socket priceAgentSocket = service.app.getPriceAgentSocketRef();
				if(priceAgentSocket != null && priceAgentSocket.isConnected()){
					try {
						priceAgentSocket.close();
					} catch (IOException ex) {
						System.out.println(ex);
					}
				}
			}
			
			service.connection.closeConnection();
			
			//System.out.println("=----------------------------------- closed");
			//LoginActivity.identityPassed = false;
			service.app.data.clear();
		}catch(Exception e){
			Log.e(TAG, e.getLocalizedMessage(), e.fillInStackTrace());			
		}

		service.broadcast(ServiceFunction.ACT_GO_TO_MAIN_PAGE, null);

		//System.out.println("=----------------------------------- move to login");
		return true;
	}

	
	
}

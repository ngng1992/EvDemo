package com.mfinance.everjoy.app.model;

import android.os.Message;
import android.util.Log;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

public class LoginProgress
{
	private boolean isLoginUpdated = false;
	private boolean isLogonComplete = false;
	
	private final static LoginProgress instance = new LoginProgress();
		
	public static void reset(){
		instance.isLoginUpdated = false;
		instance.isLogonComplete = false;
	}

	public static void setLoginUpdated(FxMobileTraderService service)
	{
		instance.isLoginUpdated = true;
		instance.checkLoginComplete(service);
	}
	
	public static boolean isLoginComplete()
	{
		return instance.isLogonComplete;
	}
	
	private void checkLoginComplete(final FxMobileTraderService service)
	{
		if( isLogonComplete == false )
		{
			if(instance.isLoginUpdated)
			{
				isLogonComplete = true;
				
				// Login Succeed, update the RR index for Server Connection
				if( CompanySettings.checkProdServer() == 1 ){
						service.app.RoundRobinIndexProd = service.app.iTrialIndexProd;
				}
				
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							if (!service.app.isAutoRelogin) {
								int defaultPage = service.app.getDefaultPage();
								// 默认页defaultPage = 4；
								Log.e("login", "登录成功后 默认页 defaultPage = " + defaultPage);

								Message msg;
								msg = Message.obtain(null, ServiceFunction.SRV_DEFAULT_LOGIN_PAGE);
								Log.e("login", "msg = Message.obtain(null, defaultPage)");

								int what = msg.what;
								Log.e("login", "登录成功后msg的what = " + what);

								service.handler.handleMessage(msg);
							}
							else{
								if (service.app.getSecLoginID() != null && service.app.getSecPasswordToken() != null){
									//Reconnect to level 3
									service.broadcast(ServiceFunction.ACT_RECONNECT_SECURITY, null);
									service.broadcast(ServiceFunction.ACT_INVISIBLE_POP_UP, null);
								}
								else {
									//Reconnect login level completed
									service.app.isAutoRelogin = false;
									service.broadcast(ServiceFunction.ACT_INVISIBLE_POP_UP, null);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	
				thread.start();
			}	
		}
	}
}
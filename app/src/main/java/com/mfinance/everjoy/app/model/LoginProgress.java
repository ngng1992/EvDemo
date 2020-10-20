package com.mfinance.everjoy.app.model;

import android.os.Message;
import android.util.Log;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.pojo.PasswordControl;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

public class LoginProgress
{
	private boolean isPositionUpdated = false;
	private boolean isAccountUpdated = false;
	private boolean isOrderUpdated = false;
	private boolean isExecutedOrderUpdated = false;
	private boolean isCancelledOrderUpdated = false;
	private boolean isPriceUpdated = false;
	private boolean isLogonComplete = false;
	
	private final static LoginProgress instance = new LoginProgress();
		
	public static void reset(){
		instance.isPositionUpdated = false;
		instance.isAccountUpdated = false;
		instance.isOrderUpdated = false;
		instance.isExecutedOrderUpdated = false;
		instance.isCancelledOrderUpdated = false;
		instance.isPriceUpdated = false;
		instance.isLogonComplete = false;
	}
	
	public static void setPositionUpdated(FxMobileTraderService service)
	{
		instance.isPositionUpdated = true;
		instance.checkLoginComplete(service);
	}
	public static void setAccountUpdated(FxMobileTraderService service)
	{
		instance.isAccountUpdated = true;
		instance.checkLoginComplete(service);
	}
	public static void setOrderUpdated(FxMobileTraderService service)
	{
		instance.isOrderUpdated = true;
		instance.checkLoginComplete(service);
	}
	public static void setExecutedOrderUpdated(FxMobileTraderService service)
	{
		instance.isExecutedOrderUpdated = true;
		instance.checkLoginComplete(service);
	}
	public static void setCancelledOrderUpdated(FxMobileTraderService service)
	{
		instance.isCancelledOrderUpdated = true;
		instance.checkLoginComplete(service);
	}
	public static void setPriceUpdated(FxMobileTraderService service)
	{
		instance.isPriceUpdated = true;
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
			if(instance.isAccountUpdated && instance.isCancelledOrderUpdated && instance.isExecutedOrderUpdated &&
				instance.isOrderUpdated && instance.isPositionUpdated && instance.isPriceUpdated)
			{
				isLogonComplete = true;
				
				// Login Succeed, update the RR index for Server Connection
				if( !CompanySettings.ENABLE_FATCH_SERVER && !CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX )
				{
					if( service.app.isDemoPlatform )
					{
						service.app.RoundRobinIndexDemo = service.app.iTrialIndexDemo;
					}
					else if( CompanySettings.checkProdServer() == 1 )
					{
						service.app.RoundRobinIndexProd = service.app.iTrialIndexProd;
					}
					else if( CompanySettings.checkProdServer() == 2 )
					{
						service.app.RoundRobinIndexProd2 = service.app.iTrialIndexProd2;
					}
					else if( CompanySettings.checkProdServer() == 3 )
					{
						service.app.RoundRobinIndexProd3 = service.app.iTrialIndexProd3;
					}
					else if( CompanySettings.checkProdServer() == 4 )
					{
						service.app.RoundRobinIndexProd4 = service.app.iTrialIndexProd4;
					}
					else if( CompanySettings.checkProdServer() == 5 )
					{
						service.app.RoundRobinIndexProd5 = service.app.iTrialIndexProd5;
					}
					else if( CompanySettings.checkProdServer() == 6 )
					{
						service.app.RoundRobinIndexProd6 = service.app.iTrialIndexProd6;
					}
					else if( CompanySettings.checkProdServer() == 7 )
					{
						service.app.RoundRobinIndexProd7 = service.app.iTrialIndexProd7;
					}
					else if( CompanySettings.checkProdServer() == 8 )
					{
						service.app.RoundRobinIndexProd8 = service.app.iTrialIndexProd8;
					}
					else if( CompanySettings.checkProdServer() == 9 )
					{
						service.app.RoundRobinIndexProd9 = service.app.iTrialIndexProd9;
					}
					else if( CompanySettings.checkProdServer() == 10 )
					{
						service.app.RoundRobinIndexProd10 = service.app.iTrialIndexProd10;
					}
				}
				
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						PasswordControl passwordControl = service.app.data.getPasswordControl();
						try {
							int defaultPage = service.app.getDefaultPage();
							// 默认页defaultPage = 4；
							Log.e("login", "登录成功后 默认页 defaultPage = " + defaultPage);

							if (CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN && ServiceFunction.ACT_GO_TO_DASHBOARD != defaultPage) {
								service.handler.handleMessage(Message.obtain(null, ServiceFunction.SRV_FINISH_ACTIVITY));
							}
							Message msg;
							if ((passwordControl.isFirstLogin() && CompanySettings.FORCE_FIRST_LOGIN_CHANGE_PASSWORD_FUNC) || passwordControl.getAge() > passwordControl.getMaxPasswordAge()) {
								msg = Message.obtain(null, ServiceFunction.SRV_CHANGE_PASSWORD);
							} else {
								// 执行=================
								msg = Message.obtain(null, defaultPage);
								Log.e("login", "msg = Message.obtain(null, defaultPage)");
							}
							int what = msg.what;
							Log.e("login", "登录成功后msg的what = " + what);

							service.handler.handleMessage(msg);
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
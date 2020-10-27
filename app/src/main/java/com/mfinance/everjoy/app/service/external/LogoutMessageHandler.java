package com.mfinance.everjoy.app.service.external;

import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;

@Deprecated // future may handle IDDictionary.TRADER_SYSTEM_TYPE IDDictionary.TRADER_DISCONNECT_ACCOUNT
public class LogoutMessageHandler extends ServerMessageHandler {
	public LogoutMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		service.bQuit = true;
		service.app.bLogon = false;
		service.onLogout();
		if (BuildConfig.DEBUG)
			Log.i(TAG, "[Close Connection]");
		try {
			service.connection.closeConnection();

			service.app.data.clear();
			service.app.defaultContract = null;
		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage(), e.fillInStackTrace());
			// respond to dealer that the disconnection fail
			MessageObj requestMsg = MessageObj.getMessageObj(IDDictionary.SERVER_IO_SERVICE_TYPE, IDDictionary.SERVER_IO_TRADER_RESPONSE_DISCONNECT_ACCOUNT);
			requestMsg.addField("toid", msgObj.getField("frname")); // toid is the Entity we are going to response to
			requestMsg.addField("sDisconnectedAC", msgObj.getField("toid"));
			requestMsg.addField("msgCode", "408");
			service.connection.sendMessage(requestMsg.convertToString());
		}
		if (!CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN)
			service.broadcast(ServiceFunction.ACT_GO_TO_LOGIN, null);
		else {
			service.app.data.setBalanceRecord(null);
			service.addPriceLoadingFromXML();
			service.broadcast(ServiceFunction.ACT_GO_TO_DASHBOARD, null);
		}
	}

	@Override
	public boolean isStatusLess() {
		return false;
	}

	@Override
	public boolean isBalanceRecalRequire() {
		return false;
	}
}
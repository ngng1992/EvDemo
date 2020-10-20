package com.mfinance.everjoy.app.service.external;

import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.DashboardItemRespository;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.MobileTraderApplication.BuySellStrengthPriceAdjustSetting;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

public class BuySellStrengthMessageHandler extends ServerMessageHandler {

	public BuySellStrengthMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		//Log.i(TAG, msgObj.convertToString());
		//System.out.println("login:" + msgObj.convertToString());
		try
		{
			int len = Utility.toInteger(msgObj.getField("noitem"), 0);
			
			for (int i=0;i<len;i++)
			{
				String contract = msgObj.getField(Protocol.BuySellStrengthSetting.CONTRACT +i);
				BuySellStrengthPriceAdjustSetting setting = MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.get(contract);
				if (setting == null)
				{
					setting = new BuySellStrengthPriceAdjustSetting();
					MobileTraderApplication.hmBuySellStrengthPriceAdjustSetting.put(contract, setting);
				}
				setting.openPosWaitSec = Utility.toInteger(msgObj.getField(Protocol.BuySellStrengthSetting.OPEN_SECOND+i), -1);
				setting.liqWaitSec = Utility.toInteger(msgObj.getField(Protocol.BuySellStrengthSetting.LIQUIDATION_SECOND+i), -1);
			}
		}
		catch (Exception e) 
		{
			Log.e(TAG, "Unable to read Buy Sell Strength Message", e);
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


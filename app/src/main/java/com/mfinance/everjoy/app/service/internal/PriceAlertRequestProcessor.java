package com.mfinance.everjoy.app.service.internal;

import android.os.Message;
import android.util.Log;

import com.mfinance.everjoy.app.bo.BalanceRecord;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;

public class PriceAlertRequestProcessor implements MessageProcessor{
	private final String TAG = this.getClass().getSimpleName();
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
        try {
			MessageObj priceAlertRequestMsg = MessageObj.getMessageObj(IDDictionary.SERVER_IO_SERVICE_TYPE, IDDictionary.SERVER_IO_REQUEST_UPDATE_PRICE_ALERT);
			priceAlertRequestMsg.addField(ServiceFunction.SEND_PRICE_ALERT_MODE, msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_MODE));
			priceAlertRequestMsg.addField(ServiceFunction.SEND_PRICE_ALERT_CONTRACT, msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_CONTRACT));
			priceAlertRequestMsg.addField(ServiceFunction.SEND_PRICE_ALERT_ACCOUNT, msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_ACCOUNT));

			if (msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_MODE).equals("1")) {
				priceAlertRequestMsg.addField(ServiceFunction.SEND_PRICE_ALERT_TYPE, msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_TYPE));
				priceAlertRequestMsg.addField(ServiceFunction.SEND_PRICE_ALERT_GOODTILL, msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_GOODTILL));
				priceAlertRequestMsg.addField(ServiceFunction.SEND_PRICE_ALERT_PRICE, msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_PRICE));
				priceAlertRequestMsg.addField(ServiceFunction.SEND_PRICE_ALERT_VOLATILITY, msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_VOLATILITY));
				priceAlertRequestMsg.addField(ServiceFunction.SEND_PRICE_ALERT_ID, msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_ID));
				priceAlertRequestMsg.addField(ServiceFunction.SEND_PRICE_ALERT_ACTIVE, msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_ACTIVE));
			}
			else if (msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_MODE).equals("2"))
				priceAlertRequestMsg.addField(ServiceFunction.SEND_PRICE_ALERT_ID, msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_ID));
			else if (msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_MODE).equals("0")){
				priceAlertRequestMsg.addField(ServiceFunction.SEND_PRICE_ALERT_TYPE, msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_TYPE));
				priceAlertRequestMsg.addField(ServiceFunction.SEND_PRICE_ALERT_GOODTILL, msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_GOODTILL));
				priceAlertRequestMsg.addField(ServiceFunction.SEND_PRICE_ALERT_PRICE, msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_PRICE));
				priceAlertRequestMsg.addField(ServiceFunction.SEND_PRICE_ALERT_VOLATILITY, msg.getData().getString(ServiceFunction.SEND_PRICE_ALERT_VOLATILITY));
			}
//			//executedOrderHistoryRequestMsg.addField("acc", account.strAccount);
			System.out.println("PriceAlertRequestProcessor " + priceAlertRequestMsg.convertToString());
			service.connection.sendMessage(priceAlertRequestMsg.convertToString(true));
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Exception in processMessage",e.fillInStackTrace());
			return false;
		}	
	}
	
}


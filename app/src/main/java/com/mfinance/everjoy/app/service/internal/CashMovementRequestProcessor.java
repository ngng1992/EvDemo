package com.mfinance.everjoy.app.service.internal;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.mfinance.everjoy.app.bo.BalanceRecord;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;

import java.util.HashMap;

public class CashMovementRequestProcessor implements MessageProcessor{
	public enum RequestType {
		DEPOSIT("1", 1), WITHDRAWAL("2", 2), CANCEL_REQUEST("3", 3);
		private final String value;
		private final int integer;
		RequestType(String value, int integer) {
			this.value = value;
			this.integer = integer;
		}

		public String getValue() {
			return value;
		}

		public int getInteger() {
			return integer;
		}

		public static RequestType fromString(String s) throws Exception {
			for (RequestType t : values()) {
				if (t.value.equals(s)) {
					return t;
				}
			}
			throw new Exception("type not found: " + s);
		}

		public static RequestType fromInteger(int s) throws Exception {
			for (RequestType t : values()) {
				if (t.integer == s) {
					return t;
				}
			}
			throw new Exception("type not found: " + s);
		}
	}
	private final String TAG = this.getClass().getSimpleName();
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		Bundle data = msg.getData();
		RequestType type;
		HashMap<String, String> request;
		try {
			type = RequestType.fromInteger(msg.arg1);
			request = (HashMap<String, String>) data.getSerializable("request");
		} catch (Exception ex) {
			Log.e(TAG, "Exception in processMessage",ex.fillInStackTrace());
			return false;
		}

		BalanceRecord account = service.app.data.getBalanceRecord();
		
        try {			
			MessageObj executedOrderHistoryRequestMsg = MessageObj.getMessageObj(IDDictionary.SERVER_IO_SERVICE_TYPE ,IDDictionary.SERVER_IO_REQUEST_DEPOSIT_WITHDRAWAL);
			executedOrderHistoryRequestMsg.importBody(request);
			executedOrderHistoryRequestMsg.addField("frid", "SYS");
			executedOrderHistoryRequestMsg.addField("acc", account.strAccount);
			executedOrderHistoryRequestMsg.addField("accountID", account.strAccount);
			executedOrderHistoryRequestMsg.addField("type", type.getValue());
			service.connection.sendMessage(executedOrderHistoryRequestMsg.convertToString(true));

		    Log.i(TAG, executedOrderHistoryRequestMsg.convertToString());
		    Log.i(TAG, "[Sent CashMovement Request.]");
		    
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Exception in processMessage",e.fillInStackTrace());
			return false;
		}	
	}
	
}


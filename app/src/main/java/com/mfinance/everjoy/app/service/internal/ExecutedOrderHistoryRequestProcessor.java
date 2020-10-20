package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.bo.BalanceRecord;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class ExecutedOrderHistoryRequestProcessor implements MessageProcessor{
	private final String TAG = this.getClass().getSimpleName();
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		Bundle data = msg.getData();
		
		String sFrom = data.getString(ServiceFunction.SRV_SEND_EXECUTED_ORDER_HISTORY_REQUEST_FROM);
		String sTo = data.getString(ServiceFunction.SRV_SEND_EXECUTED_ORDER_HISTORY_REQUEST_TO);
		BalanceRecord account = service.app.data.getBalanceRecord();
		
        try {			
			MessageObj executedOrderHistoryRequestMsg = MessageObj.getMessageObj(IDDictionary.SERVER_IO_SERVICE_TYPE, IDDictionary.SERVER_IO_REQUEST_EXECUTE_ORDER_HISTORY);
			executedOrderHistoryRequestMsg.addField(Protocol.ExecutedOrderHistoryRequestMessage.USER_TYPE, "2");
		    executedOrderHistoryRequestMsg.addField(Protocol.ExecutedOrderHistoryRequestMessage.GET_ALL, "0");
		    executedOrderHistoryRequestMsg.addField(Protocol.ExecutedOrderHistoryRequestMessage.FROM, sFrom);
		    executedOrderHistoryRequestMsg.addField(Protocol.ExecutedOrderHistoryRequestMessage.TO, sTo);
		    executedOrderHistoryRequestMsg.addField(Protocol.ExecutedOrderHistoryRequestMessage.CONTRACT, "");
		    executedOrderHistoryRequestMsg.addField(Protocol.ExecutedOrderHistoryRequestMessage.ACCOUNT, account.strAccount);
		    executedOrderHistoryRequestMsg.addField(Protocol.ExecutedOrderHistoryRequestMessage.ROLE, "2");
		    executedOrderHistoryRequestMsg.addField(Protocol.EditOrderRequest.GROUP_CODE, account.strGroup);
		    executedOrderHistoryRequestMsg.addField(Protocol.EditOrderRequest.BUY_SELL, "");
		    service.connection.sendMessage(executedOrderHistoryRequestMsg.convertToString(true));
		    
		    
	
		    //Log.i(TAG, executedOrderHistoryRequestMsg.convertToString());
		    //Log.i(TAG, "[Sent executed order history request.]");
		    
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Exception in processMessage",e.fillInStackTrace());
			return false;
		}	
	}
	
}


package com.mfinance.everjoy.app.service.internal;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;

public class ReportErrorProcessor implements MessageProcessor{
	private final String TAG = "ReportErrorProcessor";

	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		Bundle data = msg.getData();

		String strErrMsg = data.getString(ServiceFunction.MESSAGE);	
		
		Log.i("ReportError", strErrMsg);
		
		try {
			MessageObj reportMsg = MessageObj.getMessageObj(IDDictionary.SERVER_IO_SERVICE_TYPE, IDDictionary.SERVER_IO_REPORT_ERRORS);

			try {
				reportMsg.addField(Protocol.ReportError.MESSAGE, strErrMsg);
			} catch(Exception e){}

			service.connection.sendMessage(reportMsg.convertToString(true));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
	}
}
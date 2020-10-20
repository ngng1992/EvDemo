package com.mfinance.everjoy.app.service.external;


import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.bo.CashMovementRecordBuilder;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

public class CashMovementHistoryMessageHandler extends ServerMessageHandler {

	public CashMovementHistoryMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		
		if(msgObj == null){
			if (BuildConfig.DEBUG)
				Log.d(TAG, "msgObj is empty");
			return;
		}	
		service.app.data.clearCashMovementRecord();
        int numNewRow = Utility.toInteger(msgObj.getField(Protocol.CashMovementHistoryMessage.NOTIEM), 0);
        //String sUpdate = msgObj.getField(Protocol.CashMovementHistoryMessage.UPDATE);
        
        for (int i=numNewRow; i>=1; i--){
        	String strTmpGroupCode = msgObj.getField(Protocol.CashMovementHistoryMessage.GROUP_CODE+i);
        	if (strTmpGroupCode == null)
                strTmpGroupCode = "";
        	//if (!FXConstants.FILTER_BY_GROUP_FUNC || parentFrame.strUserGroupCode.equals(strTmpGroupCode)){
			String status = msgObj.getField(Protocol.CashMovementHistoryMessage.DISPLAY_STATUS + i);
        	service.app.data.addCashMovementRecord(
					new CashMovementRecordBuilder()
							.setsAccountNumber(msgObj.getField(Protocol.CashMovementHistoryMessage.ACCOUNT + i))
							.setsRef(msgObj.getField(Protocol.CashMovementHistoryMessage.REF + i))
							.setsBankName(msgObj.getField(Protocol.CashMovementHistoryMessage.BANKNAME + i))
							.setsBankAccount(msgObj.getField(Protocol.CashMovementHistoryMessage.BANKNUMBER + i))
							.setsAmount(msgObj.getField(Protocol.CashMovementHistoryMessage.AMOUNT + i))
							.setsStatus(status != null ? status : msgObj.getField(Protocol.CashMovementHistoryMessage.STATUS + i))
							.setsRequestDate(msgObj.getField(Protocol.CashMovementHistoryMessage.CREATEDATE + i))
							.setsUpdateDate(msgObj.getField(Protocol.CashMovementHistoryMessage.SYSTEMTIME + i))
							.setLastUpdateBy(msgObj.getField(Protocol.CashMovementHistoryMessage.LAST_UPDATE_BY + i))
							.createCashMovementRecord());
        		
        	//}
        		
        }
        
    	service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
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



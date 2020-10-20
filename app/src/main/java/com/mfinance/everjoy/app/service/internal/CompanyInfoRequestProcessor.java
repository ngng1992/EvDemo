package com.mfinance.everjoy.app.service.internal;

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

public class CompanyInfoRequestProcessor implements MessageProcessor{
    private final String TAG = this.getClass().getSimpleName();
    @Override
    public boolean processMessage(Message msg, FxMobileTraderService service) {
        BalanceRecord account = service.app.data.getBalanceRecord();

        try {
            MessageObj executedOrderHistoryRequestMsg = MessageObj.getMessageObj(IDDictionary.SERVER_IO_SERVICE_TYPE, IDDictionary.SERVER_IO_REQUEST_COMPANY_TERMS);
            executedOrderHistoryRequestMsg.addField("acc", account.strAccount);
            service.connection.sendMessage(executedOrderHistoryRequestMsg.convertToString(true));

            //Log.i(TAG, executedOrderHistoryRequestMsg.convertToString());
            //Log.i(TAG, "[SentCompanyInfoRequestProcessor.]");

            return true;
        } catch (Exception e) {
            Log.e(TAG, "Exception in processMessage",e.fillInStackTrace());
            return false;
        }
    }

}


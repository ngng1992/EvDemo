package com.mfinance.everjoy.app.service.internal;

import android.os.Bundle;
import android.os.Message;

import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;

import java.util.ArrayList;
import java.util.HashMap;

public class LiquidateAllProcessor implements MessageProcessor {
    @Override
    public boolean processMessage(Message msg, FxMobileTraderService service) {
        Bundle data = msg.getData();
        if (msg.arg1 == 0) {
            service.broadcast(ServiceFunction.ACT_GO_TO_LIQUIDATE_ALL, msg.getData());
            return true;
        }
        try {
            ArrayList<TransactionObj> trans = data.getParcelableArrayList("trans");
            for (TransactionObj tran : trans) {
                tran.contract = service.app.data.getContract(tran.sContract);
                service.addTransaction(tran);
            }
            HashMap<String, String> request = (HashMap<String, String>) data.getSerializable("request");
            MessageObj messageObj = new MessageObj(IDDictionary.SERVER_DEAL_SERVICE_TYPE, IDDictionary.SERVER_DEAL_REQUEST_MULTIPLE_LIQUIDATE);
            messageObj.importBody(request);
            messageObj.addField("ac", service.app.data.getBalanceRecord().strAccount);
            service.connection.sendMessage(messageObj.convertToString(true));
        } catch (Exception ex) {

        }

        return true;
    }
}

package com.mfinance.everjoy.app.service.external;


import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.pojo.BankAccount;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;

import java.util.ArrayList;
import java.util.HashMap;

public class BankInfoMessageHandler extends ServerMessageHandler {

    public BankInfoMessageHandler(FxMobileTraderService service) {
        super(service);
    }

    @Override
    public void handleMessage(MessageObj msgIncoming) {
        //Log.i(TAG, "[BankInfoMessageHandler.]" + msgIncoming.convertToString());
        if (msgObj == null) {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "msgObj is empty");
            return;
        }
        service.app.data.clearCashMovementRecord();
        ArrayList<BankAccount> bankList = new ArrayList<BankAccount>();
        if (msgIncoming.getField("ba1") != null && msgIncoming.getField("ba1").length() > 0 && msgIncoming.getField("bn1") != null && msgIncoming.getField("bn1").length() > 0) {
            bankList.add(new BankAccount(msgIncoming.getField("ba1"), msgIncoming.getField("bn1")));
        }
        if (msgIncoming.getField("ba2") != null && msgIncoming.getField("ba2").length() > 0 && msgIncoming.getField("bn2") != null && msgIncoming.getField("bn2").length() > 0) {
            bankList.add(new BankAccount(msgIncoming.getField("ba2"), msgIncoming.getField("bn2")));
        }
        if (msgIncoming.getField("ba3") != null && msgIncoming.getField("ba3").length() > 0 && msgIncoming.getField("bn3") != null && msgIncoming.getField("bn3").length() > 0) {
            bankList.add(new BankAccount(msgIncoming.getField("ba3"), msgIncoming.getField("bn3")));
        }
        service.app.data.setBankList(bankList);
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



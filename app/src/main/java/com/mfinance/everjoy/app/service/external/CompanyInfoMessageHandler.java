package com.mfinance.everjoy.app.service.external;


import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.bo.CashMovementRecord;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.LiquidationRecord;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class CompanyInfoMessageHandler extends ServerMessageHandler {

    public CompanyInfoMessageHandler(FxMobileTraderService service) {
        super(service);
    }

    @Override
    public void handleMessage(MessageObj msgIncoming) {
        //Log.i(TAG, "[CompanyInfoMessageHandler.]"+msgIncoming.convertToString());
        if(msgObj == null){
            if (BuildConfig.DEBUG)
                Log.d(TAG, "msgObj is empty");
            return;
        }
        Function<String, String> replaceBr = s -> s.replace("<br>", "\n");
        Map<String, String> withdrawalMap = new HashMap<>();
        withdrawalMap.put("en", Optional.ofNullable(msgIncoming.getField("tm1")).map(replaceBr).orElse(""));
        withdrawalMap.put("zh_CN", Optional.ofNullable(msgIncoming.getField("tm2")).map(replaceBr).orElse(""));
        withdrawalMap.put("zh_TW", Optional.ofNullable(msgIncoming.getField("tm3")).map(replaceBr).orElse(""));
        service.app.data.setWithdrawalTermsMap(withdrawalMap);
        Map<String, String> depositMap = new HashMap<>();
        depositMap.put("en", Optional.ofNullable(msgIncoming.getField("dtm1")).map(replaceBr).orElse(""));
        depositMap.put("zh_CN", Optional.ofNullable(msgIncoming.getField("dtm2")).map(replaceBr).orElse(""));
        depositMap.put("zh_TW", Optional.ofNullable(msgIncoming.getField("dtm3")).map(replaceBr).orElse(""));
        service.app.data.setDepositTermsMap(depositMap);
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



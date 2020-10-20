package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;

public class MoveToCustListProcessor implements MessageProcessor {
    private final String TAG = "MoveToCustListProcessor";
    @Override
    public boolean processMessage(Message msg, FxMobileTraderService service) {
        service.broadcast(ServiceFunction.ACT_GO_TO_CUST_LIST, null);
        return true;
    }

}

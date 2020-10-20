package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

import android.os.Message;

public class MoveToTwoFAProcessor implements MessageProcessor {
    private final String TAG = "MoveToTwoFAProcessor";
    @Override
    public boolean processMessage(Message msg, FxMobileTraderService service) {
        service.broadcast(ServiceFunction.ACT_GO_TO_TWO_FA, null);
        return true;
    }

}

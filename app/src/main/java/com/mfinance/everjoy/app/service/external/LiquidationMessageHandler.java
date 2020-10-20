package com.mfinance.everjoy.app.service.external;

import android.os.Bundle;
import android.os.Message;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;

import java.util.HashMap;

public class LiquidationMessageHandler extends ServerMessageHandler {
    /**
     * Constructor
     *
     * @param service background service
     */
    public LiquidationMessageHandler(FxMobileTraderService service) {
        super(service);
    }

    @Override
    public boolean isStatusLess() {
        return false;
    }

    @Override
    public boolean isBalanceRecalRequire() {
        return false;
    }

    @Override
    public void handleMessage(MessageObj msgObj) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("response", new HashMap<>(msgObj.cloneBody()));
        service.broadcast(ServiceFunction.ACT_TRADER_RECEIVE_MULTIPLE_LIQUIDATE_RETURN, bundle);
    }
}
